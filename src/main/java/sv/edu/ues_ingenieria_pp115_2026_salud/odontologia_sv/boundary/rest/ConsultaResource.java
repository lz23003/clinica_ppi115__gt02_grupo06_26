package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import java.text.SimpleDateFormat;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.io.ByteArrayOutputStream;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest.dto.ConsultaDetalleDTO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoPasoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

@Stateless
@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource implements Serializable {

 @Inject
private ProcedimientoService procedimientoService;

@Inject
private ConsultaProcedimientoService consultaProcedimientoService;

@Inject
private ConsultaProcedimientoPasoService consultaProcedimientoPasoService;

@Inject
private ConsultaService consultaService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/consultas?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<Consulta> lista = consultaService.findRange(first, max);
            int total = consultaService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las consultas")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las consultas")
                    .header("X-Message", "Error al listar las consultas")
                    .build();
        }
    }

    // GET /api/consultas/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<Consulta> lista = consultaService.findAll();
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
             nuevo.setFechaFin(null); 
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

            actual.setFechaInicio(modificado.getFechaInicio());
            actual.setFechaFin(modificado.getFechaFin());
            actual.setReferenciaExterna(modificado.getReferenciaExterna());
            actual.setObservaciones(modificado.getObservaciones());
            actual.setIdPersonaRol(modificado.getIdPersonaRol());

            Consulta actualizado = consultaService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "Consulta actualizado")
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

    @GET
    @Path("/{id}/pdf")
    @Produces("application/pdf")
    public Response descargarPdf(@PathParam("id") UUID id) {
        try {
            Consulta consulta = consultaService.buscarPorId(id);
            if (consulta == null) {
                return Response.status(404).build();
            }

            // 1. Crear el documento y la página
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                // 2. Escribir el contenido
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    // Usar una fuente estándar (Helvetica)
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.newLineAtOffset(50, 700); // Posición inicial

                    // Datos del ticket
                    contentStream.showText("OdontologiaSV - Ticket de Cita");
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Paciente: " + consulta.getIdPersonaRol().getIdPersona().getNombres()
                            + " " + consulta.getIdPersonaRol().getIdPersona().getApellidos());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Fecha: " + consulta.getFechaInicio().toString());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Clínica: " + consulta.getIdPersonaRol().getIdClinica().getNombre());
                    // Acá podrías seguir agregando líneas para los procedimientos, etc.

                    contentStream.endText();
                }

                // 3. Convertir a bytes y devolver
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                document.save(baos);

                return Response.ok(baos.toByteArray())
                        .header("Content-Disposition", "attachment; filename=ticket.pdf")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/{id}/ticket")
    @Produces("application/pdf")
    public Response descargarTicket(@PathParam("id") UUID id) {
        try {
            Consulta consulta = consultaService.buscarPorId(id);
            if (consulta == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Consulta no encontrada").build();
            }

            // 1. Obtener los datos para el PDF
            String paciente = "Paciente Desconocido";
            if (consulta.getIdPersonaRol() != null && consulta.getIdPersonaRol().getIdPersona() != null) {
                paciente = consulta.getIdPersonaRol().getIdPersona().getNombres() + " "
                        + consulta.getIdPersonaRol().getIdPersona().getApellidos();
            }

            String clinica = (consulta.getIdPersonaRol() != null && consulta.getIdPersonaRol().getIdClinica() != null)
                    ? consulta.getIdPersonaRol().getIdClinica().getNombre() : "Clinica Central";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String fecha = consulta.getFechaInicio() != null ? sdf.format(consulta.getFechaInicio()) : "Sin fecha";
            String motivo = consulta.getObservaciones() != null ? consulta.getObservaciones() : "Consulta General";

            // 2. Generar el PDF con PDFBox
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try (PDDocument document = new PDDocument()) {
                // Creamos una hoja tamaño A5 (Media carta: 420 x 595 puntos)
                PDPage page = new PDPage(new PDRectangle(420, 595));
                document.addPage(page);

                try (PDPageContentStream cs = new PDPageContentStream(document, page)) {

                    // --- TÍTULO ---
                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20);
                    cs.newLineAtOffset(40, 540);
                    cs.showText("ClinicaSV");
                    cs.endText();

                    // --- SUBTÍTULO ---
                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                    cs.newLineAtOffset(40, 525);
                    cs.showText("Comprobante de Cita Medica");
                    cs.endText();

                    // --- LÍNEA DIVISORIA ---
                    cs.setLineWidth(1);
                    cs.moveTo(40, 510);
                    cs.lineTo(380, 510);
                    cs.stroke();

                    // --- DATOS DEL PACIENTE ---
                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
                    cs.newLineAtOffset(40, 480);
                    cs.showText("Paciente:");
                    cs.endText();

                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
                    cs.newLineAtOffset(130, 480);
                    cs.showText(paciente);
                    cs.endText();

                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
                    cs.newLineAtOffset(40, 455);
                    cs.showText("Clinica:");
                    cs.endText();

                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
                    cs.newLineAtOffset(130, 455);
                    cs.showText(clinica);
                    cs.endText();

                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
                    cs.newLineAtOffset(40, 430);
                    cs.showText("Fecha:");
                    cs.endText();

                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
                    cs.newLineAtOffset(130, 430);
                    cs.showText(fecha);
                    cs.endText();

                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
                    cs.newLineAtOffset(40, 405);
                    cs.showText("Motivo:");
                    cs.endText();

                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
                    cs.newLineAtOffset(130, 405);
                    cs.showText(motivo);
                    cs.endText();

                    // --- LÍNEA DIVISORIA ---
                    cs.moveTo(40, 380);
                    cs.lineTo(380, 380);
                    cs.stroke();

                    // --- PIE DE PÁGINA ---
                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
                    cs.newLineAtOffset(40, 360);
                    cs.showText("Este documento es un comprobante generado por el sistema.");
                    cs.endText();

                    cs.beginText();
                    cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
                    cs.newLineAtOffset(40, 345);
                    cs.showText("Por favor presentese 10 minutos antes de su cita.");
                    cs.endText();
                }

                // Guardar el documento en el flujo de salida
                document.save(baos);
            }

            byte[] pdfBytes = baos.toByteArray();

            return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=\"ticket_cita.pdf\"")
                    .header("Access-Control-Expose-Headers", "Content-Disposition")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error al generar PDF: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/guardar-notas")
    public Response guardarNotas(@PathParam("id") UUID id,
            @QueryParam("diagnostico") String diagnostico,
            @QueryParam("plan") String plan) {
        try {
            Consulta c = consultaService.buscarPorId(id);
            if (c == null) {
                return Response.status(404).entity("Consulta no encontrada").build();
            }

            // Concatenamos las notas en el campo observaciones
            String notas = "DIAGNÓSTICO: " + diagnostico + " | PLAN: " + plan;
            c.setObservaciones(notas);

            consultaService.modificar(c);
            return Response.ok(c).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error al guardar notas: " + e.getMessage()).build();
        }
    }

      @PUT
    @Path("/{id}/finalizar")
    public Response finalizarConsulta(@PathParam("id") UUID id) {
        try {
            Consulta c = consultaService.buscarPorId(id);
            if (c == null) {
                return Response.status(404).entity("Consulta no encontrada").build();
            }

            c.setFechaFin(new Date()); 
            consultaService.modificar(c);

            // 🔥 FIX EXTRA: Finalizar todos los pasos que quedaron pendientes en esta consulta
            List<ConsultaProcedimiento> cps = consultaProcedimientoService.buscarPorConsulta(id);
            for (ConsultaProcedimiento cp : cps) {
                List<ConsultaProcedimientoPaso> pasos = consultaProcedimientoPasoService.buscarPorConsultaProcedimiento(cp.getIdConsultaProcedimiento());
                for (ConsultaProcedimientoPaso paso : pasos) {
                    if (!"FINALIZADO".equals(paso.getEstado())) {
                        paso.setEstado("FINALIZADO");
                        paso.setFechaFin(new Date());
                        consultaProcedimientoPasoService.modificar(paso);
                    }
                }
            }

            return Response.ok(c).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error al finalizar: " + e.getMessage()).build();
        }
    }

   @GET
