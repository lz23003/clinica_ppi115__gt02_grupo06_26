package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoPasoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;

@RequestScoped
@Path("/consultas-procedimientos-pasos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaProcedimientoPasoResource extends DefaultResource<ConsultaProcedimientoPaso> {

    @Inject
    private ConsultaProcedimientoPasoService consultaProcedimientoPasoService;

    @Override
    protected ServiceInterface<ConsultaProcedimientoPaso> getService() {
        return consultaProcedimientoPasoService;
    }

    @Override
    protected String getNombre() {
        return "consulta procedimiento paso";
    }

    @Override
    protected UUID getId(ConsultaProcedimientoPaso entidad) {
        return entidad.getIdConsultaProcedimientoPaso();
    }

    @Override
    protected void asignarId(ConsultaProcedimientoPaso entidad, UUID id) {
        entidad.setIdConsultaProcedimientoPaso(id);
    }

    @Override
    protected void copiarCampos(ConsultaProcedimientoPaso actual, ConsultaProcedimientoPaso modificado) {
        actual.setFechaInicio(modificado.getFechaInicio());
        actual.setFechaFin(modificado.getFechaFin());
        actual.setEstado(modificado.getEstado());
        actual.setValor(modificado.getValor());
        actual.setIdProcedimientoPaso(modificado.getIdProcedimientoPaso());
        actual.setIdConsultaProcedimiento(modificado.getIdConsultaProcedimiento());
        actual.setIdPersonaRol(modificado.getIdPersonaRol());
    }

    // POST /api/v1/consultas-procedimientos-pasos
    @POST
    public Response crear(ConsultaProcedimientoPaso nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/consultas-procedimientos-pasos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ConsultaProcedimientoPaso modificado) {
        return actualizarEntidad(id, modificado);
    }
    
}
