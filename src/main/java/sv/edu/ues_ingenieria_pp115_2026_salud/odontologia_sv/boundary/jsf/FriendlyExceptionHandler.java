package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.FacesException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FriendlyExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger LOGGER = Logger.getLogger(FriendlyExceptionHandler.class.getName());
    private final ExceptionHandler wrapped;

    public FriendlyExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();
        while (events.hasNext()) {
            ExceptionQueuedEvent event = events.next();
            Throwable exception = event.getContext().getException();
            LOGGER.log(Level.SEVERE, "Error inesperado durante el procesamiento de una vista JSF", exception);

            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                context.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "No fue posible completar la operación",
                        "Ocurrió un error inesperado. Intente nuevamente más tarde."));
            }
            events.remove();
        }
        getWrapped().handle();
    }
}
