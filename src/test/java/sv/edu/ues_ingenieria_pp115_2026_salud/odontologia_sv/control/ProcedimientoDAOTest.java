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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Procedimiento> typedQuery;

    @InjectMocks
    private ProcedimientoDAO dao;

    private Procedimiento limpieza;
    private Procedimiento extraccion;
    private Procedimiento inactivo;

    @BeforeEach
    public void setUp() {
        limpieza = new Procedimiento();
        limpieza.setIdProcedimiento(UUID.randomUUID());
        limpieza.setNombre("Limpieza Dental");
        limpieza.setActivo(true);

        extraccion = new Procedimiento();
        extraccion.setIdProcedimiento(UUID.randomUUID());
        extraccion.setNombre("Extracción");
        extraccion.setActivo(true);

    
    }

    @Test
    public void testBuscarPorNombreProcedimientosConNullDevuelveListaVacia() {
        List<Procedimiento> resultado = dao.buscarPorNombreProcedimientos(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorNombreProcedimientosConValorValido() {
        List<Procedimiento> esperado = List.of(limpieza);
        when(em.createNamedQuery("Procedimiento.findByNombre", Procedimiento.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Procedimiento> resultado = dao.buscarPorNombreProcedimientos("limpieza");

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createNamedQuery("Procedimiento.findByNombre", Procedimiento.class);
        verify(typedQuery, times(1)).setParameter("nombre", "%LIMPIEZA%");
    }

    @Test
    public void testBuscarPorNombreProcedimientosConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Procedimiento.findByNombre", Procedimiento.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Procedimiento> resultado = dao.buscarPorNombreProcedimientos("limpieza");
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testListarActivosConsultaConActivoTrue() {
        List<Procedimiento> esperado = List.of(limpieza, extraccion);
        when(em.createNamedQuery("Procedimiento.findByActivo", Procedimiento.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Procedimiento> resultado = dao.listarActivos();

        assertThat(resultado).hasSize(2);
        verify(em, times(1)).createNamedQuery("Procedimiento.findByActivo", Procedimiento.class);
        verify(typedQuery, times(1)).setParameter("activo", true);
    }

    @Test
    public void testListarActivosConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Procedimiento.findByActivo", Procedimiento.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Procedimiento> resultado = dao.listarActivos();
        assertThat(resultado).isEmpty();
    }
}
