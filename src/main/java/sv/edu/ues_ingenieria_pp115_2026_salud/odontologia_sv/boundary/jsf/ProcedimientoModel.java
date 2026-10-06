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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoSecuenciaDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ProcedimientoPasoExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.RolDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.ExamenDAO;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Procedimiento;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPaso;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoSecuencia;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.ProcedimientoPasoExamen;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Rol;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity.Examen;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.event.NodeSelectEvent;

@Named
@ViewScoped
public class ProcedimientoModel extends DefaultModel<Procedimiento> implements Serializable {

    @Inject
    private ProcedimientoDAO procedimientoDao;
    @Inject
    private ProcedimientoPasoDAO pasoDao;
    @Inject
    private ProcedimientoPasoSecuenciaDAO secuenciaDao;
    @Inject
    private ProcedimientoPasoExamenDAO pasoExamenDao;
    @Inject
    private RolDAO rolDao;
    @Inject
    private ExamenDAO examenDao;

    private TreeNode rootNode;
    private TreeNode selectedNode;

    private ProcedimientoPaso pasoRegistro;
    private ESTADO_CRUD pasoEstado = ESTADO_CRUD.NADA;
    private List<ProcedimientoPaso> pasosAsignados = new ArrayList<>();
    private UUID idPasoDependencia;
    private List<Rol> rolesDisponibles;

    private ProcedimientoPasoExamen examenRegistro;
    private ESTADO_CRUD examenEstado = ESTADO_CRUD.NADA;
    private List<ProcedimientoPasoExamen> examenesAsignados = new ArrayList<>();
    private List<Examen> examenesDisponibles;

    private int activa = 0;

    @PostConstruct
    public void init() {
        super.inicializador();
        rolesDisponibles = rolDao.listarActivos();
        examenesDisponibles = examenDao.listarActivos();
    }

    public void onTabChange(TabChangeEvent event) {
        this.activa = event.getIndex();
    }

    @Override
    public void onRowSelect(SelectEvent<Procedimiento> event) {
        super.onRowSelect(event);
        if (event != null && event.getObject() != null) {
            UUID idProc = event.getObject().getIdProcedimiento();
            pasosAsignados = pasoDao.buscarPorProcedimiento(idProc);
            construirArbolPasos(idProc);
            this.activa = 0;
            resetSubEstados();
        }
    }

    @Override
    public void btnNuevoHandler(ActionEvent event) {
        super.btnNuevoHandler(event);
        resetSubEstados();
        this.activa = 0;
    }

    @Override
    public void btnCancelarHandler(ActionEvent event) {
        super.btnCancelarHandler(event);
        resetSubEstados();
        this.activa = 0;
    }

    private void resetSubEstados() {
        pasoRegistro = null;
        pasoEstado = ESTADO_CRUD.NADA;
        pasosAsignados = (registro != null) ? pasoDao.buscarPorProcedimiento(registro.getIdProcedimiento()) : new ArrayList<>();
        if (registro != null) {
            construirArbolPasos(registro.getIdProcedimiento());
        } else {
            rootNode = new DefaultTreeNode(new ProcedimientoPaso(), null);
        }
        idPasoDependencia = null;

        examenRegistro = null;
        examenEstado = ESTADO_CRUD.NADA;
        examenesAsignados = new ArrayList<>();
    }

    private void construirArbolPasos(UUID idProc) {
        rootNode = new DefaultTreeNode(new ProcedimientoPaso(), null);
        List<ProcedimientoPaso> todosLosPasos = pasoDao.buscarPorProcedimiento(idProc);

        List<ProcedimientoPaso> raices = new ArrayList<>();
        for (ProcedimientoPaso p : todosLosPasos) {
            List<ProcedimientoPasoSecuencia> seqs = secuenciaDao.buscarPorPaso(p.getIdProcedimientoPaso());
            boolean tienePadre = seqs.stream().anyMatch(s -> "ANTERIOR".equals(s.getTipoSecuencia()));
            if (!tienePadre) {
                raices.add(p);
            }
        }

        for (ProcedimientoPaso raiz : raices) {
            TreeNode nodoRaiz = new DefaultTreeNode(raiz, rootNode);
            construirHijosRecursivo(nodoRaiz, todosLosPasos);
        }
    }

