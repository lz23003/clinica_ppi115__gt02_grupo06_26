package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoExamen;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/procedimientos-pasos-examenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimientoPasoExamenResource implements Serializable {

    @Inject
    private ProcedimientoPasoExamenService procedimientoPasoExamenService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/procedimientos-pasos-examenes?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<ProcedimientoPasoExamen> lista = procedimientoPasoExamenService.findRange(first, max);
            int total = procedimientoPasoExamenService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los procedimientos-pasos-examenes")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los procedimientos-pasos-examenes")
                    .header("X-Message", "Error al listar los procedimientos-pasos-examenes")
                    .build();
        }
    }

    // GET /api/procedimientos-pasos-examenes/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<ProcedimientoPasoExamen> lista = procedimientoPasoExamenService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los procedimientos-pasos-examenes")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los procedimientos-pasos-examenes")
                    .header("X-Message", "Error al obtener todos los procedimientos-pasos-examenes")
                    .build();
        }
    }

    // GET /api/procedimientos-pasos-examenes/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            ProcedimientoPasoExamen entidad = procedimientoPasoExamenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el procedimiento-paso-examen con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso-examen con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "El procedimiento-paso-examen encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el procedimiento-paso-examen por id")
                    .header("X-Message", "Error al obtener el procedimiento-paso-examen por id")
                    .build();
        }
    }

    // POST /api/procedimientos-pasos-examenes
    @POST
    public Response crear(ProcedimientoPasoExamen nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            
            nuevo.setIdProcedimientoPasoExamen(UUID.randomUUID());

            procedimientoPasoExamenService.crear(nuevo);

            UUID idGenerado = nuevo.getIdProcedimientoPasoExamen();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "El procedimiento-paso-examen creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el procedimiento-paso-examen: " + e.getMessage())
                    .header("X-Message", "Error al crear el procedimiento-paso-examen")
                    .build();
        }
    }

    // PUT /api/procedimientos-pasos-examenes/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ProcedimientoPasoExamen modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            ProcedimientoPasoExamen actual = procedimientoPasoExamenService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el procedimiento-paso-examen con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso-examen con id " + id)
                        .build();
            }

         
            actual.setFechaCreacion(modificado.getFechaCreacion());
            actual.setActivo(modificado.getActivo());
            actual.setObservaciones(modificado.getObservaciones());
            actual.setIdExamen(modificado.getIdExamen());
            actual.setIdProcedimientoPaso(modificado.getIdProcedimientoPaso());

            ProcedimientoPasoExamen actualizado = procedimientoPasoExamenService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "El procedimiento-paso-examen actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el procedimiento-paso-examen: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el procedimiento-paso-examen")
                    .build();
        }
    }

    // DELETE /api/procedimientos-pasos-examenes/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            ProcedimientoPasoExamen entidad = procedimientoPasoExamenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el procedimiento-paso-examen con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso-examen con id " + id)
                        .build();
            }

            procedimientoPasoExamenService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "El procedimiento-paso-examen eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el procedimiento-paso-examen")
                    .header("X-Message", "Error al eliminar el procedimiento-paso-examen")
                    .build();
        }
    }
    
    
        @GET
    @Path("/por-paso/{idProcedimientoPaso}")
    public Response buscarPorPaso(@PathParam("idProcedimientoPaso") UUID idProcedimientoPaso) {
        try {
            List<ProcedimientoPasoExamen> lista = procedimientoPasoExamenService.buscarPorPaso(idProcedimientoPaso);
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }
}
