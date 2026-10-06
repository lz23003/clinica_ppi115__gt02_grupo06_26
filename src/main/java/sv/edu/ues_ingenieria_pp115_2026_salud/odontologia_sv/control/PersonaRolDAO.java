
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;

@Stateless
public class PersonaRolDAO extends DefaultDAO<PersonaRol>{

    public PersonaRolDAO() {
        super(PersonaRol.class);
    }
    
    @PersistenceContext(unitName="Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
     public List<PersonaRol> buscarPorPersona(UUID idPersona) {
        try {
            if (idPersona != null) {
                String jpql = "SELECT pr FROM PersonaRol pr WHERE pr.idPersona.idPersona = :idPersona";
                TypedQuery<PersonaRol> query = em.createQuery(jpql, PersonaRol.class);
                query.setParameter("idPersona", idPersona);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
     
      public List<PersonaRol> buscarPorRol(UUID idRol) {
        try {
            if (idRol != null) {
                String jpql = "SELECT pr FROM PersonaRol pr WHERE pr.idRol.idRol = :idRol";
                TypedQuery<PersonaRol> query = em.createQuery(jpql, PersonaRol.class);
                query.setParameter("idRol", idRol);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
      
      public List<PersonaRol> buscarPorClinica(UUID idClinica) {
        try {
            if (idClinica != null) {
                String jpql = "SELECT pr FROM PersonaRol pr WHERE pr.idClinica.idClinica = :idClinica";
                TypedQuery<PersonaRol> query = em.createQuery(jpql, PersonaRol.class);
                query.setParameter("idClinica", idClinica);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
      
       public PersonaRol buscarPorPersonaYRol(UUID idPersona, UUID idRol) {
        try {
            if (idPersona != null && idRol != null) {
                String jpql = "SELECT pr FROM PersonaRol pr " +
                              "WHERE pr.idPersona.idPersona = :idPersona AND pr.idRol.idRol = :idRol";
                TypedQuery<PersonaRol> query = em.createQuery(jpql, PersonaRol.class);
                query.setParameter("idPersona", idPersona);
                query.setParameter("idRol", idRol);
                
                List<PersonaRol> resultados = query.getResultList();
                return resultados.isEmpty() ? null : resultados.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
       }
    
    
    
    
    
    
}
