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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;

@ExtendWith(MockitoExtension.class)
public class ExamenDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Examen> typedQuery;

    @InjectMocks
    private ExamenDAO dao;

    private Examen radiografia;
    private Examen hemograma;
    private Examen inactivo;

    @BeforeEach
    public void setUp() {
        radiografia = new Examen();
        radiografia.setIdExamen(UUID.randomUUID());
        radiografia.setNombre("Radiografía Periapical");
        radiografia.setActivo(true);

        hemograma = new Examen();
        hemograma.setIdExamen(UUID.randomUUID());
        hemograma.setNombre("Hemograma Completo");
        hemograma.setActivo(true);

       
    }

    @Test
    public void testBuscarPorNombreConNullDevuelveListaVacia() {
        List<Examen> resultado = dao.buscarPorNombre(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorNombreConValorValido() {
        List<Examen> esperado = List.of(radiografia);
        when(em.createNamedQuery("Examen.findByNombre", Examen.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Examen> resultado = dao.buscarPorNombre("radio");

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("nombre", "%RADIO%");
    }

    @Test
    public void testBuscarPorNombreConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Examen.findByNombre", Examen.class))
                .thenThrow(new RuntimeException("Error simulado"));
        List<Examen> resultado = dao.buscarPorNombre("radio");
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testListarActivosConsultaConActivoTrue() {
        List<Examen> esperado = List.of(radiografia, hemograma);
        when(em.createNamedQuery("Examen.findByActivo", Examen.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Examen> resultado = dao.listarActivos();

        assertThat(resultado).hasSize(2);
        verify(typedQuery, times(1)).setParameter("activo", true);
    }

    @Test
    public void testListarActivosConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Examen.findByActivo", Examen.class))
                .thenThrow(new RuntimeException("Error simulado"));
        List<Examen> resultado = dao.listarActivos();
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testFindByNombreLikeConNullDevuelveListaVacia() {
        List<Examen> resultado = dao.findByNombreLike(null, 0, 5);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testFindByNombreLikeConFirstNegativoDevuelveListaVacia() {
        List<Examen> resultado = dao.findByNombreLike("radio", -1, 5);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testFindByNombreLikeConMaxCeroDevuelveListaVacia() {
        List<Examen> resultado = dao.findByNombreLike("radio", 0, 0);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testFindByNombreLikeConValorValido() {
        List<Examen> esperado = List.of(radiografia);
        when(em.createNamedQuery("Examen.findByNombreLike", Examen.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Examen> resultado = dao.findByNombreLike("radio", 0, 5);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("nombre", "%RADIO%");
        verify(typedQuery, times(1)).setFirstResult(0);
        verify(typedQuery, times(1)).setMaxResults(5);
    }

    @Test
    public void testFindByNombreLikeConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Examen.findByNombreLike", Examen.class))
                .thenThrow(new RuntimeException("Error simulado"));
        List<Examen> resultado = dao.findByNombreLike("radio", 0, 5);
        assertThat(resultado).isEmpty();
    }
}