
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

@Stateless
public class RolDAO extends DefaultDAO<Rol>{

    public RolDAO() {
        super(Rol.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    public List<Rol> buscarPorNombre(String nombre){
        try {
            if(nombre!=null){
                TypedQuery<Rol> query=em.createNamedQuery("Rol.findByNombre",Rol.class);
                query.setParameter("nombre","%"+nombre.trim().toUpperCase()+"%");
                return query.getResultList();
            }
        } catch (Exception ex) {
           Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

        }
        return List.of();
        
    }
    
    public List<Rol> listarActivos() {
        try {
            TypedQuery<Rol> query = em.createNamedQuery("Rol.findByActivo", Rol.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    public List<Rol> findByNombreLikeActivo(final String query, int first, int max) {
    try {
        if (query != null  && first >= 0 && max > 0) {
            String jpql = "SELECT r FROM Rol r "
                    + "WHERE r.activo = true "
                    + "AND UPPER(r.nombre) LIKE :filtro "
                    + "ORDER BY r.nombre ASC";
            TypedQuery<Rol> q = em.createQuery(jpql, Rol.class);
            q.setParameter("filtro", "%" + query.trim().toUpperCase() + "%");
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        }
    } catch (Exception ex) {
        Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    return List.of();
}
    
    
}
