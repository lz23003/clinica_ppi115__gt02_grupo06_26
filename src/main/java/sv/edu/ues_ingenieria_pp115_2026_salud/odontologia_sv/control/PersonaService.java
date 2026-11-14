package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;

@Stateless
public class PersonaService extends DefaultService<Persona>{
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    public PersonaService() {
        super(Persona.class);
    }

    @Override
    protected EntityManager getEntityManager() {
         return em;
    }
    
    public List<Persona> buscarPorNombre(String nombres){
       try{
        if(nombres!=null){
           TypedQuery<Persona> q=em.createNamedQuery("Persona.findByNombres",Persona.class);
            q.setParameter("nombres","%"+nombres.trim().toUpperCase()+"%");
            return q.getResultList();
        }
       }catch(Exception ex){
           Logger.getLogger(PersonaService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
       }
       return List.of();
    }
    
    
    public List<Persona> buscarPorApellido(String apellidos){
        try {
            if(apellidos!=null){
                TypedQuery<Persona> q=em.createNamedQuery("Persona.findByApellidos",Persona.class);
                q.setParameter("apellidos","%"+apellidos.trim().toUpperCase()+"%");
                return q.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);
        }
        return List.of();
    }
    
    
    public Persona buscarPorDocumento(String valorDocumento){
        try{
            if(valorDocumento!=null){
                TypedQuery<Persona> q=em.createNamedQuery("Persona.findByDocumento",Persona.class);
                q.setParameter("valor",valorDocumento.trim());
                var resultado=q.getResultList();
                return resultado.isEmpty() ? null: resultado.get(0);
    
            }
        }catch(Exception ex){
              Logger.getLogger(Persona.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);

        }
        return null;
    }

    public List<Persona> buscarPorNombreCompleto(final String query, int first, int max) {
        if (query == null || query.trim().length() < 3 || first < 0 || max <= 0) {
            return List.of();
        }
        try {
            TypedQuery<Persona> namedQuery = em.createNamedQuery("Persona.findByNombreCompleto", Persona.class);
            namedQuery.setParameter("filtro", "%" + query.trim().toUpperCase() + "%");
            namedQuery.setFirstResult(first);
            namedQuery.setMaxResults(max);
            return namedQuery.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(PersonaService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudieron buscar personas por nombre", ex);
        }
    }
    
    
    
    
}
