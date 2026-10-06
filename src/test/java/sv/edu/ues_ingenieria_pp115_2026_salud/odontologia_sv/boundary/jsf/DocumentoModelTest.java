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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DocumentoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoDocumentoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DocumentoModelTest {

    @Mock 
    private DocumentoDAO documentoDao;
    @Mock 
    private PersonaDAO personaDao;
    @Mock 
    private TipoDocumentoDAO tipoDocumentoDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private DocumentoModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new DocumentoModel();

        Field docField = DocumentoModel.class.getDeclaredField("documentoDao");
        docField.setAccessible(true);
        docField.set(model, documentoDao);

        Field personaField = DocumentoModel.class.getDeclaredField("personaDao");
        personaField.setAccessible(true);
        personaField.set(model, personaDao);

        Field tipoField = DocumentoModel.class.getDeclaredField("tipoDocumentoDao");
        tipoField.setAccessible(true);
        tipoField.set(model, tipoDocumentoDao);

        // Stub de las listas
        lenient().when(personaDao.findRange(anyInt(), anyInt())).thenReturn(List.of());
        lenient().when(tipoDocumentoDao.findRange(anyInt(), anyInt())).thenReturn(List.of());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        Documento d = new Documento();
        d.setIdDocumento(id);
        assertThat(model.getId(d)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        Documento d = model.crearEntidad();
        assertThat(d).isNotNull();
        assertThat(d.getIdDocumento()).isNotNull();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(documentoDao);
    }

    @Test
    public void testInicializarCargaListas() {
        verify(personaDao).findRange(0, 1000);
        verify(tipoDocumentoDao).findRange(0, 100);
    }

    @Test
    public void testGetListas() {
        assertThat(model.getListaPersonas()).isNotNull();
        assertThat(model.getListaTiposDocumento()).isNotNull();
    }
}
