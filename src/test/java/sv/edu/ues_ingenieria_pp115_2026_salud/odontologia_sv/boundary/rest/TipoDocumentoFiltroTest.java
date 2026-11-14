package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.rest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TipoDocumentoFiltroTest {

    @Test
    void mapsSupportedFrontendDocumentTypesToBackendNames() {
        assertThat(TipoDocumentoFiltro.patron(" dui ")).isEqualTo("DUI");
        assertThat(TipoDocumentoFiltro.patron("Pasaporte")).isEqualTo("PASAPORTE");
        assertThat(TipoDocumentoFiltro.patron("LICENCIA")).isEqualTo("LICENCIA");
    }

    @Test
    void rejectsUnsupportedOrMissingDocumentType() {
        assertThatThrownBy(() -> TipoDocumentoFiltro.patron("carnet"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TipoDocumentoFiltro.patron(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
