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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoExamen;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoExamenDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<ProcedimientoPasoExamen> typedQuery;

    @InjectMocks
    private ProcedimientoPasoExamenDAO dao;

    private ProcedimientoPasoExamen ppe;
    private UUID idPaso;
    private UUID idExamen;

    @BeforeEach
    public void setUp() {
        ppe = new ProcedimientoPasoExamen();
        ppe.setIdProcedimientoPasoExamen(UUID.randomUUID());
        ppe.setActivo(true);

        idPaso = UUID.randomUUID();
        idExamen = UUID.randomUUID();
    }

    @Test
    public void testBuscarPorPasoConNullDevuelveListaVacia() {
        List<ProcedimientoPasoExamen> resultado = dao.buscarPorPaso(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ProcedimientoPasoExamen.class));
    }

    @Test
    public void testBuscarPorPasoConIdValido() {
        List<ProcedimientoPasoExamen> esperado = List.of(ppe);
        when(em.createQuery(anyString(), eq(ProcedimientoPasoExamen.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPasoExamen> resultado = dao.buscarPorPaso(idPaso);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idPaso", idPaso);
    }

    @Test
    public void testBuscarPorPasoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ProcedimientoPasoExamen.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPasoExamen> resultado = dao.buscarPorPaso(idPaso);
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPorExamenConNullDevuelveListaVacia() {
        List<ProcedimientoPasoExamen> resultado = dao.buscarPorExamen(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ProcedimientoPasoExamen.class));
    }

    @Test
    public void testBuscarPorExamenConIdValido() {
        List<ProcedimientoPasoExamen> esperado = List.of(ppe);
        when(em.createQuery(anyString(), eq(ProcedimientoPasoExamen.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPasoExamen> resultado = dao.buscarPorExamen(idExamen);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idExamen", idExamen);
    }

    @Test
    public void testBuscarPorExamenConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ProcedimientoPasoExamen.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPasoExamen> resultado = dao.buscarPorExamen(idExamen);
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testListarActivosConsultaConActivoTrue() {
        List<ProcedimientoPasoExamen> esperado = List.of(ppe);
        when(em.createNamedQuery("ProcedimientoPasoExamen.findByActivo", ProcedimientoPasoExamen.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ProcedimientoPasoExamen> resultado = dao.listarActivos();

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("activo", true);
    }

    @Test
    public void testListarActivosConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("ProcedimientoPasoExamen.findByActivo", ProcedimientoPasoExamen.class))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ProcedimientoPasoExamen> resultado = dao.listarActivos();
        assertThat(resultado).isEmpty();
    }
}