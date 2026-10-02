package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;

@Stateless
public class ClinicaDAO extends DefaultDAO<Clinica> {

    public ClinicaDAO() {
        super(Clinica.class);
    }

    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Clinica> buscarPorNombre(String nombre) {
        try {
            if (nombre != null) {
                TypedQuery<Clinica> query = em.createNamedQuery("Clinica.findByNombre", Clinica.class);
                query.setParameter("nombre", "%" + nombre.trim().toUpperCase() + "%");
                return query.getResultList();
            }

        } catch (Exception ex) {
            Logger.getLogger(Clinica.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

        }

        return List.of();
    }

    public List<Clinica> listarActivas() {
        try {
            TypedQuery<Clinica> query = em.createNamedQuery("Clinica.findByActivo", Clinica.class);
            query.setParameter("activo", true);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ClinicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<Clinica> buscarPorTipo(String tipo) {
        try {
            if (tipo != null) {
                TypedQuery<Clinica> query = em.createNamedQuery("Clinica.findByTipo", Clinica.class);
                query.setParameter("tipo", tipo.trim().toUpperCase());
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ClinicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

}
