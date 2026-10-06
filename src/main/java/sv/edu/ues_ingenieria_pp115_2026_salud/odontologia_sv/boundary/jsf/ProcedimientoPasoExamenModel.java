package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoExamen;

@Named
@ViewScoped
public class ProcedimientoPasoExamenModel extends DefaultModel<ProcedimientoPasoExamen> implements Serializable {

    @Inject
    private ProcedimientoPasoExamenDAO procedimientoPasoExamenDao;

    @Inject
    private ProcedimientoPasoDAO procedimientoPasoDao;

    @Inject
    private ExamenDAO examenDao;

    private List<ProcedimientoPaso> listaPasos;
    private List<Examen> listaExamenes;

    @PostConstruct
    public void inicializar() {
        listaPasos = procedimientoPasoDao.findRange(0, 1000);
        listaExamenes = examenDao.listarActivos();
    }

    @Override
    protected Object getId(ProcedimientoPasoExamen object) {
        return object != null ? object.getIdProcedimientoPasoExamen() : null;
    }

    @Override
    protected DefaultDAO<ProcedimientoPasoExamen> getDao() {
        return procedimientoPasoExamenDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected ProcedimientoPasoExamen crearEntidad() {
        ProcedimientoPasoExamen ppe = new ProcedimientoPasoExamen();
        ppe.setIdProcedimientoPasoExamen(UUID.randomUUID());
        ppe.setFechaCreacion(new Date());
        ppe.setActivo(true);
        return ppe;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.procedimientoPasoExamen");
    }

    public List<ProcedimientoPaso> getListaPasos() {
        return listaPasos;
    }

    public List<Examen> getListaExamenes() {
        return listaExamenes;
    }
}