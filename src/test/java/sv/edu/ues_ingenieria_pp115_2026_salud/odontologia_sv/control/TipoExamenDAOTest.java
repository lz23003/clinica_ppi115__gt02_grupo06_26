
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;

@ExtendWith(MockitoExtension.class)
public class TipoExamenDAOTest {
    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<TipoExamen> typedQuery;

    @InjectMocks
    private TipoExamenDAO dao;

  
    private TipoExamen tipoRadiografico;
    private TipoExamen tipoClinico;
    private TipoExamen tipoInactivo;

   
    @BeforeEach
    public void setUp() {
        tipoRadiografico = new TipoExamen();
        tipoRadiografico.setIdTipoExamen(UUID.randomUUID());
        tipoRadiografico.setNombre("Radiográfico");
        tipoRadiografico.setActivo(true);

        tipoClinico = new TipoExamen();
        tipoClinico.setIdTipoExamen(UUID.randomUUID());
        tipoClinico.setNombre("Clínico");
        tipoClinico.setActivo(true);

   
    }


    @Test
    public void testBuscarPorNombreConNullDevuelveListaVacia() {
        List<TipoExamen> resultado = dao.buscarPorNombre(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorNombreConValorValido() {
        List<TipoExamen> esperado = List.of(tipoRadiografico);

        when(em.createNamedQuery("TipoExamen.findByNombre", TipoExamen.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<TipoExamen> resultado = dao.buscarPorNombre("radio");

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createNamedQuery("TipoExamen.findByNombre", TipoExamen.class);
        verify(typedQuery, times(1)).setParameter("nombre", "%RADIO%");
    }

    @Test
    public void testBuscarPorNombreConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("TipoExamen.findByNombre", TipoExamen.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<TipoExamen> resultado = dao.buscarPorNombre("radio");

        assertThat(resultado).isEmpty();
    }

  
    @Test
    public void testListarActivosConsultaConActivoTrue() {
        List<TipoExamen> esperado = List.of(tipoRadiografico, tipoClinico);

        when(em.createNamedQuery("TipoExamen.findByActivo", TipoExamen.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<TipoExamen> resultado = dao.listarActivos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(TipoExamen::getActivo).containsOnly(true);
        verify(em, times(1)).createNamedQuery("TipoExamen.findByActivo", TipoExamen.class);
        verify(typedQuery, times(1)).setParameter("activo", true);
    }

    @Test
    public void testListarActivosConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("TipoExamen.findByActivo", TipoExamen.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<TipoExamen> resultado = dao.listarActivos();

        assertThat(resultado).isEmpty();
    }


    @Test
    public void testFindByNombreLikeConNullDevuelveListaVacia() {
        List<TipoExamen> resultado = dao.findByNombreLike(null, 0, 5);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testFindByNombreLikeConFirstNegativoDevuelveListaVacia() {
        List<TipoExamen> resultado = dao.findByNombreLike("radio", -1, 5);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testFindByNombreLikeConMaxCeroDevuelveListaVacia() {
        List<TipoExamen> resultado = dao.findByNombreLike("radio", 0, 0);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testFindByNombreLikeConValorValido() {
        List<TipoExamen> esperado = List.of(tipoRadiografico);

        when(em.createNamedQuery("TipoExamen.findByNombreLike", TipoExamen.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<TipoExamen> resultado = dao.findByNombreLike("radio", 0, 5);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createNamedQuery("TipoExamen.findByNombreLike", TipoExamen.class);
        verify(typedQuery, times(1)).setParameter("nombre", "%RADIO%");
        verify(typedQuery, times(1)).setFirstResult(0);
        verify(typedQuery, times(1)).setMaxResults(5);
    }

    @Test
    public void testFindByNombreLikeConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("TipoExamen.findByNombreLike", TipoExamen.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<TipoExamen> resultado = dao.findByNombreLike("radio", 0, 5);

        assertThat(resultado).isEmpty();
    }
    
}
