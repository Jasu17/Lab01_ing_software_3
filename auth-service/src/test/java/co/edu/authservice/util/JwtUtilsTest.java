package co.edu.authservice.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secret", "12345678901234567890123456789012");
        ReflectionTestUtils.setField(jwtUtils, "expirationMs", 60_000L);
    }

    @Test
    void elTokenDelEstudianteLlevaSuEstudianteId() {
        Claims claims = jwtUtils.parseToken(jwtUtils.generateToken("estudiante", "ESTUDIANTE", 1L));

        // JJWT lee los números pequeños como Integer; get(..., Long.class) hace la conversión
        assertThat(claims.get("estudianteId", Long.class)).isEqualTo(1L);
    }

    @Test
    void elTokenDeUnAdminNoLlevaEstudianteId() {
        Claims claims = jwtUtils.parseToken(jwtUtils.generateToken("admin", "ADMIN", null));

        assertThat(claims.containsKey("estudianteId")).isFalse();
    }
}
