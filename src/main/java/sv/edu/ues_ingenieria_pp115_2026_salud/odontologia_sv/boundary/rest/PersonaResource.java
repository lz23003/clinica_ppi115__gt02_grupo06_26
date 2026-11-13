package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/personas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonaResource implements Serializable {

    @Inject
    private PersonaService personaService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/personas?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<Persona> lista = personaService.findRange(first, max);
            int total = personaService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las personas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las personas")
                    .header("X-Message", "Error al listar las personas")
                    .build();
        }
    }

    // GET /api/personas/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Persona> lista = personaService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de las personas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos las personas")
                    .header("X-Message", "Error al obtener todos las personas")
                    .build();
        }
    }

    // GET /api/personas/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            Persona entidad = personaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro la persona con id " + id)
                        .header("X-Message", "No se encontro la persona con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "Persona encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la persona por id")
                    .header("X-Message", "Error al obtener la persona por id")
                    .build();
        }
    }

    // POST /api/personas
    @POST
    public Response crear(Persona nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {

            nuevo.setIdPersona(UUID.randomUUID());

            personaService.crear(nuevo);

            UUID idGenerado = nuevo.getIdPersona();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "Persona creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear la persona: " + e.getMessage())
                    .header("X-Message", "Error al crear la persona")
                    .build();
        }
    }

    // PUT /api/personas/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Persona modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            Persona actual = personaService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro la persona con id " + id)
                        .header("X-Message", "No se encontro la persona con id " + id)
                        .build();
            }

            actual.setNombres(modificado.getNombres());
            actual.setApellidos(modificado.getApellidos());
            actual.setFechaNacimiento(modificado.getFechaNacimiento());
            actual.setFechaCreacion(modificado.getFechaCreacion());

            Persona actualizado = personaService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "Persona actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar la persona: " + e.getMessage())
                    .header("X-Message", "Error al actualizar la persona")
                    .build();
        }
    }

    // DELETE /api/personas/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            Persona entidad = personaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro la persona con id " + id)
                        .header("X-Message", "No se encontro la persona con id " + id)
                        .build();
            }

            personaService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "Persona eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar la persona")
                    .header("X-Message", "Error al eliminar la persona")
                    .build();
        }
    }

    @GET
    @Path("/buscar")
    public Response buscarPorDocumento(
            @QueryParam("tipoDocumento") UUID tipoDocumento,
            @QueryParam("valorDocumento") String valorDocumento) {
        try {
            if (valorDocumento == null || valorDocumento.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Documento requerido").build();
            }

            // Usamos una consulta JPQL personalizada en el servicio para buscar por ambos
            // Si no la tienes, puedes hacer un findAll y filtrar en Java para salir del paso:
            List<Persona> todas = personaService.findAll();
            Persona encontrada = todas.stream()
                    .filter(p -> p.getDocumentoList() != null && p.getDocumentoList().stream()
                    .anyMatch(d -> d.getValor().equals(valorDocumento.trim())
                    && (tipoDocumento == null || d.getIdTipoDocumento().getIdTipoDocumento().equals(tipoDocumento))))
                    .findFirst()
                    .orElse(null);

            if (encontrada == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("No se encontró persona").build();
            }
            return Response.ok(encontrada).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

}
