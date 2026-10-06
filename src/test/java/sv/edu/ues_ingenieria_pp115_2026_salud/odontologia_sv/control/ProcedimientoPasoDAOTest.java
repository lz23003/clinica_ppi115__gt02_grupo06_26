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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<ProcedimientoPaso> typedQuery;

    @InjectMocks
    private ProcedimientoPasoDAO dao;

    private ProcedimientoPaso paso;
    private UUID idProcedimiento;
    private UUID idRol;

    @BeforeEach
    public void setUp() {
        paso = new ProcedimientoPaso();
        paso.setIdProcedimientoPaso(UUID.randomUUID());
        paso.setNombre("Paso 1");
        paso.setIndicaFin(false);

        idProcedimiento = UUID.randomUUID();
        idRol = UUID.randomUUID();
    }

    @Test
    public void testBuscarPorProcedimientoConNullDevuelveListaVacia() {
        List<ProcedimientoPaso> resultado = dao.buscarPorProcedimiento(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ProcedimientoPaso.class));
    }

    @Test
    public void testBuscarPorProcedimientoConIdValido() {
        List<ProcedimientoPaso> esperado = List.of(paso);
        when(em.createQuery(anyString(), eq(ProcedimientoPaso.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPaso> resultado = dao.buscarPorProcedimiento(idProcedimiento);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idProcedimiento", idProcedimiento);
    }

    @Test
    public void testBuscarPorProcedimientoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ProcedimientoPaso.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPaso> resultado = dao.buscarPorProcedimiento(idProcedimiento);
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPorNombreConNullDevuelveListaVacia() {
        List<ProcedimientoPaso> resultado = dao.buscarPorNombre(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorNombreConValorValido() {
        List<ProcedimientoPaso> esperado = List.of(paso);
        when(em.createNamedQuery("ProcedimientoPaso.findByNombre", ProcedimientoPaso.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPaso> resultado = dao.buscarPorNombre("paso");

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("nombre", "%PASO%");
    }

    @Test
    public void testBuscarPorNombreConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("ProcedimientoPaso.findByNombre", ProcedimientoPaso.class))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPaso> resultado = dao.buscarPorNombre("paso");
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPasosFinales() {
        List<ProcedimientoPaso> esperado = List.of(paso);
        when(em.createQuery(anyString(), eq(ProcedimientoPaso.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPaso> resultado = dao.buscarPasosFinales();

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createQuery(anyString(), eq(ProcedimientoPaso.class));
    }

    @Test
    public void testBuscarPasosFinalesConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ProcedimientoPaso.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPaso> resultado = dao.buscarPasosFinales();
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPorRolConNullDevuelveListaVacia() {
        List<ProcedimientoPaso> resultado = dao.buscarPorRol(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ProcedimientoPaso.class));
    }

    @Test
    public void testBuscarPorRolConIdValido() {
        List<ProcedimientoPaso> esperado = List.of(paso);
        when(em.createQuery(anyString(), eq(ProcedimientoPaso.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPaso> resultado = dao.buscarPorRol(idRol);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idRol", idRol);
    }

    @Test
    public void testBuscarPorRolConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ProcedimientoPaso.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPaso> resultado = dao.buscarPorRol(idRol);
        assertThat(resultado).isEmpty();
    }
}