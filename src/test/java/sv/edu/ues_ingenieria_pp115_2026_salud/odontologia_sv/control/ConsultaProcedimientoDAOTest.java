
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
public class ConsultaProcedimientoDAOTest {
    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<ConsultaProcedimiento> typedQuery;

    @InjectMocks
    private ConsultaProcedimientoDAO dao;

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
        List<ConsultaProcedimiento> resultado = dao.buscarPorConsulta(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ConsultaProcedimiento.class));
    }

    @Test
    public void testBuscarPorConsultaConIdValido() {
        List<ConsultaProcedimiento> esperado = List.of(procedimiento);

        when(em.createQuery(anyString(), eq(ConsultaProcedimiento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimiento> resultado = dao.buscarPorConsulta(idConsulta);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createQuery(anyString(), eq(ConsultaProcedimiento.class));
        verify(typedQuery, times(1)).setParameter("idConsulta", idConsulta);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testBuscarPorConsultaConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ConsultaProcedimiento.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<ConsultaProcedimiento> resultado = dao.buscarPorConsulta(idConsulta);

        assertThat(resultado).isEmpty();
    }

 

    @Test
    public void testBuscarPorProcedimientoConNullDevuelveListaVacia() {
        List<ConsultaProcedimiento> resultado = dao.buscarPorProcedimiento(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ConsultaProcedimiento.class));
    }

    @Test
    public void testBuscarPorProcedimientoConIdValido() {
        List<ConsultaProcedimiento> esperado = List.of(procedimiento);

        when(em.createQuery(anyString(), eq(ConsultaProcedimiento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimiento> resultado = dao.buscarPorProcedimiento(idProcedimiento);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createQuery(anyString(), eq(ConsultaProcedimiento.class));
        verify(typedQuery, times(1)).setParameter("idProcedimiento", idProcedimiento);
    }

    @Test
    public void testBuscarPorProcedimientoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ConsultaProcedimiento.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<ConsultaProcedimiento> resultado = dao.buscarPorProcedimiento(idProcedimiento);

        assertThat(resultado).isEmpty();
    }


    @Test
    public void testBuscarPorRangoFechasConInicioNullDevuelveListaVacia() {
        List<ConsultaProcedimiento> resultado = dao.buscarPorRangoFechas(null, fechaFin);

        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ConsultaProcedimiento.class));
    }

    @Test
    public void testBuscarPorRangoFechasConFinNullDevuelveListaVacia() {
        List<ConsultaProcedimiento> resultado = dao.buscarPorRangoFechas(fechaInicio, null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ConsultaProcedimiento.class));
    }

    @Test
    public void testBuscarPorRangoFechasConValoresValidos() {
        List<ConsultaProcedimiento> esperado = List.of(procedimiento);

        when(em.createQuery(anyString(), eq(ConsultaProcedimiento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimiento> resultado = dao.buscarPorRangoFechas(fechaInicio, fechaFin);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createQuery(anyString(), eq(ConsultaProcedimiento.class));
        verify(typedQuery, times(1)).setParameter("inicio", fechaInicio);
        verify(typedQuery, times(1)).setParameter("fin", fechaFin);
    }

    @Test
    public void testBuscarPorRangoFechasConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ConsultaProcedimiento.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<ConsultaProcedimiento> resultado = dao.buscarPorRangoFechas(fechaInicio, fechaFin);

        assertThat(resultado).isEmpty();
    }
   
}
