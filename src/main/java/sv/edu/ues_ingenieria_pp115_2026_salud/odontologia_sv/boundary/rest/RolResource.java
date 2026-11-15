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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;

@RequestScoped
@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RolResource extends DefaultResource<Rol> {

    @Inject
    private RolService rolService;

    @Override
    protected ServiceInterface<Rol> getService() {
        return rolService;
    }

    @Override
    protected String getNombre() {
        return "rol";
    }

    @Override
    protected UUID getId(Rol entidad) {
        return entidad.getIdRol();
    }

    @Override
    protected void asignarId(Rol entidad, UUID id) {
        entidad.setIdRol(id);
    }

    @Override
    protected void copiarCampos(Rol actual, Rol modificado) {
        actual.setNombre(modificado.getNombre());
        actual.setActivo(modificado.getActivo());
        actual.setObservaciones(modificado.getObservaciones());
    }

    // POST /api/v1/roles
    @POST
    public Response crear(Rol nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/roles/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Rol modificado) {
        return actualizarEntidad(id, modificado);
    }
}
