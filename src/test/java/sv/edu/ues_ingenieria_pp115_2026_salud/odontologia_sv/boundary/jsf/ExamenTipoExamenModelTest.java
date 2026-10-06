package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenTipoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ExamenTipoExamen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ExamenTipoExamenModelTest {

    @Mock
    private ExamenTipoExamenDAO examenTipoExamenDao;
    @Mock 
    private ExamenDAO examenDao;
    @Mock
    private TipoExamenDAO tipoExamenDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private ExamenTipoExamenModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new ExamenTipoExamenModel();

        Field eteField = ExamenTipoExamenModel.class.getDeclaredField("examenTipoExamenDao");
        eteField.setAccessible(true);
        eteField.set(model, examenTipoExamenDao);

        Field exField = ExamenTipoExamenModel.class.getDeclaredField("examenDao");
        exField.setAccessible(true);
        exField.set(model, examenDao);

        Field teField = ExamenTipoExamenModel.class.getDeclaredField("tipoExamenDao");
        teField.setAccessible(true);
        teField.set(model, tipoExamenDao);

        lenient().when(examenDao.listarActivos()).thenReturn(List.of());
        lenient().when(tipoExamenDao.listarActivos()).thenReturn(List.of());

        model.inicializador();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        ExamenTipoExamen ete = new ExamenTipoExamen();
        ete.setIdExamenTipoExamen(id);
        assertThat(model.getId(ete)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        ExamenTipoExamen ete = model.crearEntidad();
        assertThat(ete).isNotNull();
        assertThat(ete.getIdExamenTipoExamen()).isNotNull();
        assertThat(ete.getFechaCreacion()).isNotNull();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(examenTipoExamenDao);
    }

    @Test
    public void testInicializadorCargaListas() {
        verify(examenDao).listarActivos();
        verify(tipoExamenDao).listarActivos();
    }

    @Test
    public void testGetListas() {
        assertThat(model.getExamenesDisponibles()).isNotNull();
        assertThat(model.getTiposExamenDisponibles()).isNotNull();
    }
}