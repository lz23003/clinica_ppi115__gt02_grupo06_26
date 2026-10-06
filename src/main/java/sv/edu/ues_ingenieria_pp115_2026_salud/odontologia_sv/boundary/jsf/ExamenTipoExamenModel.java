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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenTipoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ExamenTipoExamen;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;

@Named
@ViewScoped
public class ExamenTipoExamenModel extends DefaultModel<ExamenTipoExamen> implements Serializable {

    @Inject
    private ExamenTipoExamenDAO examenTipoExamenDao;
    
    @Inject
    private ExamenDAO examenDao;
    
    @Inject
    private TipoExamenDAO tipoExamenDao;

    private List<Examen> examenesDisponibles;
    private List<TipoExamen> tiposExamenDisponibles;

    @PostConstruct
    @Override
    public void inicializador() {
        super.inicializador();
        examenesDisponibles = examenDao.listarActivos();
        tiposExamenDisponibles = tipoExamenDao.listarActivos();
    }

    @Override
    protected Object getId(ExamenTipoExamen object) {
        return object != null ? object.getIdExamenTipoExamen() : null;
    }

    @Override
    protected DefaultDAO<ExamenTipoExamen> getDao() {
        return examenTipoExamenDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected ExamenTipoExamen crearEntidad() {
        ExamenTipoExamen ete = new ExamenTipoExamen();
        ete.setIdExamenTipoExamen(UUID.randomUUID());
        ete.setFechaCreacion(new Date()); 
        return ete;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.examenTipoExamen");
    }

    public List<Examen> getExamenesDisponibles() {
        return examenesDisponibles;
    }

    public List<TipoExamen> getTiposExamenDisponibles() {
        return tiposExamenDisponibles;
    }
}