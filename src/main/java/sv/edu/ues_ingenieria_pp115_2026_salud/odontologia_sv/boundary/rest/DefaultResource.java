package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.x500.X500Principal;
import org.primefaces.util.DynamicContentSrcBuilder;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;

public abstract class DefaultResource<T> {

    private static final Logger LOG = Logger.getLogger(DefaultResource.class.getName());

    /**
     * Tope de registros por página: aunque el cliente pida más, se limita a
     * este valor.
     */
    protected static final int MAX_LIMITE = 100;

    @Inject
    protected UriInfo uriInfo;

    // ---- Datos que cada recurso concreto debe indicar ----
    /**
     * Servicio de la entidad (ej. examenService).
     */
    protected abstract ServiceInterface<T> getService();

    /**
     * Nombre en singular, usado en los mensajes (ej. "examen").
     */
    protected abstract String getNombre();

    /**
     * Devuelve el UUID de la entidad.
     */
    protected abstract UUID getId(T entidad);

    /**
     * Asigna el UUID a una entidad nueva.
     */
    protected abstract void asignarId(T entidad, UUID id);

    /**
     * Copia al registro existente los campos editables del recibido.
     */
    protected abstract void copiarCampos(T actual, T modificado);

    //Utilidades
    //Respuesta de error en JSON valido: {"mensaje":"..."
    protected Response error(int status, String mensaje) {
        String texto = Objects.toString(mensaje, "Solicitud invalida");
        return Response.status(status)
                .entity(Map.of("mensaje", texto))
                .header("X-Message", texto)
                .build();
    }

    /*Ejecuta la accion y traduce las excepciones:IllegalArgumentExcption
    (dato invalito) -> 400; cualquier otra -> 500 con registro en el log.
    el contenedor EJB envuelve las excepciones en EJBException, por eso 
    tambien se revisa getCause()
     */
    protected Response ejecutar(String operacion, Supplier<Response> accion) {
        try {
            return accion.get();
        } catch (Exception e) {
            Throwable causa = (e.getCause() != null) ? e.getCause() : e;
            if (causa instanceof IllegalArgumentException) {
                return error(400, causa.getMessage());
            }
            LOG.log(Level.SEVERE, "Error al " + operacion, e);
            return error(500, "Error interno al " + operacion);
        }
    }

    protected Response noEncontrado(UUID id) {
        return error(404, "No se encontro el " + getNombre() + " con id " + id);
    }

    //Endpoints comunes
    //GET/api/v1/<recurso>?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        if (first < 0) {
            return error(400, "El parametro first no puede ser negativo");
        }
        if (max < 1) {
            return error(400, "El parametro 'max' debe ser mayor o igual a 1");
        }
        final int limite = Math.min(max, MAX_LIMITE);

        return ejecutar("Listar los " + getNombre() + "s", () -> {
            List<T> lista = getService().findRange(first, limite);
            int total = getService().count();
            return Response.ok(lista).header("X-Total-Count", total).build();
        });
    }

    // GET /api/v1/<recurso>/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        return ejecutar("listar todos los " + getNombre() + "s", ()
                -> Response.ok(getService().findAll()).build());
    }

    // GET /api/v1/<recurso>/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        return ejecutar("obtener el " + getNombre(), () -> {
            T entidad = getService().buscarPorId(id);
            if (entidad == null) {
                return noEncontrado(id);
            }
            return Response.ok(entidad).build();
        });
    }

    // DELETE /api/v1/<recurso>/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        return ejecutar("eliminar el " + getNombre(), () -> {
            T entidad = getService().buscarPorId(id);
            if (entidad == null) {
                return noEncontrado(id);
            }
            getService().eliminar(entidad);
            return Response.noContent().build();
        });
    }

    // ---- Lógica de POST y PUT (la subclase la llama desde sus propios métodos,
    //      porque el tipo concreto del cuerpo debe estar visible para JSON-B) ----
    protected Response crearEntidad(T nuevo) {
        if (nuevo == null) {
            return error(400, "El cuerpo de la peticion no puede estar vacio");
        }
        return ejecutar("crear el " + getNombre(), () -> {
            asignarId(nuevo, UUID.randomUUID());
            getService().crear(nuevo);
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(getId(nuevo).toString())
                    .build();
            return Response.created(uri).entity(nuevo).build();
        });
    }

    protected Response actualizarEntidad(UUID id, T modificado) {
        if (modificado == null) {
            return error(400, "El cuerpo de la peticion no puede estar vacio");
        }
        return ejecutar("actualizar el " + getNombre(), () -> {
            T actual = getService().buscarPorId(id);
            if (actual == null) {
                return noEncontrado(id);
            }
            copiarCampos(actual, modificado);
            return Response.ok(getService().modificar(actual)).build();
        });
    }

}
