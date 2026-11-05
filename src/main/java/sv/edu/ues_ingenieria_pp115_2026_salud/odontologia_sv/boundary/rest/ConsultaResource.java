package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource implements Serializable {

    @Inject
    private ConsultaService consultaService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/consultas?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<Consulta> lista = consultaService.findRange(first, max);
            int total = consultaService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las consultas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las consultas")
                    .header("X-Message", "Error al listar las consultas")
                    .build();
        }
    }

    // GET /api/consultas/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Consulta> lista = consultaService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de las consultas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos las consultas")
                    .header("X-Message", "Error al obtener todos las consultas")
                    .build();
        }
    }

    // GET /api/consultas/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            Consulta entidad = consultaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro la consulta con id " + id)
                        .header("X-Message", "No se encontro la consulta con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "Consulta encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la consulta por id")
                    .header("X-Message", "Error al obtener la consulta por id")
                    .build();
        }
    }

    // POST /api/consultas
    @POST
    public Response crear(Consulta nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdConsulta(UUID.randomUUID());

            consultaService.crear(nuevo);

            UUID idGenerado = nuevo.getIdConsulta();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "Consulta creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear la consulta: " + e.getMessage())
                    .header("X-Message", "Error al crear la consulta")
                    .build();
        }
    }

    // PUT /api/consultas/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Consulta modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            Consulta actual = consultaService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta con id " + id)
                        .header("X-Message", "No se encontro la consulta con id " + id)
                        .build();
            }

         
            actual.setFechaInicio(modificado.getFechaInicio());
            actual.setFechaFin(modificado.getFechaFin());
            actual.setReferenciaExterna(modificado.getReferenciaExterna());
            actual.setObservaciones(modificado.getObservaciones());
            actual.setIdPersonaRol(modificado.getIdPersonaRol());

            Consulta actualizado = consultaService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "Consulta actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar la consulta: " + e.getMessage())
                    .header("X-Message", "Error al actualizar la consulta")
                    .build();
        }
    }

    // DELETE /api/consultas/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            Consulta entidad = consultaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta con id " + id)
                        .header("X-Message", "No se encontro la consulta con id " + id)
                        .build();
            }

            consultaService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "Consulta eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar la consulta")
                    .header("X-Message", "Error al eliminar la consulta")
                    .build();
        }
    }
}
