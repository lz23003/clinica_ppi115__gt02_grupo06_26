package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.OrdenExamen;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class OrdenExamenDAO extends DefaultDAO<OrdenExamen>{


    public OrdenExamenDAO() {
        super(OrdenExamen.class);
    }

    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<OrdenExamen> buscarPorConsultaProcedimientoPaso(UUID idConsultaProcedimientoPaso){
        try{
            if(idConsultaProcedimientoPaso!=null){
                String jpql = "SELECT oe FROM OrdenExamen oe " +
                        "WHERE oe.idConsultaProcedimientoPaso.idConsultaProcedimientoPaso = :idPaso";

                TypedQuery<OrdenExamen> query=em.createQuery(jpql,OrdenExamen.class);
                query.setParameter("idPaso",idConsultaProcedimientoPaso);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(OrdenExamen.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

        }

        return List.of();
    }

    public List<OrdenExamen> buscarPorRangoFechas(Date inicio, Date fin) {
        try {
            if (inicio != null && fin != null) {
                String jpql = "SELECT oe FROM OrdenExamen oe " +
                        "WHERE oe.fechaCreacion BETWEEN :inicio AND :fin";
                TypedQuery<OrdenExamen> query = em.createQuery(jpql, OrdenExamen.class);
                query.setParameter("inicio", inicio);
                query.setParameter("fin", fin);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(OrdenExamenDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }





}
