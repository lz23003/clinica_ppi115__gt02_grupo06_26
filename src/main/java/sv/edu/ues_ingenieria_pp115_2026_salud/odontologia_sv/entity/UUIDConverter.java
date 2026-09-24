
package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

@Converter
public class UUIDConverter implements AttributeConverter<UUID, UUID>{

    @Override
    public UUID convertToDatabaseColumn(UUID x) {
         // Cuando guardamos en la base de datos
        // El UUID de Java se convierte a UUID de PostgreSQL
        return x;
    }

    @Override
    public UUID convertToEntityAttribute(UUID y) {
        // Cuando leemos de la base de datos
        // El UUID de PostgreSQL se convierte a UUID de Java
        return y;
    }
    
}
