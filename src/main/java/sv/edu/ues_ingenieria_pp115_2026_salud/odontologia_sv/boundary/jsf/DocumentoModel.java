package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DocumentoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoDocumentoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;

@Named
@ViewScoped
public class DocumentoModel extends DefaultModel<Documento> implements Serializable {

    @Inject
    private DocumentoService documentoService;
    @Inject
    private PersonaService personaService;
    @Inject
    private TipoDocumentoService tipoDocumentoService;

    @Override
    protected Object getId(Documento documento) {
        return documento == null ? null : documento.getIdDocumento();
    }

    @Override
    protected DefaultService<Documento> getService() {
        return documentoService;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Documento crearEntidad() {
        Documento documento = new Documento();
        documento.setIdDocumento(UUID.randomUUID());
        return documento;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.documento");
    }

    public List<Persona> completarPersona(String query) {
        return personaService.buscarPorNombreCompleto(query, 0, 10);
    }

    public List<TipoDocumento> completarTipoDocumento(String query) {
        return tipoDocumentoService.findByNombreLikeActivo(query, 0, 10);
    }
}
