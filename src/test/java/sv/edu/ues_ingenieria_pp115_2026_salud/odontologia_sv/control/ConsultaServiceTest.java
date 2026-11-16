
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;

@ExtendWith(MockitoExtension.class)
public class ConsultaServiceTest {
    
    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Consulta> typedQuery;

    @InjectMocks
    private ConsultaService service;


    private Consulta consultaActiva;
    private Consulta consultaCerrada;
    private UUID idPaciente;
    private UUID idPersonaRol;
    private Date fechaInicio;
    private Date fechaFin;

  
    @BeforeEach
    public void setUp() {
        consultaActiva = new Consulta();
        consultaActiva.setIdConsulta(UUID.randomUUID());
        consultaActiva.setFechaInicio(new Date());

        consultaCerrada = new Consulta();
        consultaCerrada.setIdConsulta(UUID.randomUUID());
        consultaCerrada.setFechaInicio(new Date());
        consultaCerrada.setFechaFin(new Date());

        idPaciente = UUID.randomUUID();
        idPersonaRol = UUID.randomUUID();

        fechaInicio = new Date(System.currentTimeMillis() - 86400000L); 
        fechaFin = new Date(); 
    }

    @Test
    public void testBuscarPorConsultasActivasDevuelveLista() {
        List<Consulta> esperado = List.of(consultaActiva);

        when(em.createNamedQuery(anyString(), eq(Consulta.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Consulta> resultado = service.buscarPorConsultasActivas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdConsulta()).isEqualTo(consultaActiva.getIdConsulta());
        verify(em, times(1)).createNamedQuery(anyString(), eq(Consulta.class));
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testBuscarPorConsultasActivasConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery(anyString(), eq(Consulta.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Consulta> resultado = service.buscarPorConsultasActivas();

        assertThat(resultado).isEmpty();
    }

 
    @Test
    public void testBuscarPorPacienteConNullDevuelveListaVacia() {
        List<Consulta> resultado = service.buscarPorPaciente(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarPorPacienteConIdValido() {
        List<Consulta> esperado = List.of(consultaActiva, consultaCerrada);

        when(em.createNamedQuery(anyString(), eq(Consulta.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Consulta> resultado = service.buscarPorPaciente(idPaciente);

        assertThat(resultado).hasSize(2);
        verify(em, times(1)).createNamedQuery(anyString(), eq(Consulta.class));
        verify(typedQuery, times(1)).setParameter("idPaciente", idPaciente);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testBuscarPorPacienteConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery(anyString(), eq(Consulta.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Consulta> resultado = service.buscarPorPaciente(idPaciente);

        assertThat(resultado).isEmpty();
    }

  
    @Test
    public void testBuscarPorRangoFechasConInicioNullDevuelveListaVacia() {
        
        List<Consulta> resultado = service.buscarPorRangoFechas(null, fechaFin);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarPorRangoFechasConFinNullDevuelveListaVacia() {
        List<Consulta> resultado = service.buscarPorRangoFechas(fechaInicio, null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarPorRangoFechasConValoresValidos() {
        List<Consulta> esperado = List.of(consultaActiva);

        when(em.createNamedQuery(anyString(), eq(Consulta.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Consulta> resultado = service.buscarPorRangoFechas(fechaInicio, fechaFin);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createNamedQuery(anyString(), eq(Consulta.class));
        verify(typedQuery, times(1)).setParameter("inicio", fechaInicio);
        verify(typedQuery, times(1)).setParameter("fin", fechaFin);
    }

    @Test
    public void testBuscarPorRangoFechasConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery(anyString(), eq(Consulta.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Consulta> resultado = service.buscarPorRangoFechas(fechaInicio, fechaFin);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Busca consultas por clínica, rol y rango de fechas sin paginación")
    public void testBuscarDashboardPorFiltros() {
        UUID idClinica = UUID.randomUUID();
        UUID idRol = UUID.randomUUID();
        Date desde = new Date(1_700_000_000_000L);
        Date hasta = new Date(1_700_086_400_000L);
        List<Consulta> expected = List.of(consultaActiva);

        when(em.createNamedQuery("Consulta.buscarDashboardPorFiltros", Consulta.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expected);

        List<Consulta> actual = service.buscarDashboardPorFiltros(idClinica, idRol, desde, hasta);

        assertThat(actual).containsExactly(consultaActiva);
        verify(typedQuery).setParameter("idClinica", idClinica);
        verify(typedQuery).setParameter("idRol", idRol);
        verify(typedQuery).setParameter("desde", desde);
        verify(typedQuery).setParameter("hasta", hasta);
        verify(typedQuery).getResultList();
    }

    @Test
    @DisplayName("Rechaza un rango de fechas vacío o invertido")
    public void testBuscarDashboardPorFiltrosRechazaRangoInvalido() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                service.buscarDashboardPorFiltros(null, null, fechaFin, fechaInicio))
                .isInstanceOf(IllegalArgumentException.class);
        verify(em, never()).createNamedQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarCitasPublicasAplicaFiltrosYLimite() {
        UUID clinicId = UUID.randomUUID();
        Date start = new Date(System.currentTimeMillis() - 86400000L);
        Date end = new Date();
        when(em.createNamedQuery("Consulta.findCitasPublicas", Consulta.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(consultaActiva));

        List<Consulta> results = service.buscarCitasPublicas(
                "DUI", " 01234567-8 ", clinicId, start, end, 100);

        assertThat(results).containsExactly(consultaActiva);
        verify(typedQuery).setParameter("tipoDocumento", "%DUI%");
        verify(typedQuery).setParameter("numeroDocumento", "01234567-8");
        verify(typedQuery).setParameter("idClinica", clinicId);
        verify(typedQuery).setParameter("desde", start);
        verify(typedQuery).setParameter("hasta", end);
        verify(typedQuery).setMaxResults(100);
    }

    @Test
    public void testBuscarCitasPublicasRejectsInvalidRange() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                service.buscarCitasPublicas("DUI", "123", UUID.randomUUID(), fechaFin, fechaInicio, 100))
                .isInstanceOf(IllegalArgumentException.class);
        verify(em, never()).createNamedQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void guardaIdentificadoresDeExamenEnObservacionesSinTablaRelacion() {
        UUID examId = UUID.randomUUID();
        Examen exam = new Examen(examId);
        exam.setActivo(true);
        consultaActiva.setObservaciones("{\"historial\":[{\"paso\":\"Triaje\"}]}");

        when(em.find(Examen.class, examId)).thenReturn(exam);
        service.actualizarExamenes(consultaActiva, List.of(examId));

        assertThat(consultaActiva.getExamenIds()).containsExactly(examId);
        assertThat(service.obtenerExamenIds(consultaActiva)).containsExactly(examId);
        assertThat(consultaActiva.getObservaciones())
                .contains("\"paso\":\"Triaje\"")
                .contains("\"examenIds\":[\"" + examId + "\"]");
        verify(em).find(Examen.class, examId);
    }

    @Test
    public void devuelveListaVaciaCuandoConsultaNoTieneExamenesGuardados() {
        assertThat(service.obtenerExamenIds(consultaActiva)).isEmpty();
    }

    @Test
    public void rechazaExamenInexistenteOInactivo() {
        UUID examId = UUID.randomUUID();
        when(em.find(Examen.class, examId)).thenReturn(null);

        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                service.actualizarExamenes(consultaActiva, List.of(examId)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no existe o está inactivo");
    }


    @Test
    @DisplayName("buscarPorPersonaRol(null) devuelve lista vacía sin consultar")
    public void testBuscarPorPersonaRolConNullDevuelveListaVacia() {
        List<Consulta> resultado = service.buscarPorPersonaRol(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarPorPersonaRolConIdValido() {
        List<Consulta> esperado = List.of(consultaActiva);

        when(em.createNamedQuery(anyString(), eq(Consulta.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Consulta> resultado = service.buscarPorPersonaRol(idPersonaRol);

        
        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createNamedQuery(anyString(), eq(Consulta.class));
        verify(typedQuery, times(1)).setParameter("idPersonaRol", idPersonaRol);
    }

    @Test
    public void testBuscarPorPersonaRolConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery(anyString(), eq(Consulta.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Consulta> resultado = service.buscarPorPersonaRol(idPersonaRol);

        assertThat(resultado).isEmpty();
    }
    
}
