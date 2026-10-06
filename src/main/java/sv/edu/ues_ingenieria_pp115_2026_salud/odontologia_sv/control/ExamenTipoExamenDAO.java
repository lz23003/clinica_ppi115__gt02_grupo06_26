
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ExamenTipoExamen;

@Stateless
public class ExamenTipoExamenDAO extends DefaultDAO<ExamenTipoExamen>{

    public ExamenTipoExamenDAO() {
        super(ExamenTipoExamen.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<ExamenTipoExamen> buscarPorExamen(UUID idExamen) {
        try {
            if (idExamen != null) {
                String jpql = "SELECT ete FROM ExamenTipoExamen ete WHERE ete.idExamen.idExamen = :idExamen";
                TypedQuery<ExamenTipoExamen> query = em.createQuery(jpql, ExamenTipoExamen.class);
                query.setParameter("idExamen", idExamen);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ExamenTipoExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ExamenTipoExamen> buscarPorTipoExamen(UUID idTipoExamen) {
        try {
            if (idTipoExamen != null) {
                String jpql = "SELECT ete FROM ExamenTipoExamen ete WHERE ete.idTipoExamen.idTipoExamen = :idTipoExamen";
                TypedQuery<ExamenTipoExamen> query = em.createQuery(jpql, ExamenTipoExamen.class);
                query.setParameter("idTipoExamen", idTipoExamen);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ExamenTipoExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
}
