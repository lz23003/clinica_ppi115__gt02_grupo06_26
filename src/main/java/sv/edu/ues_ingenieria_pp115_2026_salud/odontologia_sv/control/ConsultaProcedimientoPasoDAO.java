
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;


@Stateless
public class ConsultaProcedimientoPasoDAO extends DefaultDAO<ConsultaProcedimientoPaso> {

    public ConsultaProcedimientoPasoDAO() {
        super(ConsultaProcedimientoPaso.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    public List<ConsultaProcedimientoPaso> buscarPorConsultaProcedimiento(UUID idConsultaProcedimiento) {
        try {
            if (idConsultaProcedimiento != null) {
                String jpql = "SELECT cpp FROM ConsultaProcedimientoPaso cpp " +
                              "WHERE cpp.idConsultaProcedimiento.idConsultaProcedimiento = :idConsultaProcedimiento " +
                              "ORDER BY cpp.fechaInicio ASC";
                TypedQuery<ConsultaProcedimientoPaso> query = em.createQuery(jpql, ConsultaProcedimientoPaso.class);
                query.setParameter("idConsultaProcedimiento", idConsultaProcedimiento);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoPasoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ConsultaProcedimientoPaso> buscarPorEstado(String estado) {
        try {
            if (estado != null) {
                String jpql = "SELECT cpp FROM ConsultaProcedimientoPaso cpp WHERE cpp.estado = :estado";
                TypedQuery<ConsultaProcedimientoPaso> query = em.createQuery(jpql, ConsultaProcedimientoPaso.class);
                query.setParameter("estado", estado.trim().toUpperCase());
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoPasoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ConsultaProcedimientoPaso> buscarPorPersonaRol(UUID idPersonaRol) {
        try {
            if (idPersonaRol != null) {
                String jpql = "SELECT cpp FROM ConsultaProcedimientoPaso cpp " +
                              "WHERE cpp.idPersonaRol.idPersonaRol = :idPersonaRol";
                TypedQuery<ConsultaProcedimientoPaso> query = em.createQuery(jpql, ConsultaProcedimientoPaso.class);
                query.setParameter("idPersonaRol", idPersonaRol);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoPasoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ConsultaProcedimientoPaso> buscarPendientes() {
        try {
            String jpql = "SELECT cpp FROM ConsultaProcedimientoPaso cpp " +
                          "WHERE cpp.estado = 'PENDIENTE' " +
                          "ORDER BY cpp.fechaInicio ASC";
            TypedQuery<ConsultaProcedimientoPaso> query = em.createQuery(jpql, ConsultaProcedimientoPaso.class);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoPasoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    
    
    
}
