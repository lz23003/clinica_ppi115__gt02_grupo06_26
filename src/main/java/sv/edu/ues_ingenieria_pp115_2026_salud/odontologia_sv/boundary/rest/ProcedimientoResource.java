package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/procedimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimientoResource implements Serializable {

    @Inject
    private ProcedimientoService procedimientoService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/procedimientos?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<Procedimiento> lista = procedimientoService.findRange(first, max);
            int total = procedimientoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los procedimientos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los procedimientos")
                    .header("X-Message", "Error al listar los procedimientos")
                    .build();
        }
    }

    // GET /api/procedimientos/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Procedimiento> lista = procedimientoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los procedimientos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los procedimientos")
                    .header("X-Message", "Error al obtener todos los procedimientos")
                    .build();
        }
    }

    // GET /api/procedimientos/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            Procedimiento entidad = procedimientoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el procedimiento con id " + id)
                        .header("X-Message", "No se encontro el procedimiento con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "Procedimiento encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el procedimiento por id")
                    .header("X-Message", "Error al obtener el procedimiento por id")
                    .build();
        }
    }

    // POST /api/procedimientos
    @POST
    public Response crear(Procedimiento nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            
            nuevo.setIdProcedimiento(UUID.randomUUID());

            procedimientoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdProcedimiento();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "Procedimiento creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el procedimiento: " + e.getMessage())
                    .header("X-Message", "Error al crear el procedimiento")
                    .build();
        }
    }

    // PUT /api/procedimientos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Procedimiento modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            Procedimiento actual = procedimientoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el procedimiento con id " + id)
                        .header("X-Message", "No se encontro el procedimiento con id " + id)
                        .build();
            }

          
            actual.setNombre(modificado.getNombre());
            actual.setActivo(modificado.getActivo());
            actual.setObservaciones(modificado.getObservaciones());

            Procedimiento actualizado = procedimientoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "Procedimiento actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el procedimiento: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el procedimiento")
                    .build();
        }
    }

    // DELETE /api/procedimientos/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            Procedimiento entidad = procedimientoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el procedimiento con id " + id)
                        .header("X-Message", "No se encontro el procedimiento con id " + id)
                        .build();
            }

            procedimientoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "Procedimiento eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el procedimiento")
                    .header("X-Message", "Error al eliminar el procedimiento")
                    .build();
        }
    }
}
