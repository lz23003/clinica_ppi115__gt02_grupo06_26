
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
public class DocumentoDAO extends DefaultDAO<Documento>{

    public DocumentoDAO() {
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
                String jpql = "SELECT d FROM Documento d WHERE d.idPersona.idPersona = :idPersona";
                TypedQuery<Documento> query = em.createQuery(jpql, Documento.class);
                query.setParameter("idPersona", idPersona);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<Documento> buscarPorTipoDocumento(UUID idTipoDocumento) {
        try {
            if (idTipoDocumento != null) {
                String jpql = "SELECT d FROM Documento d WHERE d.idTipoDocumento.idTipoDocumento = :idTipoDocumento";
                TypedQuery<Documento> query = em.createQuery(jpql, Documento.class);
                query.setParameter("idTipoDocumento", idTipoDocumento);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public Documento buscarPorValor(String valor) {
        try {
            if (valor != null) {
                String jpql = "SELECT d FROM Documento d WHERE d.valor = :valor";
                TypedQuery<Documento> query = em.createQuery(jpql, Documento.class);
                query.setParameter("valor", valor.trim());
                
                List<Documento> resultados = query.getResultList();
                return resultados.isEmpty() ? null : resultados.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public List<Documento> buscarPorPersonaYTipo(UUID idPersona, UUID idTipoDocumento) {
        try {
            if (idPersona != null && idTipoDocumento != null) {
                String jpql = "SELECT d FROM Documento d " +
                              "WHERE d.idPersona.idPersona = :idPersona " +
                              "AND d.idTipoDocumento.idTipoDocumento = :idTipoDocumento";
                TypedQuery<Documento> query = em.createQuery(jpql, Documento.class);
                query.setParameter("idPersona", idPersona);
                query.setParameter("idTipoDocumento", idTipoDocumento);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(DocumentoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
    
}
