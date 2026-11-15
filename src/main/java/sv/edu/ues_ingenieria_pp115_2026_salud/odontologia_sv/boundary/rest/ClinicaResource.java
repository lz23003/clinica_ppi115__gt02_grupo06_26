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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;

@RequestScoped
@Path("/clinicas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClinicaResource extends DefaultResource<Clinica> {

    @Inject
    private ClinicaService clinicaService;

    @Override
    protected ServiceInterface<Clinica> getService() {
        return clinicaService;
    }

    @Override
    protected String getNombre() {
        return "clinica";
    }

    @Override
    protected UUID getId(Clinica entidad) {
        return entidad.getIdClinica();
    }

    @Override
    protected void asignarId(Clinica entidad, UUID id) {
        entidad.setIdClinica(id);
    }

    @Override
    protected void copiarCampos(Clinica actual, Clinica modificado) {
        actual.setNombre(modificado.getNombre());
        actual.setActivo(modificado.getActivo());
        actual.setTipo(modificado.getTipo());
        actual.setComentarios(modificado.getComentarios());
    }

    // POST /api/v1/clinicas
    @POST
    public Response crear(Clinica nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/clinicas/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Clinica modificado) {
        return actualizarEntidad(id, modificado);
    }
}
