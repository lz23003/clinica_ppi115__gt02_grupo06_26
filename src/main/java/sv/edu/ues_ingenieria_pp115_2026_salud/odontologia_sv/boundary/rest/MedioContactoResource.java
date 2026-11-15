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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.MedioContactoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;

@RequestScoped
@Path("/medios-contacto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedioContactoResource extends DefaultResource<MedioContacto> {

    @Inject
    private MedioContactoService medioContactoService;

    @Override
    protected ServiceInterface<MedioContacto> getService() {
        return medioContactoService;
    }

    @Override
    protected String getNombre() {
        return "medio contacto";
    }

    @Override
    protected UUID getId(MedioContacto entidad) {
        return entidad.getIdMedioContacto();
    }

    @Override
    protected void asignarId(MedioContacto entidad, UUID id) {
        entidad.setIdMedioContacto(id);
    }

    @Override
    protected void copiarCampos(MedioContacto actual, MedioContacto modificado) {
        actual.setValor(modificado.getValor());
        actual.setFechaCreacion(modificado.getFechaCreacion());
        actual.setIdPersona(modificado.getIdPersona());
        actual.setIdTipoMedioContacto(modificado.getIdTipoMedioContacto());
    }

    // POST /api/v1/medios-contacto
    @POST
    public Response crear(MedioContacto nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/medios-contacto/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, MedioContacto modificado) {
        return actualizarEntidad(id, modificado);
    }
}
