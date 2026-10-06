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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;

@ExtendWith(MockitoExtension.class)
public class DocumentoDAOTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Documento> typedQuery;

    @InjectMocks
    private DocumentoDAO dao;

    private Documento doc;
    private UUID idPersona;
    private UUID idTipoDoc;

    @BeforeEach
    public void setUp() {
        doc = new Documento();
        doc.setIdDocumento(UUID.randomUUID());
        doc.setValor("01234567-8");
        idPersona = UUID.randomUUID();
        idTipoDoc = UUID.randomUUID();
    }

    @Test
    public void testBuscarPorPersonaConNullDevuelveListaVacia() {
        List<Documento> resultado = dao.buscarPorPersona(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(Documento.class));
    }

    @Test
    public void testBuscarPorPersonaConIdValido() {
        when(em.createQuery(anyString(), eq(Documento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(doc));

        List<Documento> resultado = dao.buscarPorPersona(idPersona);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idPersona", idPersona);
    }

    @Test
    public void testBuscarPorPersonaConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(Documento.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorPersona(idPersona)).isEmpty();
    }

    @Test
    public void testBuscarPorTipoDocumentoConNullDevuelveListaVacia() {
        List<Documento> resultado = dao.buscarPorTipoDocumento(null);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(Documento.class));
    }

    @Test
    public void testBuscarPorTipoDocumentoConIdValido() {
        when(em.createQuery(anyString(), eq(Documento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(doc));

        List<Documento> resultado = dao.buscarPorTipoDocumento(idTipoDoc);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idTipoDocumento", idTipoDoc);
    }

    @Test
    public void testBuscarPorTipoDocumentoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(Documento.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorTipoDocumento(idTipoDoc)).isEmpty();
    }

    @Test
    public void testBuscarPorValorConNullDevuelveNull() {
        Documento resultado = dao.buscarPorValor(null);
        assertThat(resultado).isNull();
        verify(em, never()).createQuery(anyString(), eq(Documento.class));
    }

    @Test
    public void testBuscarPorValorConValorValido() {
        when(em.createQuery(anyString(), eq(Documento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(doc));

        Documento resultado = dao.buscarPorValor("01234567-8");

        assertThat(resultado).isNotNull();
        verify(typedQuery, times(1)).setParameter("valor", "01234567-8");
    }

    @Test
    public void testBuscarPorValorSinResultadosDevuelveNull() {
        when(em.createQuery(anyString(), eq(Documento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        Documento resultado = dao.buscarPorValor("inexistente");
        assertThat(resultado).isNull();
    }

    @Test
    public void testBuscarPorValorConExcepcionDevuelveNull() {
        when(em.createQuery(anyString(), eq(Documento.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorValor("x")).isNull();
    }

    @Test
    public void testBuscarPorPersonaYTipoConNullDevuelveListaVacia() {
        List<Documento> resultado = dao.buscarPorPersonaYTipo(null, idTipoDoc);
        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(Documento.class));
    }

    @Test
    public void testBuscarPorPersonaYTipoConValoresValidos() {
        when(em.createQuery(anyString(), eq(Documento.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(doc));

        List<Documento> resultado = dao.buscarPorPersonaYTipo(idPersona, idTipoDoc);

        assertThat(resultado).hasSize(1);
        verify(typedQuery, times(1)).setParameter("idPersona", idPersona);
        verify(typedQuery, times(1)).setParameter("idTipoDocumento", idTipoDoc);
    }

    @Test
    public void testBuscarPorPersonaYTipoConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(Documento.class)))
                .thenThrow(new RuntimeException("Error simulado"));
        assertThat(dao.buscarPorPersonaYTipo(idPersona, idTipoDoc)).isEmpty();
    }
}