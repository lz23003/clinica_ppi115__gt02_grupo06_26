package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;

@ExtendWith(MockitoExtension.class)
public class ConsultaProcedimientoPasoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<ConsultaProcedimientoPaso> typedQuery;

    @InjectMocks
    private ConsultaProcedimientoPasoDAO dao;

    private ConsultaProcedimientoPaso cpp;
    private UUID idConsultaProcedimiento;
    private UUID idPersonaRol;

    @BeforeEach
    public void setUp() {
        cpp = new ConsultaProcedimientoPaso();
        cpp.setIdConsultaProcedimientoPaso(UUID.randomUUID());
        cpp.setEstado("PENDIENTE");
        idConsultaProcedimiento = UUID.randomUUID();
        idPersonaRol = UUID.randomUUID();
    }

    @Test
    public void testBuscarPorConsultaProcedimientoConNullDevuelveListaVacia() {
        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorConsultaProcedimiento(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ConsultaProcedimientoPaso.class));
    }

    @Test
    public void testBuscarPorConsultaProcedimientoConIdValido() {
        List<ConsultaProcedimientoPaso> esperado = List.of(cpp);
        when(em.createQuery(anyString(), eq(ConsultaProcedimientoPaso.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorConsultaProcedimiento(idConsultaProcedimiento);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idConsultaProcedimiento", idConsultaProcedimiento);
    }

    @Test
    public void testBuscarPorConsultaProcedimientoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ConsultaProcedimientoPaso.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorConsultaProcedimiento(idConsultaProcedimiento);
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPorEstadoConNullDevuelveListaVacia() {
        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorEstado(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ConsultaProcedimientoPaso.class));
    }

    @Test
    public void testBuscarPorEstadoConValorValido() {
        List<ConsultaProcedimientoPaso> esperado = List.of(cpp);
        when(em.createQuery(anyString(), eq(ConsultaProcedimientoPaso.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorEstado("pendiente");

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("estado", "PENDIENTE");
    }

    @Test
    public void testBuscarPorEstadoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ConsultaProcedimientoPaso.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorEstado("pendiente");
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPorPersonaRolConNullDevuelveListaVacia() {
        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorPersonaRol(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ConsultaProcedimientoPaso.class));
    }

    @Test
    public void testBuscarPorPersonaRolConIdValido() {
        List<ConsultaProcedimientoPaso> esperado = List.of(cpp);
        when(em.createQuery(anyString(), eq(ConsultaProcedimientoPaso.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorPersonaRol(idPersonaRol);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idPersonaRol", idPersonaRol);
    }

    @Test
    public void testBuscarPorPersonaRolConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ConsultaProcedimientoPaso.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ConsultaProcedimientoPaso> resultado = dao.buscarPorPersonaRol(idPersonaRol);
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPendientes() {
        List<ConsultaProcedimientoPaso> esperado = List.of(cpp);
        when(em.createQuery(anyString(), eq(ConsultaProcedimientoPaso.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ConsultaProcedimientoPaso> resultado = dao.buscarPendientes();

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createQuery(anyString(), eq(ConsultaProcedimientoPaso.class));
    }

    @Test
    public void testBuscarPendientesConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ConsultaProcedimientoPaso.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ConsultaProcedimientoPaso> resultado = dao.buscarPendientes();
        assertThat(resultado).isEmpty();
    }
}