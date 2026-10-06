package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenTipoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ExamenTipoExamen;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;

@Named
@ViewScoped
public class ExamenModel extends DefaultModel<Examen> implements Serializable {

    @Inject
    private ExamenDAO examenDao;
    @Inject
    private ExamenTipoExamenDAO examenTipoExamenDao;
    @Inject
    private TipoExamenDAO tipoExamenDao;

    private ExamenTipoExamen examenTipoExamenRegistro;
    private ESTADO_CRUD examenTipoExamenEstado = ESTADO_CRUD.NADA;
    private List<ExamenTipoExamen> tiposAsignados = new ArrayList<>();

    private int activa = 0;

    public void cambiarTab(TabChangeEvent event) {
        this.activa = event.getIndex();
    }

 
    @Override
    protected Object getId(Examen object) {
        return object != null ? object.getIdExamen() : null;
    }

    @Override
    protected DefaultDAO<Examen> getDao() {
        return examenDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Examen crearEntidad() {
        Examen e = new Examen();
        e.setIdExamen(UUID.randomUUID());
        e.setActivo(true);
        return e;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.examen");
    }

  

   
    @Override
    public void onRowSelect(SelectEvent<Examen> event) {
        super.onRowSelect(event);
        if (event != null && event.getObject() != null) {
            UUID idExamen = event.getObject().getIdExamen();
            tiposAsignados = examenTipoExamenDao.buscarPorExamen(idExamen);
            this.activa = 0;
        }
    }
    
    public void onExamenTipoExamenSelect(SelectEvent<ExamenTipoExamen> event) {
    if (event != null && event.getObject() != null) {
        this.examenTipoExamenRegistro = event.getObject();
        this.examenTipoExamenEstado = ESTADO_CRUD.MODIFICAR;
    }
}

    @Override
    public void btnNuevoHandler(ActionEvent event) {
        super.btnNuevoHandler(event);
        resetSubEstados();
        this.activa = 0;
    }

    @Override
    public void btnCancelarHandler(ActionEvent event) {
        super.btnCancelarHandler(event);
        resetSubEstados();
        this.activa = 0;
    }

    private void resetSubEstados() {
        this.examenTipoExamenRegistro = null;
        this.examenTipoExamenEstado = ESTADO_CRUD.NADA;
        this.tiposAsignados = new ArrayList<>();
    }

 
    public void btnNuevoTipoExamenHandler(ActionEvent event) {
        ExamenTipoExamen ete = new ExamenTipoExamen();
        ete.setIdExamenTipoExamen(UUID.randomUUID());
        ete.setFechaCreacion(new Date());
        ete.setIdExamen(registro);
        this.examenTipoExamenRegistro = ete;
        this.examenTipoExamenEstado = ESTADO_CRUD.CREAR;
    }

    public void btnGuardarTipoExamenHandler(ActionEvent event) {
        try {
            if (examenTipoExamenRegistro == null
                    || examenTipoExamenRegistro.getIdTipoExamen() == null) {
                enviarMensaje(getText("model.crud.advertencia"),
                        "Debe seleccionar un tipo de examen.",
                        FacesMessage.SEVERITY_WARN);
                return;
            }

            // Validar que el tipo sea activo
            if (!Boolean.TRUE.equals(examenTipoExamenRegistro.getIdTipoExamen().getActivo())) {
                enviarMensaje(getText("model.crud.advertencia"),
                        "No se puede asignar un tipo de examen inactivo.",
                        FacesMessage.SEVERITY_WARN);
                return;
            }

            if (examenTipoExamenEstado == ESTADO_CRUD.CREAR) {
                examenTipoExamenDao.crear(examenTipoExamenRegistro);
                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.creado"),
                        FacesMessage.SEVERITY_INFO);
            } else if (examenTipoExamenEstado == ESTADO_CRUD.MODIFICAR) {
                examenTipoExamenDao.modificar(examenTipoExamenRegistro);
                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.actualizado"),
                        FacesMessage.SEVERITY_INFO);
            }

            tiposAsignados = examenTipoExamenDao.buscarPorExamen(registro.getIdExamen());
            examenTipoExamenRegistro = null;
            examenTipoExamenEstado = ESTADO_CRUD.NADA;
        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"),
                    getText("model.crud.error.guardar"),
                    FacesMessage.SEVERITY_ERROR);
            ex.printStackTrace();
        }
    }

    public void btnCancelarTipoExamenHandler(ActionEvent event) {
        examenTipoExamenRegistro = null;
        examenTipoExamenEstado = ESTADO_CRUD.NADA;
    }

    
    public List<TipoExamen> completarTipoExamen(String query) {
        return tipoExamenDao.findByNombreLikeActivo(query, 0, 10);
    }

 
    public ExamenTipoExamen getExamenTipoExamenRegistro() {
        return examenTipoExamenRegistro;
    }

    public void setExamenTipoExamenRegistro(ExamenTipoExamen e) {
        this.examenTipoExamenRegistro = e;
    }

    public ESTADO_CRUD getExamenTipoExamenEstado() {
        return examenTipoExamenEstado;
    }

    public void setExamenTipoExamenEstado(ESTADO_CRUD e) {
        this.examenTipoExamenEstado = e;
    }

    public List<ExamenTipoExamen> getTiposAsignados() {
        return tiposAsignados;
    }

    public void setTiposAsignados(List<ExamenTipoExamen> l) {
        this.tiposAsignados = l;
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }
}