@Path("/por-clinica-rol")
public Response buscarPorClinicaYRol(
        @QueryParam("idClinica") UUID idClinica,
        @QueryParam("idRol") UUID idRol,
        @QueryParam("fechaInicio") String fechaInicioStr,
        @QueryParam("fechaFin") String fechaFinStr) {
    try {
        if (idClinica == null) {
            return Response.status(400).entity("idClinica requerido").build();
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date inicio = (fechaInicioStr != null && !fechaInicioStr.isEmpty()) ? sdf.parse(fechaInicioStr) : null;
        Date fin = (fechaFinStr != null && !fechaFinStr.isEmpty()) ? sdf.parse(fechaFinStr) : null;

        if (fin != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(fin);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            fin = cal.getTime();
        }

        List<Consulta> lista = consultaService.buscarPorClinicaYRol(idClinica, idRol, inicio, fin);
        return Response.ok(lista).build();
    } catch (Exception e) {
        return Response.status(500).entity("Error: " + e.getMessage()).build();
    }
}

@GET
@Path("/{id}/detalle-completo")
public Response detalleCompleto(@PathParam("id") UUID id) {
    try {
        Consulta c = consultaService.buscarPorId(id);
        if (c == null) return Response.status(404).entity("Consulta no encontrada").build();

        ConsultaDetalleDTO dto = new ConsultaDetalleDTO();
        dto.idConsulta = c.getIdConsulta();
        dto.fechaInicio = c.getFechaInicio() != null ? c.getFechaInicio().toInstant().toString() : null;
        dto.fechaFin = c.getFechaFin() != null ? c.getFechaFin().toInstant().toString() : null;
        dto.referenciaExterna = c.getReferenciaExterna();
        dto.observaciones = c.getObservaciones();

        if (c.getIdPersonaRol() != null) {
            Persona p = c.getIdPersonaRol().getIdPersona();
            if (p != null) {
                dto.paciente = new ConsultaDetalleDTO.PersonaDetalleDTO();
                dto.paciente.idPersona = p.getIdPersona();
                dto.paciente.nombres = p.getNombres();
                dto.paciente.apellidos = p.getApellidos();
                dto.paciente.fechaNacimiento = p.getFechaNacimiento() != null
                        ? p.getFechaNacimiento().toInstant().toString() : null;
            }
            Clinica cli = c.getIdPersonaRol().getIdClinica();
            if (cli != null) {
                dto.clinica = new ConsultaDetalleDTO.ClinicaDetalleDTO();
                dto.clinica.idClinica = cli.getIdClinica();
                dto.clinica.nombre = cli.getNombre();
            }
        }

        List<ConsultaProcedimiento> cps = consultaProcedimientoService.buscarPorConsulta(id);
        dto.procedimientos = new java.util.ArrayList<>();

        for (ConsultaProcedimiento cp : cps) {
            ConsultaDetalleDTO.ProcedimientoDetalleDTO pd = new ConsultaDetalleDTO.ProcedimientoDetalleDTO();
            pd.idConsultaProcedimiento = cp.getIdConsultaProcedimiento();
            pd.idProcedimiento = cp.getIdProcedimiento();
            pd.fechaInicio = cp.getFechaInicio() != null ? cp.getFechaInicio().toInstant().toString() : null;
            pd.fechaFin = cp.getFechaFin() != null ? cp.getFechaFin().toInstant().toString() : null;
            pd.observaciones = cp.getObservaciones();

            Procedimiento proc = procedimientoService.buscarPorId(cp.getIdProcedimiento());
            if (proc != null) pd.nombreProcedimiento = proc.getNombre();

            pd.pasos = new java.util.ArrayList<>();
            List<ConsultaProcedimientoPaso> pasos = consultaProcedimientoPasoService.buscarPorConsultaProcedimiento(cp.getIdConsultaProcedimiento());

            for (ConsultaProcedimientoPaso paso : pasos) {
                ConsultaDetalleDTO.PasoDetalleDTO psd = new ConsultaDetalleDTO.PasoDetalleDTO();
                psd.idConsultaProcedimientoPaso = paso.getIdConsultaProcedimientoPaso();
                psd.estado = paso.getEstado();
                psd.fechaInicio = paso.getFechaInicio() != null ? paso.getFechaInicio().toInstant().toString() : null;
                psd.fechaFin = paso.getFechaFin() != null ? paso.getFechaFin().toInstant().toString() : null;
                psd.valor = paso.getValor();

                if (paso.getIdProcedimientoPaso() != null) {
                    psd.idProcedimientoPaso = paso.getIdProcedimientoPaso().getIdProcedimientoPaso();
                    psd.nombrePaso = paso.getIdProcedimientoPaso().getNombre();
                }
                if (paso.getIdPersonaRol() != null) {
                    PersonaRol pr = paso.getIdPersonaRol();
                    if (pr.getIdPersona() != null) {
                        psd.realizadoPor = new ConsultaDetalleDTO.PersonaDetalleDTO();
                        psd.realizadoPor.idPersona = pr.getIdPersona().getIdPersona();
                        psd.realizadoPor.nombres = pr.getIdPersona().getNombres();
                        psd.realizadoPor.apellidos = pr.getIdPersona().getApellidos();
                    }
                    if (pr.getIdRol() != null) {
                        psd.idRolRealizador = pr.getIdRol().getIdRol();
                        psd.nombreRolRealizador = pr.getIdRol().getNombre();
                    }
                }
                pd.pasos.add(psd);
            }
            dto.procedimientos.add(pd);
        }

        return Response.ok(dto).build();
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(500).entity("Error: " + e.getMessage()).build();
    }
}
    

}
