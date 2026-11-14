package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoPasoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;

@Stateless
@Path("/consultas-procedimientos-pasos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaProcedimientoPasoResource implements Serializable {

    @Inject
    private ConsultaProcedimientoPasoService consultaProcedimientoPasoService;

    @Inject
    private UriInfo uriInfo;

    // GET /api/consultas-procedimientos-pasos?first=0&max=50
    @GET
    public Response listar(
            @QueryParam("first") @DefaultValue("0") int first,
            @QueryParam("max") @DefaultValue("50") int max) {

        try {
            List<ConsultaProcedimientoPaso> lista = consultaProcedimientoPasoService.findRange(first, max);
            int total = consultaProcedimientoPasoService.count();
            return Response.ok(lista)
                    .header("X-Total-Count", total)
                    .header("X-Message", "Listado paginado de las consultas-procedimientos-pasos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar las consultas-procedimientos-pasos")
                    .header("X-Message", "Error al listar las consultas-procedimientos-pasos")
                    .build();
        }
    }

    // GET /api/consultas-procedimientos-pasos/all
    @GET
    @Path("/all")
    public Response listarTodos() {
        try {
            List<ConsultaProcedimientoPaso> lista = consultaProcedimientoPasoService.findAll();
            return Response.ok(lista)
                    .header("X-Message", "Listado completo de las consultas-procedimientos-pasos")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos las consultas-procedimientos-pasos")
                    .header("X-Message", "Error al obtener todos las consultas-procedimientos-pasos")
                    .build();
        }
    }

    // GET /api/consultas-procedimientos-pasos/{id}
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") UUID id) {
        try {
            ConsultaProcedimientoPaso entidad = consultaProcedimientoPasoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro la consulta-procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento-paso con id " + id)
                        .build();
            }
            return Response.ok(entidad)
                    .header("X-Message", "La consulta-procedimiento-paso encontrado")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la consulta-procedimiento-paso por id")
                    .header("X-Message", "Error al obtener la consulta-procedimiento-paso por id")
                    .build();
        }
    }

    // POST /api/consultas-procedimientos-pasos
    @POST
    public Response crear(ConsultaProcedimientoPaso nuevo) {
        if (nuevo == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {

            nuevo.setIdConsultaProcedimientoPaso(UUID.randomUUID());

            consultaProcedimientoPasoService.crear(nuevo);

            UUID idGenerado = nuevo.getIdConsultaProcedimientoPaso();
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(idGenerado.toString())
                    .build();

            return Response.created(uri)
                    .entity(nuevo)
                    .header("X-Message", "La consulta-procedimiento-paso creado exitosamente")
                    .build(); // 201 Created
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al crear la consulta-procedimiento-paso: " + e.getMessage())
                    .header("X-Message", "Error al crear la consulta-procedimiento-paso")
                    .build();
        }
    }

    // PUT /api/consultas-procedimientos-pasos/{id}
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") UUID id, ConsultaProcedimientoPaso modificado) {
        if (modificado == null) {
            return Response.status(422)
                    .entity("El cuerpo de la peticion no puede estar vacio o mal formado")
                    .header("X-Message", "El cuerpo de la peticion no puede estar vacio")
                    .build();
        }

        try {
            ConsultaProcedimientoPaso actual = consultaProcedimientoPasoService.buscarPorId(id);
            if (actual == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta-procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento-paso con id " + id)
                        .build();
            }

            actual.setFechaInicio(modificado.getFechaInicio());
            actual.setFechaFin(modificado.getFechaFin());
            actual.setEstado(modificado.getEstado());
            actual.setValor(modificado.getValor());
            actual.setIdProcedimientoPaso(modificado.getIdProcedimientoPaso());
            actual.setIdConsultaProcedimiento(modificado.getIdConsultaProcedimiento());
            actual.setIdPersonaRol(modificado.getIdPersonaRol());

            ConsultaProcedimientoPaso actualizado = consultaProcedimientoPasoService.modificar(actual);

            return Response.ok(actualizado)
                    .header("X-Message", "La consulta-procedimiento-paso actualizado")
                    .build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al actualizar la consulta-procedimiento-paso: " + e.getMessage())
                    .header("X-Message", "Error al actualizar la consulta-procedimiento-paso")
                    .build();
        }
    }

    // DELETE /api/consultas-procedimientos-pasos/{id}
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            ConsultaProcedimientoPaso entidad = consultaProcedimientoPasoService.buscarPorId(id);
            if (entidad == null) {
                return Response.status(404)
                        .entity("No se encontro la consulta-procedimiento-paso con id " + id)
                        .header("X-Message", "No se encontro la consulta-procedimiento-paso con id " + id)
                        .build();
            }

            consultaProcedimientoPasoService.eliminar(entidad);
            return Response.noContent()
                    .header("X-Message", "La consulta-procedimiento-paso eliminado")
                    .build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(500)
                    .entity("Error al eliminar la consulta-procedimiento-paso")
                    .header("X-Message", "Error al eliminar la consulta-procedimiento-paso")
                    .build();
        }
    }

    @PUT
    @Path("/{id}/estado")
    public Response cambiarEstado(@PathParam("id") UUID id, @QueryParam("nuevoEstado") String nuevoEstado) {
        try {
            ConsultaProcedimientoPaso paso = consultaProcedimientoPasoService.buscarPorId(id);
            if (paso == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró el paso").build();
            }

            paso.setEstado(nuevoEstado.toUpperCase());

            // Si es FINALIZADO, le ponemos la fecha de fin actual
            if ("FINALIZADO".equalsIgnoreCase(nuevoEstado)) {
                paso.setFechaFin(new java.util.Date());
            }

            consultaProcedimientoPasoService.modificar(paso);

            return Response.ok(paso)
                    .header("X-Message", "Estado actualizado a " + nuevoEstado).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error al actualizar estado: " + e.getMessage()).build();
        }
    }

    // 1. Endpoint para obtener los pasos de un procedimiento
    @GET
    @Path("/procedimiento/{idProcedimiento}")
    public Response obtenerPasosDeProcedimiento(@PathParam("idProcedimiento") UUID idProcedimiento) {
        try {
            List<ProcedimientoPaso> pasos = consultaProcedimientoPasoService.obtenerPasosDeProcedimiento(idProcedimiento);
            return Response.ok(pasos).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    // 2. Endpoint para INICIAR una consulta (crea el primer paso automáticamente)
    @POST
    @Path("/iniciar")
    public Response iniciarConsultaProcedimiento(
            @QueryParam("idConsultaProcedimiento") UUID idConsultaProcedimiento,
            @QueryParam("idProcedimiento") UUID idProcedimiento,
            @QueryParam("idPersonaRol") UUID idPersonaRol) {
        try {
            // Traemos los pasos del procedimiento
            List<ProcedimientoPaso> pasos = consultaProcedimientoPasoService.obtenerPasosDeProcedimiento(idProcedimiento);

            if (pasos.isEmpty()) {
                return Response.status(400).entity("El procedimiento no tiene pasos definidos.").build();
            }

            // Tomamos el primer paso
            ProcedimientoPaso primerPaso = pasos.get(0);

            // Creamos el registro del paso en la consulta
            ConsultaProcedimientoPaso nuevoPaso = new ConsultaProcedimientoPaso();
            nuevoPaso.setIdConsultaProcedimientoPaso(UUID.randomUUID());
            nuevoPaso.setFechaInicio(new Date());
            nuevoPaso.setEstado("PENDIENTE");

            return Response.ok(nuevoPaso).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error al iniciar: " + e.getMessage()).build();
        }
    }

 

    @GET
    @Path("/consulta/{idConsultaProcedimiento}")
    public Response buscarPorConsultaProcedimiento(@PathParam("idConsultaProcedimiento") UUID idConsultaProcedimiento) {
        try {
            List<ConsultaProcedimientoPaso> lista = consultaProcedimientoPasoService.buscarPorConsultaProcedimiento(idConsultaProcedimiento);
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }
    
        @POST
    @Path("/avanzar/{idConsultaProcedimiento}")
    public Response avanzarSiguientePaso(
            @PathParam("idConsultaProcedimiento") UUID idConsultaProcedimiento,
            @QueryParam("idPasoActual") UUID idPasoActual,
            @QueryParam("idSiguientePaso") UUID idSiguientePaso,
            @QueryParam("idPersonaRol") UUID idPersonaRol) {
        try {
            // 1. Finalizar el paso actual (si existe)
            if (idPasoActual != null) {
                ConsultaProcedimientoPaso pasoActual = consultaProcedimientoPasoService.buscarPorId(idPasoActual);
                if (pasoActual != null) {
                    pasoActual.setEstado("FINALIZADO");
                    pasoActual.setFechaFin(new java.util.Date());
                    consultaProcedimientoPasoService.modificar(pasoActual);
                }
            }

            // 2. Crear el nuevo paso en la Base de Datos
            if (idSiguientePaso != null) {
                ConsultaProcedimientoPaso nuevoPaso = new ConsultaProcedimientoPaso();
                nuevoPaso.setIdConsultaProcedimientoPaso(UUID.randomUUID());
                nuevoPaso.setFechaInicio(new java.util.Date());
                nuevoPaso.setEstado("PENDIENTE");

                ProcedimientoPaso pp = new ProcedimientoPaso();
                pp.setIdProcedimientoPaso(idSiguientePaso);
                nuevoPaso.setIdProcedimientoPaso(pp);

                ConsultaProcedimiento cp = new ConsultaProcedimiento();
                cp.setIdConsultaProcedimiento(idConsultaProcedimiento);
                nuevoPaso.setIdConsultaProcedimiento(cp);

                if (idPersonaRol != null) {
                    PersonaRol pr = new PersonaRol();
                    pr.setIdPersonaRol(idPersonaRol);
                    nuevoPaso.setIdPersonaRol(pr);
                }

                consultaProcedimientoPasoService.crear(nuevoPaso);
                
                return Response.status(201).entity(nuevoPaso).build();
            }

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(500).entity("Error al avanzar: " + e.getMessage()).build();
        }
    }
    
       
}
