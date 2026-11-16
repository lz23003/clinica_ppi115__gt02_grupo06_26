package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@ApplicationScoped
public class JwtTokenService {

    private static final String ALGORITHM = "HmacSHA256";
    private static final String JWT_HEADER = Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
    private static final long TOKEN_LIFETIME_SECONDS = 60 * 60;
    private static final byte[] EPHEMERAL_SIGNING_KEY = createEphemeralSigningKey();

    public boolean isConfigured() {
        return signingKey() != null;
    }

    public String issueToken(UUID employeeId) {
        byte[] key = signingKey();
        if (key == null) {
            throw new IllegalStateException("Configure GALENOSV_JWT_SECRET para habilitar el login.");
        }
        long issuedAt = Instant.now().getEpochSecond();
        JsonObject claims = Json.createObjectBuilder()
                .add("sub", employeeId.toString())
                .add("iss", "galenosv")
                .add("iat", issuedAt)
                .add("exp", issuedAt + TOKEN_LIFETIME_SECONDS)
                .build();
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(claims.toString().getBytes(StandardCharsets.UTF_8));
        String unsigned = JWT_HEADER + "." + payload;
        try {
            return unsigned + "." + Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(sign(unsigned, key));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("No se pudo firmar el token de sesión.", ex);
        }
    }

    public boolean isValid(String token) {
        byte[] key = signingKey();
        if (key == null || token == null) {
            return false;
        }
        try {
            String[] parts = token.split("\\.", -1);
            if (parts.length != 3 || !JWT_HEADER.equals(parts[0])) {
                return false;
            }
            byte[] expected = sign(parts[0] + "." + parts[1], key);
            byte[] actual = Base64.getUrlDecoder().decode(parts[2]);
            if (!java.security.MessageDigest.isEqual(expected, actual)) {
                return false;
            }
            byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
            try (JsonReader reader = Json.createReader(new StringReader(
                    new String(payload, StandardCharsets.UTF_8)))) {
                JsonObject claims = reader.readObject();
                return "galenosv".equals(claims.getString("iss", ""))
                        && UUID.fromString(claims.getString("sub", "")).toString()
                                .equals(claims.getString("sub", ""))
                        && claims.getJsonNumber("exp").longValue() > Instant.now().getEpochSecond();
            }
        } catch (IllegalArgumentException | GeneralSecurityException ex) {
            return false;
        }
    }

    public UUID getSubjectId(String token) {
        if (!isValid(token)) {
            return null;
        }
        try {
            String payloadPart = token.split("\\.", -1)[1];
            byte[] payload = Base64.getUrlDecoder().decode(payloadPart);
            try (JsonReader reader = Json.createReader(new StringReader(
                    new String(payload, StandardCharsets.UTF_8)))) {
                return UUID.fromString(reader.readObject().getString("sub"));
            }
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private byte[] signingKey() {
        String configured = System.getenv("GALENOSV_JWT_SECRET");
        if (configured == null || configured.isBlank()) {
            configured = System.getProperty("galenosv.jwt.secret");
        }
        if (configured == null || configured.isBlank()) {
            return EPHEMERAL_SIGNING_KEY;
        }
        byte[] key = configured.getBytes(StandardCharsets.UTF_8);
        return key.length >= 32 ? key : null;
    }

    private static byte[] createEphemeralSigningKey() {
        byte[] key = new byte[32];
        new java.security.SecureRandom().nextBytes(key);
        return key;
    }

    private byte[] sign(String value, byte[] key) throws GeneralSecurityException {
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(key, ALGORITHM));
        return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
    }
}
