package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConsultaProcedimientoModelTest {

    @Mock
    private ConsultaProcedimientoDAO consultaProcedimientoDao;
    @Mock
    private ConsultaDAO consultaDao;
    @Mock
    private ProcedimientoDAO procedimientoDao;
    @Mock
    private FacesContext facesContextMock;
    @Mock
    private Application applicationMock;
    @Mock
    private ResourceBundle resourceBundleMock;

    private ConsultaProcedimientoModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new ConsultaProcedimientoModel();

        Field f1 = ConsultaProcedimientoModel.class.getDeclaredField("consultaProcedimientoDao");
        f1.setAccessible(true);
        f1.set(model, consultaProcedimientoDao);

        Field f2 = ConsultaProcedimientoModel.class.getDeclaredField("consultaDao");
        f2.setAccessible(true);
        f2.set(model, consultaDao);

        Field f3 = ConsultaProcedimientoModel.class.getDeclaredField("procedimientoDao");
        f3.setAccessible(true);
        f3.set(model, procedimientoDao);

        lenient().when(consultaDao.buscarPorConsultasActivas()).thenReturn(new ArrayList<>());
        lenient().when(procedimientoDao.listarActivos()).thenReturn(new ArrayList<>());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        ConsultaProcedimiento cp = new ConsultaProcedimiento();
        cp.setIdConsultaProcedimiento(id);
        assertThat(model.getId(cp)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        ConsultaProcedimiento cp = model.crearEntidad();
        assertThat(cp).isNotNull();
        assertThat(cp.getIdConsultaProcedimiento()).isNotNull();
        assertThat(cp.getFechaInicio()).isNotNull();
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(consultaProcedimientoDao);
    }

    @Test
    public void testInicializar() {
        verify(consultaDao).buscarPorConsultasActivas();
        verify(procedimientoDao).listarActivos();
    }

    @Test
    public void testObtenerNombreProcedimientoConNull() {
        assertThat(model.obtenerNombreProcedimiento(null)).isEmpty();
    }

    @Test
    public void testObtenerNombreProcedimientoConIdExistente() {
        UUID idProc = UUID.randomUUID();
        Procedimiento p = new Procedimiento();
        p.setIdProcedimiento(idProc);
        p.setNombre("Limpieza Dental");

        model.getListaProcedimientos().add(p);

        String resultado = model.obtenerNombreProcedimiento(idProc);

        assertThat(resultado).isEqualTo("Limpieza Dental");
    }

    @Test
    public void testObtenerNombreProcedimientoConIdInexistente() {
        String resultado = model.obtenerNombreProcedimiento(UUID.randomUUID());
        assertThat(resultado).isEqualTo("Desconocido");
    }

    @Test
    public void testFormatearFechaConsulta() {
        Date fecha = new Date();
        String resultado = model.formatearFechaConsulta(fecha);
        assertThat(resultado).matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}");
    }
}
