package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DocumentoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;

@RequestScoped
@Path("/documentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentoResource extends DefaultResource<Documento> {

    @Inject
    private DocumentoService documentoService;

    @Override
    protected ServiceInterface<Documento> getService() {
        return documentoService;
    }

    @Override
    protected String getNombre() {
        return "documento";
    }

    @Override
    protected UUID getId(Documento entidad) {
        return entidad.getIdDocumento();
    }

    @Override
    protected void asignarId(Documento entidad, UUID id) {
        entidad.setIdDocumento(id);
    }

    @Override
    protected void copiarCampos(Documento actual, Documento modificado) {
        actual.setValor(modificado.getValor());
        actual.setRutaFisica(modificado.getRutaFisica());
        actual.setIdPersona(modificado.getIdPersona());
        actual.setIdTipoDocumento(modificado.getIdTipoDocumento());
    }

    // POST /api/v1/documentos
    @POST
    public Response crear(Documento nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/documentos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Documento modificado) {
        return actualizarEntidad(id, modificado);
    }
}
