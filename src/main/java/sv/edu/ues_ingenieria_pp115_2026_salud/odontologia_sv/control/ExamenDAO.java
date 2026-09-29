package control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;

@Stateless
public class ExamenDAO extends DefaultDAO<Examen> {

    public ExamenDAO() {
        super(Examen.class);
    }

    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;

    }

    public List<Examen> buscarPorNombre(String nombre) {
        try {
            if (nombre != null) {
                TypedQuery<Examen> query = em.createNamedQuery("Examen.findByNombre", Examen.class);
                query.setParameter("nombre", "%" + nombre.trim().toUpperCase() + "%");
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<Examen> listarActivos() {
        try {
            TypedQuery<Examen> query = em.createNamedQuery("Examen.findByActivo", Examen.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    //Metodos con LIKE
    public List<Examen> findByNombreLike(final String nombre, int first, int max) {
        try {
            if (nombre != null && first >= 0 && max > 0) {
                TypedQuery<Examen> query = em.createNamedQuery("Examen.findByNombreLike",Examen.class);
                query.setParameter("nombre","%" +nombre.trim().toUpperCase()+ "%");
                query.setFirstResult(first);
                query.setMaxResults(max);
                return query.getResultList();
            }

        } catch (Exception ex) {
                    Logger.getLogger(ExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

        }

        return List.of();
    }

}
