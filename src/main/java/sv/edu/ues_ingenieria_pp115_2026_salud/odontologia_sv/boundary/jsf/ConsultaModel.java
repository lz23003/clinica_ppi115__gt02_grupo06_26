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
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ConsultaProcedimientoPasoDAO;
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
    private ConsultaDAO consultaDao;

    @Inject
    private PersonaRolDAO personaRolDao;

    @Inject
    private ClinicaDAO clinicaDao;

    @Inject
    private ProcedimientoDAO procedimientoDao;

    @Inject
    private ConsultaProcedimientoDAO consultaProcedimientoDao;

    @Inject
    private ProcedimientoPasoDAO procedimientoPasoDao;

    @Inject
    private ConsultaProcedimientoPasoDAO consultaProcedimientoPasoDao;

    private List<PersonaRol> listaPersonaRoles;
    private List<Clinica> listaClinicas;
    private List<Procedimiento> listaProcedimientos;

    private Date fechaFiltroInicio;
    private Date fechaFiltroFin;
    private Clinica clinicaFiltro;
    private ConsultaProcedimiento procedimientoSeleccionado;

    private String criterioBusqueda = "nombre";
    private Clinica clinicaTrabajo;

    private ConsultaProcedimiento nuevoConsultaProcedimiento;
    private List<Consulta> modeloList;
    private List<ConsultaProcedimiento> procedimientosDeConsulta;

    @PostConstruct
    public void inicializar() {
        listaClinicas = clinicaDao.findRange(0, 100);
        listaProcedimientos = procedimientoDao.listarActivos();

        if (listaClinicas != null && !listaClinicas.isEmpty()) {
            String nombreClinicaSesion = sesionUsuario.getClinicaUsuario();
            clinicaTrabajo = listaClinicas.stream()
                    .filter(c -> c.getNombre() != null && c.getNombre().equalsIgnoreCase(nombreClinicaSesion))
                    .findFirst()
                    .orElse(listaClinicas.get(0));
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

        cargarPersonasDeClinica();
        filtrarConsultas();
    }

    public void cargarPersonasDeClinica() {
        if (clinicaTrabajo != null) {
            listaPersonaRoles = personaRolDao.buscarPorClinica(clinicaTrabajo.getIdClinica());
        } else {
            listaPersonaRoles = personaRolDao.findRange(0, 1000);
        }
    }

    public void filtrarConsultas() {
        if (fechaFiltroInicio != null && fechaFiltroFin != null) {
            List<Consulta> todas = consultaDao.buscarPorRangoFechas(fechaFiltroInicio, fechaFiltroFin);
            if (clinicaFiltro != null) {
                modeloList = todas.stream().filter(c -> c.getIdPersonaRol() != null
                        && c.getIdPersonaRol().getIdClinica() != null
                        && c.getIdPersonaRol().getIdClinica().getIdClinica().equals(clinicaFiltro.getIdClinica()))
                        .collect(Collectors.toList());
            } else {
                modeloList = todas;
            }
        } else {
            List<Consulta> todas = consultaDao.findRange(0, 1000);
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
            procedimientosDeConsulta = consultaProcedimientoDao.buscarPorConsulta(registro.getIdConsulta());
        }
    }

    public void prepararNuevoProcedimiento() {
        nuevoConsultaProcedimiento = new ConsultaProcedimiento();
        nuevoConsultaProcedimiento.setIdConsultaProcedimiento(UUID.randomUUID());
        nuevoConsultaProcedimiento.setFechaInicio(new Date());
        if (registro != null) {
            nuevoConsultaProcedimiento.setIdConsulta(registro);
        }
    }

    public void guardarProcedimiento() {
        if (nuevoConsultaProcedimiento != null && nuevoConsultaProcedimiento.getIdProcedimiento() != null) {

            ConsultaProcedimiento existente = consultaProcedimientoDao.buscarPorId(nuevoConsultaProcedimiento.getIdConsultaProcedimiento());
            boolean esNuevo = (existente == null);

            if (esNuevo) {
                consultaProcedimientoDao.crear(nuevoConsultaProcedimiento);

                List<ProcedimientoPaso> pasos = procedimientoPasoDao.buscarPorProcedimiento(nuevoConsultaProcedimiento.getIdProcedimiento());
                if (pasos != null && !pasos.isEmpty()) {
                    ProcedimientoPaso pasoInicial = pasos.get(0);
                    ConsultaProcedimientoPaso cpp = new ConsultaProcedimientoPaso();
                    cpp.setIdConsultaProcedimientoPaso(UUID.randomUUID());
                    cpp.setIdConsultaProcedimiento(nuevoConsultaProcedimiento);
                    cpp.setIdProcedimientoPaso(pasoInicial);
                    cpp.setFechaInicio(new Date());
                    cpp.setEstado("CREADO");

                    if (pasoInicial.getIdRol() != null && clinicaTrabajo != null) {
                        List<PersonaRol> personal = personaRolDao.buscarPorClinica(clinicaTrabajo.getIdClinica());

                        List<PersonaRol> candidatos = personal.stream()
                                .filter(pr -> pr.getIdRol() != null && pr.getIdRol().getIdRol().equals(pasoInicial.getIdRol().getIdRol()))
                                .collect(Collectors.toList());

                        if (!candidatos.isEmpty()) {
                            cpp.setIdPersonaRol(candidatos.get(0));
                        }
                    }

                    consultaProcedimientoPasoDao.crear(cpp);

                    if (nuevoConsultaProcedimiento.getConsultaProcedimientoPasoList() == null) {
                        nuevoConsultaProcedimiento.setConsultaProcedimientoPasoList(new java.util.ArrayList<>());
                    }
                    nuevoConsultaProcedimiento.getConsultaProcedimientoPasoList().add(cpp);
                   
                    consultaProcedimientoDao.modificar(nuevoConsultaProcedimiento);
                }
            } else {
                consultaProcedimientoDao.modificar(nuevoConsultaProcedimiento);
            }

            registro = consultaDao.buscarPorId(registro.getIdConsulta());
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
        }
    }

    public void prepararModificarProcedimiento(ConsultaProcedimiento cp) {
        this.nuevoConsultaProcedimiento = cp;
    }

    public void eliminarProcedimiento(ConsultaProcedimiento cp) {
        if (cp != null) {
            try {
                List<ConsultaProcedimientoPaso> pasosDependientes = consultaProcedimientoPasoDao.buscarPorConsultaProcedimiento(cp.getIdConsultaProcedimiento());
                if (pasosDependientes != null && !pasosDependientes.isEmpty()) {
                    for (ConsultaProcedimientoPaso paso : pasosDependientes) {
                        consultaProcedimientoPasoDao.eliminar(paso);
                    }
                }

                consultaProcedimientoDao.eliminar(cp);

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
        for (Procedimiento p : listaProcedimientos) {
            if (p.getIdProcedimiento().equals(idProcedimiento)) {
                return p.getNombre();
            }
        }
        return getText("app.desconocido");
    }

    public void onProcedimientoSelect(SelectEvent<ConsultaProcedimiento> event) {
        this.nuevoConsultaProcedimiento = event.getObject();
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
    protected DefaultDAO<Consulta> getDao() {
        return consultaDao;
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

    public List<PersonaRol> getListaPersonaRoles() {
        return listaPersonaRoles;
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

    public List<Clinica> getListaClinicas() {
        return listaClinicas;
    }

    public List<Procedimiento> getListaProcedimientos() {
        return listaProcedimientos;
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
