package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.MedioContactoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/medios-contacto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedioContactoResource implements Serializable {

    @Inject
    private MedioContactoService medioContactoService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/medios-contacto?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<MedioContacto> lista = medioContactoService.findRange(first, max);
            int total = medioContactoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los medios de contacto")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los medios de contacto")
                    .header("X-Message", "Error al listar los medios de contacto")
                    .build();
        }
    }

    // GET /api/medios-contacto/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<MedioContacto> lista = medioContactoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los medios de contacto")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los medios de contacto")
                    .header("X-Message", "Error al obtener todos los medios de contacto")
                    .build();
        }
    }

    // GET /api/medios-contacto/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            MedioContacto entidad = medioContactoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el medio de contacto con id " + id)
                        .header("X-Message", "No se encontro el medio de contacto con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "El medio de contacto encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el medio de contacto por id")
                    .header("X-Message", "Error al obtener el medio de contacto por id")
                    .build();
        }
    }

    // POST /api/medios-contacto
    @POST
    public Response crear(MedioContacto nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdMedioContacto(UUID.randomUUID());

            medioContactoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdMedioContacto();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "El medio de contacto creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el medio de contacto: " + e.getMessage())
                    .header("X-Message", "Error al crear el medio de contacto")
                    .build();
        }
    }

    // PUT /api/medios-contacto/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, MedioContacto modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            MedioContacto actual = medioContactoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el medio de contacto con id " + id)
                        .header("X-Message", "No se encontro el medio de contacto con id " + id)
                        .build();
            }

          
            actual.setValor(modificado.getValor());
            actual.setFechaCreacion(modificado.getFechaCreacion());
            actual.setIdPersona(modificado.getIdPersona());
            actual.setIdTipoMedioContacto(modificado.getIdTipoMedioContacto());

            MedioContacto actualizado = medioContactoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "El medio de contacto actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el medio de contacto: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el medio de contacto")
                    .build();
        }
    }

    // DELETE /api/medios-contacto/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            MedioContacto entidad = medioContactoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el medio de contacto con id " + id)
                        .header("X-Message", "No se encontro el medio de contacto con id " + id)
                        .build();
            }

            medioContactoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "El medio de contacto eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el medio de contacto")
                    .header("X-Message", "Error al eliminar el medio de contacto")
                    .build();
        }
    }
}
