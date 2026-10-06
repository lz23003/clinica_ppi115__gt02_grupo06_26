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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConsultaModelTest {

    @Mock 
    private ConsultaDAO consultaDao;
    @Mock 
    private PersonaRolDAO personaRolDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private ConsultaModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new ConsultaModel();

        Field f1 = ConsultaModel.class.getDeclaredField("consultaDao");
        f1.setAccessible(true);
        f1.set(model, consultaDao);

        Field f2 = ConsultaModel.class.getDeclaredField("personaRolDao");
        f2.setAccessible(true);
        f2.set(model, personaRolDao);

        lenient().when(personaRolDao.findRange(anyInt(), anyInt())).thenReturn(List.of());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        Consulta c = new Consulta();
        c.setIdConsulta(id);
        assertThat(model.getId(c)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        Consulta c = model.crearEntidad();
        assertThat(c).isNotNull();
        assertThat(c.getIdConsulta()).isNotNull();
        assertThat(c.getFechaInicio()).isNotNull();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(consultaDao);
    }

    @Test
    public void testInicializar() {
        verify(personaRolDao).findRange(0, 1000);
    }

    @Test
    public void testGetListaPersonaRoles() {
        assertThat(model.getListaPersonaRoles()).isNotNull();
    }
}