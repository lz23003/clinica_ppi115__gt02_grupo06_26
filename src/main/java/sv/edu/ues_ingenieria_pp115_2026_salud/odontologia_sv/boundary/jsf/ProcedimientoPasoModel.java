package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

@Named
@ViewScoped
public class ProcedimientoPasoModel extends DefaultModel<ProcedimientoPaso> implements Serializable {

    @Inject
    private ProcedimientoPasoDAO procedimientoPasoDao;

    @Inject
    private ProcedimientoDAO procedimientoDao;

    @Inject
    private RolDAO rolDao;

    private List<Procedimiento> listaProcedimientos;
    private List<Rol> listaRoles;
    
    private UUID idPasoDependencia;

    @PostConstruct
    public void inicializar() {
        listaProcedimientos = procedimientoDao.listarActivos();
        listaRoles = rolDao.findRange(0, 100); 
    }

    @Override
    protected Object getId(ProcedimientoPaso object) {
        return object != null ? object.getIdProcedimientoPaso() : null;
    }

    @Override
    protected DefaultDAO<ProcedimientoPaso> getDao() {
        return procedimientoPasoDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected ProcedimientoPaso crearEntidad() {
        ProcedimientoPaso paso = new ProcedimientoPaso();
        paso.setIdProcedimientoPaso(UUID.randomUUID());
        paso.setIndicaFin(false);
        return paso;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.procedimientoPaso");
    }
    
    @Override
    public void btnCancelarHandler(ActionEvent event) {
        super.btnCancelarHandler(event);
        this.idPasoDependencia = null;
    }

    @Override
    public void btnNuevoHandler(ActionEvent event) {
        super.btnNuevoHandler(event);
        this.idPasoDependencia = null;
    }

    public List<Procedimiento> getListaProcedimientos() {
        return listaProcedimientos;
    }

    public List<Rol> getListaRoles() {
        return listaRoles;
    }

    public UUID getIdPasoDependencia() {
        return idPasoDependencia;
    }

    public void setIdPasoDependencia(UUID idPasoDependencia) {
        this.idPasoDependencia = idPasoDependencia;
    }
}