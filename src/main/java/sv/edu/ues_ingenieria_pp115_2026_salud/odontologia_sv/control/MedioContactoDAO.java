
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;

@Stateless
public class MedioContactoDAO extends DefaultDAO<MedioContacto>{

    public MedioContactoDAO() {
        super(MedioContacto.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em ;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
     public List<MedioContacto> buscarPorPersona(UUID idPersona) {
        try {
            if (idPersona != null) {
                String jpql = "SELECT mc FROM MedioContacto mc WHERE mc.idPersona.idPersona = :idPersona";
                TypedQuery<MedioContacto> query = em.createQuery(jpql, MedioContacto.class);
                query.setParameter("idPersona", idPersona);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(MedioContactoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<MedioContacto> buscarPorTipoMedioContacto(UUID idTipoMedioContacto) {
        try {
            if (idTipoMedioContacto != null) {
                String jpql = "SELECT mc FROM MedioContacto mc " +
                              "WHERE mc.idTipoMedioContacto.idTipoMedioContacto = :idTipoMedioContacto";
                TypedQuery<MedioContacto> query = em.createQuery(jpql, MedioContacto.class);
                query.setParameter("idTipoMedioContacto", idTipoMedioContacto);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(MedioContactoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<MedioContacto> buscarPorValor(String valor) {
        try {
            if (valor != null) {
                String jpql = "SELECT mc FROM MedioContacto mc WHERE mc.valor LIKE :valor";
                TypedQuery<MedioContacto> query = em.createQuery(jpql, MedioContacto.class);
                query.setParameter("valor", "%" + valor.trim() + "%");
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(MedioContactoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<MedioContacto> buscarPorPersonaYTipo(UUID idPersona, UUID idTipoMedioContacto) {
        try {
            if (idPersona != null && idTipoMedioContacto != null) {
                String jpql = "SELECT mc FROM MedioContacto mc " +
                              "WHERE mc.idPersona.idPersona = :idPersona " +
                              "AND mc.idTipoMedioContacto.idTipoMedioContacto = :idTipoMedioContacto";
                TypedQuery<MedioContacto> query = em.createQuery(jpql, MedioContacto.class);
                query.setParameter("idPersona", idPersona);
                query.setParameter("idTipoMedioContacto", idTipoMedioContacto);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(MedioContactoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
}
