package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;

@ExtendWith(MockitoExtension.class)
public class PersonaRolDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<PersonaRol> typedQuery;

    @InjectMocks
    private PersonaRolDAO dao;

    private PersonaRol pr;
    private UUID idPersona;
    private UUID idRol;
    private UUID idClinica;

    @BeforeEach
    public void setUp() {
        pr = new PersonaRol();
        pr.setIdPersonaRol(UUID.randomUUID());
        idPersona = UUID.randomUUID();
        idRol = UUID.randomUUID();
        idClinica = UUID.randomUUID();
    }

    @Test
    public void testBuscarPorPersonaConNullDevuelveListaVacia() {
        List<PersonaRol> resultado = dao.buscarPorPersona(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(PersonaRol.class));
    }

    @Test
    @DisplayName("buscarPorPersona(id) consulta con parámetro")
    public void testBuscarPorPersonaConIdValido() {
        when(em.createQuery(anyString(), eq(PersonaRol.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(pr));

        List<PersonaRol> resultado = dao.buscarPorPersona(idPersona);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idPersona", idPersona);
    }

    @Test
    public void testBuscarPorPersonaConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(PersonaRol.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorPersona(idPersona)).isEmpty();
    }

    @Test
    @DisplayName("buscarPorRol(null) devuelve lista vacía")
    public void testBuscarPorRolConNullDevuelveListaVacia() {
        List<PersonaRol> resultado = dao.buscarPorRol(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(PersonaRol.class));
    }

    @Test
    public void testBuscarPorRolConIdValido() {
        when(em.createQuery(anyString(), eq(PersonaRol.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(pr));

        List<PersonaRol> resultado = dao.buscarPorRol(idRol);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idRol", idRol);
    }

    @Test
    public void testBuscarPorRolConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(PersonaRol.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorRol(idRol)).isEmpty();
    }

    @Test
    public void testBuscarPorClinicaConNullDevuelveListaVacia() {
        List<PersonaRol> resultado = dao.buscarPorClinica(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(PersonaRol.class));
    }

    @Test
    public void testBuscarPorClinicaConIdValido() {
        when(em.createQuery(anyString(), eq(PersonaRol.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(pr));

        List<PersonaRol> resultado = dao.buscarPorClinica(idClinica);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idClinica", idClinica);
    }

    @Test
    public void testBuscarPorClinicaConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(PersonaRol.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorClinica(idClinica)).isEmpty();
    }

    @Test
    public void testBuscarPorPersonaYRolConNullDevuelveNull() {
        PersonaRol resultado = dao.buscarPorPersonaYRol(null, idRol);
        assertThat(resultado).isNull();
        verify(em, never()).createQuery(anyString(), eq(PersonaRol.class));
    }

    @Test
    public void testBuscarPorPersonaYRolConValoresValidos() {
        when(em.createQuery(anyString(), eq(PersonaRol.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(pr));

        PersonaRol resultado = dao.buscarPorPersonaYRol(idPersona, idRol);

        assertThat(resultado).isNotNull();
        verify(typedQuery, times(1)).setParameter("idPersona", idPersona);
        verify(typedQuery, times(1)).setParameter("idRol", idRol);
    }

    @Test
    public void testBuscarPorPersonaYRolSinResultadosDevuelveNull() {
        when(em.createQuery(anyString(), eq(PersonaRol.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        PersonaRol resultado = dao.buscarPorPersonaYRol(idPersona, idRol);
        assertThat(resultado).isNull();
    }

    @Test
    public void testBuscarPorPersonaYRolConExcepcionDevuelveNull() {
        when(em.createQuery(anyString(), eq(PersonaRol.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorPersonaYRol(idPersona, idRol)).isNull();
    }
}