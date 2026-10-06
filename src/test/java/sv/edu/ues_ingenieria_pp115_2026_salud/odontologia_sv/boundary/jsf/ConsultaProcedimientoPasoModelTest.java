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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConsultaProcedimientoPasoModelTest {

    @Mock
    private ConsultaProcedimientoPasoDAO consultaProcedimientoPasoDao;
    @Mock
    private ConsultaProcedimientoDAO consultaProcedimientoDao;
    @Mock
    private PersonaRolDAO personaRolDao;
    @Mock
    private ProcedimientoDAO procedimientoDao;
    @Mock
    private FacesContext facesContextMock;
    @Mock
    private Application applicationMock;
    @Mock
    private ResourceBundle resourceBundleMock;

    private ConsultaProcedimientoPasoModel model;

    @BeforeEach
    public void setUp() throws Exception {
        lenient().when(facesContextMock.getApplication()).thenReturn(applicationMock);
        lenient().when(applicationMock.getResourceBundle(facesContextMock, "crud"))
                .thenReturn(resourceBundleMock);
        lenient().when(resourceBundleMock.getString(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        model = new ConsultaProcedimientoPasoModel();

        Field f1 = ConsultaProcedimientoPasoModel.class.getDeclaredField("consultaProcedimientoPasoDao");
        f1.setAccessible(true);
        f1.set(model, consultaProcedimientoPasoDao);

        Field f2 = ConsultaProcedimientoPasoModel.class.getDeclaredField("consultaProcedimientoDao");
        f2.setAccessible(true);
        f2.set(model, consultaProcedimientoDao);

        Field f3 = ConsultaProcedimientoPasoModel.class.getDeclaredField("personaRolDao");
        f3.setAccessible(true);
        f3.set(model, personaRolDao);

        Field f4 = ConsultaProcedimientoPasoModel.class.getDeclaredField("procedimientoDao");
        f4.setAccessible(true);
        f4.set(model, procedimientoDao);
        lenient().when(consultaProcedimientoDao.findRange(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        lenient().when(personaRolDao.findRange(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        lenient().when(procedimientoDao.findRange(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        model.inicializar();
    }

    @Test
    public void testGetId() {
        UUID id = UUID.randomUUID();
        ConsultaProcedimientoPaso cpp = new ConsultaProcedimientoPaso();
        cpp.setIdConsultaProcedimientoPaso(id);
        assertThat(model.getId(cpp)).isEqualTo(id);
    }

    @Test
    public void testGetIdConNull() {
        assertThat(model.getId(null)).isNull();
    }

    @Test
    public void testCrearEntidad() {
        ConsultaProcedimientoPaso cpp = model.crearEntidad();
        assertThat(cpp).isNotNull();
        assertThat(cpp.getIdConsultaProcedimientoPaso()).isNotNull();
        assertThat(cpp.getFechaInicio()).isNotNull();
        assertThat(cpp.getEstado()).isEqualTo("PENDIENTE");
    }

    @Test
    public void testGetDao() {
        assertThat(model.getDao()).isSameAs(consultaProcedimientoPasoDao);
    }

    @Test
    public void testInicializar() {
        verify(consultaProcedimientoDao).findRange(0, 1000);
        verify(personaRolDao).findRange(0, 1000);
        verify(procedimientoDao).findRange(0, 1000);
    }

    @Test
    public void testObtenerInfoConNull() {
        assertThat(model.obtenerInfoConsultaProcedimiento(null)).isEmpty();
    }

    @Test
    public void testObtenerInfoConDatos() {
        // given
        Consulta c = new Consulta();
        c.setFechaInicio(new Date());

        UUID idProc = UUID.randomUUID();
        Procedimiento p = new Procedimiento();
        p.setIdProcedimiento(idProc);
        p.setNombre("Limpieza");

        ConsultaProcedimiento cp = new ConsultaProcedimiento();
        cp.setIdConsulta(c);
        cp.setIdProcedimiento(idProc);

        model.getListaProcedimientos().add(p);

        // when
        String resultado = model.obtenerInfoConsultaProcedimiento(cp);

        // then
        assertThat(resultado).contains("Limpieza");
        assertThat(resultado).contains("/");
    }

    @Test
    public void testObtenerInfoSinFecha() {
        ConsultaProcedimiento cp = new ConsultaProcedimiento();

        String resultado = model.obtenerInfoConsultaProcedimiento(cp);

        assertThat(resultado).contains("Sin fecha");
    }
}
