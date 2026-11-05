
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoDocumentoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;

@Named
@ViewScoped
public class TipoDocumentoModel extends DefaultModel<TipoDocumento> implements Serializable{

    @Inject
    protected TipoDocumentoService TipoDocumentoService;
    
    @Inject
    protected FacesContext FacesContext ;
    
    
    @Override
    protected Object getId(TipoDocumento object) {
        return object!=null ? object.getIdTipoDocumento():null;
    }

    @Override
    protected DefaultService<TipoDocumento> getService() {
        return TipoDocumentoService;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext;
    }

    @Override
    protected TipoDocumento crearEntidad() {
        TipoDocumento tipoDoc=new TipoDocumento();
        tipoDoc.setIdTipoDocumento(UUID.randomUUID());
        tipoDoc.setActivo(true);
        tipoDoc.setExpresionRegular(".");
        return tipoDoc;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.tipoDocumento");
    }   
    
}
