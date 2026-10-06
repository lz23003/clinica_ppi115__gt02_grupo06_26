package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import jakarta.persistence.criteria.Root;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultDAOTest {

    //subClase Dummy para instancial DefaulDAO
    public static class DefaultDAOTestImpl extends DefaultDAO<Object> {

        private final EntityManager em;

        public DefaultDAOTestImpl(EntityManager em) {
            super(Object.class);
            this.em = em;
        }

        @Override
        protected EntityManager getEntityManager() {
            return em;
        }

    }

    //MOCKS
    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Object> typedQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Object> criteriaQuery;

    @Mock
    private CriteriaQuery<Long> countCriteriaQuery;
    
    @Mock
    private TypedQuery<Long> countTypedQuery;
    
    @Mock
    private Root<Object> root;

    // La instancia del DAO que vamos a probar.
    private DefaultDAOTestImpl dao;

    //garantiza que cada test empiece con un dao limpio
    @BeforeEach
    public void setUp() {
        dao = new DefaultDAOTestImpl(em);
    }

    
    
    @Test
    public void testCrearConNull(){
        assertThatThrownBy(()->dao.crear(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El registro no puede ser nulo");
    }
    
    @Test
    public void testCrear(){
        Object registro=new Object();
        
        dao.crear(registro);
        
        verify(em, times(1)).persist(registro);
        verify(em, times(1)).flush();
    }
    
    @Test
    public void testCrearConException(){
        Object registro=new Object();
        
        doThrow(new RuntimeException("Error simulado")).when(em).persist(any());
        
        //verificamos que se lanza la excepcion
        assertThatThrownBy(()->dao.crear(registro))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Error al crear el registro ");
    }
    
    
     @Test
    public void testModificarConNull(){
        assertThatThrownBy(()->dao.modificar(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El registro no puede ser nulo");
    }
    
    
    @Test
    public void testModificar(){
        Object registro=new Object();
        
        Object registroGestionado=new Object();
        
        when(em.merge(registro)).thenReturn(registroGestionado);
        
        Object resultado=dao.modificar(registro);
        
        assertThat(resultado).isEqualTo(registroGestionado);
        
        verify(em, times(1)).merge(registro);
        verify(em,times(1)).flush();
        
    }
    
    @Test
    public void testModificarConException(){
        Object registro=new Object();
        doThrow(new RuntimeException("Error simulado")).when(em).merge(any());
        
        assertThatThrownBy(()->dao.modificar(registro))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Error al modificar el registro");
        
        
    }

    @Test
    public void testEliminarConNull(){
        assertThatThrownBy(()->dao.eliminar(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El registro no puede ser nulo");
    }
    
    @Test
    public void testEliminar(){
        Object registro=new Object();
        Object registroGestionado=new Object();
        
        when(em.merge(registro)).thenReturn(registroGestionado);
        dao.eliminar(registro);
        
        verify(em,times(1)).merge(registro);
        verify(em, times(1)).remove(registroGestionado);
    }
    
    @Test
    public void testEliminarConExeption(){
        Object registro=new Object();
        
        doThrow(new RuntimeException("Error simulado")).when(em).remove(registro);
        
        assertThatThrownBy(()->dao.eliminar(registro))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Error al eliminar el registro");
               
                     
    }
    
    
    @Test
    public void testBuscarPorIdNull(){
        assertThatThrownBy(()->dao.buscarPorId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El id no puede ser nulo");
    }
    
    @Test
    public void testBuscarPorId(){
        Object id=new Object();
        Object respuesta=new Object();
        
        when(em.find(Object.class, id)).thenReturn(respuesta);
        
        Object resultado=dao.buscarPorId(id);
        
        assertThat(resultado).isEqualTo(respuesta);
        verify(em, times(1)).find(Object.class, id);
        
    }
    
    @Test
    public void testBuscarPorIdException(){
        Object id=new Object();
        
        doThrow(new RuntimeException("Error simulado")).when(em).find(Object.class, id);
        
        assertThatThrownBy(()->dao.buscarPorId(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Error al buscar por id");
    }

    @Test
    @DisplayName("findRange(-1,5)")
    public void testFindRangeFirstNegativo(){
        assertThatThrownBy(()->dao.findRange(-1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Los parametros first y max deben de ser validos (first>=0, max>0)");
    }
    
    @Test
    public void testFindRangeMaxCero(){
        assertThatThrownBy(()->dao.findRange(0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("parametros");
                
    }
    
    @Test
    public void testFindRange(){
       Object registro1=new Object();
       List<Object> registros=List.of(registro1);
       
        when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Object.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Object.class)).thenReturn(root);
        
        when(em.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(registros);
        
        
        List<Object> resultado=dao.findRange(0, 5);
        
        assertThat(resultado).hasSize(1);
        
        verify(typedQuery,times(1)).setFirstResult(0);
        verify(typedQuery,times(1)).setMaxResults(5);
    }
    
    
    @Test
    public void testFinRangeException(){
        when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("Error simulado"));
        
        assertThatThrownBy(()->dao.findRange(0, 5))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Error al ejecutar findRange");
    }
    
    @Test
    public void testFindAll(){
        Object registro=new Object();
        List<Object> lista=List.of(registro);
        
        when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Object.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Object.class)).thenReturn(root);
        when(em.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(lista);
        
        
        List<Object> resultados=dao.findAll();
        
        assertThat(resultados).hasSize(1);
    }
    
    
    @Test
    public void TestFindAllException(){
        when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("Error simulado"));
        
        assertThatThrownBy(()->dao.findAll())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Error");
    }
    
    
    @Test
    public void testCount(){
        
        Long totalEsperado=42L;
        
      when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Long.class)).thenReturn(countCriteriaQuery);
    when(countCriteriaQuery.from(Object.class)).thenReturn(root);
    when(criteriaBuilder.count(root)).thenReturn(null);   // el cb.count() devuelve un Expression
    when(em.createQuery(countCriteriaQuery)).thenReturn(countTypedQuery);
    when(countTypedQuery.getSingleResult()).thenReturn(totalEsperado);
      
    int resultado=dao.count();
    
        assertThat(resultado).isEqualTo(42);
    }
    
    @Test
    public void testCountException(){
        when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("Error simulado"));
        
        assertThatThrownBy(()->dao.count())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Error al contar los registros");
                
    }
    
}
