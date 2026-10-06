
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;

@ExtendWith(MockitoExtension.class)
public class ConsultaDAOTest {
    
    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Consulta> typedQuery;

    @InjectMocks
    private ConsultaDAO dao;


    private Consulta consultaActiva;
    private Consulta consultaCerrada;
    private UUID idPaciente;
    private UUID idPersonaRol;
    private Date fechaInicio;
    private Date fechaFin;

  
    @BeforeEach
    public void setUp() {
        consultaActiva = new Consulta();
        consultaActiva.setIdConsulta(UUID.randomUUID());
        consultaActiva.setFechaInicio(new Date());

        consultaCerrada = new Consulta();
        consultaCerrada.setIdConsulta(UUID.randomUUID());
        consultaCerrada.setFechaInicio(new Date());
        consultaCerrada.setFechaFin(new Date());

        idPaciente = UUID.randomUUID();
        idPersonaRol = UUID.randomUUID();

        fechaInicio = new Date(System.currentTimeMillis() - 86400000L); 
        fechaFin = new Date(); 
    }

    @Test
    public void testBuscarPorConsultasActivasDevuelveLista() {
        List<Consulta> esperado = List.of(consultaActiva);

        when(em.createQuery(anyString(), eq(Consulta.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Consulta> resultado = dao.buscarPorConsultasActivas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdConsulta()).isEqualTo(consultaActiva.getIdConsulta());
        verify(em, times(1)).createQuery(anyString(), eq(Consulta.class));
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testBuscarPorConsultasActivasConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(Consulta.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Consulta> resultado = dao.buscarPorConsultasActivas();

        assertThat(resultado).isEmpty();
    }

 
    @Test
    public void testBuscarPorPacienteConNullDevuelveListaVacia() {
        List<Consulta> resultado = dao.buscarPorPaciente(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarPorPacienteConIdValido() {
        List<Consulta> esperado = List.of(consultaActiva, consultaCerrada);

        when(em.createQuery(anyString(), eq(Consulta.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Consulta> resultado = dao.buscarPorPaciente(idPaciente);

        assertThat(resultado).hasSize(2);
        verify(em, times(1)).createQuery(anyString(), eq(Consulta.class));
        verify(typedQuery, times(1)).setParameter("idPaciente", idPaciente);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testBuscarPorPacienteConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(Consulta.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Consulta> resultado = dao.buscarPorPaciente(idPaciente);

        assertThat(resultado).isEmpty();
    }

  
    @Test
    public void testBuscarPorRangoFechasConInicioNullDevuelveListaVacia() {
        
        List<Consulta> resultado = dao.buscarPorRangoFechas(null, fechaFin);

        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarPorRangoFechasConFinNullDevuelveListaVacia() {
        List<Consulta> resultado = dao.buscarPorRangoFechas(fechaInicio, null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarPorRangoFechasConValoresValidos() {
        List<Consulta> esperado = List.of(consultaActiva);

        when(em.createQuery(anyString(), eq(Consulta.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Consulta> resultado = dao.buscarPorRangoFechas(fechaInicio, fechaFin);

        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createQuery(anyString(), eq(Consulta.class));
        verify(typedQuery, times(1)).setParameter("inicio", fechaInicio);
        verify(typedQuery, times(1)).setParameter("fin", fechaFin);
    }

    @Test
    public void testBuscarPorRangoFechasConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(Consulta.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Consulta> resultado = dao.buscarPorRangoFechas(fechaInicio, fechaFin);

        assertThat(resultado).isEmpty();
    }


    @Test
    @DisplayName("buscarPorPersonaRol(null) devuelve lista vacía sin consultar")
    public void testBuscarPorPersonaRolConNullDevuelveListaVacia() {
        List<Consulta> resultado = dao.buscarPorPersonaRol(null);

        assertThat(resultado).isEmpty();
        verify(em, never()).createQuery(anyString(), eq(Consulta.class));
    }

    @Test
    public void testBuscarPorPersonaRolConIdValido() {
        List<Consulta> esperado = List.of(consultaActiva);

        when(em.createQuery(anyString(), eq(Consulta.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Consulta> resultado = dao.buscarPorPersonaRol(idPersonaRol);

        
        assertThat(resultado).hasSize(1);
        verify(em, times(1)).createQuery(anyString(), eq(Consulta.class));
        verify(typedQuery, times(1)).setParameter("idPersonaRol", idPersonaRol);
    }

    @Test
    public void testBuscarPorPersonaRolConExcepcionDevuelveListaVacia() {
        when(em.createQuery(anyString(), eq(Consulta.class)))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Consulta> resultado = dao.buscarPorPersonaRol(idPersonaRol);

        assertThat(resultado).isEmpty();
    }
    
}
