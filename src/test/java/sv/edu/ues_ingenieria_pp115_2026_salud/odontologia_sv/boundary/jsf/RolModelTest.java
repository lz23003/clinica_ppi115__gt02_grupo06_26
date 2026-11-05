package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import java.lang.reflect.Field;
import java.util.ResourceBundle;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class RolModelTest {

    @Mock 
    private RolService rolService;
    @Mock
    private FacesContext facesContextMock;
    @Mock
    private Application applicationMock;
    @Mock
    private ResourceBundle resourceBundleMock;

    private RolModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new RolModel();

        Field daoField = RolModel.class.getDeclaredField("rolService");
        daoField.setAccessible(true);
        daoField.set(model, rolService);

        Field facesField = RolModel.class.getDeclaredField("FacesContext");
        facesField.setAccessible(true);
        facesField.set(model, facesContextMock);

        model.inicializador();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        Rol r = new Rol();
        r.setIdRol(id);
        assertThat(model.getId(r)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        Rol r = model.crearEntidad();
        assertThat(r).isNotNull();
        assertThat(r.getIdRol()).isNotNull();
        assertThat(r.getActivo()).isTrue();
    }

    @Test
    public void testGetService() {
        assertThat(model.getService()).isSameAs(rolService);
    }
}