package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoDocumentoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/tipos-documento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoDocumentoResource implements Serializable {

    @Inject
    private TipoDocumentoService tipoDocumentoService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/tipos-documento?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<TipoDocumento> lista = tipoDocumentoService.findRange(first, max);
            int total = tipoDocumentoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los tipos de documento")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los tipos de documento")
                    .header("X-Message", "Error al listar los tipos de documento")
                    .build();
        }
    }

    // GET /api/tipos-documento/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<TipoDocumento> lista = tipoDocumentoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los tipos de documento")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los tipos de documento")
                    .header("X-Message", "Error al obtener todos los tipos de documento")
                    .build();
        }
    }

    // GET /api/tipos-documento/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            TipoDocumento entidad = tipoDocumentoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el tipo de documento con id " + id)
                        .header("X-Message", "No se encontro el tipo de documento con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "El tipo de documento encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el tipo de documento por id")
                    .header("X-Message", "Error al obtener el tipo de documento por id")
                    .build();
        }
    }

    // POST /api/tipos-documento
    @POST
    public Response crear(TipoDocumento nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            
            nuevo.setIdTipoDocumento(UUID.randomUUID());

            tipoDocumentoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdTipoDocumento();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "El tipo de documento creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el tipo de documento: " + e.getMessage())
                    .header("X-Message", "Error al crear el tipo de documento")
                    .build();
        }
    }

    // PUT /api/tipos-documento/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, TipoDocumento modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            TipoDocumento actual = tipoDocumentoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el tipo de documento con id " + id)
                        .header("X-Message", "No se encontro el tipo de documento con id " + id)
                        .build();
            }

           
            actual.setNombre(modificado.getNombre());
            actual.setIndicaciones(modificado.getIndicaciones());
            actual.setExpresionRegular(modificado.getExpresionRegular());
            actual.setActivo(modificado.getActivo());

            TipoDocumento actualizado = tipoDocumentoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "El tipo de documento actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el tipo de documento: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el tipo de documento")
                    .build();
        }
    }

    // DELETE /api/tipos-documento/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            TipoDocumento entidad = tipoDocumentoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el tipo de documento con id " + id)
                        .header("X-Message", "No se encontro el tipo de documento con id " + id)
                        .build();
            }

            tipoDocumentoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "El tipo de documento eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el tipo de documento")
                    .header("X-Message", "Error al eliminar el tipo de documento")
                    .build();
        }
    }
}
