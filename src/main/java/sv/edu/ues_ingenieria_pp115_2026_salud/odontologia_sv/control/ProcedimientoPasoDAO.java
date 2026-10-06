
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;

@Stateless
public class ProcedimientoPasoDAO extends DefaultDAO<ProcedimientoPaso>{

    public ProcedimientoPasoDAO() {
        super(ProcedimientoPaso.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<ProcedimientoPaso> buscarPorProcedimiento(UUID idProcedimiento) {
        try {
            if (idProcedimiento != null) {
                String jpql = "SELECT pp FROM ProcedimientoPaso pp " +
                              "WHERE pp.idProcedimiento.idProcedimiento = :idProcedimiento " +
                              "ORDER BY pp.idProcedimientoPaso ASC";
                TypedQuery<ProcedimientoPaso> query = em.createQuery(jpql, ProcedimientoPaso.class);
                query.setParameter("idProcedimiento", idProcedimiento);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    
    public List<ProcedimientoPaso> buscarPorNombre(String nombre) {
        try {
            if (nombre != null) {
                TypedQuery<ProcedimientoPaso> query = em.createNamedQuery("ProcedimientoPaso.findByNombre", ProcedimientoPaso.class);
                query.setParameter("nombre", "%" + nombre.trim().toUpperCase() + "%");
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
     public List<ProcedimientoPaso> buscarPasosFinales() {
        try {
            String jpql = "SELECT pp FROM ProcedimientoPaso pp WHERE pp.indicaFin = true";
            TypedQuery<ProcedimientoPaso> query = em.createQuery(jpql, ProcedimientoPaso.class);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
     
     public List<ProcedimientoPaso> buscarPorRol(UUID idRol) {
        try {
            if (idRol != null) {
                String jpql = "SELECT pp FROM ProcedimientoPaso pp WHERE pp.idRol.idRol = :idRol";
                TypedQuery<ProcedimientoPaso> query = em.createQuery(jpql, ProcedimientoPaso.class);
                query.setParameter("idRol", idRol);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
}
