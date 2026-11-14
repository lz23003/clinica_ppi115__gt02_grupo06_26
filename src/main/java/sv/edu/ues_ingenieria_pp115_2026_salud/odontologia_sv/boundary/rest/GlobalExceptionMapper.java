package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = "Ocurrió un error inesperado. Intente nuevamente más tarde.";
        if (exception instanceof WebApplicationException webException) {
            status = webException.getResponse().getStatus();
            message = status == Response.Status.NOT_FOUND.getStatusCode()
                    ? "No se encontró el recurso solicitado."
                    : "La solicitud no pudo ser procesada.";
            LOGGER.log(Level.WARNING, "Error procesando una solicitud REST", exception);
        } else {
            LOGGER.log(Level.SEVERE, "Error inesperado en un servicio REST", exception);
        }

        return Response.status(status)
                .type("application/json")
                .entity(Map.of("message", message))
                .build();
    }
}
