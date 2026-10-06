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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;

@ExtendWith(MockitoExtension.class)
public class PersonaDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Persona> typedQuery;

    @InjectMocks
    private PersonaDAO dao;

    private Persona persona;

    @BeforeEach
    public void setUp() {
        persona = new Persona();
        persona.setIdPersona(UUID.randomUUID());
        persona.setNombres("Juan");
        persona.setApellidos("Pérez");
    }

    @Test
    public void testBuscarPorNombreConNullDevuelveListaVacia() {
        List<Persona> resultado = dao.buscarPorNombre(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorNombreConValorValido() {
        when(em.createNamedQuery("Persona.findByNombres", Persona.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(persona));

        List<Persona> resultado = dao.buscarPorNombre("juan");

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("nombres", "%JUAN%");
    }

    @Test
    public void testBuscarPorNombreConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Persona.findByNombres", Persona.class))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorNombre("juan")).isEmpty();
    }

    @Test
    public void testBuscarPorApellidoConNullDevuelveListaVacia() {
        List<Persona> resultado = dao.buscarPorApellido(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createNamedQuery(anyString(), any());
    }

    @Test
    public void testBuscarPorApellidoConValorValido() {
        when(em.createNamedQuery("Persona.findByApellidos", Persona.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(persona));

        List<Persona> resultado = dao.buscarPorApellido("perez");

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("apellidos", "%PEREZ%");
    }

    @Test
    public void testBuscarPorApellidoConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Persona.findByApellidos", Persona.class))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorApellido("perez")).isEmpty();
    }

    @Test
    public void testBuscarPorDocumentoConNullDevuelveNull() {
        Persona resultado = dao.buscarPorDocumento(null);
        assertThat(resultado).isNull();
        verify(em, never()).createQuery(anyString(), eq(Persona.class));
    }

    @Test
    public void testBuscarPorDocumentoConValorValido() {
        when(em.createQuery(anyString(), eq(Persona.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(persona));

        Persona resultado = dao.buscarPorDocumento("01234567-8");

        assertThat(resultado).isNotNull();
        verify(typedQuery, times(1)).setParameter("valor", "01234567-8");
    }

    @Test
    public void testBuscarPorDocumentoSinResultadosDevuelveNull() {
        when(em.createQuery(anyString(), eq(Persona.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        Persona resultado = dao.buscarPorDocumento("inexistente");
        assertThat(resultado).isNull();
    }

    @Test
    public void testBuscarPorDocumentoConExcepcionDevuelveNull() {
        when(em.createQuery(anyString(), eq(Persona.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorDocumento("x")).isNull();
    }
}