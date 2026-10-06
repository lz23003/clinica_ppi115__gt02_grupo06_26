package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EntityConverterTest {

    @Mock
    private FacesContext facesContextMock;

    @Mock
    private UIViewRoot viewRootMock;

    @Mock
    private UIComponent componentMock;

    private Map<String, Object> viewMap;

    private EntityConverter converter;

    @BeforeEach
    public void setUp() {
        viewMap = new HashMap<>();

       lenient().when(facesContextMock.getViewRoot()).thenReturn(viewRootMock);
        lenient().when(viewRootMock.getViewMap()).thenReturn(viewMap);

        converter = new EntityConverter();
    }

    // TESTS DE getAsString

    @Test
    public void testGetAsStringConNull() {
        String resultado = converter.getAsString(facesContextMock, componentMock, null);
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testGetAsStringConObjeto() {
        Object objeto = new Object();

        String id = converter.getAsString(facesContextMock, componentMock, objeto);

        assertThat(id).isNotBlank();
    }

    @Test
    public void testGetAsStringGuardaEnViewMap() {
        Object objeto = new Object();

        converter.getAsString(facesContextMock, componentMock, objeto);

        Map<String, Object> map = (Map<String, Object>) viewMap.get("entityConverter.objects");
        assertThat(map).containsValue(objeto);
    }

    @Test
    public void testGetAsStringReutilizaId() {
        Object objeto = new Object();

        String id1 = converter.getAsString(facesContextMock, componentMock, objeto);
        String id2 = converter.getAsString(facesContextMock, componentMock, objeto);

        assertThat(id1).isEqualTo(id2);
    }

    @Test
    public void testGetAsStringDosObjetosDistintos() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        String id1 = converter.getAsString(facesContextMock, componentMock, obj1);
        String id2 = converter.getAsString(facesContextMock, componentMock, obj2);

        assertThat(id1).isNotEqualTo(id2);
    }

    // TESTS DE getAsObject

    @Test
    public void testGetAsObjectConNull() {
        Object resultado = converter.getAsObject(facesContextMock, componentMock, null);
        assertThat(resultado).isNull();
    }

    @Test
    public void testGetAsObjectConCadenaVacia() {
        Object resultado = converter.getAsObject(facesContextMock, componentMock, "");
        assertThat(resultado).isNull();
    }

    @Test
    public void testGetAsObjectConCadenaBlanca() {
        Object resultado = converter.getAsObject(facesContextMock, componentMock, "   ");
        assertThat(resultado).isNull();
    }

    @Test
    public void testGetAsObjectConIdValido() {
        Object objeto = new Object();
        String id = converter.getAsString(facesContextMock, componentMock, objeto);

        Object resultado = converter.getAsObject(facesContextMock, componentMock, id);

        assertThat(resultado).isSameAs(objeto);
    }

    @Test
    public void testGetAsObjectConIdInexistente() {
        Object resultado = converter.getAsObject(facesContextMock, componentMock, "id-inexistente");
        assertThat(resultado).isNull();
    }

    // TEST DE CICLO COMPLETO

    @Test
    public void testCicloCompleto() {
        Object objeto = new Object();

        String id = converter.getAsString(facesContextMock, componentMock, objeto);
        Object recuperado = converter.getAsObject(facesContextMock, componentMock, id);

        assertThat(recuperado).isSameAs(objeto);
    }

    @Test
    public void testCicloCompletoConEntidad() {
        sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica clinica =
                new sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica();
        clinica.setIdClinica(java.util.UUID.randomUUID());
        clinica.setNombre("Clínica Test");

        String id = converter.getAsString(facesContextMock, componentMock, clinica);
        Object recuperado = converter.getAsObject(facesContextMock, componentMock, id);

        assertThat(recuperado).isSameAs(clinica);
        assertThat(((sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica) recuperado).getNombre())
                .isEqualTo("Clínica Test");
    }
}