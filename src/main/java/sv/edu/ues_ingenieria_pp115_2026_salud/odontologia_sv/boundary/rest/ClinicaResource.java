package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/clinicas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClinicaResource implements Serializable {

    @Inject
    private ClinicaService clinicaService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/clinicas?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<Clinica> lista = clinicaService.findRange(first, max);
            int total = clinicaService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las clinicas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las clinicas")
                    .header("X-Message", "Error al listar las clinicas")
                    .build();
        }
    }

    // GET /api/clinicas/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Clinica> lista = clinicaService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de las clinicas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos las clinicas")
                    .header("X-Message", "Error al obtener todos las clinicas")
                    .build();
        }
    }

    // GET /api/clinicas/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            Clinica entidad = clinicaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro la clinica con id " + id)
                        .header("X-Message", "No se encontro la clinica con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "Clinica encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la clinica por id")
                    .header("X-Message", "Error al obtener la clinica por id")
                    .build();
        }
    }

    // POST /api/clinicas
    @POST
    public Response crear(Clinica nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            // El ID lo genera el servidor con UUID.randomUUID().
            // Se ignora cualquier ID que venga en el body del POST.
            nuevo.setIdClinica(UUID.randomUUID());

            clinicaService.crear(nuevo);

            UUID idGenerado = nuevo.getIdClinica();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "Clinica creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear la clinica: " + e.getMessage())
                    .header("X-Message", "Error al crear la clinica")
                    .build();
        }
    }

    // PUT /api/clinicas/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Clinica modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            Clinica actual = clinicaService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro la clinica con id " + id)
                        .header("X-Message", "No se encontro la clinica con id " + id)
                        .build();
            }

            actual.setNombre(modificado.getNombre());
            actual.setActivo(modificado.getActivo());
            actual.setTipo(modificado.getTipo());
            actual.setComentarios(modificado.getComentarios());

            Clinica actualizado = clinicaService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "Clinica actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar la clinica: " + e.getMessage())
                    .header("X-Message", "Error al actualizar la clinica")
                    .build();
        }
    }

    // DELETE /api/clinicas/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            Clinica entidad = clinicaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro la clinica con id " + id)
                        .header("X-Message", "No se encontro la clinica con id " + id)
                        .build();
            }

            clinicaService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "Clinica eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar la clinica")
                    .header("X-Message", "Error al eliminar la clinica")
                    .build();
        }
    }

    @GET
    @Path("/activas")
    public Response listarActivas() {
        try {
            List<Clinica> lista = clinicaService.listarActivas();
            return Response.ok(lista)
                    .header("X-Message", "Listado de clinicas activas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las clinicas activas")
                    .build();
        }
    }
}
