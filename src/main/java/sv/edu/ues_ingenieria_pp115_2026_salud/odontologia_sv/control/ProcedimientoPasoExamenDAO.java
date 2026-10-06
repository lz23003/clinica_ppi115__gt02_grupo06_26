
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoExamen;

@Stateless
public class ProcedimientoPasoExamenDAO extends DefaultDAO<ProcedimientoPasoExamen>{

    public ProcedimientoPasoExamenDAO() {
        super(ProcedimientoPasoExamen.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em ;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<ProcedimientoPasoExamen> buscarPorPaso(UUID idProcedimientoPaso) {
        try {
            if (idProcedimientoPaso != null) {
                String jpql = "SELECT ppe FROM ProcedimientoPasoExamen ppe " +
                              "WHERE ppe.idProcedimientoPaso.idProcedimientoPaso = :idPaso";
                TypedQuery<ProcedimientoPasoExamen> query = em.createQuery(jpql, ProcedimientoPasoExamen.class);
                query.setParameter("idPaso", idProcedimientoPaso);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ProcedimientoPasoExamen> buscarPorExamen(UUID idExamen) {
        try {
            if (idExamen != null) {
                String jpql = "SELECT ppe FROM ProcedimientoPasoExamen ppe " +
                              "WHERE ppe.idExamen.idExamen = :idExamen";
                TypedQuery<ProcedimientoPasoExamen> query = em.createQuery(jpql, ProcedimientoPasoExamen.class);
                query.setParameter("idExamen", idExamen);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ProcedimientoPasoExamen> listarActivos() {
        try {
            TypedQuery<ProcedimientoPasoExamen> query = em.createNamedQuery("ProcedimientoPasoExamen.findByActivo",ProcedimientoPasoExamen.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
}
