package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;

import java.io.Serializable;
import java.net.URI;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource implements Serializable {

    private static final ZoneId CLINIC_ZONE = ZoneId.of("America/El_Salvador");

    @Inject
    private ConsultaService consultaService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/consultas?first=0&max=50 or /api/consultas?idClinica=...&idRol=...&desde=...&hasta=...
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max,
            @QueryParam("idClinica") String idClinica,
            @QueryParam("idRol") String idRol,
            @QueryParam("desde") String desde,
            @QueryParam("hasta") String hasta) {

        try {
            if (hasValue(idClinica) || hasValue(idRol) || hasValue(desde) || hasValue(hasta)) {
                Date fechaDesde = parseDateStart(desde);
                Date fechaHasta = parseDateEndExclusive(hasta);
                if (fechaDesde != null && fechaHasta != null && !fechaDesde.before(fechaHasta)) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(java.util.Map.of("message",
                                    "La fecha inicial debe ser anterior o igual a la fecha final."))
                            .build();
                }
                List<Consulta> lista = consultaService.buscarDashboardPorFiltros(
                        parseUuid(idClinica, "idClinica"),
                        parseUuid(idRol, "idRol"),
                        fechaDesde,
                        fechaHasta);
                incluirExamenIds(lista);
                return Response.ok(lista)
                        .header("X-Total-Count", lista.size())
                        .header("X-Message", "Listado de consultas filtrado")
                        .build();
            }

            List<Consulta> lista = consultaService.findRange(first, max);
            incluirExamenIds(lista);
            int total = consultaService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las consultas")
                    .build();
        } catch (DateTimeException | IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(java.util.Map.of("message", "Los filtros de consulta no tienen un formato válido."))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las consultas")
                    .header("X-Message", "Error al listar las consultas")
                    .build();
        }
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    private UUID parseUuid(String value, String field) {
        if (!hasValue(value)) return null;
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("El filtro " + field + " no es válido.", ex);
        }
    }

    private Date parseDateStart(String value) {
        if (!hasValue(value)) return null;
        return Date.from(LocalDate.parse(value).atStartOfDay(CLINIC_ZONE).toInstant());
    }

    private Date parseDateEndExclusive(String value) {
        if (!hasValue(value)) return null;
        return Date.from(LocalDate.parse(value).plusDays(1).atStartOfDay(CLINIC_ZONE).toInstant());
    }

    private void incluirExamenIds(List<Consulta> consultas) {
        consultas.forEach(consulta ->
                consulta.setExamenIds(consultaService.obtenerExamenIds(consulta)));
    }

    // GET /api/consultas/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Consulta> lista = consultaService.findAll();
            incluirExamenIds(lista);
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de las consultas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos las consultas")
                    .header("X-Message", "Error al obtener todos las consultas")
                    .build();
        }
    }

    // GET /api/consultas/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            Consulta entidad = consultaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro la consulta con id " + id)
                        .header("X-Message", "No se encontro la consulta con id " + id)
                        .build();
            }
            entidad.setExamenIds(consultaService.obtenerExamenIds(entidad));
            return Response.ok(entidad)
                    .header("X-Message", "Consulta encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la consulta por id")
                    .header("X-Message", "Error al obtener la consulta por id")
                    .build();
        }
    }

    // POST /api/consultas
    @POST
    public Response crear(Consulta nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
           
            nuevo.setIdConsulta(UUID.randomUUID());

            consultaService.crear(nuevo);

            UUID idGenerado = nuevo.getIdConsulta();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "Consulta creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear la consulta: " + e.getMessage())
                    .header("X-Message", "Error al crear la consulta")
                    .build();
        }
    }

    // PUT /api/consultas/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Consulta modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            Consulta actual = consultaService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta con id " + id)
                        .header("X-Message", "No se encontro la consulta con id " + id)
                        .build();
            }

         
            if (modificado.getFechaInicio() != null) {
                actual.setFechaInicio(modificado.getFechaInicio());
            }
            if (modificado.getFechaFin() != null) {
                actual.setFechaFin(modificado.getFechaFin());
            }
            if (modificado.getReferenciaExterna() != null) {
                actual.setReferenciaExterna(modificado.getReferenciaExterna());
            }
            if (modificado.getObservaciones() != null) {
                actual.setObservaciones(modificado.getObservaciones());
            }
            if (modificado.getIdPersonaRol() != null) {
                actual.setIdPersonaRol(modificado.getIdPersonaRol());
            }
            if (modificado.getExamenIds() != null) {
                consultaService.actualizarExamenes(actual, modificado.getExamenIds());
            }

            Consulta actualizado = consultaService.modificar(actual);
            actualizado.setExamenIds(consultaService.obtenerExamenIds(actualizado));

            return Response.ok(actualizado)
                    .header("X-Message", "Consulta actualizado")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(java.util.Map.of("message", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar la consulta: " + e.getMessage())
                    .header("X-Message", "Error al actualizar la consulta")
                    .build();
        }
    }

    // DELETE /api/consultas/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            Consulta entidad = consultaService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta con id " + id)
                        .header("X-Message", "No se encontro la consulta con id " + id)
                        .build();
            }

            consultaService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "Consulta eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar la consulta")
                    .header("X-Message", "Error al eliminar la consulta")
                    .build();
        }
    }
}
