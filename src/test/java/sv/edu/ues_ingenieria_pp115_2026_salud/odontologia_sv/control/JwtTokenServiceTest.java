package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class JwtTokenServiceTest {

    @Test
    void issuesAndValidatesSessionTokensWithoutExternalConfiguration() {
        JwtTokenService service = new JwtTokenService();
        UUID employeeId = UUID.randomUUID();

        assertTrue(service.isConfigured());
        String token = service.issueToken(employeeId);

        assertNotNull(token);
        assertTrue(service.isValid(token));
        assertEquals(employeeId, service.getSubjectId(token));
    }

    @Test
    void rejectsModifiedSessionTokens() {
        JwtTokenService service = new JwtTokenService();
        String token = service.issueToken(UUID.randomUUID());
        int signatureStart = token.lastIndexOf('.') + 1;
        char firstSignatureCharacter = token.charAt(signatureStart);
        String modifiedToken = token.substring(0, signatureStart)
                + (firstSignatureCharacter == 'A' ? 'B' : 'A')
                + token.substring(signatureStart + 1);

        assertFalse(service.isValid(modifiedToken));
    }
}
