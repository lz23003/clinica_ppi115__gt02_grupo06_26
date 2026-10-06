
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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;

@Stateless
public class ConsultaProcedimientoDAO extends DefaultDAO<ConsultaProcedimiento>{

    public ConsultaProcedimientoDAO() {
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
                String jpql = "SELECT cp FROM ConsultaProcedimiento cp WHERE cp.idConsulta.idConsulta = :idConsulta";
                TypedQuery<ConsultaProcedimiento> query = em.createQuery(jpql, ConsultaProcedimiento.class);
                query.setParameter("idConsulta", idConsulta);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
     
    
   public List<ConsultaProcedimiento> buscarPorProcedimiento(UUID idProcedimiento) {
        try {
            if (idProcedimiento != null) {
                // Nota: idProcedimiento es Object por el problema de FK faltante
                String jpql = "SELECT cp FROM ConsultaProcedimiento cp WHERE cp.idProcedimiento = :idProcedimiento";
                TypedQuery<ConsultaProcedimiento> query = em.createQuery(jpql, ConsultaProcedimiento.class);
                query.setParameter("idProcedimiento", idProcedimiento);
                return query.getResultList();
            }
        } catch (Exception ex) {
                     Logger.getLogger(ConsultaProcedimientoDAO.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);


        }
        return List.of();
    }

   
   public List<ConsultaProcedimiento> buscarPorRangoFechas(Date inicio, Date fin) {
        try {
            if (inicio != null && fin != null) {
                String jpql = "SELECT cp FROM ConsultaProcedimiento cp " +
                              "WHERE cp.fechaInicio BETWEEN :inicio AND :fin";
                TypedQuery<ConsultaProcedimiento> query = em.createQuery(jpql, ConsultaProcedimiento.class);
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    
    
    
}
