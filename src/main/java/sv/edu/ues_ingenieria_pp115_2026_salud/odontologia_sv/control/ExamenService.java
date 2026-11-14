package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;

@Stateless
public class ExamenService extends DefaultService<Examen> {

    public ExamenService() {
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
            Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<Examen> listarActivos() {
        try {
            TypedQuery<Examen> query = em.createNamedQuery("Examen.findByActivo", Examen.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<Examen> buscarActivosPorNombre(final String query, int first, int max) {
        if (query == null || query.trim().length() < 3 || first < 0 || max <= 0) {
            return List.of();
        }
        try {
            TypedQuery<Examen> namedQuery = em.createNamedQuery("Examen.findByNombreLikeActivo", Examen.class);
            namedQuery.setParameter("filtro", "%" + query.trim().toUpperCase() + "%");
            namedQuery.setFirstResult(first);
            namedQuery.setMaxResults(max);
            return namedQuery.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudieron buscar exámenes activos", ex);
        }
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
                    Logger.getLogger(ExamenService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

        }

        return List.of();
    }

}
