package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;

import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolService;
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
    private ClinicaService clinicaService;
    @Inject
    private RolService rolService;
    @Inject
    private PersonaRolService personaRolService;

    private Clinica clinicaSeleccionada;
    private Rol rolSeleccionado;
    private Persona personaSeleccionada;
    
    public void actualizarPersonas() {
        personaSeleccionada = null;
    }

    public List<Clinica> completarClinica(String query) {
        return clinicaService.findByNombreLikeActiva(query, 0, 10);
    }

    public List<Rol> completarRol(String query) {
        return rolService.findByNombreLikeActivo(query, 0, 10);
    }

    public List<Persona> completarPersona(String query) {
        if (clinicaSeleccionada == null || rolSeleccionado == null) {
            return List.of();
        }
        return personaRolService.buscarPorClinicaRolYNombre(
                        clinicaSeleccionada.getIdClinica(),
                        rolSeleccionado.getIdRol(),
                        query,
                        0,
                        10)
                .stream()
                .map(PersonaRol::getIdPersona)
                .distinct()
                .toList();
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
