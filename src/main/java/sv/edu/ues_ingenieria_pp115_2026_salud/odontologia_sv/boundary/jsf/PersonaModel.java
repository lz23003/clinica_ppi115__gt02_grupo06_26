package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ClinicaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DocumentoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.MedioContactoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.PersonaRolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoDocumentoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.TipoMedioContactoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Clinica;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Documento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.MedioContacto;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Persona;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.PersonaRol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoDocumento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.TipoMedioContacto;

@Named
@ViewScoped
public class PersonaModel extends DefaultModel<Persona> implements Serializable {

    @Inject
    private PersonaDAO personaDao;
    @Inject
    private DocumentoDAO documentoDao;
    @Inject
    private MedioContactoDAO medioContactoDao;
    @Inject
    private PersonaRolDAO personaRolDao;
    @Inject
    private TipoDocumentoDAO tipoDocumentoDao;
    @Inject
    private TipoMedioContactoDAO tipoMedioContactoDao;
    @Inject
    private RolDAO rolDao;
    @Inject
    private ClinicaDAO clinicaDao;

    private Documento documentoRegistro;
    private ESTADO_CRUD documentoEstado = ESTADO_CRUD.NADA;
    private List<Documento> documentosAsignados = new ArrayList<>();
    private List<TipoDocumento> tiposDocumentoDisponibles;

    private MedioContacto medioContactoRegistro;
    private ESTADO_CRUD medioContactoEstado = ESTADO_CRUD.NADA;
    private List<MedioContacto> mediosContactoAsignados = new ArrayList<>();
    private List<TipoMedioContacto> tiposMedioContactoDisponibles;

    private PersonaRol personaRolRegistro;
    private ESTADO_CRUD personaRolEstado = ESTADO_CRUD.NADA;
    private List<PersonaRol> rolesAsignados = new ArrayList<>();
    private List<Rol> rolesDisponibles;
    private List<Clinica> clinicasDisponibles;
    private Clinica clinicaSeleccionada;

    private int activa = 0;

    public void onTabChange(TabChangeEvent event) {
        this.activa = event.getIndex();
    }

    @Override
    protected Object getId(Persona object) {
        return object != null ? object.getIdPersona() : null;
    }

