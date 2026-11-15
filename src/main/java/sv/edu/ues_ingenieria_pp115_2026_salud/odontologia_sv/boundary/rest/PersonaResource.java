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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;

@RequestScoped
@Path("/personas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonaResource extends DefaultResource<Persona> {

    @Inject
    private PersonaService personaService;

    @Override
    protected ServiceInterface<Persona> getService() {
        return personaService;
    }

    @Override
    protected String getNombre() {
        return "persona";
    }

    @Override
    protected UUID getId(Persona entidad) {
        return entidad.getIdPersona();
    }

    @Override
    protected void asignarId(Persona entidad, UUID id) {
        entidad.setIdPersona(id);
    }

    @Override
    protected void copiarCampos(Persona actual, Persona modificado) {
        actual.setNombres(modificado.getNombres());
        actual.setApellidos(modificado.getApellidos());
        actual.setFechaNacimiento(modificado.getFechaNacimiento());
        actual.setFechaCreacion(modificado.getFechaCreacion());
    }

    // POST /api/v1/personas
    @POST
    public Response crear(Persona nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/personas/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Persona modificado) {
        return actualizarEntidad(id, modificado);
    }
}
