package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;

import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

@Named
@ViewScoped
public class CambiarRolModel implements Serializable {

    @Inject
    private SesionUsuario sesionUsuario;

    @Inject
    private ClinicaDAO clinicaDao;
    @Inject
    private RolDAO rolDao;
    @Inject
    private PersonaDAO personaDao;
    @Inject
    private PersonaRolDAO personaRolDao;

    private List<Clinica> clinicasDisponibles;
    private List<Rol> rolesDisponibles;
    private List<Persona> personasDisponibles;

    private Clinica clinicaSeleccionada;
    private Rol rolSeleccionado;
    private Persona personaSeleccionada;

    @PostConstruct
    public void init() {
        clinicasDisponibles = clinicaDao.listarActivas();
        rolesDisponibles = rolDao.listarActivos();      
        personasDisponibles = new ArrayList<>(); 
    }
    
    public void actualizarPersonas() {
        personasDisponibles = new ArrayList<>();
        
        if (clinicaSeleccionada != null && rolSeleccionado != null) {
            List<PersonaRol> asignaciones = personaRolDao.buscarPorClinica(clinicaSeleccionada.getIdClinica());
            
            for (PersonaRol pr : asignaciones) {
                if (pr.getIdRol().equals(rolSeleccionado) && !personasDisponibles.contains(pr.getIdPersona())) {
                    personasDisponibles.add(pr.getIdPersona());
                }
            }
        }
    }

    public void aplicarCambioRol() {
        if (personaSeleccionada != null && rolSeleccionado != null && clinicaSeleccionada != null) {
            String nombreCompleto = personaSeleccionada.getNombres() + " " + personaSeleccionada.getApellidos();
            sesionUsuario.setNombreUsuario(nombreCompleto);
            sesionUsuario.setRolUsuario(rolSeleccionado.getNombre());
            sesionUsuario.setClinicaUsuario(clinicaSeleccionada.getNombre());
        } else {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "crud");
        
        String summary = bundle.getString("mensaje.error");
        String detail = bundle.getString("cambiarRol.error.camposRequeridos");
        
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }
    }

    public List<Clinica> getClinicasDisponibles() {
        return clinicasDisponibles;
    }

    public List<Rol> getRolesDisponibles() {
        return rolesDisponibles;
    }

    public List<Persona> getPersonasDisponibles() {
        return personasDisponibles;
    }

    public Clinica getClinicaSeleccionada() {
        return clinicaSeleccionada;
    }

    public void setClinicaSeleccionada(Clinica clinicaSeleccionada) {
        this.clinicaSeleccionada = clinicaSeleccionada;
    }

    public Rol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(Rol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public Persona getPersonaSeleccionada() {
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(Persona personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }
}