    private void construirHijosRecursivo(TreeNode nodoPadre, List<ProcedimientoPaso> todosLosPasos) {
        ProcedimientoPaso pasoPadre = (ProcedimientoPaso) nodoPadre.getData();
        if (pasoPadre == null || pasoPadre.getIdProcedimientoPaso() == null) {
            return;
        }

        for (ProcedimientoPaso p : todosLosPasos) {
            List<ProcedimientoPasoSecuencia> seqs = secuenciaDao.buscarPorPaso(p.getIdProcedimientoPaso());
            for (ProcedimientoPasoSecuencia seq : seqs) {
                if ("ANTERIOR".equals(seq.getTipoSecuencia()) && pasoPadre.getIdProcedimientoPaso().equals(seq.getIdProcedimientoPasoReferencia())) {
                    TreeNode nodoHijo = new DefaultTreeNode(p, nodoPadre);
                    construirHijosRecursivo(nodoHijo, todosLosPasos);
                }
            }
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        if (event != null && event.getTreeNode() != null && event.getTreeNode().getData() != null) {
            this.pasoRegistro = (ProcedimientoPaso) event.getTreeNode().getData();
            if (pasoRegistro.getIdProcedimientoPaso() == null) {
                this.pasoRegistro = null;
                this.pasoEstado = ESTADO_CRUD.NADA;
                return;
            }
            this.pasoEstado = ESTADO_CRUD.MODIFICAR;

            this.examenesAsignados = pasoExamenDao.buscarPorPaso(pasoRegistro.getIdProcedimientoPaso());
            this.examenEstado = ESTADO_CRUD.NADA;

            this.idPasoDependencia = null;
            List<ProcedimientoPasoSecuencia> dependencias = secuenciaDao.buscarPorPaso(pasoRegistro.getIdProcedimientoPaso());
            for (ProcedimientoPasoSecuencia seq : dependencias) {
                if ("ANTERIOR".equals(seq.getTipoSecuencia())) {
                    this.idPasoDependencia = seq.getIdProcedimientoPasoReferencia();
                    break;
                }
            }
        }
    }

    public void onPasoSelect(SelectEvent<ProcedimientoPaso> event) {
        this.pasoRegistro = event.getObject();
        this.pasoEstado = ESTADO_CRUD.MODIFICAR;

        this.examenesAsignados = pasoExamenDao.buscarPorPaso(pasoRegistro.getIdProcedimientoPaso());
        this.examenEstado = ESTADO_CRUD.NADA;

        this.idPasoDependencia = null;
        List<ProcedimientoPasoSecuencia> dependencias = secuenciaDao.buscarPorPaso(pasoRegistro.getIdProcedimientoPaso());
        for (ProcedimientoPasoSecuencia seq : dependencias) {
            if ("ANTERIOR".equals(seq.getTipoSecuencia())) {
                this.idPasoDependencia = seq.getIdProcedimientoPasoReferencia();
                break;
            }
        }
    }

    public String getNombrePasoDependencia() {
        if (idPasoDependencia != null && pasosAsignados != null) {
            return pasosAsignados.stream()
                    .filter(p -> p.getIdProcedimientoPaso().equals(idPasoDependencia))
                    .map(ProcedimientoPaso::getNombre)
                    .findFirst()
                    .orElse("");
        }
        return "";
    }

    public List<ProcedimientoPaso> getPasosHijos(UUID idPasoPadre) {
        List<ProcedimientoPaso> hijos = new ArrayList<>();
        if (idPasoPadre == null || pasosAsignados == null) {
            return hijos;
        }
        for (ProcedimientoPaso p : pasosAsignados) {
            List<ProcedimientoPasoSecuencia> seqs = secuenciaDao.buscarPorPaso(p.getIdProcedimientoPaso());
            for (ProcedimientoPasoSecuencia seq : seqs) {
                if ("ANTERIOR".equals(seq.getTipoSecuencia()) && idPasoPadre.equals(seq.getIdProcedimientoPasoReferencia())) {
                    hijos.add(p);
                }
            }
        }
        return hijos;
    }

    public List<ProcedimientoPasoExamen> getExamenesPorPaso(UUID idPaso) {
        if (idPaso == null) {
            return new ArrayList<>();
        }
        return pasoExamenDao.buscarPorPaso(idPaso);
    }

    public void btnNuevoPasoHandler(ActionEvent event) {
        pasoRegistro = new ProcedimientoPaso();
        pasoRegistro.setIdProcedimientoPaso(UUID.randomUUID());
        pasoRegistro.setIdProcedimiento(this.registro);
        pasoRegistro.setIndicaFin(false);

        this.idPasoDependencia = null;
        this.pasoEstado = ESTADO_CRUD.CREAR;
        this.examenesAsignados = new ArrayList<>();
        this.examenEstado = ESTADO_CRUD.NADA;
    }

    public void btnNuevoPasoDependienteHandler(ActionEvent event) {
        if (pasoRegistro != null && pasoRegistro.getIdProcedimientoPaso() != null) {
            UUID dependenciaPrevia = pasoRegistro.getIdProcedimientoPaso();

            pasoRegistro = new ProcedimientoPaso();
            pasoRegistro.setIdProcedimientoPaso(UUID.randomUUID());
            pasoRegistro.setIdProcedimiento(this.registro);
            pasoRegistro.setIndicaFin(false);

            this.idPasoDependencia = dependenciaPrevia;
            this.pasoEstado = ESTADO_CRUD.CREAR;
            this.examenesAsignados = new ArrayList<>();
            this.examenEstado = ESTADO_CRUD.NADA;
        }
    }

    public boolean isTienePasoRaiz() {
        if (pasosAsignados == null || pasosAsignados.isEmpty()) {
            return false;
        }
        for (ProcedimientoPaso p : pasosAsignados) {
            List<ProcedimientoPasoSecuencia> seqs = secuenciaDao.buscarPorPaso(p.getIdProcedimientoPaso());
            boolean tienePadre = seqs.stream().anyMatch(s -> "ANTERIOR".equals(s.getTipoSecuencia()));
            if (!tienePadre) {
                return true;
            }
        }
        return false;
    }

    public boolean isTieneDependientes() {
        if (pasoRegistro != null && pasoRegistro.getIdProcedimientoPaso() != null) {
            return !getPasosHijos(pasoRegistro.getIdProcedimientoPaso()).isEmpty();
        }
        return false;
    }

    public void btnGuardarPasoHandler(ActionEvent event) {
        try {
            if (pasoEstado == ESTADO_CRUD.CREAR) {
                pasoDao.crear(pasoRegistro);
                enviarMensaje(getText("model.crud.exito"), getText("model.crud.creado"), FacesMessage.SEVERITY_INFO);
            } else if (pasoEstado == ESTADO_CRUD.MODIFICAR) {
                pasoDao.modificar(pasoRegistro);
                enviarMensaje(getText("model.crud.exito"), getText("model.crud.actualizado"), FacesMessage.SEVERITY_INFO);
            }

            List<ProcedimientoPasoSecuencia> dependencias = secuenciaDao.buscarPorPaso(pasoRegistro.getIdProcedimientoPaso());
            ProcedimientoPasoSecuencia seqExistente = dependencias.stream().filter(d -> "ANTERIOR".equals(d.getTipoSecuencia())).findFirst().orElse(null);

            if (idPasoDependencia != null) {
                if (seqExistente != null) {
                    seqExistente.setIdProcedimientoPasoReferencia(idPasoDependencia);
                    secuenciaDao.modificar(seqExistente);
                } else {
                    ProcedimientoPasoSecuencia nuevaSeq = new ProcedimientoPasoSecuencia();
                    nuevaSeq.setIdProcedimientoPasoSecuencia(UUID.randomUUID());
                    nuevaSeq.setIdProcedimientoPaso(pasoRegistro);
                    nuevaSeq.setTipoSecuencia("ANTERIOR");
                    nuevaSeq.setIdProcedimientoPasoReferencia(idPasoDependencia);
                    secuenciaDao.crear(nuevaSeq);
                }
            } else if (seqExistente != null) {
                secuenciaDao.eliminar(seqExistente);
            }

            for (ProcedimientoPasoExamen ppe : examenesAsignados) {
                if (ppe.getIdProcedimientoPaso() == null) {
                    ppe.setIdProcedimientoPaso(pasoRegistro);
                }
                if (pasoExamenDao.buscarPorId(ppe.getIdProcedimientoPasoExamen()) == null) {
                    pasoExamenDao.crear(ppe);
                } else {
                    pasoExamenDao.modificar(ppe);
                }
            }

            pasosAsignados = pasoDao.buscarPorProcedimiento(registro.getIdProcedimiento());
            construirArbolPasos(registro.getIdProcedimiento());
            pasoEstado = ESTADO_CRUD.MODIFICAR;

        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"), getText("model.crud.error.guardar"), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void btnEliminarPasoHandler(ActionEvent event) {
        try {
            if (pasoRegistro != null) {
                if (isTieneDependientes()) {
                    enviarMensaje(getText("model.crud.error"), getText("mensaje.error.eliminar.dependientes"), FacesMessage.SEVERITY_ERROR);
                    return;
                }

                for (ProcedimientoPasoSecuencia seq : secuenciaDao.buscarPorPaso(pasoRegistro.getIdProcedimientoPaso())) {
                    secuenciaDao.eliminar(seq);
                }
                for (ProcedimientoPasoExamen ex : pasoExamenDao.buscarPorPaso(pasoRegistro.getIdProcedimientoPaso())) {
                    pasoExamenDao.eliminar(ex);
                }

                pasoDao.eliminar(pasoRegistro);
                enviarMensaje(getText("model.crud.exito"), getText("model.crud.eliminado"), FacesMessage.SEVERITY_INFO);
                resetSubEstados();
            }
        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"), getText("model.crud.error.eliminar"), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void btnCancelarPasoHandler(ActionEvent event) {
        resetSubEstados();
    }

    public void onExamenSelect(SelectEvent<ProcedimientoPasoExamen> event) {
        this.examenRegistro = event.getObject();
        this.examenEstado = ESTADO_CRUD.NADA;
    }

    public void btnNuevoExamenHandler(ActionEvent event) {
        examenRegistro = new ProcedimientoPasoExamen();
        examenRegistro.setIdProcedimientoPasoExamen(UUID.randomUUID());
        examenRegistro.setIdProcedimientoPaso(pasoRegistro);
        examenRegistro.setFechaCreacion(new Date());
        examenRegistro.setActivo(true);
        examenEstado = ESTADO_CRUD.CREAR;
    }

    public void btnGuardarExamenHandler(ActionEvent event) {
        try {
            if (examenRegistro != null && examenRegistro.getIdExamen() != null) {
                if (examenEstado == ESTADO_CRUD.CREAR) {
                    boolean duplicado = examenesAsignados.stream()
                            .anyMatch(e -> e.getIdExamen().getIdExamen().equals(examenRegistro.getIdExamen().getIdExamen()));
                    if (!duplicado) {
                        examenesAsignados.add(examenRegistro);
                    }
                }
                examenRegistro = null;
                examenEstado = ESTADO_CRUD.NADA;
            }
        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"), "Error al registrar examen", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void btnEliminarExamenHandler(ProcedimientoPasoExamen ppe) {
        if (ppe != null) {
            examenesAsignados.remove(ppe);
            try {
                if (pasoExamenDao.buscarPorId(ppe.getIdProcedimientoPasoExamen()) != null) {
                    pasoExamenDao.eliminar(ppe);
                }
            } catch (Exception e) {
            }
            examenRegistro = null;
            examenEstado = ESTADO_CRUD.NADA;
        }
    }

    public void btnCancelarExamenHandler(ActionEvent event) {
        examenRegistro = null;
        examenEstado = ESTADO_CRUD.NADA;
    }

    @Override
    protected Object getId(Procedimiento object) {
        return object != null ? object.getIdProcedimiento() : null;
    }

    @Override
    protected DefaultDAO<Procedimiento> getDao() {
        return procedimientoDao;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Procedimiento crearEntidad() {
        Procedimiento proc = new Procedimiento();
        proc.setIdProcedimiento(UUID.randomUUID());
        proc.setActivo(true);
        return proc;
    }

    @Override
    public String getNombreEntidad() {
        return getText("entidad.procedimiento");
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }

    public ProcedimientoPaso getPasoRegistro() {
        return pasoRegistro;
    }

    public void setPasoRegistro(ProcedimientoPaso pasoRegistro) {
        this.pasoRegistro = pasoRegistro;
    }

    public ESTADO_CRUD getPasoEstado() {
        return pasoEstado;
    }

    public List<ProcedimientoPaso> getPasosAsignados() {
        return pasosAsignados;
    }

    public UUID getIdPasoDependencia() {
        return idPasoDependencia;
    }

    public void setIdPasoDependencia(UUID idPasoDependencia) {
        this.idPasoDependencia = idPasoDependencia;
    }

    public List<Rol> getRolesDisponibles() {
        return rolesDisponibles;
    }

    public ProcedimientoPasoExamen getExamenRegistro() {
        return examenRegistro;
    }

    public void setExamenRegistro(ProcedimientoPasoExamen examenRegistro) {
        this.examenRegistro = examenRegistro;
    }

    public ESTADO_CRUD getExamenEstado() {
        return examenEstado;
    }

    public List<ProcedimientoPasoExamen> getExamenesAsignados() {
        return examenesAsignados;
    }

    public List<Examen> getExamenesDisponibles() {
        return examenesDisponibles;
    }
}
