package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control.DefaultDAO;

public abstract class DefaultModel<T> implements Serializable {

    protected T registro;
    protected ESTADO_CRUD estado;
    protected LazyDataModel<T> modelo;
    protected int registrosPorPagina = 5;

    // MÉTODOS ABSTRACTOS
    protected abstract Object getId(T object);

    protected abstract DefaultDAO<T> getDao();

    protected abstract FacesContext getFacesContext();

    protected abstract T crearEntidad();

    public abstract String getNombreEntidad();

    // INICIALIZACIÓN DEL LAZY DATA MODEL
   
    @PostConstruct
    public void inicializador() {
        estado = ESTADO_CRUD.NADA;

        modelo = new LazyDataModel<T>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getRowKey(T object) {
                Object id = getId(object);
                if (object != null && id != null) {
                    return id.toString();
                } else {
                    return null;
                }
            }
            
            @Override
            public T getRowData(String rowKey){
                if(rowKey != null){
                    // Primero buscar en los datos cargados actualmente
                    List<T> wrappedData = getWrappedData();
                    if(wrappedData != null && !wrappedData.isEmpty()){
                        return wrappedData.stream()
                                .filter(r -> rowKey.equals(getId(r).toString()))
                                .findFirst()
                                .orElse(null);
                    }
                   
                }
                return null;
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                try {
                    return getDao().count();
                } catch (Exception ex) {
                    enviarMensaje(getText("model.crud.error"),
                            getText("model.crud.error.contar"),
                            FacesMessage.SEVERITY_ERROR);
                    return 0;
                }
            }

            @Override
            public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                try {
                    int total = getDao().count();
                    this.setRowCount(total);
                    return getDao().findRange(first, pageSize);
                } catch (Exception ex) {
                    enviarMensaje(getText("model.crud.error"),
                            getText("model.crud.error.cargar"),
                            FacesMessage.SEVERITY_ERROR);
                     ex.printStackTrace();
                }
                return List.of();
            }
        };

        modelo.setPageSize(registrosPorPagina);
    }

    // Envia mensajes con titulo,detalle y severidad
    public void enviarMensaje(String titulo, String detalle, FacesMessage.Severity severity) {
        FacesMessage mensaje = new FacesMessage(severity, titulo, detalle);
        FacesContext contexto = getFacesContext();
        if (contexto != null) {
            contexto.addMessage(null, mensaje);
        }
    }

    // MÉTODOS DE CRUD
    public void btnNuevoHandler(ActionEvent event) {
        this.registro = crearEntidad();
        this.estado = ESTADO_CRUD.CREAR;
    }

    public void btnCrearHandler(ActionEvent event) {
        try {
            if (estado == ESTADO_CRUD.CREAR) {
                getDao().crear(registro);
                enviarMensaje(getText("model.crud.exito"), getText("model.crud.creado"), FacesMessage.SEVERITY_INFO);
            } else if (estado == ESTADO_CRUD.MODIFICAR) {
                getDao().modificar(registro);
                enviarMensaje(getText("model.crud.exito"), getText("model.crud.actualizado"), FacesMessage.SEVERITY_INFO);
            } else {
                enviarMensaje(getText("model.crud.advertencia"), getText("model.crud.accion"), FacesMessage.SEVERITY_WARN);
                return;
            }
            registro = null;
            estado = ESTADO_CRUD.NADA;
        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"), getText("model.crud.error.guardar"), FacesMessage.SEVERITY_ERROR);
            ex.printStackTrace();
        }
    }

    public void btnEliminarHandler(T registroSeleccionado) {
        try {
            getDao().eliminar(registroSeleccionado);
            enviarMensaje(getText("model.crud.exito"), getText("model.crud.eliminado"), FacesMessage.SEVERITY_INFO);
            
            this.estado=ESTADO_CRUD.NADA;
            this.registro=null;
        } catch (Exception ex) {
            enviarMensaje(getText("model.crud.error"), getText("model.crud.error.eliminar"), FacesMessage.SEVERITY_ERROR);
            ex.printStackTrace();
        }
    }

    public void btnCancelarHandler(ActionEvent event) {
        registro = null;
        estado = ESTADO_CRUD.NADA;
    }

    public void onRowSelect(SelectEvent<T> event) {
        if (event != null && event.getObject() != null) {
            this.registro = event.getObject();
            this.estado = ESTADO_CRUD.MODIFICAR;
        }
    }

    //Metodo para obtener un texto del ResourceBundle "crud"
    protected String getText(String key) {
        try {
            FacesContext ctx = getFacesContext();
            if (ctx == null) {
                return key;
            }
            ResourceBundle bundle = ctx.getApplication().getResourceBundle(ctx, "crud");
            return bundle.getString(key);

        } catch (Exception ex) {
            return key;
        }
    }

    // GETTERS Y SETTERS
    public T getRegistro() {
        return registro;
    }

    public void setRegistro(T registro) {
        this.registro = registro;
    }

    public ESTADO_CRUD getEstado() {
        return estado;
    }

    public void setEstado(ESTADO_CRUD estado) {
        this.estado = estado;
    }

    public LazyDataModel<T> getModelo() {
        return modelo;
    }

    public void setModelo(LazyDataModel<T> modelo) {
        this.modelo = modelo;
    }

    public int getRegistrosPorPagina() {
        return registrosPorPagina;
    }

    public void setRegistrosPorPagina(int registrosPorPagina) {
        this.registrosPorPagina = registrosPorPagina;
    }

    public String getEstadoNombre() {
        return estado != null ? estado.name() : "NADA";
    }

}
