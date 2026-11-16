
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.OrdenExamen;

@Stateless
public class OrdenExamenService extends DefaultService<OrdenExamen> {

    @PersistenceContext(unitName = "Clinica_PU")
    EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrdenExamenService() {
        super(OrdenExamen.class);
    }
    
    
public List<OrdenExamen> buscarPorPaso(UUID idConsultaProcedimientoPaso) {
    try {
        String jpql = "SELECT o FROM OrdenExamen o " +
                      "WHERE o.idConsultaProcedimientoPaso.idConsultaProcedimientoPaso = :idPaso";
        TypedQuery<OrdenExamen> query = em.createQuery(jpql, OrdenExamen.class);
        query.setParameter("idPaso", idConsultaProcedimientoPaso);
        return query.getResultList();
    } catch (Exception ex) {
        Logger.getLogger(OrdenExamenService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    return List.of();
}
   
   
    
}
