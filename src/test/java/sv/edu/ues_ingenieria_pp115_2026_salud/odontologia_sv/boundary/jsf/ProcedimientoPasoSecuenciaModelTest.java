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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoSecuencia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProcedimientoPasoSecuenciaModelTest {

    @Mock 
    private ProcedimientoPasoSecuenciaDAO procedimientoPasoSecuenciaDao;
    @Mock 
    private ProcedimientoPasoDAO procedimientoPasoDao;
    @Mock 
    private FacesContext facesContextMock;
    @Mock 
    private Application applicationMock;
    @Mock 
    private ResourceBundle resourceBundleMock;

    private ProcedimientoPasoSecuenciaModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new ProcedimientoPasoSecuenciaModel();

        Field f1 = ProcedimientoPasoSecuenciaModel.class.getDeclaredField("procedimientoPasoSecuenciaDao");
        f1.setAccessible(true);
        f1.set(model, procedimientoPasoSecuenciaDao);

        Field f2 = ProcedimientoPasoSecuenciaModel.class.getDeclaredField("procedimientoPasoDao");
        f2.setAccessible(true);
        f2.set(model, procedimientoPasoDao);

        lenient().when(procedimientoPasoDao.findRange(anyInt(), anyInt())).thenReturn(List.of());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        ProcedimientoPasoSecuencia pps = new ProcedimientoPasoSecuencia();
        pps.setIdProcedimientoPasoSecuencia(id);
        assertThat(model.getId(pps)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        ProcedimientoPasoSecuencia pps = model.crearEntidad();
        assertThat(pps).isNotNull();
        assertThat(pps.getIdProcedimientoPasoSecuencia()).isNotNull();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(procedimientoPasoSecuenciaDao);
    }

    @Test
    public void testInicializar() {
        verify(procedimientoPasoDao).findRange(0, 1000);
    }

    @Test
    public void testGetListaPasos() {
        assertThat(model.getListaPasos()).isNotNull();
    }
}