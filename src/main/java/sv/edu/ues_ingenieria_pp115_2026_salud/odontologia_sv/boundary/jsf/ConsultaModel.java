package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.primefaces.event.SelectEvent;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoPasoService;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Consulta;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ConsultaProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;

@Named
@ViewScoped
public class ConsultaModel extends DefaultModel<Consulta> implements Serializable {

    @Inject
    private SesionUsuario sesionUsuario;

    @Inject
    private ConsultaService consultaService;

    @Inject
    private PersonaRolService personaRolService;

    @Inject
    private ClinicaService clinicaService;

    @Inject
    private ProcedimientoService procedimientoService;

    @Inject
    private ConsultaProcedimientoService consultaProcedimientoService;

    @Inject
    private ProcedimientoPasoService procedimientoPasoService;

    @Inject
    private ConsultaProcedimientoPasoService consultaProcedimientoPasoService;

    private Date fechaFiltroInicio;
    private Date fechaFiltroFin;
    private Clinica clinicaFiltro;
    private ConsultaProcedimiento procedimientoSeleccionado;

    private String criterioBusqueda = "nombre";
    private Clinica clinicaTrabajo;

    private ConsultaProcedimiento nuevoConsultaProcedimiento;
    private Procedimiento procedimientoParaAsignar;
    private List<Consulta> modeloList;
    private List<ConsultaProcedimiento> procedimientosDeConsulta;

    @PostConstruct
    public void inicializar() {
        String nombreClinicaSesion = sesionUsuario.getClinicaUsuario();
        if (nombreClinicaSesion != null && nombreClinicaSesion.trim().length() >= 3) {
            clinicaTrabajo = clinicaService.findByNombreLikeActiva(nombreClinicaSesion, 0, 10).stream()
                    .filter(c -> c.getNombre() != null && c.getNombre().equalsIgnoreCase(nombreClinicaSesion))
                    .findFirst()
                    .orElse(null);
            clinicaFiltro = clinicaTrabajo;
        }

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        fechaFiltroInicio = cal.getTime();

        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        fechaFiltroFin = cal.getTime();

        filtrarConsultas();
    }

    public List<PersonaRol> completarPersonaRol(String query) {
        if (clinicaTrabajo == null) {
            return List.of();
        }
        return personaRolService.buscarPorClinicaYNombre(clinicaTrabajo.getIdClinica(), query, 0, 10);
    }

    public List<Procedimiento> completarProcedimiento(String query) {
        return procedimientoService.buscarActivosPorNombre(query, 0, 10);
    }

    public void filtrarConsultas() {
        if (fechaFiltroInicio != null && fechaFiltroFin != null) {
            List<Consulta> todas = consultaService.buscarPorRangoFechas(fechaFiltroInicio, fechaFiltroFin);
            if (clinicaFiltro != null) {
                modeloList = todas.stream().filter(c -> c.getIdPersonaRol() != null
                        && c.getIdPersonaRol().getIdClinica() != null
                        && c.getIdPersonaRol().getIdClinica().getIdClinica().equals(clinicaFiltro.getIdClinica()))
                        .collect(Collectors.toList());
            } else {
                modeloList = todas;
            }
        } else {
            List<Consulta> todas = consultaService.findRange(0, 1000);
            if (clinicaFiltro != null) {
                modeloList = todas.stream().filter(c -> c.getIdPersonaRol() != null
                        && c.getIdPersonaRol().getIdClinica() != null
                        && c.getIdPersonaRol().getIdClinica().getIdClinica().equals(clinicaFiltro.getIdClinica()))
                        .collect(Collectors.toList());
            } else {
                modeloList = todas;
            }
        }
    }

    @Override
    public void onRowSelect(SelectEvent<Consulta> event) {
        super.onRowSelect(event);
        cargarProcedimientos();
    }

    public void cargarProcedimientos() {
        if (registro != null && registro.getIdConsulta() != null) {
            procedimientosDeConsulta = consultaProcedimientoService.buscarPorConsulta(registro.getIdConsulta());
        }
    }

    public void prepararNuevoProcedimiento() {
        nuevoConsultaProcedimiento = new ConsultaProcedimiento();
        nuevoConsultaProcedimiento.setIdConsultaProcedimiento(UUID.randomUUID());
        nuevoConsultaProcedimiento.setFechaInicio(new Date());
        procedimientoParaAsignar = null;
        if (registro != null) {
            nuevoConsultaProcedimiento.setIdConsulta(registro);
        }
    }

