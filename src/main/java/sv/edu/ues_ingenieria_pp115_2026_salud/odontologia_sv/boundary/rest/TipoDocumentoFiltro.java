package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import java.util.Locale;

final class TipoDocumentoFiltro {

    private TipoDocumentoFiltro() {
    }

    static String patron(String tipoDocumento) {
        if (tipoDocumento == null) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio.");
        }
        return switch (tipoDocumento.trim().toUpperCase(Locale.ROOT)) {
            case "DUI" -> "DUI";
            case "PASAPORTE" -> "PASAPORTE";
            case "LICENCIA" -> "LICENCIA";
            default -> throw new IllegalArgumentException("El tipo de documento no es válido.");
        };
    }
}
