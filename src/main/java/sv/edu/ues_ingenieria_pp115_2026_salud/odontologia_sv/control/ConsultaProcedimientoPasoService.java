
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
public class ConsultaProcedimientoPasoService extends DefaultService<ConsultaProcedimientoPaso> {

    public ConsultaProcedimientoPasoService() {
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
                TypedQuery<ConsultaProcedimientoPaso> query = em.createNamedQuery("ConsultaProcedimientoPaso.findByConsultaProcedimiento", ConsultaProcedimientoPaso.class);
                query.setParameter("idConsultaProcedimiento", idConsultaProcedimiento);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoPasoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ConsultaProcedimientoPaso> buscarPorEstado(String estado) {
        try {
            if (estado != null) {
                TypedQuery<ConsultaProcedimientoPaso> query = em.createNamedQuery("ConsultaProcedimientoPaso.findByEstado", ConsultaProcedimientoPaso.class);
                query.setParameter("estado", estado.trim().toUpperCase());
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoPasoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ConsultaProcedimientoPaso> buscarPorPersonaRol(UUID idPersonaRol) {
        try {
            if (idPersonaRol != null) {
                TypedQuery<ConsultaProcedimientoPaso> query = em.createNamedQuery("ConsultaProcedimientoPaso.findByPersonaRol", ConsultaProcedimientoPaso.class);
                query.setParameter("idPersonaRol", idPersonaRol);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoPasoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ConsultaProcedimientoPaso> buscarPendientes() {
        try {
            TypedQuery<ConsultaProcedimientoPaso> query = em.createNamedQuery("ConsultaProcedimientoPaso.findPendientes", ConsultaProcedimientoPaso.class);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ConsultaProcedimientoPasoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    
    
    
}
