package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.OrdenExamenService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.OrdenExamen;

@Stateless
@Path("/ordenes-examen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrdenExamenResource implements Serializable {

    @Inject
    private OrdenExamenService ordenExamenService;
    
     @Inject
    private UriInfo uriInfo;

@POST
public Response crear(OrdenExamen nuevo) {
    if (nuevo == null) {
        return Response.status(422).entity("El cuerpo no puede estar vacío").build();
    }
    try {
        nuevo.setIdOrdenExamen(UUID.randomUUID());
        ordenExamenService.crear(nuevo);

        UUID idGenerado = nuevo.getIdOrdenExamen();
        URI uri = uriInfo.getAbsolutePathBuilder().path(idGenerado.toString()).build();

        return Response.created(uri)
                .entity(nuevo)
                .header("X-Message", "Orden de examen creada exitosamente")
                .build();
    } catch (Exception e) {
        return Response.status(500)
                .entity("Error al crear la orden: " + e.getMessage())
                .build();
    }
}
     @GET
    @Path("/por-paso/{idPaso}")
    public Response buscarPorPaso(@PathParam("idPaso") UUID idPaso) {
        try {
            List<OrdenExamen> lista = ordenExamenService.buscarPorPaso(idPaso);
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }
    
    
        @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            OrdenExamen entidad = ordenExamenService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404).entity("No se encontró la orden de examen").build();
            }
            ordenExamenService.eliminar(entidad);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(500).entity("Error al eliminar: " + e.getMessage()).build();
        }
    }
}
