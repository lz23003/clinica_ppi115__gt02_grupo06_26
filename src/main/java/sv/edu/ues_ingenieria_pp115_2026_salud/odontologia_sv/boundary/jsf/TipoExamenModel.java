
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;


@Named
@ViewScoped
public class TipoExamenModel extends DefaultModel<TipoExamen> implements Serializable{
    
     @Inject
    private TipoExamenService tipoExamenService;

    @Inject
    private FacesContext facesContext;

    // =============================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS
    // =============================================

    @Override
    protected Object getId(TipoExamen object) {
        return object != null ? object.getIdTipoExamen() : null;
    }

    @Override
    protected DefaultService<TipoExamen> getService() {
        return tipoExamenService;
    }

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected TipoExamen crearEntidad() {
        TipoExamen te = new TipoExamen();
        te.setIdTipoExamen(UUID.randomUUID());
        te.setActivo(true);
        return te;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.tipoExamen");
    }

    

    
}
