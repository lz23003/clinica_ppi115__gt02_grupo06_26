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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoModelTest {

    @Mock
    private ProcedimientoPasoDAO procedimientoPasoDao;
    @Mock 
    private ProcedimientoDAO procedimientoDao;
    @Mock 
    private RolDAO rolDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock
    private ResourceBundle resourceBundleMock;

    private ProcedimientoPasoModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new ProcedimientoPasoModel();

        Field f1 = ProcedimientoPasoModel.class.getDeclaredField("procedimientoPasoDao");
        f1.setAccessible(true);
        f1.set(model, procedimientoPasoDao);

        Field f2 = ProcedimientoPasoModel.class.getDeclaredField("procedimientoDao");
        f2.setAccessible(true);
        f2.set(model, procedimientoDao);

        Field f3 = ProcedimientoPasoModel.class.getDeclaredField("rolDao");
        f3.setAccessible(true);
        f3.set(model, rolDao);

        lenient().when(procedimientoDao.listarActivos()).thenReturn(List.of());
        lenient().when(rolDao.findRange(anyInt(), anyInt())).thenReturn(List.of());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        ProcedimientoPaso pp = new ProcedimientoPaso();
        pp.setIdProcedimientoPaso(id);
        assertThat(model.getId(pp)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        ProcedimientoPaso pp = model.crearEntidad();
        assertThat(pp).isNotNull();
        assertThat(pp.getIdProcedimientoPaso()).isNotNull();
        assertThat(pp.getIndicaFin()).isFalse();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(procedimientoPasoDao);
    }

    @Test
    public void testInicializar() {
        verify(procedimientoDao).listarActivos();
        verify(rolDao).findRange(0, 100);
    }

    @Test
    public void testGetListas() {
        assertThat(model.getListaProcedimientos()).isNotNull();
        assertThat(model.getListaRoles()).isNotNull();
    }
}