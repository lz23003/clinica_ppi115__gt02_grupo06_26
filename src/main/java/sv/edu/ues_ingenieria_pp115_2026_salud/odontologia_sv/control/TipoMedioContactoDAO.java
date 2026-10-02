package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class TipoMedioContactoDAO extends DefaultDAO<TipoMedioContacto> {


    public TipoMedioContactoDAO() {
        super(TipoMedioContacto.class);
    }

    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TipoMedioContacto> buscarPorNombre(String nombre) {
        try {
            if (nombre != null) {
                TypedQuery<TipoMedioContacto> query = em.createNamedQuery("TipoMedioContacto.findByNombre", TipoMedioContacto.class);
                query.setParameter("nombre", "%" + nombre.trim().toUpperCase() + "%");
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoMedioContactoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<TipoMedioContacto> listarActivos() {
        try {
            TypedQuery<TipoMedioContacto> query = em.createNamedQuery("TipoMedioContacto.findByActivo", TipoMedioContacto.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(TipoMedioContactoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public TipoMedioContacto buscarPorExpresionRegular(String expresionRegular) {
        try {
            if (expresionRegular != null) {
                String jpql = "SELECT tmc FROM TipoMedioContacto tmc WHERE tmc.expresionRegular = :expresionRegular";
                TypedQuery<TipoMedioContacto> query = em.createQuery(jpql, TipoMedioContacto.class);
                query.setParameter("expresionRegular", expresionRegular);

                List<TipoMedioContacto> resultados = query.getResultList();
                return resultados.isEmpty() ? null : resultados.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoMedioContactoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

}
