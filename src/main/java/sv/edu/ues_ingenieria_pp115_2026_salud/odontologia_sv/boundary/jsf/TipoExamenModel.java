
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;


@Named
@ViewScoped
public class TipoExamenModel extends DefaultModel<TipoExamen> implements Serializable{
    
     @Inject
    private TipoExamenDAO tipoExamenDao;

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
    protected DefaultDAO<TipoExamen> getDao() {
        return tipoExamenDao;
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
