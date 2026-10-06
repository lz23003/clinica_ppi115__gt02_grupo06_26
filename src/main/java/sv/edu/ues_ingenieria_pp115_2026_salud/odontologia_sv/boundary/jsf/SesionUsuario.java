package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Named
@SessionScoped
public class SesionUsuario implements Serializable {

    private Map<String, Locale> idiomasDisponibles = new HashMap<>();
    private String idiomaSeleccionado = "es";
    private Locale locale = new Locale("es", "SV");

    private String nombreUsuario;
    private String rolUsuario;
    private String clinicaUsuario;

    @PostConstruct
    public void inicializador() {
        Locale espa = new Locale("es", "SV");
        Locale ingl = new Locale("en", "US");
        idiomasDisponibles.put("Español", espa);
        idiomasDisponibles.put("English", ingl);

        this.idiomaSeleccionado = "Español";
        this.locale = espa;

        this.nombreUsuario = "Invitado";
        this.rolUsuario = "Sin Asignar";
        this.clinicaUsuario = "Ninguna Clínica";
    }
    
    public void cerrarSesion() {
        this.nombreUsuario = "Invitado";
        this.rolUsuario = "Sin Asignar";
        this.clinicaUsuario = "Ninguna Clínica";
    }

    /**
     * Cambia el idioma de la aplicación.
     */
    public void cambiarIdioma(AjaxBehaviorEvent event) {
        try {
            Locale nuevoLocale = idiomasDisponibles.get(idiomaSeleccionado);

            if (nuevoLocale != null) {
                this.locale = nuevoLocale;

                FacesContext context = FacesContext.getCurrentInstance();
                if (context != null && context.getViewRoot() != null) {
                    context.getViewRoot().setLocale(nuevoLocale);
                }

                System.out.println("Idioma cambiado a: " + nuevoLocale);
            }
        } catch (Exception ex) {
            System.err.println(" Error al cambiar idioma: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // GETTERS Y SETTERS
    public Map<String, Locale> getIdiomasDisponibles() {
        return idiomasDisponibles;
    }

    public void setIdiomasDisponibles(Map<String, Locale> idiomasDisponibles) {
        this.idiomasDisponibles = idiomasDisponibles;
    }

    public String getIdiomaSeleccionado() {
        return idiomaSeleccionado;
    }

    public void setIdiomaSeleccionado(String idiomaSeleccionado) {
        this.idiomaSeleccionado = idiomaSeleccionado;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getClinicaUsuario() {
        return clinicaUsuario;
    }

    public void setClinicaUsuario(String clinicaUsuario) {
        this.clinicaUsuario = clinicaUsuario;
    }
}
