package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoMedioContactoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/tipos-medio-contacto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoMedioContactoResource implements Serializable {

    @Inject
    private TipoMedioContactoService tipoMedioContactoService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/tipos-medio-contacto?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<TipoMedioContacto> lista = tipoMedioContactoService.findRange(first, max);
            int total = tipoMedioContactoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los tipos de medio de contacto")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los tipos de medio de contacto")
                    .header("X-Message", "Error al listar los tipos de medio de contacto")
                    .build();
        }
    }

    // GET /api/tipos-medio-contacto/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<TipoMedioContacto> lista = tipoMedioContactoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los tipos de medio de contacto")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los tipos de medio de contacto")
                    .header("X-Message", "Error al obtener todos los tipos de medio de contacto")
                    .build();
        }
    }

    // GET /api/tipos-medio-contacto/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            TipoMedioContacto entidad = tipoMedioContactoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el tipo de medio de contacto con id " + id)
                        .header("X-Message", "No se encontro el tipo de medio de contacto con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "El tipo de medio de contacto encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el tipo de medio de contacto por id")
                    .header("X-Message", "Error al obtener el tipo de medio de contacto por id")
                    .build();
        }
    }

    // POST /api/tipos-medio-contacto
    @POST
    public Response crear(TipoMedioContacto nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            
            nuevo.setIdTipoMedioContacto(UUID.randomUUID());

            tipoMedioContactoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdTipoMedioContacto();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "El tipo de medio de contacto creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el tipo de medio de contacto: " + e.getMessage())
                    .header("X-Message", "Error al crear el tipo de medio de contacto")
                    .build();
        }
    }

    // PUT /api/tipos-medio-contacto/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, TipoMedioContacto modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            TipoMedioContacto actual = tipoMedioContactoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el tipo de medio de contacto con id " + id)
                        .header("X-Message", "No se encontro el tipo de medio de contacto con id " + id)
                        .build();
            }

           
            actual.setNombre(modificado.getNombre());
            actual.setIndicaciones(modificado.getIndicaciones());
            actual.setExpresionRegular(modificado.getExpresionRegular());
            actual.setActivo(modificado.getActivo());

            TipoMedioContacto actualizado = tipoMedioContactoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "El tipo de medio de contacto actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el tipo de medio de contacto: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el tipo de medio de contacto")
                    .build();
        }
    }

    // DELETE /api/tipos-medio-contacto/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            TipoMedioContacto entidad = tipoMedioContactoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el tipo de medio de contacto con id " + id)
                        .header("X-Message", "No se encontro el tipo de medio de contacto con id " + id)
                        .build();
            }

            tipoMedioContactoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "El tipo de medio de contacto eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el tipo de medio de contacto")
                    .header("X-Message", "Error al eliminar el tipo de medio de contacto")
                    .build();
        }
    }
}
