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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoExamen;

@RequestScoped
@Path("/tipos-examen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoExamenResource extends DefaultResource<TipoExamen> {

    @Inject
    private TipoExamenService tipoExamenService;

    @Override
    protected ServiceInterface<TipoExamen> getService() {
        return tipoExamenService;
    }

    @Override
    protected String getNombre() {
        return "tipo examen";
    }

    @Override
    protected UUID getId(TipoExamen entidad) {
        return entidad.getIdTipoExamen();
    }

    @Override
    protected void asignarId(TipoExamen entidad, UUID id) {
        entidad.setIdTipoExamen(id);
    }

    @Override
    protected void copiarCampos(TipoExamen actual, TipoExamen modificado) {
        actual.setNombre(modificado.getNombre());
        actual.setActivo(modificado.getActivo());
        actual.setObservaciones(modificado.getObservaciones());
    }

    // POST /api/v1/tipos-examen
    @POST
    public Response crear(TipoExamen nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/tipos-examen/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, TipoExamen modificado) {
        return actualizarEntidad(id, modificado);
    }
}
