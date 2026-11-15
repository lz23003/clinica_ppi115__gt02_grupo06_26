package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
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
    private static final Logger LOGGER = Logger.getLogger(CitasResource.class.getName());

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

    @GET
    @Path("/{id}/ticket")
    @Produces("application/pdf")
    public Response descargarTicket(@PathParam("id") UUID id) {
        Consulta consulta = consultaService.buscarPorId(id);
        if (consulta == null || consulta.getFechaInicio() == null
                || consulta.getIdPersonaRol() == null
                || consulta.getIdPersonaRol().getIdClinica() == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "No se encontró la cita solicitada."))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }

        try (PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A5);
            document.addPage(page);
            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                content.newLineAtOffset(45, 535);
                content.showText("GalenoSV - Ticket de cita");
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
                content.newLineAtOffset(0, -35);
                writePdfLines(content, "Fecha: " + DATE_FORMAT.format(
                        consulta.getFechaInicio().toInstant().atZone(CLINIC_ZONE)));
                writePdfLines(content, "Hora: " + TIME_FORMAT.format(
                        consulta.getFechaInicio().toInstant().atZone(CLINIC_ZONE)));
                writePdfLines(content, "Clinica: " + consulta.getIdPersonaRol().getIdClinica().getNombre());
                writePdfLines(content, "Procedimientos:");
                List<String> procedures = consultaProcedimientoService.buscarNombresPorConsultas(List.of(id))
                        .getOrDefault(id, List.of());
                if (procedures.isEmpty()) {
                    writePdfLines(content, "Por confirmar");
                } else {
                    for (String procedure : procedures) {
                        writePdfLines(content, "- " + procedure);
                    }
                }
                content.endText();
            }
            document.save(output);
            return Response.ok(output.toByteArray(), "application/pdf")
                    .header("Content-Disposition", "attachment; filename=\"galenosv-cita-" + id + ".pdf\"")
                    .build();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "No se pudo generar el ticket PDF de la cita " + id, ex);
            return Response.serverError()
                    .entity(Map.of("message", "No se pudo generar el ticket de la cita."))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }

    private void writePdfLines(PDPageContentStream content, String text) throws IOException {
        String normalized = Normalizer.normalize(text == null ? "" : text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\x20-\\x7E]", "?");
        for (int start = 0; start < normalized.length(); start += 70) {
            content.newLineAtOffset(0, -20);
            content.showText(normalized.substring(start, Math.min(start + 70, normalized.length())));
        }
    }

    private Response badRequest(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("message", message))
                .build();
    }
}
