
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;

@Stateless
public class MedioContactoService extends DefaultService<MedioContacto>{

    public MedioContactoService() {
        super(MedioContacto.class);
    }
    
    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em ;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
     public List<MedioContacto> buscarPorPersona(UUID idPersona) {
        try {
            if (idPersona != null) {
                TypedQuery<MedioContacto> query = em.createNamedQuery("MedioContacto.findByPersona", MedioContacto.class);
                query.setParameter("idPersona", idPersona);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(MedioContactoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<MedioContacto> buscarPorTipoMedioContacto(UUID idTipoMedioContacto) {
        try {
            if (idTipoMedioContacto != null) {
                TypedQuery<MedioContacto> query = em.createNamedQuery("MedioContacto.findByTipoMedioContacto", MedioContacto.class);
                query.setParameter("idTipoMedioContacto", idTipoMedioContacto);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(MedioContactoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<MedioContacto> buscarPorValor(String valor) {
        try {
            if (valor != null) {
                TypedQuery<MedioContacto> query = em.createNamedQuery("MedioContacto.findByValorLike", MedioContacto.class);
                query.setParameter("valor", "%" + valor.trim() + "%");
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(MedioContactoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<MedioContacto> buscarPorPersonaYTipo(UUID idPersona, UUID idTipoMedioContacto) {
        try {
            if (idPersona != null && idTipoMedioContacto != null) {
                TypedQuery<MedioContacto> query = em.createNamedQuery("MedioContacto.findByPersonaYTipo", MedioContacto.class);
                query.setParameter("idPersona", idPersona);
                query.setParameter("idTipoMedioContacto", idTipoMedioContacto);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(MedioContactoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    
}
