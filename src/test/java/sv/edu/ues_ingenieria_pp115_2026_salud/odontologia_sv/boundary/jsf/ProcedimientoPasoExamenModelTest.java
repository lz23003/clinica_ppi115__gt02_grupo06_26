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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoExamen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoExamenModelTest {

    @Mock 
    private ProcedimientoPasoExamenDAO procedimientoPasoExamenDao;
    @Mock 
    private ProcedimientoPasoDAO procedimientoPasoDao;
    @Mock
    private ExamenDAO examenDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private ProcedimientoPasoExamenModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new ProcedimientoPasoExamenModel();

        Field f1 = ProcedimientoPasoExamenModel.class.getDeclaredField("procedimientoPasoExamenDao");
        f1.setAccessible(true);
        f1.set(model, procedimientoPasoExamenDao);

        Field f2 = ProcedimientoPasoExamenModel.class.getDeclaredField("procedimientoPasoDao");
        f2.setAccessible(true);
        f2.set(model, procedimientoPasoDao);

        Field f3 = ProcedimientoPasoExamenModel.class.getDeclaredField("examenDao");
        f3.setAccessible(true);
        f3.set(model, examenDao);

        lenient().when(procedimientoPasoDao.findRange(anyInt(), anyInt())).thenReturn(List.of());
        lenient().when(examenDao.listarActivos()).thenReturn(List.of());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoExamen ppe = new ProcedimientoPasoExamen();
        ppe.setIdProcedimientoPasoExamen(id);
        assertThat(model.getId(ppe)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        ProcedimientoPasoExamen ppe = model.crearEntidad();
        assertThat(ppe).isNotNull();
        assertThat(ppe.getIdProcedimientoPasoExamen()).isNotNull();
        assertThat(ppe.getFechaCreacion()).isNotNull();
        assertThat(ppe.getActivo()).isTrue();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(procedimientoPasoExamenDao);
    }

    @Test
    public void testInicializar() {
        verify(procedimientoPasoDao).findRange(0, 1000);
        verify(examenDao).listarActivos();
    }

    @Test
    public void testGetListas() {
        assertThat(model.getListaPasos()).isNotNull();
        assertThat(model.getListaExamenes()).isNotNull();
    }
}