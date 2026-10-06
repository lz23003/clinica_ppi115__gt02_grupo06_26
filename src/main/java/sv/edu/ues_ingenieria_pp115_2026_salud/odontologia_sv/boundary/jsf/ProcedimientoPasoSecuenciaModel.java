package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoSecuencia;

@Named
@ViewScoped
public class ProcedimientoPasoSecuenciaModel extends DefaultModel<ProcedimientoPasoSecuencia> implements Serializable {

    @Inject
    private ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDao;

    @Inject
    private ProcedimientoPasoDAO procedimientoPasoDao;

    private List<ProcedimientoPaso> listaPasos;

    @PostConstruct
    public void inicializar() {
        listaPasos = procedimientoPasoDao.findRange(0, 1000); 
    }

    @Override
    protected Object getId(ProcedimientoPasoSecuencia object) {
        return object != null ? object.getIdProcedimientoPasoSecuencia() : null;
    }

    @Override
    protected DefaultDAO<ProcedimientoPasoSecuencia> getDao() {
        return procedimientoPasoSecuenciaDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected ProcedimientoPasoSecuencia crearEntidad() {
        ProcedimientoPasoSecuencia secuencia = new ProcedimientoPasoSecuencia();
        secuencia.setIdProcedimientoPasoSecuencia(UUID.randomUUID());
        return secuencia;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.procedimientoPasoSecuencia");
    }

    public List<ProcedimientoPaso> getListaPasos() {
        return listaPasos;
    }
}