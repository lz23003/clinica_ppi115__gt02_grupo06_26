package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

@Named
@ViewScoped
public class ConsultaProcedimientoModel extends DefaultModel<ConsultaProcedimiento> implements Serializable {

    @Inject
    private ConsultaProcedimientoDAO consultaProcedimientoDao;

    @Inject
    private ConsultaDAO consultaDao;

    @Inject
    private ProcedimientoDAO procedimientoDao;

    private List<Consulta> listaConsultas;
    private List<Procedimiento> listaProcedimientos;

    @PostConstruct
    public void inicializar() {
        listaConsultas = consultaDao.buscarPorConsultasActivas();
        listaProcedimientos = procedimientoDao.listarActivos();
    }

    @Override
    protected Object getId(ConsultaProcedimiento object) {
        return object != null ? object.getIdConsultaProcedimiento() : null;
    }

    @Override
    protected DefaultDAO<ConsultaProcedimiento> getDao() {
        return consultaProcedimientoDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected ConsultaProcedimiento crearEntidad() {
        ConsultaProcedimiento cp = new ConsultaProcedimiento();
        cp.setIdConsultaProcedimiento(UUID.randomUUID());
        cp.setFechaInicio(new Date());
        return cp;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.consultaProcedimiento");
    }

    public String obtenerNombreProcedimiento(Object idProcedimiento) {
        if (idProcedimiento == null) return "";
        for (Procedimiento p : listaProcedimientos) {
            if (p.getIdProcedimiento().equals(idProcedimiento)) {
                return p.getNombre();
            }
        }
        return "Desconocido";
    }

    public List<Consulta> getListaConsultas() {
        return listaConsultas;
    }

    public List<Procedimiento> getListaProcedimientos() {
        return listaProcedimientos;
    }
    
    public String formatearFechaConsulta(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(fecha);
    }
}