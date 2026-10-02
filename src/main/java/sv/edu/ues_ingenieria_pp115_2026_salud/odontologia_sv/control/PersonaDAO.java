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
public class PersonaDAO extends DefaultDAO<Persona>{
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    public PersonaDAO() {
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
           Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
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
                var jpql="SELECT p FROM Persona p JOIN p.documentoList d WHERE d.valor= :valor";
                
                TypedQuery<Persona> q=em.createQuery(jpql,Persona.class);
                q.setParameter("valor",valorDocumento.trim());
                var resultado=q.getResultList();
                return resultado.isEmpty() ? null: resultado.get(0);
                
            }
        }catch(Exception ex){
              Logger.getLogger(Persona.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);

        }
        return null;
    }

    
    
    
    
}
