package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Le ensena a JAX-RS como convertir un String que viene en la URL
 * (un @PathParam o @QueryParam) a un java.util.UUID.
 *
 * ¿Por que hace falta?
 * --------------------
 * Cuando declaras un endpoint asi:
 *
 *     @GET
 *     @Path("{id}")
 *     public Response obtener(@PathParam("id") UUID id) { ... }
 *
 * JAX-RS recibe el {id} como String (asi vienen las URLs) y necesita
 * construir un UUID. El estandar JAX-RS no garantiza conversion para UUID,
 * asi que lo registramos nosotros.
 *
 * ¿Como funciona el mecanismo?
 * ----------------------------
 * 1. JAX-RS, al momento de inyectar un parametro anotado con @PathParam,
 *    pregunta a TODOS los @Provider registrados: "tienen un ParamConverter
 *    para el tipo X?". El primer que dice "si" gana.
 * 2. Aqui implementamos forName(...) y devolvemos un ParamConverter<UUID>
 *    cuando el tipo pedido es UUID.class.
 * 3. fromString(...) recibe el String y lo convierte; toString(...) hace
 *    lo inverso (se usa si alguna vez hay que construir una URL a partir
 *    de un UUID, ej. en un Location header).
 *
 * La anotacion @Provider es lo que hace que JAX-RS lo detecte automaticamente.
 */
@Provider
public class UUIDParamConverterProvider implements ParamConverterProvider {

    /**
     * Conversor concreto para UUID. Lo definimos como constante para no
     * instanciarlo cada vez que JAX-RS lo pida (es stateless, no tiene
     * estado interno, asi que es seguro compartirlo).
     */
    private static final ParamConverter<UUID> UUID_CONVERTER = new ParamConverter<>() {

        @Override
        public UUID fromString(String value) {
            // Si el parametro no viene (nulo o vacio), devolvemos null
            // en vez de lanzar excepcion. Asi el endpoint puede devolver
            // un 400 Bad Request con un mensaje claro en vez de romper.
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            try {
                return UUID.fromString(value.trim());
            } catch (IllegalArgumentException ex) {
                // Si el String no es un UUID valido, lanzamos para que
                // JAX-RS genere un 400 Bad Request automatico.
                throw new IllegalArgumentException(
                    "El valor '" + value + "' no es un UUID valido");
            }
        }

        @Override
        public String toString(UUID value) {
            return value == null ? null : value.toString();
        }
    };

    /**
     * Este metodo lo invoca JAX-RS para cada parametro que necesita convertir.
     * Devolvemos nuestro conversor solo si el tipo pedido es exactamente UUID,
     * en caso contrario devolvemos null (= "yo no se convertir este tipo,
     * pregunta a otro provider").
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> rawType,
                                              Type genericType,
                                              Annotation[] annotations) {
        if (rawType == UUID.class) {
            return (ParamConverter<T>) UUID_CONVERTER;
        }
        return null;
    }
}
