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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonaRolModelTest {

    @Mock 
    private PersonaRolDAO personaRolDao;
    @Mock 
    private PersonaDAO personaDao;
    @Mock 
    private RolDAO rolDao;
    @Mock 
    private ClinicaDAO clinicaDao;
    @Mock
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private PersonaRolModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new PersonaRolModel();

        Field f1 = PersonaRolModel.class.getDeclaredField("personaRolDao");
        f1.setAccessible(true);
        f1.set(model, personaRolDao);

        Field f2 = PersonaRolModel.class.getDeclaredField("personaDao");
        f2.setAccessible(true);
        f2.set(model, personaDao);

        Field f3 = PersonaRolModel.class.getDeclaredField("rolDao");
        f3.setAccessible(true);
        f3.set(model, rolDao);

        Field f4 = PersonaRolModel.class.getDeclaredField("clinicaDao");
        f4.setAccessible(true);
        f4.set(model, clinicaDao);

        lenient().when(personaDao.findRange(anyInt(), anyInt())).thenReturn(List.of());
        lenient().when(rolDao.findRange(anyInt(), anyInt())).thenReturn(List.of());
        lenient().when(clinicaDao.findRange(anyInt(), anyInt())).thenReturn(List.of());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        PersonaRol pr = new PersonaRol();
        pr.setIdPersonaRol(id);
        assertThat(model.getId(pr)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        PersonaRol pr = model.crearEntidad();
        assertThat(pr).isNotNull();
        assertThat(pr.getIdPersonaRol()).isNotNull();
        assertThat(pr.getFechaCreacion()).isNotNull();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(personaRolDao);
    }

    @Test
    public void testInicializar() {
        verify(personaDao).findRange(0, 1000);
        verify(rolDao).findRange(0, 100);
        verify(clinicaDao).findRange(0, 100);
    }

    @Test
    public void testGetListas() {
        assertThat(model.getListaPersonas()).isNotNull();
        assertThat(model.getListaRoles()).isNotNull();
        assertThat(model.getListaClinicas()).isNotNull();
    }
}