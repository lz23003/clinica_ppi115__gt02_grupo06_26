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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;

@RequestScoped
@Path("/consultas-procedimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaProcedimientoResource extends DefaultResource<ConsultaProcedimiento> {

    @Inject
    private ConsultaProcedimientoService consultaProcedimientoService;

    @Override
    protected ServiceInterface<ConsultaProcedimiento> getService() {
        return consultaProcedimientoService;
    }

    @Override
    protected String getNombre() {
        return "consulta procedimiento";
    }

    @Override
    protected UUID getId(ConsultaProcedimiento entidad) {
        return entidad.getIdConsultaProcedimiento();
    }

    @Override
    protected void asignarId(ConsultaProcedimiento entidad, UUID id) {
        entidad.setIdConsultaProcedimiento(id);
    }

    @Override
    protected void copiarCampos(ConsultaProcedimiento actual, ConsultaProcedimiento modificado) {
        actual.setIdProcedimiento(modificado.getIdProcedimiento());
        actual.setFechaInicio(modificado.getFechaInicio());
        actual.setFechaFin(modificado.getFechaFin());
        actual.setObservaciones(modificado.getObservaciones());
        actual.setIdConsulta(modificado.getIdConsulta());
    }

    // POST /api/v1/consultas-procedimientos
    @POST
    public Response crear(ConsultaProcedimiento nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/consultas-procedimientos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ConsultaProcedimiento modificado) {
        return actualizarEntidad(id, modificado);
    }

}
