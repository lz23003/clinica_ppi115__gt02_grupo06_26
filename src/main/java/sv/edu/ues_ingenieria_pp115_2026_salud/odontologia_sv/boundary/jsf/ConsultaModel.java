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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;

@Named
@ViewScoped
public class ConsultaModel extends DefaultModel<Consulta> implements Serializable {

    @Inject
    private ConsultaDAO consultaDao;

    @Inject
    private PersonaRolDAO personaRolDao;

    private List<PersonaRol> listaPersonaRoles;

    @PostConstruct
    public void inicializar() {
        listaPersonaRoles = personaRolDao.findRange(0, 1000); 
    }

    @Override
    protected Object getId(Consulta object) {
        return object != null ? object.getIdConsulta() : null;
    }

    @Override
    protected DefaultDAO<Consulta> getDao() {
        return consultaDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Consulta crearEntidad() {
        Consulta consulta = new Consulta();
        consulta.setIdConsulta(UUID.randomUUID());
        consulta.setFechaInicio(new Date());
        return consulta;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.consulta");
    }

    public List<PersonaRol> getListaPersonaRoles() {
        return listaPersonaRoles;
    }
}