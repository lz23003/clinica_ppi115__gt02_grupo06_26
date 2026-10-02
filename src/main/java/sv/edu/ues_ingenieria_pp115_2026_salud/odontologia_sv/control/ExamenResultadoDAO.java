package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ExamenResultado;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ExamenResultadoDAO extends DefaultDAO<ExamenResultado>{


    public ExamenResultadoDAO() {
        super(ExamenResultado.class);
    }

    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<ExamenResultado> buscarPorOrdenExamen(UUID idOrdenExamen) {
        try {
            if (idOrdenExamen != null) {
                String jpql = "SELECT er FROM ExamenResultado er " +
                        "WHERE er.idOrdenExamen.idOrdenExamen = :idOrdenExamen";
                TypedQuery<ExamenResultado> query = em.createQuery(jpql, ExamenResultado.class);
                query.setParameter("idOrdenExamen", idOrdenExamen);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ExamenResultadoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
}


