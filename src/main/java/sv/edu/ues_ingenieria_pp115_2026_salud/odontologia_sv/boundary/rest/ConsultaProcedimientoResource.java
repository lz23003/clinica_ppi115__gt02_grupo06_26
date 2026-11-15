package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/consultas-procedimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaProcedimientoResource implements Serializable {

    @Inject
    private ConsultaProcedimientoService consultaProcedimientoService;
    

    @Inject
    private UriInfo uriInfo;

    // GET /api/consultas-procedimientos?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<ConsultaProcedimiento> lista = consultaProcedimientoService.findRange(first, max);
            int total = consultaProcedimientoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las consultas-procedimientos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las consultas-procedimientos")
                    .header("X-Message", "Error al listar las consultas-procedimientos")
                    .build();
        }
    }

    // GET /api/consultas-procedimientos/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<ConsultaProcedimiento> lista = consultaProcedimientoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de las consultas-procedimientos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos las consultas-procedimientos")
                    .header("X-Message", "Error al obtener todos las consultas-procedimientos")
                    .build();
        }
    }

    // GET /api/consultas-procedimientos/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            ConsultaProcedimiento entidad = consultaProcedimientoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro la consulta-procedimiento con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "La consulta-procedimiento encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la consulta-procedimiento por id")
                    .header("X-Message", "Error al obtener la consulta-procedimiento por id")
                    .build();
        }
    }

    // POST /api/consultas-procedimientos
    @POST
    public Response crear(ConsultaProcedimiento nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdConsultaProcedimiento(UUID.randomUUID());

            consultaProcedimientoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdConsultaProcedimiento();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "La consulta-procedimiento creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear la consulta-procedimiento: " + e.getMessage())
                    .header("X-Message", "Error al crear la consulta-procedimiento")
                    .build();
        }
    }

    // PUT /api/consultas-procedimientos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ConsultaProcedimiento modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            ConsultaProcedimiento actual = consultaProcedimientoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta-procedimiento con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento con id " + id)
                        .build();
            }

           
            actual.setIdProcedimiento(modificado.getIdProcedimiento());
            actual.setFechaInicio(modificado.getFechaInicio());
            actual.setFechaFin(modificado.getFechaFin());
            actual.setObservaciones(modificado.getObservaciones());
            actual.setIdConsulta(modificado.getIdConsulta());

            ConsultaProcedimiento actualizado = consultaProcedimientoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "La consulta-procedimiento actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar la consulta-procedimiento: " + e.getMessage())
                    .header("X-Message", "Error al actualizar la consulta-procedimiento")
                    .build();
        }
    }

    // DELETE /api/consultas-procedimientos/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            ConsultaProcedimiento entidad = consultaProcedimientoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta-procedimiento con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento con id " + id)
                        .build();
            }

            consultaProcedimientoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "La consulta-procedimiento eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar la consulta-procedimiento")
                    .header("X-Message", "Error al eliminar la consulta-procedimiento")
                    .build();
        }
    }
    

    
    
}
