package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;

@Stateless
@Path("/citas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CitasResource {

    private static final int MAX_RESULTS = 100;
    private static final ZoneId CLINIC_ZONE = ZoneId.systemDefault();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @Inject
    private ConsultaService consultaService;

    @Inject
    private ConsultaProcedimientoService consultaProcedimientoService;

    @POST
    @Path("/busqueda")
    public Response buscar(CitaSearchRequest request) {
        if (request == null
                || request.getNumeroDocumento() == null || request.getNumeroDocumento().isBlank()
                || request.getNumeroDocumento().trim().length() > 30
                || request.getClinica() == null || request.getClinica().isBlank()
                || request.getDesde() == null || request.getHasta() == null) {
            return badRequest("Completa tipo y número de documento, clínica y rango de fechas.");
        }

        final String documentType;
        final UUID clinicId;
        final LocalDate from;
        final LocalDate through;
        try {
            documentType = TipoDocumentoFiltro.patron(request.getTipoDocumento());
            clinicId = UUID.fromString(request.getClinica().trim());
            from = LocalDate.parse(request.getDesde(), DATE_FORMAT);
            through = LocalDate.parse(request.getHasta(), DATE_FORMAT);
        } catch (IllegalArgumentException | DateTimeParseException ex) {
            return badRequest("El tipo de documento, la clínica o las fechas no tienen un formato válido.");
        }

        if (through.isBefore(from)) {
            return badRequest("La fecha inicial debe ser anterior o igual a la fecha final.");
        }

        final Date fromInclusive;
        final Date throughExclusive;
        try {
            fromInclusive = Date.from(from.atStartOfDay(CLINIC_ZONE).toInstant());
            throughExclusive = Date.from(through.plusDays(1).atStartOfDay(CLINIC_ZONE).toInstant());
        } catch (DateTimeException | ArithmeticException ex) {
            return badRequest("El rango de fechas está fuera del intervalo permitido.");
        }
        List<Consulta> consultations = consultaService.buscarCitasPublicas(
                documentType,
                request.getNumeroDocumento().trim(),
                clinicId,
                fromInclusive,
                throughExclusive,
                MAX_RESULTS);
        List<UUID> consultationIds = consultations.stream()
                .map(Consulta::getIdConsulta)
                .toList();
        Map<UUID, List<String>> proceduresByConsultation =
                consultaProcedimientoService.buscarNombresPorConsultas(consultationIds);

        List<CitaResponse> response = consultations.stream()
                .map(consulta -> {
                    var clinic = consulta.getIdPersonaRol().getIdClinica();
                    var start = consulta.getFechaInicio().toInstant().atZone(CLINIC_ZONE);
                    return new CitaResponse(
                            consulta.getIdConsulta(),
                            DATE_FORMAT.format(start),
                            TIME_FORMAT.format(start),
                            clinic.getNombre(),
                            proceduresByConsultation.getOrDefault(consulta.getIdConsulta(), List.of()));
                })
                .collect(Collectors.toList());

        return Response.ok(response)
                .header("X-Result-Limit", MAX_RESULTS)
                .build();
    }

    private Response badRequest(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("message", message))
                .build();
    }
}
