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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoMedioContactoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

@RequestScoped
@Path("/tipos-medio-contacto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoMedioContactoResource extends DefaultResource<TipoMedioContacto> {

    @Inject
    private TipoMedioContactoService tipoMedioContactoService;

    @Override
    protected ServiceInterface<TipoMedioContacto> getService() {
        return tipoMedioContactoService;
    }

    @Override
    protected String getNombre() {
        return "tipo medio contacto";
    }

    @Override
    protected UUID getId(TipoMedioContacto entidad) {
        return entidad.getIdTipoMedioContacto();
    }

    @Override
    protected void asignarId(TipoMedioContacto entidad, UUID id) {
        entidad.setIdTipoMedioContacto(id);
    }

    @Override
    protected void copiarCampos(TipoMedioContacto actual, TipoMedioContacto modificado) {
        actual.setNombre(modificado.getNombre());
        actual.setIndicaciones(modificado.getIndicaciones());
        actual.setExpresionRegular(modificado.getExpresionRegular());
        actual.setActivo(modificado.getActivo());
    }

    // POST /api/v1/tipos-medio-contacto
    @POST
    public Response crear(TipoMedioContacto nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/tipos-medio-contacto/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, TipoMedioContacto modificado) {
        return actualizarEntidad(id, modificado);
    }

}
