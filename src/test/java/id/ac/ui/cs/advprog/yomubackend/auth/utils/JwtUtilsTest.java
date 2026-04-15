package id.ac.ui.cs.advprog.yomubackend.auth.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "yomu.jwt.secret=IniAdalahKunciRahasiaYomuBackendYangSangatPanjangSekaliAgarAman2026",
    "yomu.jwt.expiration=86400000"
})
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void testGenerateToken() {
        String token = jwtUtils.generateToken("testuser");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetUsernameFromToken() {
        String token = jwtUtils.generateToken("testuser");
        String username = jwtUtils.getUsernameFromToken(token);

        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken_Valid() {
        String token = jwtUtils.generateToken("testuser");

        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void testValidateToken_Invalid() {
        assertFalse(jwtUtils.validateToken("invalid.token.here"));
    }

    @Test
    void testValidateToken_Malformed() {
        assertFalse(jwtUtils.validateToken("not-a-jwt"));
    }
}
