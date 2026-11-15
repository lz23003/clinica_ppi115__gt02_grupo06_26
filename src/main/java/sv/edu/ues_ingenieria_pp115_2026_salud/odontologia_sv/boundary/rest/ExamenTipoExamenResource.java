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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenTipoExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ExamenTipoExamen;

@RequestScoped
@Path("/examenes-tipos-examen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExamenTipoExamenResource extends DefaultResource<ExamenTipoExamen> {

    @Inject
    private ExamenTipoExamenService examenTipoExamenService;

    @Override
    protected ServiceInterface<ExamenTipoExamen> getService() {
        return examenTipoExamenService;
    }

    @Override
    protected String getNombre() {
        return "examen tipo examen";
    }

    @Override
    protected UUID getId(ExamenTipoExamen entidad) {
        return entidad.getIdExamenTipoExamen();
    }

    @Override
    protected void asignarId(ExamenTipoExamen entidad, UUID id) {
        entidad.setIdExamenTipoExamen(id);
    }

    @Override
    protected void copiarCampos(ExamenTipoExamen actual, ExamenTipoExamen modificado) {
        actual.setFechaCreacion(modificado.getFechaCreacion());
        actual.setObservaciones(modificado.getObservaciones());
        actual.setIdExamen(modificado.getIdExamen());
        actual.setIdTipoExamen(modificado.getIdTipoExamen());
    }

    // POST /api/v1/examenes-tipos-examen
    @POST
    public Response crear(ExamenTipoExamen nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/examenes-tipos-examen/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ExamenTipoExamen modificado) {
        return actualizarEntidad(id, modificado);
    }
}
