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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;

@RequestScoped
@Path("/procedimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimientoResource extends DefaultResource<Procedimiento>{

    @Inject
    private ProcedimientoService procedimientoService;

    @Override
    protected ServiceInterface<Procedimiento> getService() {
        return procedimientoService;
    }

    @Override
    protected String getNombre() {
        return "procedimiento";
    }

    @Override
    protected UUID getId(Procedimiento entidad) {
        return entidad.getIdProcedimiento();
    }

    @Override
    protected void asignarId(Procedimiento entidad, UUID id) {
        entidad.setIdProcedimiento(id);
    }

    @Override
    protected void copiarCampos(Procedimiento actual, Procedimiento modificado) {
        actual.setNombre(modificado.getNombre());
        actual.setActivo(modificado.getActivo());
        actual.setObservaciones(modificado.getObservaciones());
    }

    // POST /api/v1/procedimientos
    @POST
    public Response crear(Procedimiento nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/procedimientos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Procedimiento modificado) {
        return actualizarEntidad(id, modificado);
    }

}
