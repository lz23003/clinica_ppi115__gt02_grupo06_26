package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.JwtTokenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;

@Stateless
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private PersonaRolService personaRolService;

    @Inject
    private JwtTokenService jwtTokenService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null
                || request.getNumeroDocumento() == null
                || request.getNumeroDocumento().isBlank()
                || request.getNumeroDocumento().trim().length() > 30) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Ingresa un tipo y número de documento válidos."))
                    .build();
        }

        final String documentType;
        try {
            documentType = TipoDocumentoFiltro.patron(request.getTipoDocumento());
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", ex.getMessage()))
                    .build();
        }

        List<PersonaRol> assignments = personaRolService.buscarAsignacionesActivasPorDocumento(
                documentType, request.getNumeroDocumento().trim());
        if (assignments.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("message", "El documento no está asociado a una asignación activa de empleado."))
                    .build();
        }
        if (!jwtTokenService.isConfigured()) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(Map.of("message", "El servidor no tiene configurada la firma de sesiones."))
                    .build();
        }

        PersonaRol firstAssignment = assignments.get(0);
        var person = firstAssignment.getIdPersona();
        List<LoginResponse.Assignment> responseAssignments = assignments.stream()
                .map(assignment -> new LoginResponse.Assignment(
                        assignment.getIdPersonaRol(),
                        assignment.getIdClinica().getIdClinica(),
                        assignment.getIdClinica().getNombre(),
                        assignment.getIdRol().getIdRol(),
                        assignment.getIdRol().getNombre()))
                .toList();
        LoginResponse.Employee employee = new LoginResponse.Employee(
                person.getIdPersona(), person.getNombres(), person.getApellidos(), responseAssignments);

        return Response.ok(new LoginResponse(
                true,
                "Identidad y asignación activa validadas.",
                employee,
                jwtTokenService.issueToken(person.getIdPersona())))
                .build();
    }
}
