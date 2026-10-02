package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;

@Stateless
public class ConsultaDAO extends DefaultDAO<Consulta>{

    public ConsultaDAO() {
        super(Consulta.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    
    @Override
    protected EntityManager getEntityManager() {
        return em;

    }
    
    public List<Consulta> buscarPorConsultasActivas(){
        try {
            String jpql="SELECT c FROM Consulta c WHERE c.fechaFin IS NULL ORDER BY c.fechaInicio ASC";
            
            TypedQuery<Consulta> query=em.createQuery(jpql,Consulta.class);
            return query.getResultList();
                    
        } catch (Exception ex) {
          Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);

          }
    
        return List.of();
    }
    
    public List<Consulta> buscarPorPaciente(UUID idPaciente){
        
        try {
            
            if(idPaciente!=null){
            String jpql="SELECT c FROM Consulta c "+
                    "JOIN c.idPersonaRol pr "+
                    "JOIN pr.idPersona p "+
                    "WHERE p.idPersona= :idPaciente "+
                    "ORDER BY c.fechaInicio DESC";
            
            TypedQuery<Consulta> query=em.createQuery(jpql,Consulta.class);
            query.setParameter("idPaciente", idPaciente);
            return query.getResultList();
        }
            
        } catch (Exception ex) {
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);

        }
        
        return List.of();
        
    }
    
    public List<Consulta> buscarPorRangoFechas(Date inicio, Date fin) {
        EntityManager em = getEntityManager();
        try {
            if (inicio != null && fin != null) {
                String jpql = "SELECT c FROM Consulta c WHERE c.fechaInicio BETWEEN :inicio AND :fin ORDER BY c.fechaInicio ASC";
                TypedQuery<Consulta> query = em.createQuery(jpql, Consulta.class);
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
                return query.getResultList();
            }
        } catch (Exception ex) {
              Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);

        }
        return List.of();
    }

    
     public List<Consulta> buscarPorPersonaRol(UUID idPersonaRol) {
        EntityManager em = getEntityManager();
        try {
            if (idPersonaRol != null) {
                String jpql = "SELECT c FROM Consulta c WHERE c.idPersonaRol.idPersonaRol = :idPersonaRol ORDER BY c.fechaInicio DESC";
                TypedQuery<Consulta> query = em.createQuery(jpql, Consulta.class);
                query.setParameter("idPersonaRol", idPersonaRol);
                return query.getResultList();
            }
        } catch (Exception ex) {
              Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE,ex.getMessage(),ex);
        }
        return List.of();
    }

    
    
}
