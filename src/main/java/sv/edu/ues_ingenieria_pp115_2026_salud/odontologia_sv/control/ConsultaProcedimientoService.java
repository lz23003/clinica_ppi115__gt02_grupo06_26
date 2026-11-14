
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;

@Stateless
public class ConsultaProcedimientoService extends DefaultService<ConsultaProcedimiento>{

    public ConsultaProcedimientoService() {
        super(ConsultaProcedimiento.class);
    }

    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
     public List<ConsultaProcedimiento> buscarPorConsulta(UUID idConsulta) {
        try {
            if (idConsulta != null) {
                TypedQuery<ConsultaProcedimiento> query = em.createNamedQuery("ConsultaProcedimiento.findByConsulta", ConsultaProcedimiento.class);
                query.setParameter("idConsulta", idConsulta);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public Map<UUID, List<String>> buscarNombresPorConsultas(List<UUID> consultaIds) {
        if (consultaIds == null || consultaIds.isEmpty()) {
            return Map.of();
        }
        try {
            TypedQuery<Object[]> query = em.createNamedQuery(
                    "ConsultaProcedimiento.findProcedureNamesByConsultas", Object[].class);
            query.setParameter("consultaIds", consultaIds);
            Map<UUID, List<String>> namesByConsultation = new LinkedHashMap<>();
            for (Object[] row : query.getResultList()) {
                UUID consultaId = (UUID) row[0];
                String nombre = (String) row[1];
                namesByConsultation.computeIfAbsent(consultaId, ignored -> new ArrayList<>()).add(nombre);
            }
            return namesByConsultation;
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudieron cargar los procedimientos de las citas", ex);
        }
    }
     
    
   public List<ConsultaProcedimiento> buscarPorProcedimiento(UUID idProcedimiento) {
        try {
            if (idProcedimiento != null) {
                TypedQuery<ConsultaProcedimiento> query = em.createNamedQuery("ConsultaProcedimiento.findByProcedimiento", ConsultaProcedimiento.class);
                query.setParameter("idProcedimiento", idProcedimiento);
                return query.getResultList();
            }
        } catch (Exception ex) {
                     Logger.getLogger(ConsultaProcedimientoService.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);


        }
        return List.of();
    }

   
   public List<ConsultaProcedimiento> buscarPorRangoFechas(Date inicio, Date fin) {
        try {
            if (inicio != null && fin != null) {
                TypedQuery<ConsultaProcedimiento> query = em.createNamedQuery("ConsultaProcedimiento.findByRangoFechas", ConsultaProcedimiento.class);
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    
    
    
}
