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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;

@RequestScoped
@Path("/procedimientos-pasos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimientoPasoResource extends DefaultResource<ProcedimientoPaso> {

    @Inject
    private ProcedimientoPasoService procedimientoPasoService;

    @Override
    protected ServiceInterface<ProcedimientoPaso> getService() {
        return procedimientoPasoService;
    }

    @Override
    protected String getNombre() {
        return "procedimiento paso";
    }

    @Override
    protected UUID getId(ProcedimientoPaso entidad) {
        return entidad.getIdProcedimientoPaso();
    }

    @Override
    protected void asignarId(ProcedimientoPaso entidad, UUID id) {
        entidad.setIdProcedimientoPaso(id);
    }

    @Override
    protected void copiarCampos(ProcedimientoPaso actual, ProcedimientoPaso modificado) {
        actual.setNombre(modificado.getNombre());
        actual.setIndicaFin(modificado.getIndicaFin());
        actual.setIdProcedimiento(modificado.getIdProcedimiento());
        actual.setIdRol(modificado.getIdRol());
    }

    // POST /api/v1/procedimientos-pasos
    @POST
    public Response crear(ProcedimientoPaso nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/procedimientos-pasos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ProcedimientoPaso modificado) {
        return actualizarEntidad(id, modificado);
    }
}
