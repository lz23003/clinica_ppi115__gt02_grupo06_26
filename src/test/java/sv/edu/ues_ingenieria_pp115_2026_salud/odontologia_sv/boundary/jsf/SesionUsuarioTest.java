package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SesionUsuarioTest {

    private SesionUsuario sesion;

    @BeforeEach
    public void setUp() {
        sesion = new SesionUsuario();
        sesion.inicializador();
    }


    @Test
    public void testInicializadorCargaIdiomas() {
        assertThat(sesion.getIdiomasDisponibles()).hasSize(2);
        assertThat(sesion.getIdiomasDisponibles()).containsKeys("Español", "English");
    }

    @Test
    public void testIdiomaPorDefecto() {
        assertThat(sesion.getIdiomaSeleccionado()).isEqualTo("Español");
        assertThat(sesion.getLocale().getLanguage()).isEqualTo("es");
    }

    @Test
    public void testGetIdiomasDisponibles() {
        assertThat(sesion.getIdiomasDisponibles()).isNotEmpty();
    }

    @Test
    public void testSetLocale() {
        Locale en = new Locale("en", "US");
        sesion.setLocale(en);
        assertThat(sesion.getLocale()).isEqualTo(en);
    }

    @Test
    public void testSetIdiomaSeleccionado() {
        sesion.setIdiomaSeleccionado("English");
        assertThat(sesion.getIdiomaSeleccionado()).isEqualTo("English");
    }

    @Test
    public void testSetIdiomasDisponibles() {
        sesion.setIdiomasDisponibles(new java.util.HashMap<>());
        assertThat(sesion.getIdiomasDisponibles()).isEmpty();
    }

    // TESTS DE cambiarIdioma()

    @Test
    public void testCambiarIdiomaAEnglish() {
        sesion.setIdiomaSeleccionado("English");

        try (MockedStatic<FacesContext> mocked = Mockito.mockStatic(FacesContext.class)) {
            mocked.when(FacesContext::getCurrentInstance).thenReturn(null);

            sesion.cambiarIdioma(mock(AjaxBehaviorEvent.class));
        }

        assertThat(sesion.getLocale().getLanguage()).isEqualTo("en");
        assertThat(sesion.getLocale().getCountry()).isEqualTo("US");
    }

    @Test
    public void testCambiarIdiomaConIdiomaInvalido() {
        Locale original = sesion.getLocale();
        sesion.setIdiomaSeleccionado("Français");   // no existe en el mapa

        try (MockedStatic<FacesContext> mocked = Mockito.mockStatic(FacesContext.class)) {
            mocked.when(FacesContext::getCurrentInstance).thenReturn(null);

            sesion.cambiarIdioma(mock(AjaxBehaviorEvent.class));
        }

        // El locale debe seguir siendo el original
        assertThat(sesion.getLocale()).isEqualTo(original);
    }

    @Test
    public void testCambiarIdiomaConFacesContextValido() {
        sesion.setIdiomaSeleccionado("English");

        FacesContext mockContext = mock(FacesContext.class);
        jakarta.faces.component.UIViewRoot mockViewRoot = mock(jakarta.faces.component.UIViewRoot.class);
        when(mockContext.getViewRoot()).thenReturn(mockViewRoot);

        try (MockedStatic<FacesContext> mocked = Mockito.mockStatic(FacesContext.class)) {
            mocked.when(FacesContext::getCurrentInstance).thenReturn(mockContext);

            sesion.cambiarIdioma(mock(AjaxBehaviorEvent.class));
        }

        assertThat(sesion.getLocale().getLanguage()).isEqualTo("en");
        org.mockito.Mockito.verify(mockViewRoot).setLocale(sesion.getLocale());
    }

    @Test
    public void testCambiarIdiomaSinViewRoot() {
        sesion.setIdiomaSeleccionado("English");

        FacesContext mockContext = mock(FacesContext.class);
        when(mockContext.getViewRoot()).thenReturn(null);

        try (MockedStatic<FacesContext> mocked = Mockito.mockStatic(FacesContext.class)) {
            mocked.when(FacesContext::getCurrentInstance).thenReturn(mockContext);

            sesion.cambiarIdioma(mock(AjaxBehaviorEvent.class));
        }

        assertThat(sesion.getLocale().getLanguage()).isEqualTo("en");
    }
    
    @Test
public void testCambiarIdiomaConExcepcion() {
    sesion.setIdiomaSeleccionado("English");

    try (MockedStatic<FacesContext> mocked = Mockito.mockStatic(FacesContext.class)) {
        mocked.when(FacesContext::getCurrentInstance)
                .thenThrow(new RuntimeException("Error simulado"));

        sesion.cambiarIdioma(mock(AjaxBehaviorEvent.class));
    }

    // Verificar que el locale se actualizó ANTES de la excepción
    assertThat(sesion.getLocale().getLanguage()).isEqualTo("en");
}
}