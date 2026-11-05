package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoPasoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/consultas-procedimientos-pasos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaProcedimientoPasoResource implements Serializable {

    @Inject
    private ConsultaProcedimientoPasoService consultaProcedimientoPasoService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/consultas-procedimientos-pasos?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<ConsultaProcedimientoPaso> lista = consultaProcedimientoPasoService.findRange(first, max);
            int total = consultaProcedimientoPasoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las consultas-procedimientos-pasos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las consultas-procedimientos-pasos")
                    .header("X-Message", "Error al listar las consultas-procedimientos-pasos")
                    .build();
        }
    }

    // GET /api/consultas-procedimientos-pasos/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<ConsultaProcedimientoPaso> lista = consultaProcedimientoPasoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de las consultas-procedimientos-pasos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos las consultas-procedimientos-pasos")
                    .header("X-Message", "Error al obtener todos las consultas-procedimientos-pasos")
                    .build();
        }
    }

    // GET /api/consultas-procedimientos-pasos/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            ConsultaProcedimientoPaso entidad = consultaProcedimientoPasoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro la consulta-procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento-paso con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "La consulta-procedimiento-paso encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la consulta-procedimiento-paso por id")
                    .header("X-Message", "Error al obtener la consulta-procedimiento-paso por id")
                    .build();
        }
    }

    // POST /api/consultas-procedimientos-pasos
    @POST
    public Response crear(ConsultaProcedimientoPaso nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdConsultaProcedimientoPaso(UUID.randomUUID());

            consultaProcedimientoPasoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdConsultaProcedimientoPaso();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "La consulta-procedimiento-paso creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear la consulta-procedimiento-paso: " + e.getMessage())
                    .header("X-Message", "Error al crear la consulta-procedimiento-paso")
                    .build();
        }
    }

    // PUT /api/consultas-procedimientos-pasos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ConsultaProcedimientoPaso modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            ConsultaProcedimientoPaso actual = consultaProcedimientoPasoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta-procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento-paso con id " + id)
                        .build();
            }

            
            actual.setFechaInicio(modificado.getFechaInicio());
            actual.setFechaFin(modificado.getFechaFin());
            actual.setEstado(modificado.getEstado());
            actual.setValor(modificado.getValor());
            actual.setIdProcedimientoPaso(modificado.getIdProcedimientoPaso());
            actual.setIdConsultaProcedimiento(modificado.getIdConsultaProcedimiento());
            actual.setIdPersonaRol(modificado.getIdPersonaRol());

            ConsultaProcedimientoPaso actualizado = consultaProcedimientoPasoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "La consulta-procedimiento-paso actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar la consulta-procedimiento-paso: " + e.getMessage())
                    .header("X-Message", "Error al actualizar la consulta-procedimiento-paso")
                    .build();
        }
    }

    // DELETE /api/consultas-procedimientos-pasos/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            ConsultaProcedimientoPaso entidad = consultaProcedimientoPasoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta-procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento-paso con id " + id)
                        .build();
            }

            consultaProcedimientoPasoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "La consulta-procedimiento-paso eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar la consulta-procedimiento-paso")
                    .header("X-Message", "Error al eliminar la consulta-procedimiento-paso")
                    .build();
        }
    }
}
