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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

@Named
@ViewScoped
public class PersonaRolModel extends DefaultModel<PersonaRol> implements Serializable {

    @Inject
    private PersonaRolDAO personaRolDao;

    @Inject
    private PersonaDAO personaDao;

    @Inject
    private RolDAO rolDao;

    @Inject
    private ClinicaDAO clinicaDao;

    private List<Persona> listaPersonas;
    private List<Rol> listaRoles;
    private List<Clinica> listaClinicas;

    @PostConstruct
    public void inicializar() {
        listaPersonas = personaDao.findRange(0, 1000);
        listaRoles = rolDao.findRange(0, 100);
        listaClinicas = clinicaDao.findRange(0, 100);
    }

    @Override
    protected Object getId(PersonaRol object) {
        return object != null ? object.getIdPersonaRol() : null;
    }

    @Override
    protected DefaultDAO<PersonaRol> getDao() {
        return personaRolDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected PersonaRol crearEntidad() {
        PersonaRol pr = new PersonaRol();
        pr.setIdPersonaRol(UUID.randomUUID());
        pr.setFechaCreacion(new Date());
        return pr;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.personaRol");
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public List<Rol> getListaRoles() {
        return listaRoles;
    }

    public List<Clinica> getListaClinicas() {
        return listaClinicas;
    }
}