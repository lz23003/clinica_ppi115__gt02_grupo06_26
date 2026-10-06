package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DocumentoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoDocumentoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;

@Named
@ViewScoped
public class DocumentoModel extends DefaultModel<Documento> implements Serializable {

    @Inject
    private DocumentoDAO documentoDao;

    @Inject
    private PersonaDAO personaDao;

    @Inject
    private TipoDocumentoDAO tipoDocumentoDao;

    private List<Persona> listaPersonas;
    private List<TipoDocumento> listaTiposDocumento;

    @PostConstruct
    public void inicializar() {
        listaPersonas = personaDao.findRange(0, 1000);
        listaTiposDocumento = tipoDocumentoDao.findRange(0, 100);
    }

    @Override
    protected Object getId(Documento object) {
        return object != null ? object.getIdDocumento() : null;
    }

    @Override
    protected DefaultDAO<Documento> getDao() {
        return documentoDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Documento crearEntidad() {
        Documento doc = new Documento();
        doc.setIdDocumento(UUID.randomUUID());
        return doc;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.documento");
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public List<TipoDocumento> getListaTiposDocumento() {
        return listaTiposDocumento;
    }
}