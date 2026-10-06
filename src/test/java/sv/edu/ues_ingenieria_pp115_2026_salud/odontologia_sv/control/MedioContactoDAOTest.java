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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;

@ExtendWith(MockitoExtension.class)
public class MedioContactoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<MedioContacto> typedQuery;

    @InjectMocks
    private MedioContactoDAO dao;

    private MedioContacto mc;
    private UUID idPersona;
    private UUID idTipo;

    @BeforeEach
    public void setUp() {
        mc = new MedioContacto();
        mc.setIdMedioContacto(UUID.randomUUID());
        mc.setValor("7777-7777");
        idPersona = UUID.randomUUID();
        idTipo = UUID.randomUUID();
    }

    @Test
    public void testBuscarPorPersonaConNullDevuelveListaVacia() {
        List<MedioContacto> resultado = dao.buscarPorPersona(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(MedioContacto.class));
    }

    @Test
    public void testBuscarPorPersonaConIdValido() {
        when(em.createQuery(anyString(), eq(MedioContacto.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(mc));

        List<MedioContacto> resultado = dao.buscarPorPersona(idPersona);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idPersona", idPersona);
    }

    @Test
    public void testBuscarPorPersonaConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(MedioContacto.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorPersona(idPersona)).isEmpty();
    }

    @Test
    public void testBuscarPorTipoMedioContactoConNullDevuelveListaVacia() {
        List<MedioContacto> resultado = dao.buscarPorTipoMedioContacto(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(MedioContacto.class));
    }

    @Test
    public void testBuscarPorTipoMedioContactoConIdValido() {
        when(em.createQuery(anyString(), eq(MedioContacto.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(mc));

        List<MedioContacto> resultado = dao.buscarPorTipoMedioContacto(idTipo);
        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idTipoMedioContacto", idTipo);
    }

    @Test
    public void testBuscarPorTipoMedioContactoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(MedioContacto.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorTipoMedioContacto(idTipo)).isEmpty();
    }

    @Test
    public void testBuscarPorValorConNullDevuelveListaVacia() {
        List<MedioContacto> resultado = dao.buscarPorValor(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(MedioContacto.class));
    }

    @Test
    public void testBuscarPorValorConValorValido() {
        when(em.createQuery(anyString(), eq(MedioContacto.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(mc));

        List<MedioContacto> resultado = dao.buscarPorValor("7777");

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("valor", "%7777%");
    }

    @Test
    public void testBuscarPorValorConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(MedioContacto.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorValor("7777")).isEmpty();
    }

    @Test
    public void testBuscarPorPersonaYTipoConNullDevuelveListaVacia() {
        List<MedioContacto> resultado = dao.buscarPorPersonaYTipo(null, idTipo);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(MedioContacto.class));
    }

    @Test
    public void testBuscarPorPersonaYTipoConValoresValidos() {
        when(em.createQuery(anyString(), eq(MedioContacto.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(mc));

        List<MedioContacto> resultado = dao.buscarPorPersonaYTipo(idPersona, idTipo);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idPersona", idPersona);
        verify(typedQuery, times(1)).setParameter("idTipoMedioContacto", idTipo);
    }

    @Test
    public void testBuscarPorPersonaYTipoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(MedioContacto.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorPersonaYTipo(idPersona, idTipo)).isEmpty();
    }
}