    public void guardarProcedimiento() {
        if (nuevoConsultaProcedimiento != null && procedimientoParaAsignar != null) {
            nuevoConsultaProcedimiento.setIdProcedimiento(procedimientoParaAsignar.getIdProcedimiento());
        }
        if (nuevoConsultaProcedimiento != null && nuevoConsultaProcedimiento.getIdProcedimiento() != null) {

            ConsultaProcedimiento existente = consultaProcedimientoService.buscarPorId(nuevoConsultaProcedimiento.getIdConsultaProcedimiento());
            boolean esNuevo = (existente == null);

            if (esNuevo) {
                consultaProcedimientoService.crear(nuevoConsultaProcedimiento);

                List<ProcedimientoPaso> pasos = procedimientoPasoService.buscarPorProcedimiento(nuevoConsultaProcedimiento.getIdProcedimiento());
                if (pasos != null && !pasos.isEmpty()) {
                    ProcedimientoPaso pasoInicial = pasos.get(0);
                    ConsultaProcedimientoPaso cpp = new ConsultaProcedimientoPaso();
                    cpp.setIdConsultaProcedimientoPaso(UUID.randomUUID());
                    cpp.setIdConsultaProcedimiento(nuevoConsultaProcedimiento);
                    cpp.setIdProcedimientoPaso(pasoInicial);
                    cpp.setFechaInicio(new Date());
                    cpp.setEstado("CREADO");

                    if (pasoInicial.getIdRol() != null && clinicaTrabajo != null) {
                        List<PersonaRol> personal = personaRolService.buscarPorClinica(clinicaTrabajo.getIdClinica());

                        List<PersonaRol> candidatos = personal.stream()
                                .filter(pr -> pr.getIdRol() != null && pr.getIdRol().getIdRol().equals(pasoInicial.getIdRol().getIdRol()))
                                .collect(Collectors.toList());

                        if (!candidatos.isEmpty()) {
                            cpp.setIdPersonaRol(candidatos.get(0));
                        }
                    }

                    consultaProcedimientoPasoService.crear(cpp);

                    if (nuevoConsultaProcedimiento.getConsultaProcedimientoPasoList() == null) {
                        nuevoConsultaProcedimiento.setConsultaProcedimientoPasoList(new java.util.ArrayList<>());
                    }
                    nuevoConsultaProcedimiento.getConsultaProcedimientoPasoList().add(cpp);
                   
                    consultaProcedimientoService.modificar(nuevoConsultaProcedimiento);
                }
            } else {
                consultaProcedimientoService.modificar(nuevoConsultaProcedimiento);
            }

            registro = consultaService.buscarPorId(registro.getIdConsulta());
            cargarProcedimientos();

            if (esNuevo && procedimientosDeConsulta != null) {
                for (ConsultaProcedimiento cp : procedimientosDeConsulta) {
                    if (cp.getIdConsultaProcedimiento().equals(nuevoConsultaProcedimiento.getIdConsultaProcedimiento())) {
                        cp.setConsultaProcedimientoPasoList(nuevoConsultaProcedimiento.getConsultaProcedimientoPasoList());
                    }
                }
            }

            nuevoConsultaProcedimiento = null;
            procedimientoSeleccionado = null;
            procedimientoParaAsignar = null;
        }
    }

    public void prepararModificarProcedimiento(ConsultaProcedimiento cp) {
        this.nuevoConsultaProcedimiento = cp;
        this.procedimientoParaAsignar = cp == null || cp.getIdProcedimiento() == null
                ? null : procedimientoService.buscarPorId(cp.getIdProcedimiento());
    }

