
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoSecuencia;

@Stateless
public class ProcedimientoPasoSecuenciaDAO extends DefaultDAO<ProcedimientoPasoSecuencia>{

    public ProcedimientoPasoSecuenciaDAO() {
        super(ProcedimientoPasoSecuencia.class);
    }
    
    @PersistenceContext(unitName ="Clinica_PU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<ProcedimientoPasoSecuencia> buscarPorPaso(UUID idProcedimientoPaso) {
        try {
            if (idProcedimientoPaso != null) {
                String jpql = "SELECT pps FROM ProcedimientoPasoSecuencia pps " +
                              "WHERE pps.idProcedimientoPaso.idProcedimientoPaso = :idPaso";
                TypedQuery<ProcedimientoPasoSecuencia> query = em.createQuery(jpql, ProcedimientoPasoSecuencia.class);
                query.setParameter("idPaso", idProcedimientoPaso);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoSecuenciaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public ProcedimientoPasoSecuencia buscarSiguientePaso(UUID idProcedimientoPaso) {
        try {
            if (idProcedimientoPaso != null) {
                String jpql = "SELECT pps FROM ProcedimientoPasoSecuencia pps " +
                              "WHERE pps.idProcedimientoPaso.idProcedimientoPaso = :idPaso " +
                              "AND pps.tipoSecuencia = 'SIGUIENTE'";
                TypedQuery<ProcedimientoPasoSecuencia> query = em.createQuery(jpql, ProcedimientoPasoSecuencia.class);
                query.setParameter("idPaso", idProcedimientoPaso);
                
                List<ProcedimientoPasoSecuencia> resultados = query.getResultList();
                return resultados.isEmpty() ? null : resultados.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoSecuenciaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public List<ProcedimientoPasoSecuencia> buscarPorTipoSecuencia(String tipoSecuencia) {
        try {
            if (tipoSecuencia != null) {
                TypedQuery<ProcedimientoPasoSecuencia> query = em.createNamedQuery("ProcedimientoPasoSecuencia.findByTipoSecuencia", ProcedimientoPasoSecuencia.class);
                query.setParameter("tipoSecuencia", tipoSecuencia.trim().toUpperCase());
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoSecuenciaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
}
