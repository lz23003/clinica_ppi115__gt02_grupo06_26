package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoSecuencia;

@Stateless
public class ProcedimientoPasoService extends DefaultService<ProcedimientoPaso> {

    public ProcedimientoPasoService() {
        super(ProcedimientoPaso.class);
    }

    @PersistenceContext(unitName = "Clinica_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

      public List<ProcedimientoPaso> buscarPorProcedimiento(UUID idProcedimiento) {
        try {
            if (idProcedimiento != null) {
                // 1. Traemos todos los pasos
                String jpql = "SELECT pp FROM ProcedimientoPaso pp " +
                              "WHERE pp.idProcedimiento.idProcedimiento = :idProcedimiento";
                TypedQuery<ProcedimientoPaso> query = em.createQuery(jpql, ProcedimientoPaso.class);
                query.setParameter("idProcedimiento", idProcedimiento);
                List<ProcedimientoPaso> todos = query.getResultList();

                if (todos.isEmpty()) return List.of();

                // 2. Encontramos el primer paso (el que NO tiene ANTERIOR)
                ProcedimientoPaso primerPaso = null;
                for (ProcedimientoPaso p : todos) {
                    String jpqlSeq = "SELECT s FROM ProcedimientoPasoSecuencia s " +
                                     "WHERE s.idProcedimientoPaso.idProcedimientoPaso = :idPaso " +
                                     "AND s.tipoSecuencia = 'ANTERIOR'";
                    TypedQuery<ProcedimientoPasoSecuencia> qSeq = em.createQuery(jpqlSeq, ProcedimientoPasoSecuencia.class);
                    qSeq.setParameter("idPaso", p.getIdProcedimientoPaso());
                    if (qSeq.getResultList().isEmpty()) {
                        primerPaso = p;
                        break;
                    }
                }

                if (primerPaso == null) return todos;

                // 3. Construimos la lista ordenada
                List<ProcedimientoPaso> ordenados = new java.util.ArrayList<>();
                ProcedimientoPaso actual = primerPaso;

                while (actual != null) {
                    ordenados.add(actual);

                    // Buscamos el SIGUIENTE (el que tiene ANTERIOR = al paso actual)
                    String jpqlSig = "SELECT s FROM ProcedimientoPasoSecuencia s " +
                                     "WHERE s.idProcedimientoPasoReferencia = :idActual " +
                                     "AND s.tipoSecuencia = 'ANTERIOR'";
                    TypedQuery<ProcedimientoPasoSecuencia> qSig = em.createQuery(jpqlSig, ProcedimientoPasoSecuencia.class);
                    qSig.setParameter("idActual", actual.getIdProcedimientoPaso());
                    List<ProcedimientoPasoSecuencia> siguientes = qSig.getResultList();

                    if (siguientes.isEmpty()) {
                        actual = null;
                    } else {
                        actual = siguientes.get(0).getIdProcedimientoPaso();
                    }
                }

                return ordenados;
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ProcedimientoPaso> buscarPorNombre(String nombre) {
        try {
            if (nombre != null) {
                TypedQuery<ProcedimientoPaso> query = em.createNamedQuery("ProcedimientoPaso.findByNombre", ProcedimientoPaso.class);
                query.setParameter("nombre", "%" + nombre.trim().toUpperCase() + "%");
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ProcedimientoPaso> buscarPasosFinales() {
        try {
            String jpql = "SELECT pp FROM ProcedimientoPaso pp WHERE pp.indicaFin = true";
            TypedQuery<ProcedimientoPaso> query = em.createQuery(jpql, ProcedimientoPaso.class);
            return query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public List<ProcedimientoPaso> buscarPorRol(UUID idRol) {
        try {
            if (idRol != null) {
                String jpql = "SELECT pp FROM ProcedimientoPaso pp WHERE pp.idRol.idRol = :idRol";
                TypedQuery<ProcedimientoPaso> query = em.createQuery(jpql, ProcedimientoPaso.class);
                query.setParameter("idRol", idRol);
                return query.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcedimientoPasoService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }


}