    public void eliminarProcedimiento(ConsultaProcedimiento cp) {
        if (cp != null) {
            try {
                List<ConsultaProcedimientoPaso> pasosDependientes = consultaProcedimientoPasoService.buscarPorConsultaProcedimiento(cp.getIdConsultaProcedimiento());
                if (pasosDependientes != null && !pasosDependientes.isEmpty()) {
                    for (ConsultaProcedimientoPaso paso : pasosDependientes) {
                        consultaProcedimientoPasoService.eliminar(paso);
                    }
                }

                consultaProcedimientoService.eliminar(cp);

                cargarProcedimientos();
                procedimientoSeleccionado = null;
                nuevoConsultaProcedimiento = null;

                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.eliminado"),
                        jakarta.faces.application.FacesMessage.SEVERITY_INFO);
            } catch (Exception ex) {
                enviarMensaje(getText("model.crud.error"),
                        getText("model.crud.error.eliminar"),
                        jakarta.faces.application.FacesMessage.SEVERITY_ERROR);
                ex.printStackTrace();
            }
        }
    }

    public void cancelarProcedimiento() {
        nuevoConsultaProcedimiento = null;
        procedimientoParaAsignar = null;
    }

    public String obtenerEtiquetaPersona(PersonaRol pr) {
        if (pr == null || pr.getIdPersona() == null) {
            return "";
        }
        if ("documento".equalsIgnoreCase(criterioBusqueda)) {
            String doc = getText("app.desconocido");
            if (pr.getIdPersona().getDocumentoList() != null && !pr.getIdPersona().getDocumentoList().isEmpty()) {
                doc = pr.getIdPersona().getDocumentoList().get(0).getValor();
            }
            return pr.getIdPersona().getNombres() + " " + pr.getIdPersona().getApellidos() + " - " + doc;
        } else if ("fecha".equalsIgnoreCase(criterioBusqueda)) {
            String fechaStr = pr.getIdPersona().getFechaNacimiento() != null
                    ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(pr.getIdPersona().getFechaNacimiento()) : getText("app.desconocido");
            return pr.getIdPersona().getNombres() + " " + pr.getIdPersona().getApellidos() + " - " + fechaStr;
        }
        return pr.getIdPersona().getNombres() + " " + pr.getIdPersona().getApellidos();
    }

    public String obtenerNombreProcedimiento(UUID idProcedimiento) {
        if (idProcedimiento == null) {
            return "";
        }
        Procedimiento procedimiento = procedimientoService.buscarPorId(idProcedimiento);
        return procedimiento == null ? getText("app.desconocido") : procedimiento.getNombre();
    }

    public void onProcedimientoSelect(SelectEvent<ConsultaProcedimiento> event) {
        this.nuevoConsultaProcedimiento = event.getObject();
        this.procedimientoParaAsignar = event.getObject().getIdProcedimiento() == null
                ? null : procedimientoService.buscarPorId(event.getObject().getIdProcedimiento());
    }

    @Override
    public void btnCrearHandler(jakarta.faces.event.ActionEvent event) {
        super.btnCrearHandler(event);
        filtrarConsultas();
    }

    @Override
    public void btnEliminarHandler(Consulta registroSeleccionado) {
        super.btnEliminarHandler(registroSeleccionado);
        filtrarConsultas();
    }

    @Override
    protected Object getId(Consulta object) {
        return object != null ? object.getIdConsulta() : null;
    }

    @Override
    protected DefaultService<Consulta> getService() {
        return consultaService;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Consulta crearEntidad() {
        Consulta consulta = new Consulta();
        consulta.setIdConsulta(UUID.randomUUID());
        consulta.setFechaInicio(new Date());
        return consulta;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.consulta");
    }

    public Date getFechaFiltroInicio() {
        return fechaFiltroInicio;
    }

    public void setFechaFiltroInicio(Date fechaFiltroInicio) {
        this.fechaFiltroInicio = fechaFiltroInicio;
    }

    public Date getFechaFiltroFin() {
        return fechaFiltroFin;
    }

    public void setFechaFiltroFin(Date fechaFiltroFin) {
        this.fechaFiltroFin = fechaFiltroFin;
    }

    public Clinica getClinicaFiltro() {
        return clinicaFiltro;
    }

    public void setClinicaFiltro(Clinica clinicaFiltro) {
        this.clinicaFiltro = clinicaFiltro;
    }

    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }

    public Procedimiento getProcedimientoParaAsignar() {
        return procedimientoParaAsignar;
    }

    public void setProcedimientoParaAsignar(Procedimiento procedimientoParaAsignar) {
        this.procedimientoParaAsignar = procedimientoParaAsignar;
    }

    public ConsultaProcedimiento getNuevoConsultaProcedimiento() {
        return nuevoConsultaProcedimiento;
    }

    public void setNuevoConsultaProcedimiento(ConsultaProcedimiento nuevoConsultaProcedimiento) {
        this.nuevoConsultaProcedimiento = nuevoConsultaProcedimiento;
    }

    public List<Consulta> getModeloList() {
        return modeloList;
    }

    public void setModeloList(List<Consulta> modeloList) {
        this.modeloList = modeloList;
    }

    public List<ConsultaProcedimiento> getProcedimientosDeConsulta() {
        return procedimientosDeConsulta;
    }

    public ConsultaProcedimiento getProcedimientoSeleccionado() {
        return procedimientoSeleccionado;
    }

    public void setProcedimientoSeleccionado(ConsultaProcedimiento procedimientoSeleccionado) {
        this.procedimientoSeleccionado = procedimientoSeleccionado;
    }

    
}
