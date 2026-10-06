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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.MedioContactoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoMedioContactoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

@Named
@ViewScoped
public class MedioContactoModel extends DefaultModel<MedioContacto> implements Serializable {

    @Inject
    private MedioContactoDAO medioContactoDao;

    @Inject
    private PersonaDAO personaDao;

    @Inject
    private TipoMedioContactoDAO tipoMedioContactoDao;

    private List<Persona> listaPersonas;
    private List<TipoMedioContacto> listaTipos;

    @PostConstruct
    public void inicializar() {
        listaPersonas = personaDao.findRange(0, 1000);
        listaTipos = tipoMedioContactoDao.findRange(0, 100);
    }

    @Override
    protected Object getId(MedioContacto object) {
        return object != null ? object.getIdMedioContacto() : null;
    }

    @Override
    protected DefaultDAO<MedioContacto> getDao() {
        return medioContactoDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected MedioContacto crearEntidad() {
        MedioContacto mc = new MedioContacto();
        mc.setIdMedioContacto(UUID.randomUUID());
        mc.setFechaCreacion(new Date());
        return mc;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.medioContacto");
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public List<TipoMedioContacto> getListaTipos() {
        return listaTipos;
    }
}