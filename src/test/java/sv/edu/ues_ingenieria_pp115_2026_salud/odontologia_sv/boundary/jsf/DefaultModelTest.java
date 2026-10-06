package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.application.Application;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import java.util.HashMap;
import java.util.List;

import java.util.ResourceBundle;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primefaces.event.SelectEvent;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultModelTest {

    static class TestEntity {

        private UUID id;
        private String nombre;

        public TestEntity() {
        }

        public TestEntity(UUID id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

    }

    static class DefaultModelTestImpl extends DefaultModel<TestEntity> {

        private final DefaultDAO<TestEntity> dao;
        private final FacesContext FacesContext;

        public DefaultModelTestImpl(DefaultDAO<TestEntity> dao, FacesContext FacesContext) {
            this.dao = dao;
            this.FacesContext = FacesContext;
        }

        @Override
        protected Object getId(TestEntity object) {
            return object != null ? object.getId() : null;
        }

        @Override
        protected DefaultDAO<TestEntity> getDao() {
            return dao;
        }

        @Override
        protected FacesContext getFacesContext() {
            return FacesContext;
        }

        @Override
        protected TestEntity crearEntidad() {

            return new TestEntity(UUID.randomUUID(), "Nueva entidad");
        }

        @Override
        public String getNombreEntidad() {
            return "TestEntidad";
        }

    }

    @Mock
    private DefaultDAO<TestEntity> daoMock;

    @Mock
    private FacesContext facesContext;

    @Mock
    private Application applicationMock;

    @Mock
    private ResourceBundle resourceBundleMock;

    private DefaultModelTestImpl model;

    @BeforeEach
    public void setUp() {
        lenient().when(facesContext.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContext, "crud")).thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        model = new DefaultModelTestImpl(daoMock, facesContext);
        model.inicializador();
    }

    @Test
    public void testBtnNuevoHandler() {
        ActionEvent event = mock(ActionEvent.class);

        model.btnNuevoHandler(event);

        assertThat(model.getRegistro()).isNotNull();
        assertThat(model.getRegistro().getId()).isNotNull();
        assertThat(model.getEstado()).isEqualTo(ESTADO_CRUD.CREAR);
    }

    @Test
    public void testBtnCancelarHandler() {
        model.registro = new TestEntity(UUID.randomUUID(), "Test");
        model.estado = ESTADO_CRUD.CREAR;

        ActionEvent event = mock(ActionEvent.class);
        model.btnCancelarHandler(event);

        assertThat(model.getRegistro()).isNull();
        assertThat(model.getEstado()).isEqualTo(ESTADO_CRUD.NADA);
    }

    @Test
    public void testOnRowSelect() {
        TestEntity entity = new TestEntity(UUID.randomUUID(), "Seleccionada");
        SelectEvent<TestEntity> event = mock(SelectEvent.class);
        when(event.getObject()).thenReturn(entity);

        model.onRowSelect(event);

        assertThat(model.getRegistro()).isSameAs(entity);
        assertThat(model.getEstado()).isEqualTo(ESTADO_CRUD.MODIFICAR);
    }

    @Test
    public void testOnRowSelectConNull() {
        model.estado = ESTADO_CRUD.NADA;

        model.onRowSelect(null);

        assertThat(model.getRegistro()).isNull();
        assertThat(model.getEstado()).isEqualTo(ESTADO_CRUD.NADA);
    }

    @Test
    public void testBtnCrearHandlerConEstadoCrear() {
        TestEntity entity = new TestEntity(UUID.randomUUID(), "Nueva");
        model.registro = entity;
        model.estado = ESTADO_CRUD.CREAR;
        ActionEvent event = mock(ActionEvent.class);

        model.btnCrearHandler(event);

        verify(daoMock, times(1)).crear(entity);
        assertThat(model.getEstado()).isEqualTo(ESTADO_CRUD.NADA);
        assertThat(model.getRegistro()).isNull();
    }

    @Test
    public void testBtnCrearHandlerConEstadoModificar() {
        TestEntity entity = new TestEntity(UUID.randomUUID(), "Modificada");
        model.registro = entity;
        model.estado = ESTADO_CRUD.MODIFICAR;
        ActionEvent event = mock(ActionEvent.class);

        model.btnCrearHandler(event);

        verify(daoMock, times(1)).modificar(entity);
        assertThat(model.getEstado()).isEqualTo(ESTADO_CRUD.NADA);
    }

    @Test
    public void testBtnCrearHandlerConEstadoNada() {
        model.estado = ESTADO_CRUD.NADA;
        ActionEvent event = mock(ActionEvent.class);

        model.btnCrearHandler(event);

        verify(daoMock, never()).crear(any());
        verify(daoMock, never()).modificar(any());
    }

    @Test
    public void testBtnCrearHandlerConExcepcion() {
        TestEntity entity = new TestEntity(UUID.randomUUID(), "Nueva");
        model.registro = entity;
        model.estado = ESTADO_CRUD.CREAR;
        ActionEvent event = mock(ActionEvent.class);

        // Simulamos error en el DAO
        org.mockito.Mockito.doThrow(new RuntimeException("Error simulado"))
                .when(daoMock).crear(any());

        model.btnCrearHandler(event);

        // El estado se resetea a NADA (por el finally implícito)
        // Pero verificamos que se llamó al menos una vez
        verify(daoMock, times(1)).crear(entity);
        verify(facesContext, times(1)).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    public void testBtnEliminarHandler() {
        TestEntity entity = new TestEntity(UUID.randomUUID(), "Eliminar");

        model.btnEliminarHandler(entity);

        verify(daoMock, times(1)).eliminar(entity);
        assertThat(model.getEstado()).isEqualTo(ESTADO_CRUD.NADA);
        assertThat(model.getRegistro()).isNull();
    }

    @Test
    public void testBtnEliminarHandlerConExcepcion() {
        TestEntity entity = new TestEntity(UUID.randomUUID(), "Eliminar");

        org.mockito.Mockito.doThrow(new RuntimeException("Error simulado"))
                .when(daoMock).eliminar(any());

        model.btnEliminarHandler(entity);

        verify(daoMock, times(1)).eliminar(entity);
        verify(facesContext, times(1)).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    public void testGetEstadoNombre() {
        model.estado = ESTADO_CRUD.CREAR;
        assertThat(model.getEstadoNombre()).isEqualTo("CREAR");

        model.estado = ESTADO_CRUD.MODIFICAR;
        assertThat(model.getEstadoNombre()).isEqualTo("MODIFICAR");

        model.estado = ESTADO_CRUD.ELIMINAR;
        assertThat(model.getEstadoNombre()).isEqualTo("ELIMINAR");

        model.estado = ESTADO_CRUD.NADA;
        assertThat(model.getEstadoNombre()).isEqualTo("NADA");
    }

    @Test
    public void testGetEstadoNombreConNull() {
        model.estado = null;
        assertThat(model.getEstadoNombre()).isEqualTo("NADA");
    }

    // TESTS DEL LazyDataModel
    @Test
    public void testGetRowKeyConObjeto() {
        UUID id = UUID.randomUUID();
        TestEntity entity = new TestEntity(id, "Test");

        String rowKey = model.getModelo().getRowKey(entity);

        assertThat(rowKey).isEqualTo(id.toString());
    }

    @Test
    public void testGetRowKeyConNull() {
        String rowKey = model.getModelo().getRowKey(null);
        assertThat(rowKey).isNull();
    }

    @Test
    public void testGetRowDataConRowKeyValido() {
        UUID id = UUID.randomUUID();
        TestEntity entity = new TestEntity(id, "Test");

        model.getModelo().setWrappedData(List.of(entity));

        TestEntity resultado = model.getModelo().getRowData(id.toString());

        assertThat(resultado).isSameAs(entity);
    }

    @Test
    public void testGetRowDataConNull() {
        TestEntity resultado = model.getModelo().getRowData(null);
        assertThat(resultado).isNull();
    }

    @Test
    public void testGetRowDataConRowKeyInexistente() {
        model.getModelo().setWrappedData(List.of(new TestEntity(UUID.randomUUID(), "Test")));

        TestEntity resultado = model.getModelo().getRowData("id-inexistente");

        assertThat(resultado).isNull();
    }

    @Test
    public void testGetRowDataConWrappedDataVacio() {
        model.getModelo().setWrappedData(List.of());

        TestEntity resultado = model.getModelo().getRowData("cualquier-id");

        assertThat(resultado).isNull();
    }

    @Test
    public void testCountLlamaAlDao() {
        when(daoMock.count()).thenReturn(42);

        int resultado = model.getModelo().count(new HashMap<>());

        assertThat(resultado).isEqualTo(42);
        verify(daoMock, times(1)).count();
    }

    @Test
    public void testCountConExcepcionDevuelveCero() {
        when(daoMock.count()).thenThrow(new RuntimeException("Error simulado"));

        int resultado = model.getModelo().count(new HashMap<>());

        assertThat(resultado).isZero();
        verify(facesContext, times(1)).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    public void testLoadLlamaAlDao() {
        List<TestEntity> esperado = List.of(new TestEntity(UUID.randomUUID(), "A"));
        when(daoMock.count()).thenReturn(1);
        when(daoMock.findRange(0, 5)).thenReturn(esperado);

        List<TestEntity> resultado = model.getModelo().load(
                0, 5,
                new HashMap<>(),
                new HashMap<>());

        assertThat(resultado).hasSize(1);
        verify(daoMock, times(1)).count();
        verify(daoMock, times(1)).findRange(0, 5);
    }

    @Test
    public void testLoadConExcepcionDevuelveListaVacia() {
        when(daoMock.count()).thenThrow(new RuntimeException("Error simulado"));

        List<TestEntity> resultado = model.getModelo().load(
                0, 5,
                new HashMap<>(),
                new HashMap<>());

        assertThat(resultado).isEmpty();
        verify(facesContext, times(1)).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    public void testGetTextConFacesContextNull() {
        DefaultModelTestImpl modelSinContexto = new DefaultModelTestImpl(daoMock, null);

        String resultado = modelSinContexto.getText("cualquier.clave");

        assertThat(resultado).isEqualTo("cualquier.clave");
    }

    @Test
    public void testGetTextConExcepcionDevuelveClave() {
        when(resourceBundleMock.getString(anyString()))
                .thenThrow(new RuntimeException("Error simulado"));

        String resultado = model.getText("cualquier.clave");

        assertThat(resultado).isEqualTo("cualquier.clave");
    }

}
