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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;

@RequestScoped
@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource extends DefaultResource<Consulta> {

    @Inject
    private ConsultaService consultaService;

    @Override
    protected ServiceInterface<Consulta> getService() {
        return consultaService;
    }

    @Override
    protected String getNombre() {
        return "consulta";
    }

    @Override
    protected UUID getId(Consulta entidad) {
        return entidad.getIdConsulta();
    }

    @Override
    protected void asignarId(Consulta entidad, UUID id) {
        entidad.setIdConsulta(id);
    }

    @Override
    protected void copiarCampos(Consulta actual, Consulta modificado) {
        actual.setFechaInicio(modificado.getFechaInicio());
        actual.setFechaFin(modificado.getFechaFin());
        actual.setReferenciaExterna(modificado.getReferenciaExterna());
        actual.setObservaciones(modificado.getObservaciones());
        actual.setIdPersonaRol(modificado.getIdPersonaRol());
    }

    // POST /api/v1/consultas
    @POST
    public Response crear(Consulta nuevo) {
        return crearEntidad(nuevo);
    }

    // PUT /api/v1/consultas/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Consulta modificado) {
        return actualizarEntidad(id, modificado);
    }
}
