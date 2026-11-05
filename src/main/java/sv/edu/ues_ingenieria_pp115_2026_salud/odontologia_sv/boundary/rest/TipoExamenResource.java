package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/tipos-examen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoExamenResource implements Serializable {

    @Inject
    private TipoExamenService tipoExamenService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/tipos-examen?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<TipoExamen> lista = tipoExamenService.findRange(first, max);
            int total = tipoExamenService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los tipos de examen")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los tipos de examen")
                    .header("X-Message", "Error al listar los tipos de examen")
                    .build();
        }
    }

    // GET /api/tipos-examen/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<TipoExamen> lista = tipoExamenService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los tipos de examen")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los tipos de examen")
                    .header("X-Message", "Error al obtener todos los tipos de examen")
                    .build();
        }
    }

    // GET /api/tipos-examen/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            TipoExamen entidad = tipoExamenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el tipo de examen con id " + id)
                        .header("X-Message", "No se encontro el tipo de examen con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "El tipo de examen encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el tipo de examen por id")
                    .header("X-Message", "Error al obtener el tipo de examen por id")
                    .build();
        }
    }

    // POST /api/tipos-examen
    @POST
    public Response crear(TipoExamen nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdTipoExamen(UUID.randomUUID());

            tipoExamenService.crear(nuevo);

            UUID idGenerado = nuevo.getIdTipoExamen();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "El tipo de examen creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el tipo de examen: " + e.getMessage())
                    .header("X-Message", "Error al crear el tipo de examen")
                    .build();
        }
    }

    // PUT /api/tipos-examen/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, TipoExamen modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            TipoExamen actual = tipoExamenService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el tipo de examen con id " + id)
                        .header("X-Message", "No se encontro el tipo de examen con id " + id)
                        .build();
            }

            
            actual.setNombre(modificado.getNombre());
            actual.setActivo(modificado.getActivo());
            actual.setObservaciones(modificado.getObservaciones());

            TipoExamen actualizado = tipoExamenService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "El tipo de examen actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el tipo de examen: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el tipo de examen")
                    .build();
        }
    }

    // DELETE /api/tipos-examen/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            TipoExamen entidad = tipoExamenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el tipo de examen con id " + id)
                        .header("X-Message", "No se encontro el tipo de examen con id " + id)
                        .build();
            }

            tipoExamenService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "El tipo de examen eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el tipo de examen")
                    .header("X-Message", "Error al eliminar el tipo de examen")
                    .build();
        }
    }
}
