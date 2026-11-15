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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ServiceInterface;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;


@RequestScoped
@Path("/examenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExamenResource extends DefaultResource<Examen> {
    
    @Inject
    private ExamenService eService;
    
    @Override
    protected ServiceInterface<Examen> getService(){
        return eService;
    }
    
    @Override
    protected String getNombre(){
        return "examen";
    }
    
    @Override
    protected UUID getId(Examen entidad){
        return entidad.getIdExamen();
    }
    
    @Override
    protected void asignarId(Examen entidad, UUID id){
        entidad.setIdExamen(id);
    }
    
    protected void copiarCampos(Examen actual, Examen modificado){
        actual.setNombre(modificado.getNombre());
        actual.setActivo(modificado.getActivo());
        actual.setObservaciones(modificado.getObservaciones());
    }
    
    //POST /api/v1/examenes
    @POST
    public Response crear(Examen nuevo){
        return crearEntidad(nuevo);
    }
    
    //PUT /api/v1/examenes/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, Examen modificado){
        return actualizarEntidad(id, modificado);
    }
    
    
}
