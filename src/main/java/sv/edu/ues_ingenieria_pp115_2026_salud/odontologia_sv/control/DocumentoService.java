
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;

@Stateless
public class DocumentoService extends DefaultService<Documento>{

    public DocumentoService() {
        super(Documento.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Documento> buscarPorPersona(UUID idPersona) {
        try {
            if (idPersona != null) {
                TypedQuery<Documento> query = em.createNamedQuery("Documento.findByPersona", Documento.class);
                query.setParameter("idPersona", idPersona);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<Documento> buscarPorTipoDocumento(UUID idTipoDocumento) {
        try {
            if (idTipoDocumento != null) {
                TypedQuery<Documento> query = em.createNamedQuery("Documento.findByTipoDocumento", Documento.class);
                query.setParameter("idTipoDocumento", idTipoDocumento);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public Documento buscarPorValor(String valor) {
        try {
            if (valor != null) {
                TypedQuery<Documento> query = em.createNamedQuery("Documento.findByValor", Documento.class);
                query.setParameter("valor", valor.trim());
                
                List<Documento> resultados = query.getResultList();
                return resultados.isEmpty() ? null : resultados.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public List<Documento> buscarPorPersonaYTipo(UUID idPersona, UUID idTipoDocumento) {
        try {
            if (idPersona != null && idTipoDocumento != null) {
                TypedQuery<Documento> query = em.createNamedQuery("Documento.findByPersonaYTipo", Documento.class);
                query.setParameter("idPersona", idPersona);
                query.setParameter("idTipoDocumento", idTipoDocumento);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    
    
    
    
}
