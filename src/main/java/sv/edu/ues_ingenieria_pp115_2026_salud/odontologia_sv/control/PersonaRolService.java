
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
public class PersonaRolService extends DefaultService<PersonaRol>{

    public PersonaRolService() {
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
                TypedQuery<PersonaRol> query = em.createNamedQuery("PersonaRol.findByPersona", PersonaRol.class);
                query.setParameter("idPersona", idPersona);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
     
      public List<PersonaRol> buscarPorRol(UUID idRol) {
        try {
            if (idRol != null) {
                TypedQuery<PersonaRol> query = em.createNamedQuery("PersonaRol.findByRol", PersonaRol.class);
                query.setParameter("idRol", idRol);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
      
      public List<PersonaRol> buscarPorClinica(UUID idClinica) {
        try {
            if (idClinica != null) {
                TypedQuery<PersonaRol> query = em.createNamedQuery("PersonaRol.findByClinica", PersonaRol.class);
                query.setParameter("idClinica", idClinica);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
      
    public PersonaRol buscarPorPersonaYRol(UUID idPersona, UUID idRol) {
        try {
            if (idPersona != null && idRol != null) {
                TypedQuery<PersonaRol> query = em.createNamedQuery("PersonaRol.findByPersonaYRol", PersonaRol.class);
                query.setParameter("idPersona", idPersona);
                query.setParameter("idRol", idRol);
                List<PersonaRol> resultados = query.getResultList();
                return resultados.isEmpty() ? null : resultados.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public List<PersonaRol> buscarAsignacionesActivasPorDocumento(String tipoDocumento, String numeroDocumento) {
        if (tipoDocumento == null || tipoDocumento.isBlank()
                || numeroDocumento == null || numeroDocumento.isBlank()) {
            return List.of();
        }
        try {
            TypedQuery<PersonaRol> query = em.createNamedQuery(
                    "PersonaRol.findActiveByDocumento", PersonaRol.class);
            query.setParameter("tipoDocumento", "%" + tipoDocumento.trim().toUpperCase() + "%");
            query.setParameter("numeroDocumento", numeroDocumento.trim());
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudieron validar las asignaciones del empleado", ex);
        }
    }

    public List<PersonaRol> buscarAsignacionesActivasPorPersona(UUID idPersona) {
        if (idPersona == null) {
            return List.of();
        }
        try {
            return em.createNamedQuery("PersonaRol.findActiveByPersona", PersonaRol.class)
                    .setParameter("idPersona", idPersona)
                    .getResultList();
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudieron validar las asignaciones activas del empleado", ex);
        }
    }

    public List<PersonaRol> buscarPorClinicaRolYNombre(UUID idClinica, UUID idRol, String nombre, int first, int max) {
        if (idClinica == null || idRol == null || nombre == null || nombre.trim().length() < 3
                || first < 0 || max <= 0) {
            return List.of();
        }
        try {
            TypedQuery<PersonaRol> query = em.createNamedQuery("PersonaRol.findByClinicaRolNombre", PersonaRol.class);
            query.setParameter("idClinica", idClinica);
            query.setParameter("idRol", idRol);
            query.setParameter("filtro", "%" + nombre.trim().toUpperCase() + "%");
            query.setFirstResult(first);
            query.setMaxResults(max);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudo buscar personal por clínica y rol", ex);
        }
    }

    public List<PersonaRol> buscarPorClinicaYNombre(UUID idClinica, String nombre, int first, int max) {
        if (idClinica == null || nombre == null || nombre.trim().length() < 3 || first < 0 || max <= 0) {
            return List.of();
        }
        try {
            TypedQuery<PersonaRol> query = em.createNamedQuery("PersonaRol.findByClinicaPersonaNombre", PersonaRol.class);
            query.setParameter("idClinica", idClinica);
            query.setParameter("filtro", "%" + nombre.trim().toUpperCase() + "%");
            query.setFirstResult(first);
            query.setMaxResults(max);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(PersonaRolService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudo buscar personal por clínica", ex);
        }
    }
    
    
    
    
    
    
}
