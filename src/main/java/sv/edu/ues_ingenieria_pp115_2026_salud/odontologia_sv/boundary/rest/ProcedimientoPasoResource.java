package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/procedimientos-pasos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimientoPasoResource implements Serializable {

    @Inject
    private ProcedimientoPasoService procedimientoPasoService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/procedimientos-pasos?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<ProcedimientoPaso> lista = procedimientoPasoService.findRange(first, max);
            int total = procedimientoPasoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los procedimientos-pasos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los procedimientos-pasos")
                    .header("X-Message", "Error al listar los procedimientos-pasos")
                    .build();
        }
    }

    // GET /api/procedimientos-pasos/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<ProcedimientoPaso> lista = procedimientoPasoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los procedimientos-pasos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los procedimientos-pasos")
                    .header("X-Message", "Error al obtener todos los procedimientos-pasos")
                    .build();
        }
    }

    // GET /api/procedimientos-pasos/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            ProcedimientoPaso entidad = procedimientoPasoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "El procedimiento-paso encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el procedimiento-paso por id")
                    .header("X-Message", "Error al obtener el procedimiento-paso por id")
                    .build();
        }
    }

    // POST /api/procedimientos-pasos
    @POST
    public Response crear(ProcedimientoPaso nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
          
            nuevo.setIdProcedimientoPaso(UUID.randomUUID());

            procedimientoPasoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdProcedimientoPaso();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "El procedimiento-paso creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el procedimiento-paso: " + e.getMessage())
                    .header("X-Message", "Error al crear el procedimiento-paso")
                    .build();
        }
    }

    // PUT /api/procedimientos-pasos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ProcedimientoPaso modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            ProcedimientoPaso actual = procedimientoPasoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso con id " + id)
                        .build();
            }

           
            actual.setNombre(modificado.getNombre());
            actual.setIndicaFin(modificado.getIndicaFin());
            actual.setIdProcedimiento(modificado.getIdProcedimiento());
            actual.setIdRol(modificado.getIdRol());

            ProcedimientoPaso actualizado = procedimientoPasoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "El procedimiento-paso actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el procedimiento-paso: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el procedimiento-paso")
                    .build();
        }
    }

    // DELETE /api/procedimientos-pasos/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            ProcedimientoPaso entidad = procedimientoPasoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso con id " + id)
                        .build();
            }

            procedimientoPasoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "El procedimiento-paso eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el procedimiento-paso")
                    .header("X-Message", "Error al eliminar el procedimiento-paso")
                    .build();
        }
    }
}
