package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

@Stateless
public class ProcedimientoDAO extends DefaultDAO<Procedimiento>{

    public ProcedimientoDAO() {
        super(Procedimiento.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Procedimiento> buscarPorNombreProcedimientos(String nombreProcedimiento){
        try {
            if(nombreProcedimiento!=null){
                TypedQuery<Procedimiento> query=em.createNamedQuery("Procedimiento.findByNombre",Procedimiento.class);
                query.setParameter("nombre","%"+nombreProcedimiento.trim().toUpperCase()+"%");
                return query.getResultList();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return List.of();
    }
    
    public List<Procedimiento> listarActivos(){
        try {
            TypedQuery<Procedimiento> query=em.createNamedQuery("Procedimiento.findByActivo",Procedimiento.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        return List.of();
    }
    
    
    
}
