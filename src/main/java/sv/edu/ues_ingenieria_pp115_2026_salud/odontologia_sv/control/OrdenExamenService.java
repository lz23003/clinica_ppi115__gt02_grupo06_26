
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
    
    
public List<OrdenExamen> buscarPorPaso(UUID idPasoPlantilla) {
    try {
        // Buscamos las órdenes cuyo paso (idConsultaProcedimientoPaso) esté vinculado a la plantilla
        String jpql = "SELECT o FROM OrdenExamen o " +
                      "WHERE o.idConsultaProcedimientoPaso.idConsultaProcedimiento.idConsultaProcedimiento = :idPasoPlantilla";
        // 🔥 Nota: Esta consulta asume que tienes una relación entre el paso ejecutado y la plantilla.
        // Si no la tienes, tendremos que hacer una consulta más compleja.
        
        // Si no existe la relación directa, usamos una subconsulta:
        String jpql2 = "SELECT o FROM OrdenExamen o " +
                       "WHERE o.idConsultaProcedimientoPaso.idProcedimientoPaso.idProcedimientoPaso = :idPasoPlantilla";
        
        TypedQuery<OrdenExamen> query = em.createQuery(jpql2, OrdenExamen.class);
        query.setParameter("idPasoPlantilla", idPasoPlantilla);
        return query.getResultList();
    } catch (Exception ex) {
        Logger.getLogger(OrdenExamenService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
    }
    return List.of();
}
   
   
    
}
