package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DocumentoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@Stateless
@Path("/documentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentoResource implements Serializable {

    @Inject
    private DocumentoService documentoService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/documentos?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<Documento> lista = documentoService.findRange(first, max);
            int total = documentoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los documentos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los documentos")
                    .header("X-Message", "Error al listar los documentos")
                    .build();
        }
    }

    // GET /api/documentos/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Documento> lista = documentoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los documentos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los documentos")
                    .header("X-Message", "Error al obtener todos los documentos")
                    .build();
        }
    }

    // GET /api/documentos/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            Documento entidad = documentoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el documento con id " + id)
                        .header("X-Message", "No se encontro el documento con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "Documento encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el documento por id")
                    .header("X-Message", "Error al obtener el documento por id")
                    .build();
        }
    }

    // POST /api/documentos
    @POST
    public Response crear(Documento nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
          
            nuevo.setIdDocumento(UUID.randomUUID());

            documentoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdDocumento();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "Documento creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el documento: " + e.getMessage())
                    .header("X-Message", "Error al crear el documento")
                    .build();
        }
    }

    // PUT /api/documentos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Documento modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            Documento actual = documentoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el documento con id " + id)
                        .header("X-Message", "No se encontro el documento con id " + id)
                        .build();
            }

       
            actual.setValor(modificado.getValor());
            actual.setRutaFisica(modificado.getRutaFisica());
            actual.setIdPersona(modificado.getIdPersona());
            actual.setIdTipoDocumento(modificado.getIdTipoDocumento());

            Documento actualizado = documentoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "Documento actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el documento: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el documento")
                    .build();
        }
    }

    // DELETE /api/documentos/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            Documento entidad = documentoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el documento con id " + id)
                        .header("X-Message", "No se encontro el documento con id " + id)
                        .build();
            }

            documentoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "Documento eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el documento")
                    .header("X-Message", "Error al eliminar el documento")
                    .build();
        }
    }
}
