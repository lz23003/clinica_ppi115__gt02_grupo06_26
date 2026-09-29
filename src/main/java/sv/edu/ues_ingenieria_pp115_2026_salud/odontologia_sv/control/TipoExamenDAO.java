
package control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;

@Stateless
public class TipoExamenDAO  extends DefaultDAO<TipoExamen>{

    public TipoExamenDAO() {
        super(TipoExamen.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    public List<TipoExamen> buscarPorNombre(String nombre) {
        try {
            if (nombre != null) {
                TypedQuery<TipoExamen> query = em.createNamedQuery("TipoExamen.findByNombre", TipoExamen.class);
                query.setParameter("nombre", "%" + nombre.trim().toUpperCase() + "%");
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<TipoExamen> listarActivos() {
        try {
            TypedQuery<TipoExamen> query = em.createNamedQuery("TipoExamen.findByActivo", TipoExamen.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(TipoExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    
    //metodos de busqueda con LIKE
    public List<TipoExamen> findByNombreLike(final String nombre,int first,int max){
        try {
            if(nombre!=null && first>=0 && max>0){
                TypedQuery<TipoExamen> query = em.createNamedQuery("TipoExamen.findByNombreLike", TipoExamen.class);
                query.setParameter("nombre","%"+ nombre.trim().toUpperCase()+"%");
                query.setFirstResult(first);
                query.setMaxResults(max);
                return query.getResultList();
            }
        } catch (Exception ex) {
               Logger.getLogger(ExamenTipoExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

        }
        return List.of();
    }
    
}
