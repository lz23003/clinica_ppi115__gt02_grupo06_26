package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class ConsultaDetalleDTO implements Serializable {
    public UUID idConsulta;
    public String fechaInicio;          // ISO string
    public String fechaFin;
    public String referenciaExterna;
    public String observaciones;

    public PersonaDetalleDTO paciente;
    public ClinicaDetalleDTO clinica;

    public List<ProcedimientoDetalleDTO> procedimientos;

    public static class PersonaDetalleDTO implements Serializable {
        public UUID idPersona;
        public String nombres;
        public String apellidos;
        public String fechaNacimiento;
    }

    public static class ClinicaDetalleDTO implements Serializable {
        public UUID idClinica;
        public String nombre;
    }

    public static class ProcedimientoDetalleDTO implements Serializable {
        public UUID idConsultaProcedimiento;
        public UUID idProcedimiento;
        public String nombreProcedimiento;
        public String fechaInicio;
        public String fechaFin;
        public String observaciones;
        public List<PasoDetalleDTO> pasos;
    }

    public static class PasoDetalleDTO implements Serializable {
        public UUID idConsultaProcedimientoPaso;
        public UUID idProcedimientoPaso;
        public String nombrePaso;
        public String estado;
        public String fechaInicio;
        public String fechaFin;
        public String valor;
        public PersonaDetalleDTO realizadoPor;
        public UUID idRolRealizador;
        public String nombreRolRealizador;
    }
}