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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoExamen;

@RequestScoped
@Path("/procedimientos-pasos-examenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimientoPasoExamenResource extends DefaultResource<ProcedimientoPasoExamen> {

    @Inject
    private ProcedimientoPasoExamenService procedimientoPasoExamenService;

    @Override
    protected ServiceInterface<ProcedimientoPasoExamen> getService() {
        return procedimientoPasoExamenService;
    }

    @Override
    protected String getNombre() {
        return "procedimiento paso examen";
    }

    @Override
    protected UUID getId(ProcedimientoPasoExamen entidad) {
        return entidad.getIdProcedimientoPasoExamen();
    }

    @Override
    protected void asignarId(ProcedimientoPasoExamen entidad, UUID id) {
        entidad.setIdProcedimientoPasoExamen(id);
    }

    @Override
    protected void copiarCampos(ProcedimientoPasoExamen actual, ProcedimientoPasoExamen modificado) {
        actual.setFechaCreacion(modificado.getFechaCreacion());
        actual.setActivo(modificado.getActivo());
        actual.setObservaciones(modificado.getObservaciones());
        actual.setIdExamen(modificado.getIdExamen());
        actual.setIdProcedimientoPaso(modificado.getIdProcedimientoPaso());
    }

    // POST /api/v1/procedimientos-pasos-examenes
    @POST
    public Response crear(ProcedimientoPasoExamen nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/procedimientos-pasos-examenes/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ProcedimientoPasoExamen modificado) {
        return actualizarEntidad(id, modificado);
    }

}
