
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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

@ExtendWith(MockitoExtension.class)
public class RolDAOTest {
   
    
   @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Rol> typedQuery;

    @InjectMocks
    private RolDAO dao;

    // =============================================
    // ENTIDADES DE PRUEBA (se reinician en cada test)
    // =============================================
    private Rol rol;
   

    // =============================================
    // SETUP
    // =============================================
    @BeforeEach
    public void setUp() {
        rol = new Rol();
        rol.setIdRol(UUID.randomUUID());
        rol.setNombre("Odontólogo General");
        rol.setActivo(true);
        rol.setObservaciones("Dentista general");

        
    }

    // =============================================
    // TESTS DE buscarPorNombre()
    // =============================================

    @Test
    public void testBuscarPorNombreConNullDevuelveListaVacia() {
        // when
        List<Rol> resultado = dao.buscarPorNombre(null);

        // then
        assertThat(resultado).isEmpty();

        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorNombreConValorValido() {
        // given
        List<Rol> esperado = List.of(rol);

        when(em.createNamedQuery("Rol.findByNombre", Rol.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        // when
        List<Rol> resultado = dao.buscarPorNombre("Odontólogo");

        // then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Odontólogo General");

        verify(em, times(1)).createNamedQuery("Rol.findByNombre", Rol.class);
        verify(typedQuery, times(1)).setParameter("nombre", "%ODONTÓLOGO%");
    }

    @Test
    public void testBuscarPorNombreConExcepcionDevuelveListaVacia() {
        // given
        when(em.createNamedQuery("Rol.findByNombre", Rol.class))
                .thenThrow(new RuntimeException("Error simulado"));

        // when
        List<Rol> resultado = dao.buscarPorNombre("Odontólogo");

        // then
        assertThat(resultado).isEmpty();
    }

    // =============================================
    // TESTS DE listarActivos()
    // =============================================

    @Test
    public void testListarActivosConsultaConActivoTrue() {
        // given
        List<Rol> esperado = List.of(rol);

        when(em.createNamedQuery("Rol.findByActivo", Rol.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        // when
        List<Rol> resultado = dao.listarActivos();

        // then
        assertThat(resultado).hasSize(1);
        assertThat(resultado).extracting(Rol::getActivo).containsOnly(true);

        // Y verificamos los parámetros
        verify(em, times(1)).createNamedQuery("Rol.findByActivo", Rol.class);
        verify(typedQuery, times(1)).setParameter("activo", true);
    }

    @Test
    public void testListarActivosConExcepcionDevuelveListaVacia() {
        // given
        when(em.createNamedQuery("Rol.findByActivo", Rol.class))
                .thenThrow(new RuntimeException("Error simulado"));

        // when
        List<Rol> resultado = dao.listarActivos();

        // then
        assertThat(resultado).isEmpty();
    }
    
}
