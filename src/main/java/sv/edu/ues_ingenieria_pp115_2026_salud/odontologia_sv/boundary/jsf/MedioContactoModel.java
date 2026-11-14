package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.MedioContactoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoMedioContactoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

@Named
@ViewScoped
public class MedioContactoModel extends DefaultModel<MedioContacto> implements Serializable {

    @Inject
    private MedioContactoService medioContactoService;
    @Inject
    private PersonaService personaService;
    @Inject
    private TipoMedioContactoService tipoMedioContactoService;

    @Override
    protected Object getId(MedioContacto medioContacto) {
        return medioContacto == null ? null : medioContacto.getIdMedioContacto();
    }

    @Override
    protected DefaultService<MedioContacto> getService() {
        return medioContactoService;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected MedioContacto crearEntidad() {
        MedioContacto medioContacto = new MedioContacto();
        medioContacto.setIdMedioContacto(UUID.randomUUID());
        medioContacto.setFechaCreacion(new Date());
        return medioContacto;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.medioContacto");
    }

    public List<Persona> completarPersona(String query) {
        return personaService.buscarPorNombreCompleto(query, 0, 10);
    }

    public List<TipoMedioContacto> completarTipoMedioContacto(String query) {
        return tipoMedioContactoService.findByNombreLikeActivo(query, 0, 10);
    }
}
