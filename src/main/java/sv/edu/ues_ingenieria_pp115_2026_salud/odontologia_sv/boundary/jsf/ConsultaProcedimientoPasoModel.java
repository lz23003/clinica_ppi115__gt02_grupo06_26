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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

@Named
@ViewScoped
public class ConsultaProcedimientoPasoModel extends DefaultModel<ConsultaProcedimientoPaso> implements Serializable {

    @Inject
    private ConsultaProcedimientoPasoDAO consultaProcedimientoPasoDao;

    @Inject
    private ConsultaProcedimientoDAO consultaProcedimientoDao;

    @Inject
    private PersonaRolDAO personaRolDao;

    @Inject
    private ProcedimientoDAO procedimientoDao;

    private List<ConsultaProcedimiento> listaConsultaProcedimientos;
    private List<PersonaRol> listaPersonaRoles;
    private List<Procedimiento> listaProcedimientos;

    @PostConstruct
    public void inicializar() {
        listaConsultaProcedimientos = consultaProcedimientoDao.findRange(0, 1000);
        listaPersonaRoles = personaRolDao.findRange(0, 1000);
        listaProcedimientos = procedimientoDao.findRange(0, 1000);
    }

    @Override
    protected Object getId(ConsultaProcedimientoPaso object) {
        return object != null ? object.getIdConsultaProcedimientoPaso() : null;
    }

    @Override
    protected DefaultDAO<ConsultaProcedimientoPaso> getDao() {
        return consultaProcedimientoPasoDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected ConsultaProcedimientoPaso crearEntidad() {
        ConsultaProcedimientoPaso paso = new ConsultaProcedimientoPaso();
        paso.setIdConsultaProcedimientoPaso(UUID.randomUUID());
        paso.setFechaInicio(new Date());
        paso.setEstado("PENDIENTE");
        return paso;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.consultaProcedimientoPaso");
    }

    public String obtenerInfoConsultaProcedimiento(ConsultaProcedimiento cp) {
        if (cp == null) return "";
        
        String fechaConsulta = (cp.getIdConsulta() != null && cp.getIdConsulta().getFechaInicio() != null) 
                ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(cp.getIdConsulta().getFechaInicio()) 
                : "Sin fecha";
        
        String nombreProcedimiento = "Desconocido";
        if (cp.getIdProcedimiento() != null && listaProcedimientos != null) {
            for (Procedimiento p : listaProcedimientos) {
                if (p.getIdProcedimiento().equals(cp.getIdProcedimiento())) {
                    nombreProcedimiento = p.getNombre();
                    break;
                }
            }
        }
        return fechaConsulta + " - " + nombreProcedimiento;
    }

    public List<ConsultaProcedimiento> getListaConsultaProcedimientos() {
        return listaConsultaProcedimientos;
    }

    public List<PersonaRol> getListaPersonaRoles() {
        return listaPersonaRoles;
    }

    public List<Procedimiento> getListaProcedimientos() {
        return listaProcedimientos;
    }
}