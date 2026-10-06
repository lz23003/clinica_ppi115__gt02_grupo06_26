package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;

@ExtendWith(MockitoExtension.class)
public class ClinicaDAOTest {

    @Mock
    private EntityManager em ;

    @Mock
    private TypedQuery<Clinica> typedQuery;

    @InjectMocks
    private ClinicaDAO dao;

    private Clinica clinica;

    @BeforeEach
    public void setUp() {
        clinica = new Clinica();
        clinica.setIdClinica(UUID.randomUUID());
        clinica.setNombre("Clínica Dental Central");
        clinica.setTipo("PRIVADA");
        clinica.setActivo(true);
    }
    
    @Test
     public void testBuscarPorNombreConNull() {
        List<Clinica> registros=dao.buscarPorNombre(null);
        
        assertThat(registros).isEmpty();
    }
     
     @Test
     public void testBuscarPorNombreConValorValido() {
        List<Clinica> esperado = List.of(clinica);

        when(em.createNamedQuery("Clinica.findByNombre", Clinica.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Clinica> resultado = dao.buscarPorNombre("Dental");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Clínica Dental Central");
        verify(em, times(1)).createNamedQuery("Clinica.findByNombre", Clinica.class);
        verify(typedQuery, times(1)).setParameter("nombre", "%DENTAL%");
    }

     @Test
     public void testBuscarPorNombreConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Clinica.findByNombre", Clinica.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Clinica> resultado = dao.buscarPorNombre("Dental");

        assertThat(resultado).isEmpty();
    }
     
     @Test
     public void testListarActivasConsultaConActivoTrue() {
        List<Clinica> esperado = List.of(clinica);

        when(em.createNamedQuery("Clinica.findByActivo", Clinica.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Clinica> resultado = dao.listarActivas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado).extracting(Clinica::getActivo).containsOnly(true);
        verify(em, times(1)).createNamedQuery("Clinica.findByActivo", Clinica.class);
        verify(typedQuery, times(1)).setParameter("activo", true);
    }
     
     @Test
     public void testListarActivasConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Clinica.findByActivo", Clinica.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Clinica> resultado = dao.listarActivas();

        assertThat(resultado).isEmpty();
    }
     
     @Test
    public void testBuscarPorTipoConValorValido() {
        List<Clinica> esperado = List.of(clinica);

        when(em.createNamedQuery("Clinica.findByTipo", Clinica.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);

        List<Clinica> resultado = dao.buscarPorTipo("Privada");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("PRIVADA");
        verify(em, times(1)).createNamedQuery("Clinica.findByTipo", Clinica.class);
        verify(typedQuery, times(1)).setParameter("tipo", "PRIVADA");
    }

    @Test
    public void testBuscarPorTipoConExcepcionDevuelveListaVacia() {
        when(em.createNamedQuery("Clinica.findByTipo", Clinica.class))
                .thenThrow(new RuntimeException("Error simulado"));

        List<Clinica> resultado = dao.buscarPorTipo("Privada");

        assertThat(resultado).isEmpty();
    }
     
    @Test
    public void testBuscarPorTipoVacio(){
        List<Clinica> esperado=List.of();
        
        when(em.createNamedQuery("Clinica.findByTipo",Clinica.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(esperado);
        
        List<Clinica> respuesta=dao.buscarPorTipo("Privada");
        
        assertThat(respuesta).isEmpty();
        verify(em,times(1)).createNamedQuery("Clinica.findByTipo",Clinica.class);
        verify(typedQuery,times(1)).setParameter("tipo", "PRIVADA");
        
    }
}
