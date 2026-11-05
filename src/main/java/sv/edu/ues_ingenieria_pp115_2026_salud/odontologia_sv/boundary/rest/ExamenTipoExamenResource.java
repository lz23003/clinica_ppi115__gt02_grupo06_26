package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenTipoExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ExamenTipoExamen;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/examenes-tipos-examen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExamenTipoExamenResource implements Serializable {

    @Inject
    private ExamenTipoExamenService examenTipoExamenService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/examenes-tipos-examen?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<ExamenTipoExamen> lista = examenTipoExamenService.findRange(first, max);
            int total = examenTipoExamenService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los examenes-tipo-examen")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los examenes-tipo-examen")
                    .header("X-Message", "Error al listar los examenes-tipo-examen")
                    .build();
        }
    }

    // GET /api/examenes-tipos-examen/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<ExamenTipoExamen> lista = examenTipoExamenService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los examenes-tipo-examen")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los examenes-tipo-examen")
                    .header("X-Message", "Error al obtener todos los examenes-tipo-examen")
                    .build();
        }
    }

    // GET /api/examenes-tipos-examen/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            ExamenTipoExamen entidad = examenTipoExamenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el examen-tipo-examen con id " + id)
                        .header("X-Message", "No se encontro el examen-tipo-examen con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "El examen-tipo-examen encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el examen-tipo-examen por id")
                    .header("X-Message", "Error al obtener el examen-tipo-examen por id")
                    .build();
        }
    }

    // POST /api/examenes-tipos-examen
    @POST
    public Response crear(ExamenTipoExamen nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
          
            nuevo.setIdExamenTipoExamen(UUID.randomUUID());

            examenTipoExamenService.crear(nuevo);

            UUID idGenerado = nuevo.getIdExamenTipoExamen();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "El examen-tipo-examen creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el examen-tipo-examen: " + e.getMessage())
                    .header("X-Message", "Error al crear el examen-tipo-examen")
                    .build();
        }
    }

    // PUT /api/examenes-tipos-examen/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ExamenTipoExamen modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            ExamenTipoExamen actual = examenTipoExamenService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el examen-tipo-examen con id " + id)
                        .header("X-Message", "No se encontro el examen-tipo-examen con id " + id)
                        .build();
            }

            // Copia manual de los campos modificables (no se copia el id ni
            // las relaciones @OneToMany, que se manejan desde el otro lado).
            actual.setFechaCreacion(modificado.getFechaCreacion());
            actual.setObservaciones(modificado.getObservaciones());
            actual.setIdExamen(modificado.getIdExamen());
            actual.setIdTipoExamen(modificado.getIdTipoExamen());

            ExamenTipoExamen actualizado = examenTipoExamenService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "El examen-tipo-examen actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el examen-tipo-examen: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el examen-tipo-examen")
                    .build();
        }
    }

    // DELETE /api/examenes-tipos-examen/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            ExamenTipoExamen entidad = examenTipoExamenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el examen-tipo-examen con id " + id)
                        .header("X-Message", "No se encontro el examen-tipo-examen con id " + id)
                        .build();
            }

            examenTipoExamenService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "El examen-tipo-examen eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el examen-tipo-examen")
                    .header("X-Message", "Error al eliminar el examen-tipo-examen")
                    .build();
        }
    }
}
