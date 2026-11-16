package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;

@Stateless
public class ConsultaService extends DefaultService<Consulta>{

    public ConsultaService() {
        super(Consulta.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    
    @Override
    protected EntityManager getEntityManager() {
        return em;

    }

    public void actualizarExamenes(Consulta consulta, List<UUID> examenIds) {
        if (consulta == null || examenIds == null) {
            throw new IllegalArgumentException("La consulta y la lista de exámenes son obligatorias.");
        }

        List<Examen> selectedExams = new java.util.ArrayList<>();
        for (UUID examenId : new java.util.LinkedHashSet<>(examenIds)) {
            if (examenId == null) {
                throw new IllegalArgumentException("La lista contiene un identificador de examen inválido.");
            }
            Examen examen = em.find(Examen.class, examenId);
            if (examen == null || !Boolean.TRUE.equals(examen.getActivo())) {
                throw new IllegalArgumentException("Uno de los exámenes seleccionados no existe o está inactivo.");
            }
            selectedExams.add(examen);
        }
        List<UUID> selectedExamIds = selectedExams.stream()
                .map(Examen::getIdExamen)
                .toList();
        consulta.setObservaciones(actualizarObservacionesConExamenes(
                consulta.getObservaciones(), selectedExamIds));
        consulta.setExamenIds(selectedExamIds);
    }

    public List<UUID> obtenerExamenIds(Consulta consulta) {
        if (consulta == null || consulta.getObservaciones() == null
                || consulta.getObservaciones().isBlank()) {
            return List.of();
        }
        JsonObject observaciones = leerObservaciones(consulta.getObservaciones());
        JsonValue examenIdsValue = observaciones.get("examenIds");
        if (examenIdsValue == null) {
            return List.of();
        }
        if (examenIdsValue.getValueType() != JsonValue.ValueType.ARRAY) {
            throw new IllegalStateException("La consulta contiene una lista de exámenes inválida.");
        }
        JsonArray examenIds = observaciones.getJsonArray("examenIds");
        List<UUID> ids = new ArrayList<>(examenIds.size());
        for (int i = 0; i < examenIds.size(); i++) {
            if (examenIds.get(i).getValueType() != JsonValue.ValueType.STRING) {
                throw new IllegalStateException("La consulta contiene un identificador de examen inválido.");
            }
            try {
                ids.add(UUID.fromString(examenIds.getString(i)));
            } catch (IllegalArgumentException ex) {
                throw new IllegalStateException("La consulta contiene un identificador de examen inválido.", ex);
            }
        }
        return List.copyOf(ids);
    }

    private String actualizarObservacionesConExamenes(String observaciones, List<UUID> examenIds) {
        JsonObject actual = observaciones == null || observaciones.isBlank()
                ? Json.createObjectBuilder().build()
                : leerObservaciones(observaciones);
        JsonObjectBuilder actualizado = Json.createObjectBuilder(actual);
        var ids = Json.createArrayBuilder();
        examenIds.forEach(id -> ids.add(id.toString()));
        actualizado.add("examenIds", ids);
        StringWriter contenido = new StringWriter();
        try (var writer = Json.createWriter(contenido)) {
            writer.writeObject(actualizado.build());
        }
        return contenido.toString();
    }

    private JsonObject leerObservaciones(String observaciones) {
        try (var reader = Json.createReader(new StringReader(observaciones))) {
            return reader.readObject();
        } catch (JsonException ex) {
            JsonObjectBuilder legado = Json.createObjectBuilder();
            legado.add("historial", Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                            .add("paso", "Registro anterior")
                            .add("notas", observaciones)));
            return legado.build();
        }
    }

    public List<Consulta> buscarDashboardPorFiltros(UUID idClinica, UUID idRol, Date desde, Date hasta) {
        if (desde != null && hasta != null && !desde.before(hasta)) {
            throw new IllegalArgumentException("El rango de fechas no es válido.");
        }
        TypedQuery<Consulta> query = em.createNamedQuery("Consulta.buscarDashboardPorFiltros", Consulta.class);
        query.setParameter("idClinica", idClinica);
        query.setParameter("idRol", idRol);
        query.setParameter("desde", desde);
        query.setParameter("hasta", hasta);
        return query.getResultList();
    }

    public Object[] buscarContextoPorConsulta(UUID idConsulta) {
        if (idConsulta == null) {
            return null;
        }
        List<Object[]> contexts = em.createNamedQuery("Consulta.findContextById", Object[].class)
                .setParameter("idConsulta", idConsulta)
                .setMaxResults(1)
                .getResultList();
        return contexts.isEmpty() ? null : contexts.get(0);
    }

    public List<Consulta> buscarCitasPublicas(
            String tipoDocumento, String numeroDocumento, UUID idClinica,
            Date desde, Date hasta, int maxResultados) {
        if (tipoDocumento == null || tipoDocumento.isBlank()
                || numeroDocumento == null || numeroDocumento.isBlank()
                || idClinica == null || desde == null || hasta == null
                || !desde.before(hasta) || maxResultados <= 0) {
            throw new IllegalArgumentException("Los filtros para buscar citas no son válidos");
        }
        try {
            TypedQuery<Consulta> query = em.createNamedQuery("Consulta.findCitasPublicas", Consulta.class);
            query.setParameter("tipoDocumento", "%" + tipoDocumento.trim().toUpperCase() + "%");
            query.setParameter("numeroDocumento", numeroDocumento.trim());
            query.setParameter("idClinica", idClinica);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);
            query.setMaxResults(maxResultados);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ConsultaService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudieron buscar las citas", ex);
        }
    }
    
    public List<Consulta> buscarPorConsultasActivas(){
        try {
            TypedQuery<Consulta> query=em.createNamedQuery("Consulta.findActivas",Consulta.class);
            return query.getResultList();
                    
        } catch (Exception ex) {
          Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);

          }
    
        return List.of();
    }
    
    public List<Consulta> buscarPorPaciente(UUID idPaciente){
        
        try {
            
            if(idPaciente!=null){
            TypedQuery<Consulta> query=em.createNamedQuery("Consulta.findByPaciente",Consulta.class);
            query.setParameter("idPaciente", idPaciente);
            return query.getResultList();
        }
            
        } catch (Exception ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);

        }
        
        return List.of();
        
    }
    
    public List<Consulta> buscarPorRangoFechas(Date inicio, Date fin) {
        EntityManager em = getEntityManager();
        try {
            if (inicio != null && fin != null) {
                TypedQuery<Consulta> query = em.createNamedQuery("Consulta.findByRangoFechas", Consulta.class);
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
                return query.getResultList();
            }
        } catch (Exception ex) {
              Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);

        }
        return List.of();
    }

    
     public List<Consulta> buscarPorPersonaRol(UUID idPersonaRol) {
        EntityManager em = getEntityManager();
        try {
            if (idPersonaRol != null) {
                TypedQuery<Consulta> query = em.createNamedQuery("Consulta.findByPersonaRol", Consulta.class);
                query.setParameter("idPersonaRol", idPersonaRol);
                return query.getResultList();
            }
        } catch (Exception ex) {
              Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);
        }
        return List.of();
    }

    
    
}
