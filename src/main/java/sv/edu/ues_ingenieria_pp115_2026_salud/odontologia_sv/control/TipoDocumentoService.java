package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class TipoDocumentoService extends DefaultService<TipoDocumento> {

    public TipoDocumentoService() {
        super(TipoDocumento.class);
    }

    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TipoDocumento> buscarPorNombre(String nombre) {
        try {
            if (nombre != null) {
                TypedQuery<TipoDocumento> query = em.createNamedQuery("TipoDocumento.findByNombre", TipoDocumento.class);
                query.setParameter("nombre", "%" + nombre.trim().toUpperCase() + "%");
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoDocumentoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<TipoDocumento> listarActivos() {
        try {
            TypedQuery<TipoDocumento> query = em.createNamedQuery("TipoDocumento.findByActivo", TipoDocumento.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(TipoDocumentoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public TipoDocumento buscarPorExpresionRegular(String expresionRegular) {
        try {
            if (expresionRegular != null) {
                TypedQuery<TipoDocumento> query = em.createNamedQuery("TipoDocumento.findByExpresionRegular", TipoDocumento.class);
                query.setParameter("expresionRegular", expresionRegular);

                List<TipoDocumento> resultados = query.getResultList();
                return resultados.isEmpty() ? null : resultados.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoDocumentoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public List<TipoDocumento> findByNombreLikeActivo(final String query, int first, int max) {
        try {
            if(query!=null && query.trim().length() >= 3 && first>=0 && max>0){
                TypedQuery<TipoDocumento> q=em.createNamedQuery("TipoDocumento.findByNombreLikeActivo",TipoDocumento.class);
                q.setParameter("filtro","%"+query.trim().toUpperCase()+"%");
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoDocumentoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException("No se pudieron buscar tipos de documento", ex);
        }
        return List.of();
    }

}
