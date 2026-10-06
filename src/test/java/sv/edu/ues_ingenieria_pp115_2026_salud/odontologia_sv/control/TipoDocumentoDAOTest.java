
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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;

@ExtendWith(MockitoExtension.class)
public class TipoDocumentoDAOTest {
    
    
    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<TipoDocumento> typedQuery;

    @InjectMocks
    private TipoDocumentoDAO dao;

    // =============================================
    // ENTIDADES DE PRUEBA (se reinician en cada test)
    // =============================================
    private TipoDocumento tipoDui;
    private TipoDocumento tipoPasaporte;
    private TipoDocumento tipoInactivo;

    // =============================================
    // SETUP
    // =============================================
    @BeforeEach
    public void setUp() {
        // Tipo de documento activo 1
        tipoDui = new TipoDocumento();
        tipoDui.setIdTipoDocumento(UUID.randomUUID());
        tipoDui.setNombre("DUI");
        tipoDui.setIndicaciones("Documento Único de Identidad");
        tipoDui.setExpresionRegular("^\\d{8}-\\d$");
        tipoDui.setActivo(true);

        // Tipo de documento activo 2
        tipoPasaporte = new TipoDocumento();
        tipoPasaporte.setIdTipoDocumento(UUID.randomUUID());
        tipoPasaporte.setNombre("Pasaporte");
        tipoPasaporte.setIndicaciones("Documento de viaje");
        tipoPasaporte.setExpresionRegular("^[A-Z]\\d{7}$");
        tipoPasaporte.setActivo(true);

      
    }

    // =============================================
    // TESTS DE buscarPorNombre()
    // =============================================

    @Test
    public void testBuscarPorNombreConNullDevuelveListaVacia() {
        // when
        List<TipoDocumento> resultado = dao.buscarPorNombre(null);

        // then
        assertThat(resultado).isEmpty();

        // Y NO se llamó al EntityManager
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorNombreConValorValido() {
        // given
        List<TipoDocumento> esperado = List.of(tipoDui);

        when(em.createNamedQuery("TipoDocumento.findByNombre", TipoDocumento.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        // when
        List<TipoDocumento> resultado = dao.buscarPorNombre("dui");

        // then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("DUI");

        // Verificamos el parámetro transformado
        verify(em, times(1)).createNamedQuery("TipoDocumento.findByNombre", TipoDocumento.class);
        verify(typedQuery, times(1)).setParameter("nombre", "%DUI%");
    }

    @Test
    public void testBuscarPorNombreConExcepcionDevuelveListaVacia() {
        // given
        when(em.createNamedQuery("TipoDocumento.findByNombre", TipoDocumento.class))
                .thenThrow(new RuntimeException("Error simulado"));

        // when
        List<TipoDocumento> resultado = dao.buscarPorNombre("DUI");

        // then
        assertThat(resultado).isEmpty();
    }

    // =============================================
    // TESTS DE listarActivos()
    // =============================================

    @Test
    public void testListarActivosConsultaConActivoTrue() {
        // given
        List<TipoDocumento> esperado = List.of(tipoDui, tipoPasaporte);

        when(em.createNamedQuery("TipoDocumento.findByActivo", TipoDocumento.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        // when
        List<TipoDocumento> resultado = dao.listarActivos();

        // then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(TipoDocumento::getActivo).containsOnly(true);

        // Verificamos el parámetro
        verify(em, times(1)).createNamedQuery("TipoDocumento.findByActivo", TipoDocumento.class);
        verify(typedQuery, times(1)).setParameter("activo", true);
    }

    @Test
    public void testListarActivosConExcepcionDevuelveListaVacia() {
        // given
        when(em.createNamedQuery("TipoDocumento.findByActivo", TipoDocumento.class))
                .thenThrow(new RuntimeException("Error simulado"));

        // when
        List<TipoDocumento> resultado = dao.listarActivos();

        // then
        assertThat(resultado).isEmpty();
    }

    // =============================================
    // TESTS DE buscarPorExpresionRegular()
    // =============================================

    @Test
    public void testBuscarPorExpresionRegularConNullDevuelveNull() {
        // when
        TipoDocumento resultado = dao.buscarPorExpresionRegular(null);

        // then
        assertThat(resultado).isNull();

        // Y NO se consultó al EntityManager
        verify(em, never()).createQuery(anyString(), any(Class.class));
    }

    @Test
    public void testBuscarPorExpresionRegularConValorValido() {
        // given
        List<TipoDocumento> esperado = List.of(tipoDui);

        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        // when
        TipoDocumento resultado = dao.buscarPorExpresionRegular("^\\d{8}-\\d$");

        // then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("DUI");

        // Verificamos el parámetro
        verify(typedQuery, times(1)).setParameter("expresionRegular", "^\\d{8}-\\d$");
    }

    @Test
    public void testBuscarPorExpresionRegularSinResultadosDevuelveNull() {
        // given: la consulta devuelve lista vacía
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        // when
        TipoDocumento resultado = dao.buscarPorExpresionRegular("inexistente");

        // then
        assertThat(resultado).isNull();
    }

    @Test
    public void testBuscarPorExpresionRegularConExcepcionDevuelveNull() {
        // given
        when(em.createQuery(anyString(), any(Class.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        // when
        TipoDocumento resultado = dao.buscarPorExpresionRegular("^\\d+$");

        // then
        assertThat(resultado).isNull();
    }
    
    
    
}

