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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoSecuenciaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoSecuencia;

@RequestScoped
@Path("/procedimientos-pasos-secuencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimientoPasoSecuenciaResource extends DefaultResource<ProcedimientoPasoSecuencia> {

    @Inject
    private ProcedimientoPasoSecuenciaService procedimientoPasoSecuenciaService;

    @Override
    protected ServiceInterface<ProcedimientoPasoSecuencia> getService() {
        return procedimientoPasoSecuenciaService;
    }

    @Override
    protected String getNombre() {
        return "procedimiento paso secuencia";
    }

    @Override
    protected UUID getId(ProcedimientoPasoSecuencia entidad) {
        return entidad.getIdProcedimientoPasoSecuencia();
    }

    @Override
    protected void asignarId(ProcedimientoPasoSecuencia entidad, UUID id) {
        entidad.setIdProcedimientoPasoSecuencia(id);
    }

    @Override
    protected void copiarCampos(ProcedimientoPasoSecuencia actual, ProcedimientoPasoSecuencia modificado) {
        actual.setIdProcedimientoPasoReferencia(modificado.getIdProcedimientoPasoReferencia());
        actual.setTipoSecuencia(modificado.getTipoSecuencia());
        actual.setIdProcedimientoPaso(modificado.getIdProcedimientoPaso());
    }

    // POST /api/v1/procedimientos-pasos-secuencias
    @POST
    public Response crear(ProcedimientoPasoSecuencia nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/procedimientos-pasos-secuencias/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ProcedimientoPasoSecuencia modificado) {
        return actualizarEntidad(id, modificado);
    }

}
