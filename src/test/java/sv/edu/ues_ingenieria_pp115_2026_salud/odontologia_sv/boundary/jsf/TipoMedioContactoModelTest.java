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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoMedioContactoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TipoMedioContactoModelTest {

    @Mock 
    private TipoMedioContactoDAO tipoMedioContactoDao;
    @Mock
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock
    private ResourceBundle resourceBundleMock;

    private TipoMedioContactoModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new TipoMedioContactoModel();

        Field daoField = TipoMedioContactoModel.class.getDeclaredField("tipoMedioContactoDao");
        daoField.setAccessible(true);
        daoField.set(model, tipoMedioContactoDao);

        Field facesField = TipoMedioContactoModel.class.getDeclaredField("FacesContext");
        facesField.setAccessible(true);
        facesField.set(model, facesContextMock);

        model.inicializador();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        TipoMedioContacto tmc = new TipoMedioContacto();
        tmc.setIdTipoMedioContacto(id);
        assertThat(model.getId(tmc)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        TipoMedioContacto tmc = model.crearEntidad();
        assertThat(tmc).isNotNull();
        assertThat(tmc.getIdTipoMedioContacto()).isNotNull();
        assertThat(tmc.getActivo()).isTrue();
        assertThat(tmc.getExpresionRegular()).isEqualTo(".");
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(tipoMedioContactoDao);
    }

    @Test
    public void testGetNombreEntidad() {
        assertThat(model.getNombreEntidad()).isEqualTo("entidad.tipoMedioContacto");
    }
}