    @Override
    protected DefaultDAO<Persona> getDao() {
        return personaDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Persona crearEntidad() {
        Persona p = new Persona();
        p.setIdPersona(UUID.randomUUID());
        p.setFechaCreacion(new Date());
        return p;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.Persona");
    }

  

    @Override
    public void onRowSelect(SelectEvent<Persona> event) {
        super.onRowSelect(event);
        if (event != null && event.getObject() != null) {
            UUID idPersona = event.getObject().getIdPersona();
            documentosAsignados = documentoDao.buscarPorPersona(idPersona);
            mediosContactoAsignados = medioContactoDao.buscarPorPersona(idPersona);
            rolesAsignados = personaRolDao.buscarPorPersona(idPersona);

            this.activa = 0;
        }
    }

    public void onDocumentoSelect(SelectEvent<Documento> event) {
        if (event != null && event.getObject() != null) {
            this.documentoRegistro = event.getObject();
            this.documentoEstado = ESTADO_CRUD.MODIFICAR;
        }
    }
    
    public void onMedioContactoSelect(SelectEvent<MedioContacto> event) {
    if (event != null && event.getObject() != null) {
        this.medioContactoRegistro = event.getObject();
        this.medioContactoEstado = ESTADO_CRUD.MODIFICAR;
    }
}


    

    public void btnNuevoDocumentoHandler(ActionEvent event) {
        Documento d = new Documento();
        d.setIdDocumento(UUID.randomUUID());
        d.setIdPersona(registro);
        this.documentoRegistro = d;
        this.documentoEstado = ESTADO_CRUD.CREAR;
    }

    @Override
    public void btnNuevoHandler(ActionEvent event) {
        super.btnNuevoHandler(event);
        resetSubEstados();
        this.activa = 0;
    }

    private void resetSubEstados() {
        this.documentoRegistro = null;
        this.documentoEstado = ESTADO_CRUD.NADA;
        this.documentosAsignados = new ArrayList<>();

        this.medioContactoRegistro = null;
        this.medioContactoEstado = ESTADO_CRUD.NADA;
        this.mediosContactoAsignados = new ArrayList<>();

        this.personaRolRegistro = null;
        this.personaRolEstado = ESTADO_CRUD.NADA;
        this.rolesAsignados = new ArrayList<>();

        this.clinicaSeleccionada = null;
    }

    @Override
    public void btnCancelarHandler(ActionEvent event) {
        // Resetear el maestro (estado = NADA, registro = null)
        super.btnCancelarHandler(event);

        resetSubEstados();
        this.activa = 0;
    }

    public void btnGuardarDocumentoHandler(ActionEvent event) {
        try {
            // Validar formato contra la regex del TipoDocumento
            if (!validarDocumentoContraRegex()) {
                return;
            }

            if (documentoEstado == ESTADO_CRUD.CREAR) {
                documentoDao.crear(documentoRegistro);
                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.creado"),
                        FacesMessage.SEVERITY_INFO);
            } else if (documentoEstado == ESTADO_CRUD.MODIFICAR) {
                documentoDao.modificar(documentoRegistro);
                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.actualizado"),
                        FacesMessage.SEVERITY_INFO);
            }
            // Refrescar la lista
            documentosAsignados = documentoDao.buscarPorPersona(registro.getIdPersona());
            documentoRegistro = null;
            documentoEstado = ESTADO_CRUD.NADA;
        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"),
                    getText("model.crud.error.guardar"),
                    FacesMessage.SEVERITY_ERROR);
            ex.printStackTrace();
        }
    }

    public void btnCancelarDocumentoHandler(ActionEvent event) {
        documentoRegistro = null;
        documentoEstado = ESTADO_CRUD.NADA;
    }

    private boolean validarDocumentoContraRegex() {
        if (documentoRegistro == null
                || documentoRegistro.getIdTipoDocumento() == null
                || documentoRegistro.getValor() == null) {
            enviarMensaje(getText("model.crud.advertencia"),
                    "Debe seleccionar un tipo y escribir un valor.",
                    FacesMessage.SEVERITY_WARN);
            return false;
        }

        String regex = documentoRegistro.getIdTipoDocumento().getExpresionRegular();
        String valor = documentoRegistro.getValor().trim();

        if (regex != null && !regex.isEmpty()) {
            try {
                if (!Pattern.matches(regex, valor)) {
                    enviarMensaje(getText("model.crud.error"),
                            "El valor no es valido. El valor no cumple la validacion de "
                            + documentoRegistro.getIdTipoDocumento().getNombre()
                            + ": \"" + regex + "\"",
                            FacesMessage.SEVERITY_ERROR);
                    return false;
                }
            } catch (Exception ex) {
                enviarMensaje(getText("model.crud.error"),
                        "Expresión regular inválida en el tipo de documento.",
                        FacesMessage.SEVERITY_ERROR);
                return false;
            }
        }
        return true;
    }

    public void btnNuevoMedioContactoHandler(ActionEvent event) {
        MedioContacto mc = new MedioContacto();
        mc.setIdMedioContacto(UUID.randomUUID());
        mc.setFechaCreacion(new Date());
        mc.setIdPersona(registro);
        this.medioContactoRegistro = mc;
        this.medioContactoEstado = ESTADO_CRUD.CREAR;
    }

    public void btnGuardarMedioContactoHandler(ActionEvent event) {
        try {
            if (!validarMedioContactoContraRegex()) {
                return;
            }

            if (medioContactoEstado == ESTADO_CRUD.CREAR) {
                medioContactoDao.crear(medioContactoRegistro);
                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.creado"),
                        FacesMessage.SEVERITY_INFO);
            } else if (medioContactoEstado == ESTADO_CRUD.MODIFICAR) {
                medioContactoDao.modificar(medioContactoRegistro);
                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.actualizado"),
                        FacesMessage.SEVERITY_INFO);
            }
            mediosContactoAsignados = medioContactoDao.buscarPorPersona(registro.getIdPersona());
            medioContactoRegistro = null;
            medioContactoEstado = ESTADO_CRUD.NADA;
        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"),
                    getText("model.crud.error.guardar"),
                    FacesMessage.SEVERITY_ERROR);
            ex.printStackTrace();
        }
    }

    public void btnCancelarMedioContactoHandler(ActionEvent event) {
        medioContactoRegistro = null;
        medioContactoEstado = ESTADO_CRUD.NADA;
    }

    private boolean validarMedioContactoContraRegex() {
        if (medioContactoRegistro == null
                || medioContactoRegistro.getIdTipoMedioContacto() == null
                || medioContactoRegistro.getValor() == null) {
            enviarMensaje(getText("model.crud.advertencia"),
                    "Debe seleccionar un tipo y escribir un valor.",
                    FacesMessage.SEVERITY_WARN);
            return false;
        }

        String regex = medioContactoRegistro.getIdTipoMedioContacto().getExpresionRegular();
        String valor = medioContactoRegistro.getValor().trim();

        if (regex != null && !regex.isEmpty()) {
            try {
                if (!Pattern.matches(regex, valor)) {
                    enviarMensaje(getText("model.crud.error"),
                            "El valor no es valido. El valor no cumple la validacion de "
                            + medioContactoRegistro.getIdTipoMedioContacto().getNombre()
                            + ": \"" + regex + "\"",
                            FacesMessage.SEVERITY_ERROR);
                    return false;
                }
            } catch (Exception ex) {
                enviarMensaje(getText("model.crud.error"),
                        "Expresión regular inválida en el tipo de medio de contacto.",
                        FacesMessage.SEVERITY_ERROR);
                return false;
            }
        }
        return true;
    }

    public void btnNuevoRolHandler(ActionEvent event) {
        if (clinicaSeleccionada == null) {
            enviarMensaje(getText("model.crud.advertencia"),
                    "Debe seleccionar una clínica primero.",
                    FacesMessage.SEVERITY_WARN);
            return;
        }

        PersonaRol pr = new PersonaRol();
        pr.setIdPersonaRol(UUID.randomUUID());
        pr.setFechaCreacion(new Date());
        pr.setIdPersona(registro);
        pr.setIdClinica(clinicaSeleccionada);
        this.personaRolRegistro = pr;
        this.personaRolEstado = ESTADO_CRUD.CREAR;

    }

    public void btnGuardarRolHandler(ActionEvent event) {
        try {
            if (personaRolEstado == ESTADO_CRUD.CREAR) {
                personaRolDao.crear(personaRolRegistro);
                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.creado"),
                        FacesMessage.SEVERITY_INFO);
            } else if (personaRolEstado == ESTADO_CRUD.MODIFICAR) {
                personaRolDao.modificar(personaRolRegistro);
                enviarMensaje(getText("model.crud.exito"),
                        getText("model.crud.actualizado"),
                        FacesMessage.SEVERITY_INFO);
            }
            rolesAsignados = personaRolDao.buscarPorPersona(registro.getIdPersona());
            personaRolRegistro = null;
            personaRolEstado = ESTADO_CRUD.NADA;
        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"),
                    getText("model.crud.error.guardar"),
                    FacesMessage.SEVERITY_ERROR);
            ex.printStackTrace();
        }
    }

    public void btnCancelarRolHandler(ActionEvent event) {
        personaRolRegistro = null;
        personaRolEstado = ESTADO_CRUD.NADA;
    }

    public List<TipoDocumento> completarTipoDocumento(String query) {
        return tipoDocumentoDao.findByNombreLikeActivo(query, 0, 10);
    }

    public List<TipoMedioContacto> completarTipoMedioContacto(String query) {
        return tipoMedioContactoDao.findByNombreLikeActivo(query, 0, 10);
    }

    public List<Rol> completarRol(String query) {
        return rolDao.findByNombreLikeActivo(query, 0, 10);
    }

    public List<Clinica> completarClinica(String query) {
        return clinicaDao.findByNombreLikeActiva(query, 0, 10);
    }

    public Documento getDocumentoRegistro() {
        return documentoRegistro;
    }

    public void setDocumentoRegistro(Documento d) {
        this.documentoRegistro = d;
    }

    public ESTADO_CRUD getDocumentoEstado() {
        return documentoEstado;
    }

    public void setDocumentoEstado(ESTADO_CRUD e) {
        this.documentoEstado = e;
    }

    public List<Documento> getDocumentosAsignados() {
        return documentosAsignados;
    }

    public void setDocumentosAsignados(List<Documento> l) {
        this.documentosAsignados = l;
    }

    public List<TipoDocumento> getTiposDocumentoDisponibles() {
        return tiposDocumentoDisponibles;
    }

    public MedioContacto getMedioContactoRegistro() {
        return medioContactoRegistro;
    }

    public void setMedioContactoRegistro(MedioContacto mc) {
        this.medioContactoRegistro = mc;
    }

    public ESTADO_CRUD getMedioContactoEstado() {
        return medioContactoEstado;
    }

    public void setMedioContactoEstado(ESTADO_CRUD e) {
        this.medioContactoEstado = e;
    }

    public List<MedioContacto> getMediosContactoAsignados() {
        return mediosContactoAsignados;
    }

    public void setMediosContactoAsignados(List<MedioContacto> l) {
        this.mediosContactoAsignados = l;
    }

    public List<TipoMedioContacto> getTiposMedioContactoDisponibles() {
        return tiposMedioContactoDisponibles;
    }

    public PersonaRol getPersonaRolRegistro() {
        return personaRolRegistro;
    }

    public void setPersonaRolRegistro(PersonaRol pr) {
        this.personaRolRegistro = pr;
    }

    public ESTADO_CRUD getPersonaRolEstado() {
        return personaRolEstado;
    }

    public void setPersonaRolEstado(ESTADO_CRUD e) {
        this.personaRolEstado = e;
    }

    public List<PersonaRol> getRolesAsignados() {
        return rolesAsignados;
    }

    public void setRolesAsignados(List<PersonaRol> l) {
        this.rolesAsignados = l;
    }

    public List<Rol> getRolesDisponibles() {
        return rolesDisponibles;
    }

    public List<Clinica> getClinicasDisponibles() {
        return clinicasDisponibles;
    }

    public Clinica getClinicaSeleccionada() {
        return clinicaSeleccionada;
    }

    public void setClinicaSeleccionada(Clinica c) {
        this.clinicaSeleccionada = c;
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }
}
