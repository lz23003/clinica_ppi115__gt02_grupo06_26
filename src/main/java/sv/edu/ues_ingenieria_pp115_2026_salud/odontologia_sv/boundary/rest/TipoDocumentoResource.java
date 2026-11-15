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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoDocumentoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;

@RequestScoped
@Path("/tipos-documento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoDocumentoResource extends DefaultResource<TipoDocumento> {

    @Inject
    private TipoDocumentoService tipoDocumentoService;

    @Override
    protected ServiceInterface<TipoDocumento> getService() {
        return tipoDocumentoService;
    }

    @Override
    protected String getNombre() {
        return "tipo documento";
    }

    @Override
    protected UUID getId(TipoDocumento entidad) {
        return entidad.getIdTipoDocumento();
    }

    @Override
    protected void asignarId(TipoDocumento entidad, UUID id) {
        entidad.setIdTipoDocumento(id);
    }

    @Override
    protected void copiarCampos(TipoDocumento actual, TipoDocumento modificado) {
        actual.setNombre(modificado.getNombre());
        actual.setIndicaciones(modificado.getIndicaciones());
        actual.setExpresionRegular(modificado.getExpresionRegular());
        actual.setActivo(modificado.getActivo());
    }

    // POST /api/v1/tipos-documento
    @POST
    public Response crear(TipoDocumento nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/tipos-documento/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, TipoDocumento modificado) {
        return actualizarEntidad(id, modificado);
    }
}
