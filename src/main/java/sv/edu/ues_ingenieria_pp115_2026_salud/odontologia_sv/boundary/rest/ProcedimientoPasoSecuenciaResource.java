package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoSecuenciaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoSecuencia;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/procedimientos-pasos-secuencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimientoPasoSecuenciaResource implements Serializable {

    @Inject
    private ProcedimientoPasoSecuenciaService procedimientoPasoSecuenciaService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/procedimientos-pasos-secuencias?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<ProcedimientoPasoSecuencia> lista = procedimientoPasoSecuenciaService.findRange(first, max);
            int total = procedimientoPasoSecuenciaService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de los procedimientos-pasos-secuencias")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar los procedimientos-pasos-secuencias")
                    .header("X-Message", "Error al listar los procedimientos-pasos-secuencias")
                    .build();
        }
    }

    // GET /api/procedimientos-pasos-secuencias/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<ProcedimientoPasoSecuencia> lista = procedimientoPasoSecuenciaService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de los procedimientos-pasos-secuencias")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los procedimientos-pasos-secuencias")
                    .header("X-Message", "Error al obtener todos los procedimientos-pasos-secuencias")
                    .build();
        }
    }

    // GET /api/procedimientos-pasos-secuencias/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            ProcedimientoPasoSecuencia entidad = procedimientoPasoSecuenciaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el procedimiento-paso-secuencia con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso-secuencia con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "El procedimiento-paso-secuencia encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el procedimiento-paso-secuencia por id")
                    .header("X-Message", "Error al obtener el procedimiento-paso-secuencia por id")
                    .build();
        }
    }

    // POST /api/procedimientos-pasos-secuencias
    @POST
    public Response crear(ProcedimientoPasoSecuencia nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdProcedimientoPasoSecuencia(UUID.randomUUID());

            procedimientoPasoSecuenciaService.crear(nuevo);

            UUID idGenerado = nuevo.getIdProcedimientoPasoSecuencia();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "El procedimiento-paso-secuencia creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear el procedimiento-paso-secuencia: " + e.getMessage())
                    .header("X-Message", "Error al crear el procedimiento-paso-secuencia")
                    .build();
        }
    }

    // PUT /api/procedimientos-pasos-secuencias/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ProcedimientoPasoSecuencia modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            ProcedimientoPasoSecuencia actual = procedimientoPasoSecuenciaService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro el procedimiento-paso-secuencia con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso-secuencia con id " + id)
                        .build();
            }

            
            actual.setIdProcedimientoPasoReferencia(modificado.getIdProcedimientoPasoReferencia());
            actual.setTipoSecuencia(modificado.getTipoSecuencia());
            actual.setIdProcedimientoPaso(modificado.getIdProcedimientoPaso());

            ProcedimientoPasoSecuencia actualizado = procedimientoPasoSecuenciaService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "El procedimiento-paso-secuencia actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar el procedimiento-paso-secuencia: " + e.getMessage())
                    .header("X-Message", "Error al actualizar el procedimiento-paso-secuencia")
                    .build();
        }
    }

    // DELETE /api/procedimientos-pasos-secuencias/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            ProcedimientoPasoSecuencia entidad = procedimientoPasoSecuenciaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro el procedimiento-paso-secuencia con id " + id)
                        .header("X-Message", "No se encontro el procedimiento-paso-secuencia con id " + id)
                        .build();
            }

            procedimientoPasoSecuenciaService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "El procedimiento-paso-secuencia eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar el procedimiento-paso-secuencia")
                    .header("X-Message", "Error al eliminar el procedimiento-paso-secuencia")
                    .build();
        }
    }
}
