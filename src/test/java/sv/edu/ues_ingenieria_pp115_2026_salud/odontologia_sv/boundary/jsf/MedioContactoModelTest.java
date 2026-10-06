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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.MedioContactoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoMedioContactoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MedioContactoModelTest {

    @Mock
    private MedioContactoDAO medioContactoDao;
    @Mock 
    private PersonaDAO personaDao;
    @Mock 
    private TipoMedioContactoDAO tipoMedioContactoDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private MedioContactoModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new MedioContactoModel();

        Field f1 = MedioContactoModel.class.getDeclaredField("medioContactoDao");
        f1.setAccessible(true);
        f1.set(model, medioContactoDao);

        Field f2 = MedioContactoModel.class.getDeclaredField("personaDao");
        f2.setAccessible(true);
        f2.set(model, personaDao);

        Field f3 = MedioContactoModel.class.getDeclaredField("tipoMedioContactoDao");
        f3.setAccessible(true);
        f3.set(model, tipoMedioContactoDao);

        lenient().when(personaDao.findRange(anyInt(), anyInt())).thenReturn(List.of());
        lenient().when(tipoMedioContactoDao.findRange(anyInt(), anyInt())).thenReturn(List.of());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        MedioContacto mc = new MedioContacto();
        mc.setIdMedioContacto(id);
        assertThat(model.getId(mc)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        MedioContacto mc = model.crearEntidad();
        assertThat(mc).isNotNull();
        assertThat(mc.getIdMedioContacto()).isNotNull();
        assertThat(mc.getFechaCreacion()).isNotNull();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(medioContactoDao);
    }

    @Test
    public void testInicializar() {
        verify(personaDao).findRange(0, 1000);
        verify(tipoMedioContactoDao).findRange(0, 100);
    }

    @Test
    public void testGetListas() {
        assertThat(model.getListaPersonas()).isNotNull();
        assertThat(model.getListaTipos()).isNotNull();
    }
}