
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

@Named
@ViewScoped
public class RolModel extends DefaultModel<Rol> implements Serializable{

    @Inject
    protected RolDAO rolDao;
    
    @Inject
    protected  FacesContext FacesContext; 
    
    @Override
    protected Object getId(Rol object) {
        return object!=null ? object.getIdRol() :null;
    }

    @Override
    protected DefaultDAO<Rol> getDao() {
        return rolDao;
    }

    @Override
    protected FacesContext getFacesContext() {
       return  FacesContext;
    }

    @Override
    protected Rol crearEntidad() {
        Rol rol=new Rol();
        rol.setIdRol(UUID.randomUUID());
        rol.setActivo(Boolean.TRUE);
        return rol;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.rol");
    }
    
}
