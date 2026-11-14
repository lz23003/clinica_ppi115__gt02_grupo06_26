package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import java.util.List;
import java.util.UUID;

public class CitaResponse {

    private UUID id;
    private String fecha;
    private String hora;
    private String clinica;
    private List<String> procedimientos;

    public CitaResponse() {
    }

    public CitaResponse(UUID id, String fecha, String hora, String clinica, List<String> procedimientos) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.clinica = clinica;
        this.procedimientos = procedimientos;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getClinica() {
        return clinica;
    }

    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    public List<String> getProcedimientos() {
        return procedimientos;
    }

    public void setProcedimientos(List<String> procedimientos) {
        this.procedimientos = procedimientos;
    }
}
