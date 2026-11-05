package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@Stateless
@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RolResource implements Serializable {

    @Inject
    private RolService rolService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/roles?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<Rol> lista = rolService.findRange(first, max);
            int total = rolService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los roles")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los roles")
                    .header("X-Message", "Error al listar los roles")
                    .build();
        }
    }

    // GET /api/roles/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Rol> lista = rolService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los roles")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los roles")
                    .header("X-Message", "Error al obtener todos los roles")
                    .build();
        }
    }

    // GET /api/roles/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            Rol entidad = rolService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el rol con id " + id)
                        .header("X-Message", "No se encontro el rol con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "Rol encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el rol por id")
                    .header("X-Message", "Error al obtener el rol por id")
                    .build();
        }
    }

    // POST /api/roles
    @POST
    public Response crear(Rol nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdRol(UUID.randomUUID());

            rolService.crear(nuevo);

            UUID idGenerado = nuevo.getIdRol();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "Rol creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el rol: " + e.getMessage())
                    .header("X-Message", "Error al crear el rol")
                    .build();
        }
    }

    // PUT /api/roles/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Rol modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            Rol actual = rolService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el rol con id " + id)
                        .header("X-Message", "No se encontro el rol con id " + id)
                        .build();
            }

           
            actual.setNombre(modificado.getNombre());
            actual.setActivo(modificado.getActivo());
            actual.setObservaciones(modificado.getObservaciones());

            Rol actualizado = rolService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "Rol actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el rol: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el rol")
                    .build();
        }
    }

    // DELETE /api/roles/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            Rol entidad = rolService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el rol con id " + id)
                        .header("X-Message", "No se encontro el rol con id " + id)
                        .build();
            }

            rolService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "Rol eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el rol")
                    .header("X-Message", "Error al eliminar el rol")
                    .build();
        }
    }
}
