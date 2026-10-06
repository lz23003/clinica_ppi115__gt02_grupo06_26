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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ExamenTipoExamen;

@ExtendWith(MockitoExtension.class)
public class ExamenTipoExamenDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<ExamenTipoExamen> typedQuery;

    @InjectMocks
    private ExamenTipoExamenDAO dao;

    private ExamenTipoExamen ete;
    private UUID idExamen;
    private UUID idTipoExamen;

    @BeforeEach
    public void setUp() {
        ete = new ExamenTipoExamen();
        ete.setIdExamenTipoExamen(UUID.randomUUID());
        idExamen = UUID.randomUUID();
        idTipoExamen = UUID.randomUUID();
    }

    @Test
    public void testBuscarPorExamenConNullDevuelveListaVacia() {
        List<ExamenTipoExamen> resultado = dao.buscarPorExamen(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ExamenTipoExamen.class));
    }

    @Test
    public void testBuscarPorExamenConIdValido() {
        List<ExamenTipoExamen> esperado = List.of(ete);
        when(em.createQuery(anyString(), eq(ExamenTipoExamen.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ExamenTipoExamen> resultado = dao.buscarPorExamen(idExamen);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idExamen", idExamen);
    }

    @Test
    public void testBuscarPorExamenConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ExamenTipoExamen.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ExamenTipoExamen> resultado = dao.buscarPorExamen(idExamen);
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPorTipoExamenConNullDevuelveListaVacia() {
        List<ExamenTipoExamen> resultado = dao.buscarPorTipoExamen(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(ExamenTipoExamen.class));
    }

    @Test
    public void testBuscarPorTipoExamenConIdValido() {
        List<ExamenTipoExamen> esperado = List.of(ete);
        when(em.createQuery(anyString(), eq(ExamenTipoExamen.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<ExamenTipoExamen> resultado = dao.buscarPorTipoExamen(idTipoExamen);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idTipoExamen", idTipoExamen);
    }

    @Test
    public void testBuscarPorTipoExamenConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(ExamenTipoExamen.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        List<ExamenTipoExamen> resultado = dao.buscarPorTipoExamen(idTipoExamen);
        assertThat(resultado).isEmpty();
    }
}