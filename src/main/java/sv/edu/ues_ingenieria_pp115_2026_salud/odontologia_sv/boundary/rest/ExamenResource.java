package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/examenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExamenResource implements Serializable {

    @Inject
    private ExamenService examenService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/examenes?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<Examen> lista = examenService.findRange(first, max);
            int total = examenService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los examenes")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los examenes")
                    .header("X-Message", "Error al listar los examenes")
                    .build();
        }
    }

    // GET /api/examenes/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Examen> lista = examenService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los examenes")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los examenes")
                    .header("X-Message", "Error al obtener todos los examenes")
                    .build();
        }
    }

    // GET /api/examenes/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            Examen entidad = examenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el examen con id " + id)
                        .header("X-Message", "No se encontro el examen con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "Examen encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el examen por id")
                    .header("X-Message", "Error al obtener el examen por id")
                    .build();
        }
    }

    // POST /api/examenes
    @POST
    public Response crear(Examen nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            
            nuevo.setIdExamen(UUID.randomUUID());

            examenService.crear(nuevo);

            UUID idGenerado = nuevo.getIdExamen();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "Examen creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el examen: " + e.getMessage())
                    .header("X-Message", "Error al crear el examen")
                    .build();
        }
    }

    // PUT /api/examenes/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Examen modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            Examen actual = examenService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el examen con id " + id)
                        .header("X-Message", "No se encontro el examen con id " + id)
                        .build();
            }

           
            actual.setNombre(modificado.getNombre());
            actual.setActivo(modificado.getActivo());
            actual.setObservaciones(modificado.getObservaciones());

            Examen actualizado = examenService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "Examen actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el examen: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el examen")
                    .build();
        }
    }

    // DELETE /api/examenes/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            Examen entidad = examenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el examen con id " + id)
                        .header("X-Message", "No se encontro el examen con id " + id)
                        .build();
            }

            examenService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "Examen eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el examen")
                    .header("X-Message", "Error al eliminar el examen")
                    .build();
        }
    }
}
