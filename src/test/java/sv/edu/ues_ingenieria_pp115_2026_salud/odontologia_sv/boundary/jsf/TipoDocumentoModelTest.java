package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import java.lang.reflect.Field;
import java.util.ResourceBundle;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoDocumentoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TipoDocumentoModelTest {

    @Mock
    private TipoDocumentoDAO tipoDocumentoDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock
    private Application applicationMock;
    @Mock
    private ResourceBundle resourceBundleMock;

    private TipoDocumentoModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new TipoDocumentoModel();

        Field daoField = TipoDocumentoModel.class.getDeclaredField("TipoDocumentoDao");
        daoField.setAccessible(true);
        daoField.set(model, tipoDocumentoDao);

        Field facesField = TipoDocumentoModel.class.getDeclaredField("FacesContext");
        facesField.setAccessible(true);
        facesField.set(model, facesContextMock);

        model.inicializador();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        TipoDocumento td = new TipoDocumento();
        td.setIdTipoDocumento(id);
        assertThat(model.getId(td)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        TipoDocumento td = model.crearEntidad();
        assertThat(td).isNotNull();
        assertThat(td.getIdTipoDocumento()).isNotNull();
        assertThat(td.getActivo()).isTrue();
        assertThat(td.getExpresionRegular()).isEqualTo(".");
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(tipoDocumentoDao);
    }

    @Test
    public void testGetNombreEntidad() {
        assertThat(model.getNombreEntidad()).isEqualTo("entidad.tipoDocumento");
    }
}