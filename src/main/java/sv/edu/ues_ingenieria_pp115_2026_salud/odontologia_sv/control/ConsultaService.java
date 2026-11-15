package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
        consulta.setExamenes(selectedExams);
        consulta.setExamenIds(examenIds);
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
