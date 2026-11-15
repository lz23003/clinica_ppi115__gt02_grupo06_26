package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.JwtTokenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private JwtTokenService jwtTokenService;

    @Inject
    private PersonaRolService personaRolService;

    @Inject
    private ConsultaService consultaService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }
        if (!isProtectedPath(requestContext.getUriInfo().getPath())) {
            return;
        }

        String authorization = requestContext.getHeaderString("Authorization");
        String token = authorization != null && authorization.startsWith("Bearer ")
                ? authorization.substring("Bearer ".length()).trim()
                : null;
        if (!jwtTokenService.isValid(token)) {
            abortUnauthorized(requestContext);
            return;
        }

        UUID employeeId = jwtTokenService.getSubjectId(token);
        if (employeeId == null) {
            abortUnauthorized(requestContext);
            return;
        }

        String path = pathWithinResource(normalize(requestContext.getUriInfo().getPath()), "consultas");
        String clinicParameter = requestContext.getUriInfo().getQueryParameters().getFirst("idClinica");
        String roleParameter = requestContext.getUriInfo().getQueryParameters().getFirst("idRol");
        boolean contextProvided = hasValue(clinicParameter) || hasValue(roleParameter);
        if (contextProvided) {
            UUID clinicId = parseUuid(clinicParameter);
            UUID roleId = parseUuid(roleParameter);
            if (clinicId == null || roleId == null || !hasAssignment(employeeId, clinicId, roleId)) {
                abortForbidden(requestContext);
                return;
            }
        }

        if ("GET".equalsIgnoreCase(requestContext.getMethod()) && "consultas".equals(path)) {
            if (!contextProvided) {
                abortForbidden(requestContext);
            }
            return;
        }

        if (path.startsWith("consultas/")) {
            String[] segments = path.split("/");
            if (segments.length != 2 || "all".equalsIgnoreCase(segments[1])) {
                abortForbidden(requestContext);
                return;
            }
            UUID consultationId = parseUuid(segments[1]);
            if (consultationId == null) {
                abortForbidden(requestContext);
                return;
            }
            Object[] context = consultaService.buscarContextoPorConsulta(consultationId);
            if (context == null || context.length < 2
                    || !hasAssignment(employeeId, (UUID) context[0], (UUID) context[1])) {
                abortForbidden(requestContext);
            }
        }
    }

    private void abortUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(Map.of("message", "Tu sesión no es válida o expiró. Inicia sesión nuevamente."))
                .build());
    }

    private void abortForbidden(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(Map.of("message", "No tienes una asignación activa para esta clínica, rol o consulta."))
                .build());
    }

    boolean hasAssignment(UUID employeeId, UUID clinicId, UUID roleId) {
        if (employeeId == null || clinicId == null || roleId == null) {
            return false;
        }
        return personaRolService.buscarAsignacionesActivasPorPersona(employeeId).stream()
                .anyMatch(assignment -> assignment.getIdClinica() != null
                        && assignment.getIdRol() != null
                        && clinicId.equals(assignment.getIdClinica().getIdClinica())
                        && roleId.equals(assignment.getIdRol().getIdRol()));
    }

    private UUID parseUuid(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    private String normalize(String path) {
        return path == null ? "" : path.replaceAll("^/+", "");
    }

    private String pathWithinResource(String path, String resource) {
        int resourceIndex = path.indexOf(resource);
        return resourceIndex < 0 ? path : path.substring(resourceIndex);
    }

    private boolean isProtectedPath(String path) {
        String normalizedPath = path == null ? "" : path.replaceAll("^/+", "");
        return isResourcePath(normalizedPath, "consultas")
                || isResourcePath(normalizedPath, "consultas-procedimientos")
                || isResourcePath(normalizedPath, "consultas-procedimientos-pasos");
    }

    private boolean isResourcePath(String path, String resource) {
        return path.equals(resource)
                || path.startsWith(resource + "/")
                || path.endsWith("/" + resource)
                || path.contains("/" + resource + "/");
    }
}
