package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import java.util.List;
import java.util.UUID;

public class LoginResponse {

    private boolean identityValidated;
    private String message;
    private Employee employee;
    private String accessToken;

    public LoginResponse() {
    }

    public LoginResponse(boolean identityValidated, String message, Employee employee) {
        this.identityValidated = identityValidated;
        this.message = message;
        this.employee = employee;
    }

    public LoginResponse(boolean identityValidated, String message, Employee employee, String accessToken) {
        this(identityValidated, message, employee);
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isIdentityValidated() {
        return identityValidated;
    }

    public void setIdentityValidated(boolean identityValidated) {
        this.identityValidated = identityValidated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public static class Employee {

        private UUID id;
        private String nombres;
        private String apellidos;
        private List<Assignment> assignments;

        public Employee() {
        }

        public Employee(UUID id, String nombres, String apellidos, List<Assignment> assignments) {
            this.id = id;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.assignments = assignments;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getNombres() {
            return nombres;
        }

        public void setNombres(String nombres) {
            this.nombres = nombres;
        }

        public String getApellidos() {
            return apellidos;
        }

        public void setApellidos(String apellidos) {
            this.apellidos = apellidos;
        }

        public List<Assignment> getAssignments() {
            return assignments;
        }

        public void setAssignments(List<Assignment> assignments) {
            this.assignments = assignments;
        }
    }

    public static class Assignment {

        private UUID idPersonaRol;
        private UUID idClinica;
        private String clinica;
        private UUID idRol;
        private String rol;

        public Assignment() {
        }

        public Assignment(UUID idPersonaRol, UUID idClinica, String clinica, UUID idRol, String rol) {
            this.idPersonaRol = idPersonaRol;
            this.idClinica = idClinica;
            this.clinica = clinica;
            this.idRol = idRol;
            this.rol = rol;
        }

        public UUID getIdPersonaRol() {
            return idPersonaRol;
        }

        public void setIdPersonaRol(UUID idPersonaRol) {
            this.idPersonaRol = idPersonaRol;
        }

        public UUID getIdClinica() {
            return idClinica;
        }

        public void setIdClinica(UUID idClinica) {
            this.idClinica = idClinica;
        }

        public String getClinica() {
            return clinica;
        }

        public void setClinica(String clinica) {
            this.clinica = clinica;
        }

        public UUID getIdRol() {
            return idRol;
        }

        public void setIdRol(UUID idRol) {
            this.idRol = idRol;
        }

        public String getRol() {
            return rol;
        }

        public void setRol(String rol) {
            this.rol = rol;
        }
    }
}
