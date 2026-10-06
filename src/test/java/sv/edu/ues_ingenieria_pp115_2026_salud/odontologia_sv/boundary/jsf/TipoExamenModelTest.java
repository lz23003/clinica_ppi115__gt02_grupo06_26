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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TipoExamenModelTest {

    @Mock 
    private TipoExamenDAO tipoExamenDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private TipoExamenModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new TipoExamenModel();

        Field daoField = TipoExamenModel.class.getDeclaredField("tipoExamenDao");
        daoField.setAccessible(true);
        daoField.set(model, tipoExamenDao);

        Field facesField = TipoExamenModel.class.getDeclaredField("facesContext");
        facesField.setAccessible(true);
        facesField.set(model, facesContextMock);

        model.inicializador();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        TipoExamen te = new TipoExamen();
        te.setIdTipoExamen(id);
        assertThat(model.getId(te)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        TipoExamen te = model.crearEntidad();
        assertThat(te).isNotNull();
        assertThat(te.getIdTipoExamen()).isNotNull();
        assertThat(te.getActivo()).isTrue();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(tipoExamenDao);
    }
}