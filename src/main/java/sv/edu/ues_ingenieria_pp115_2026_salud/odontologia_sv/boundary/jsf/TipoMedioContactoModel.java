package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoMedioContactoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

@Named
@ViewScoped
public class TipoMedioContactoModel extends DefaultModel<TipoMedioContacto> implements Serializable{

    @Inject
    protected FacesContext FacesContext;
    
    @Inject
    protected TipoMedioContactoService tipoMedioContactoService;
    
    @Override
    protected Object getId(TipoMedioContacto object) {
        return object!=null ? object.getIdTipoMedioContacto() :null;
    }

    @Override
    protected DefaultService<TipoMedioContacto> getService() {
        return tipoMedioContactoService;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext;
    }

    @Override
    protected TipoMedioContacto crearEntidad() {
        TipoMedioContacto tipoMedioContacto=new TipoMedioContacto();
        tipoMedioContacto.setIdTipoMedioContacto(UUID.randomUUID());
        tipoMedioContacto.setExpresionRegular(".");
        tipoMedioContacto.setActivo(Boolean.TRUE);
        return tipoMedioContacto;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.tipoMedioContacto");
    }
    
}
