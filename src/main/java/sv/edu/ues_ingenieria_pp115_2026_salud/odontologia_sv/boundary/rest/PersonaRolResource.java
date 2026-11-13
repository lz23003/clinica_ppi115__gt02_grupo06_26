package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/personas-roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonaRolResource implements Serializable {

    @Inject
    private PersonaRolService personaRolService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/personas-roles?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<PersonaRol> lista = personaRolService.findRange(first, max);
            int total = personaRolService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las personas-roles")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las personas-roles")
                    .header("X-Message", "Error al listar las personas-roles")
                    .build();
        }
    }

    // GET /api/personas-roles/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<PersonaRol> lista = personaRolService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de las personas-roles")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos las personas-roles")
                    .header("X-Message", "Error al obtener todos las personas-roles")
                    .build();
        }
    }

    // GET /api/personas-roles/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            PersonaRol entidad = personaRolService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro la persona-rol con id " + id)
                        .header("X-Message", "No se encontro la persona-rol con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "La persona-rol encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la persona-rol por id")
                    .header("X-Message", "Error al obtener la persona-rol por id")
                    .build();
        }
    }

    // POST /api/personas-roles
    @POST
    public Response crear(PersonaRol nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdPersonaRol(UUID.randomUUID());

            personaRolService.crear(nuevo);

            UUID idGenerado = nuevo.getIdPersonaRol();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "La persona-rol creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear la persona-rol: " + e.getMessage())
                    .header("X-Message", "Error al crear la persona-rol")
                    .build();
        }
    }

    // PUT /api/personas-roles/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, PersonaRol modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            PersonaRol actual = personaRolService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro la persona-rol con id " + id)
                        .header("X-Message", "No se encontro la persona-rol con id " + id)
                        .build();
            }

           
            actual.setFechaCreacion(modificado.getFechaCreacion());
            actual.setIdClinica(modificado.getIdClinica());
            actual.setIdPersona(modificado.getIdPersona());
            actual.setIdRol(modificado.getIdRol());

            PersonaRol actualizado = personaRolService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "La persona-rol actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar la persona-rol: " + e.getMessage())
                    .header("X-Message", "Error al actualizar la persona-rol")
                    .build();
        }
    }

    // DELETE /api/personas-roles/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            PersonaRol entidad = personaRolService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro la persona-rol con id " + id)
                        .header("X-Message", "No se encontro la persona-rol con id " + id)
                        .build();
            }

            personaRolService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "La persona-rol eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar la persona-rol")
                    .header("X-Message", "Error al eliminar la persona-rol")
                    .build();
        }
    }
    
        @GET
    @Path("/persona/{idPersona}")
    public Response buscarPorPersona(@PathParam("idPersona") UUID idPersona) {
        try {
            List<PersonaRol> lista = personaRolService.buscarPorPersona(idPersona);
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error al buscar roles: " + e.getMessage()).build();
        }
    }
}
