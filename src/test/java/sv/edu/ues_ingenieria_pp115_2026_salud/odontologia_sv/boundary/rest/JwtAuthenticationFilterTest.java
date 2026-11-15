package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.JwtTokenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    private static final String TOKEN = "test-token";
    private final UUID employeeId = UUID.randomUUID();
    private final UUID clinicId = UUID.randomUUID();
    private final UUID roleId = UUID.randomUUID();

    @Mock
    private PersonaRolService personaRolService;

    @Mock
    private ConsultaService consultaService;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @Test
    void permitsDashboardQueryWhenClinicAndRoleAreAssignedToTokenSubject() throws Exception {
        ContainerRequestContext request = request("GET", "consultas", query(clinicId, roleId));
        PersonaRol assignment = assignment(clinicId, roleId);
        when(personaRolService.buscarAsignacionesActivasPorPersona(employeeId))
                .thenReturn(List.of(assignment));

        filter.filter(request);

        verify(request, never()).abortWith(any(Response.class));
    }

    @Test
    void doesNotAuthorizeAnUnassignedClinicAndRolePair() {
        UUID otherRoleId = UUID.randomUUID();
        PersonaRol assignment = assignment(clinicId, roleId);
        when(personaRolService.buscarAsignacionesActivasPorPersona(employeeId))
                .thenReturn(List.of(assignment));
        assertFalse(filter.hasAssignment(employeeId, clinicId, otherRoleId));
    }

    @Test
    void validatesConsultationPathAgainstItsClinicAndRole() throws Exception {
        UUID consultationId = UUID.randomUUID();
        ContainerRequestContext request = request(
                "GET", "api/v1/consultas/" + consultationId, new MultivaluedHashMap<>());
        PersonaRol assignment = assignment(clinicId, roleId);
        when(consultaService.buscarContextoPorConsulta(consultationId))
                .thenReturn(new Object[]{clinicId, roleId});
        when(personaRolService.buscarAsignacionesActivasPorPersona(employeeId))
                .thenReturn(List.of(assignment));

        filter.filter(request);

        verify(consultaService).buscarContextoPorConsulta(consultationId);
        verify(request, never()).abortWith(any(Response.class));
    }

    private ContainerRequestContext request(
            String method, String path, MultivaluedMap<String, String> queryParameters) {
        ContainerRequestContext request = org.mockito.Mockito.mock(ContainerRequestContext.class);
        UriInfo uriInfo = org.mockito.Mockito.mock(UriInfo.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getPath()).thenReturn(path);
        when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
        when(request.getHeaderString("Authorization")).thenReturn("Bearer " + TOKEN);
        when(jwtTokenService.isValid(TOKEN)).thenReturn(true);
        when(jwtTokenService.getSubjectId(TOKEN)).thenReturn(employeeId);
        return request;
    }

    private MultivaluedMap<String, String> query(UUID selectedClinicId, UUID selectedRoleId) {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("idClinica", selectedClinicId.toString());
        parameters.add("idRol", selectedRoleId.toString());
        return parameters;
    }

    private PersonaRol assignment(UUID assignedClinicId, UUID assignedRoleId) {
        Clinica clinic = org.mockito.Mockito.mock(Clinica.class);
        when(clinic.getIdClinica()).thenReturn(assignedClinicId);
        Rol role = org.mockito.Mockito.mock(Rol.class);
        when(role.getIdRol()).thenReturn(assignedRoleId);
        PersonaRol assignment = org.mockito.Mockito.mock(PersonaRol.class);
        when(assignment.getIdClinica()).thenReturn(clinic);
        when(assignment.getIdRol()).thenReturn(role);
        return assignment;
    }
}
