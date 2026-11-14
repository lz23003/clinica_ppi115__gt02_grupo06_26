
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;

@ExtendWith(MockitoExtension.class)
public class ConsultaProcedimientoTest {
    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<ConsultaProcedimiento> typedQuery;

    @Mock
    private TypedQuery<Object[]> namesQuery;

    @InjectMocks
    private ConsultaProcedimientoService service;

    private ConsultaProcedimiento procedimiento;
    private UUID idConsulta;
    private UUID idProcedimiento;
    private Date fechaInicio;
    private Date fechaFin;


    @BeforeEach
    public void setUp() {
        procedimiento = new ConsultaProcedimiento();
        procedimiento.setIdConsultaProcedimiento(UUID.randomUUID());
        procedimiento.setFechaInicio(new Date());

        idConsulta = UUID.randomUUID();
        idProcedimiento = UUID.randomUUID();

        fechaInicio = new Date(System.currentTimeMillis() - 86400000L); // ayer
        fechaFin = new Date(); // hoy
    }



    @Test
    public void testBuscarPorConsultaConNullDevuelveListaVacia() {
        List<ConsultaProcedimiento> resultado = service.buscarPorConsulta(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(ConsultaProcedimiento.class));
    }

    @Test
    public void testBuscarPorConsultaConIdValido() {
        List<ConsultaProcedimiento> esperado = List.of(procedimiento);

        when(em.createNamedQuery(anyString(), eq(ConsultaProcedimiento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimiento> resultado = service.buscarPorConsulta(idConsulta);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createNamedQuery(anyString(), eq(ConsultaProcedimiento.class));
        verify(typedQuery, times(1)).setParameter("idConsulta", idConsulta);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testBuscarPorConsultaConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery(anyString(), eq(ConsultaProcedimiento.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<ConsultaProcedimiento> resultado = service.buscarPorConsulta(idConsulta);

        assertThat(resultado).isEmpty();
    }

 

    @Test
    public void testBuscarPorProcedimientoConNullDevuelveListaVacia() {
        List<ConsultaProcedimiento> resultado = service.buscarPorProcedimiento(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(ConsultaProcedimiento.class));
    }

    @Test
    public void testBuscarPorProcedimientoConIdValido() {
        List<ConsultaProcedimiento> esperado = List.of(procedimiento);

        when(em.createNamedQuery(anyString(), eq(ConsultaProcedimiento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimiento> resultado = service.buscarPorProcedimiento(idProcedimiento);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createNamedQuery(anyString(), eq(ConsultaProcedimiento.class));
        verify(typedQuery, times(1)).setParameter("idProcedimiento", idProcedimiento);
    }

    @Test
    public void testBuscarPorProcedimientoConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery(anyString(), eq(ConsultaProcedimiento.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<ConsultaProcedimiento> resultado = service.buscarPorProcedimiento(idProcedimiento);

        assertThat(resultado).isEmpty();
    }


    @Test
    public void testBuscarPorRangoFechasConInicioNullDevuelveListaVacia() {
        List<ConsultaProcedimiento> resultado = service.buscarPorRangoFechas(null, fechaFin);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(ConsultaProcedimiento.class));
    }

    @Test
    public void testBuscarPorRangoFechasConFinNullDevuelveListaVacia() {
        List<ConsultaProcedimiento> resultado = service.buscarPorRangoFechas(fechaInicio, null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(ConsultaProcedimiento.class));
    }

    @Test
    public void testBuscarPorRangoFechasConValoresValidos() {
        List<ConsultaProcedimiento> esperado = List.of(procedimiento);

        when(em.createNamedQuery(anyString(), eq(ConsultaProcedimiento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimiento> resultado = service.buscarPorRangoFechas(fechaInicio, fechaFin);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createNamedQuery(anyString(), eq(ConsultaProcedimiento.class));
        verify(typedQuery, times(1)).setParameter("inicio", fechaInicio);
        verify(typedQuery, times(1)).setParameter("fin", fechaFin);
    }

    @Test
    public void testBuscarPorRangoFechasConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery(anyString(), eq(ConsultaProcedimiento.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<ConsultaProcedimiento> resultado = service.buscarPorRangoFechas(fechaInicio, fechaFin);

        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarNombresPorConsultasAgrupaProcedimientos() {
        UUID secondConsultationId = UUID.randomUUID();
        when(em.createNamedQuery(
                "ConsultaProcedimiento.findProcedureNamesByConsultas", Object[].class))
                .thenReturn(namesQuery);
        when(namesQuery.getResultList()).thenReturn(List.of(
                new Object[]{idConsulta, "Evaluación"},
                new Object[]{idConsulta, "Limpieza"},
                new Object[]{secondConsultationId, "Radiografía"}));

        var result = service.buscarNombresPorConsultas(List.of(idConsulta, secondConsultationId));

        assertThat(result.get(idConsulta)).containsExactly("Evaluación", "Limpieza");
        assertThat(result.get(secondConsultationId)).containsExactly("Radiografía");
        verify(namesQuery).setParameter("consultaIds", List.of(idConsulta, secondConsultationId));
    }

    @Test
    public void testBuscarNombresPorConsultasVaciasNoConsulta() {
        assertThat(service.buscarNombresPorConsultas(List.of())).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), eq(Object[].class));
    }
   
}
