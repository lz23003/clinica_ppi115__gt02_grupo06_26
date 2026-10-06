package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;

@Named
@ViewScoped
public class ClinicaModel extends DefaultModel<Clinica> implements Serializable {

    @Inject
    private ClinicaDAO clinicaDao;

    @Override
    protected Object getId(Clinica object) {
        return object != null ? object.getIdClinica() : null;
    }

    @Override
    protected DefaultDAO<Clinica> getDao() {
        return clinicaDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Clinica crearEntidad() {
        Clinica clinica = new Clinica();
        clinica.setIdClinica(UUID.randomUUID());
        clinica.setActivo(true); 
        return clinica;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.clinica");
    }
}