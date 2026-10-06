
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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

@ExtendWith(MockitoExtension.class)
public class TipoMedioContactoDAOTest {
    
   
 @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<TipoMedioContacto> typedQuery;

    @InjectMocks
    private TipoMedioContactoDAO dao;


    private TipoMedioContacto tipoTelefono;
    private TipoMedioContacto tipoEmail;

 
    @BeforeEach
    public void setUp() {
        tipoTelefono = new TipoMedioContacto();
        tipoTelefono.setIdTipoMedioContacto(UUID.randomUUID());
        tipoTelefono.setNombre("Teléfono Móvil");
        tipoTelefono.setIndicaciones("Teléfono móvil salvadoreño");
        tipoTelefono.setExpresionRegular("^[67]\\d{3}-\\d{4}$");
        tipoTelefono.setActivo(true);

        tipoEmail = new TipoMedioContacto();
        tipoEmail.setIdTipoMedioContacto(UUID.randomUUID());
        tipoEmail.setNombre("Correo Electrónico");
        tipoEmail.setIndicaciones("Correo electrónico estándar");
        tipoEmail.setExpresionRegular("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        tipoEmail.setActivo(true);

       
    }


    @Test
    public void testBuscarPorNombreConNullDevuelveListaVacia() {
        List<TipoMedioContacto> resultado = dao.buscarPorNombre(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorNombreConValorValido() {
        List<TipoMedioContacto> esperado = List.of(tipoTelefono);

        when(em.createNamedQuery("TipoMedioContacto.findByNombre", TipoMedioContacto.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<TipoMedioContacto> resultado = dao.buscarPorNombre("teléfono");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Teléfono Móvil");
        verify(em, times(1)).createNamedQuery("TipoMedioContacto.findByNombre", TipoMedioContacto.class);
        verify(typedQuery, times(1)).setParameter("nombre", "%TELÉFONO%");
    }

    @Test
    public void testBuscarPorNombreConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("TipoMedioContacto.findByNombre", TipoMedioContacto.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<TipoMedioContacto> resultado = dao.buscarPorNombre("teléfono");

        assertThat(resultado).isEmpty();
    }

    // =============================================
    // TESTS DE listarActivos()
    // =============================================

    @Test
    public void testListarActivosConsultaConActivoTrue() {
        List<TipoMedioContacto> esperado = List.of(tipoTelefono, tipoEmail);

        when(em.createNamedQuery("TipoMedioContacto.findByActivo", TipoMedioContacto.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<TipoMedioContacto> resultado = dao.listarActivos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(TipoMedioContacto::getActivo).containsOnly(true);
        verify(em, times(1)).createNamedQuery("TipoMedioContacto.findByActivo", TipoMedioContacto.class);
        verify(typedQuery, times(1)).setParameter("activo", true);
    }

    @Test
    public void testListarActivosConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("TipoMedioContacto.findByActivo", TipoMedioContacto.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<TipoMedioContacto> resultado = dao.listarActivos();

        assertThat(resultado).isEmpty();
    }

    // =============================================
    // TESTS DE buscarPorExpresionRegular()
    // =============================================

    @Test
    public void testBuscarPorExpresionRegularConNullDevuelveNull() {
        TipoMedioContacto resultado = dao.buscarPorExpresionRegular(null);

        assertThat(resultado).isNull();
        verify(em, never()).createQuery(anyString(), any(Class.class));
    }

    @Test
    public void testBuscarPorExpresionRegularConValorValido() {
        List<TipoMedioContacto> esperado = List.of(tipoTelefono);

        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        TipoMedioContacto resultado = dao.buscarPorExpresionRegular("^[67]\\d{3}-\\d{4}$");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Teléfono Móvil");
        verify(typedQuery, times(1)).setParameter("expresionRegular", "^[67]\\d{3}-\\d{4}$");
    }

    @Test
    public void testBuscarPorExpresionRegularSinResultadosDevuelveNull() {
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        TipoMedioContacto resultado = dao.buscarPorExpresionRegular("inexistente");

        assertThat(resultado).isNull();
    }

    @Test
    public void testBuscarPorExpresionRegularConExcepcionDevuelveNull() {
        when(em.createQuery(anyString(), any(Class.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        TipoMedioContacto resultado = dao.buscarPorExpresionRegular("^.*$");

        assertThat(resultado).isNull();
    }
  

    
    
}
