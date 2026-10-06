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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoSecuencia;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoSecuenciaDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<ProcedimientoPasoSecuencia> typedQuery;

    @InjectMocks
    private ProcedimientoPasoSecuenciaDAO dao;

    private ProcedimientoPasoSecuencia secuencia;
    private UUID idPaso;

    @BeforeEach
    public void setUp() {
        secuencia = new ProcedimientoPasoSecuencia();
        secuencia.setIdProcedimientoPasoSecuencia(UUID.randomUUID());
        secuencia.setTipoSecuencia("SIGUIENTE");

        idPaso = UUID.randomUUID();
    }

    @Test
    public void testBuscarPorPasoConNullDevuelveListaVacia() {
        List<ProcedimientoPasoSecuencia> resultado = dao.buscarPorPaso(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ProcedimientoPasoSecuencia.class));
    }

    @Test
    public void testBuscarPorPasoConIdValido() {
        List<ProcedimientoPasoSecuencia> esperado = List.of(secuencia);
        when(em.createQuery(anyString(), eq(ProcedimientoPasoSecuencia.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPasoSecuencia> resultado = dao.buscarPorPaso(idPaso);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idPaso", idPaso);
    }

    @Test
    public void testBuscarPorPasoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ProcedimientoPasoSecuencia.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPasoSecuencia> resultado = dao.buscarPorPaso(idPaso);
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarSiguientePasoConNullDevuelveNull() {
        ProcedimientoPasoSecuencia resultado = dao.buscarSiguientePaso(null);
        assertThat(resultado).isNull();
        verify(em, never()).createQuery(anyString(), eq(ProcedimientoPasoSecuencia.class));
    }

    @Test
    public void testBuscarSiguientePasoConIdValido() {
        List<ProcedimientoPasoSecuencia> esperado = List.of(secuencia);
        when(em.createQuery(anyString(), eq(ProcedimientoPasoSecuencia.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        ProcedimientoPasoSecuencia resultado = dao.buscarSiguientePaso(idPaso);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTipoSecuencia()).isEqualTo("SIGUIENTE");
    }

    @Test
    public void testBuscarSiguientePasoSinResultadosDevuelveNull() {
        when(em.createQuery(anyString(), eq(ProcedimientoPasoSecuencia.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        ProcedimientoPasoSecuencia resultado = dao.buscarSiguientePaso(idPaso);
        assertThat(resultado).isNull();
    }

    @Test
    public void testBuscarSiguientePasoConExcepcionDevuelveNull() {
        when(em.createQuery(anyString(), eq(ProcedimientoPasoSecuencia.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        ProcedimientoPasoSecuencia resultado = dao.buscarSiguientePaso(idPaso);
        assertThat(resultado).isNull();
    }

    @Test
    public void testBuscarPorTipoSecuenciaConNullDevuelveListaVacia() {
        List<ProcedimientoPasoSecuencia> resultado = dao.buscarPorTipoSecuencia(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorTipoSecuenciaConValorValido() {
        List<ProcedimientoPasoSecuencia> esperado = List.of(secuencia);
        when(em.createNamedQuery("ProcedimientoPasoSecuencia.findByTipoSecuencia", ProcedimientoPasoSecuencia.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPasoSecuencia> resultado = dao.buscarPorTipoSecuencia("siguiente");

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("tipoSecuencia", "SIGUIENTE");
    }

    @Test
    public void testBuscarPorTipoSecuenciaConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("ProcedimientoPasoSecuencia.findByTipoSecuencia", ProcedimientoPasoSecuencia.class))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPasoSecuencia> resultado = dao.buscarPorTipoSecuencia("siguiente");
        assertThat(resultado).isEmpty();
    }
}