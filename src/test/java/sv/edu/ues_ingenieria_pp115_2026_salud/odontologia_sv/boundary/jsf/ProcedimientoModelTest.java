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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoModelTest {

    @Mock
    private ProcedimientoDAO procedimientoDao;
    @Mock
    private FacesContext facesContextMock;
    @Mock
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private ProcedimientoModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new ProcedimientoModel();

        Field daoField = ProcedimientoModel.class.getDeclaredField("procedimientoDao");
        daoField.setAccessible(true);
        daoField.set(model, procedimientoDao);

        model.inicializador();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        Procedimiento p = new Procedimiento();
        p.setIdProcedimiento(id);
        assertThat(model.getId(p)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        Procedimiento p = model.crearEntidad();
        assertThat(p).isNotNull();
        assertThat(p.getIdProcedimiento()).isNotNull();
        assertThat(p.getActivo()).isTrue();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(procedimientoDao);
    }
}