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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;

@RequestScoped
@Path("/personas-roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonaRolResource extends DefaultResource<PersonaRol> {

    @Inject
    private PersonaRolService personaRolService;

    @Override
    protected ServiceInterface<PersonaRol> getService() {
        return personaRolService;
    }

    @Override
    protected String getNombre() {
        return "persona rol";
    }

    @Override
    protected UUID getId(PersonaRol entidad) {
        return entidad.getIdPersonaRol();
    }

    @Override
    protected void asignarId(PersonaRol entidad, UUID id) {
        entidad.setIdPersonaRol(id);
    }

    @Override
    protected void copiarCampos(PersonaRol actual, PersonaRol modificado) {
        actual.setFechaCreacion(modificado.getFechaCreacion());
        actual.setIdClinica(modificado.getIdClinica());
        actual.setIdPersona(modificado.getIdPersona());
        actual.setIdRol(modificado.getIdRol());
    }

    // POST /api/v1/personas-roles
    @POST
    public Response crear(PersonaRol nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/personas-roles/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, PersonaRol modificado) {
        return actualizarEntidad(id, modificado);
    }
}
