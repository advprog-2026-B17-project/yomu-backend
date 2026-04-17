package id.ac.ui.cs.advprog.yomubackend.auth.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void testAuthResponseNoArgsConstructor() {
        AuthResponse response = new AuthResponse();

        assertNull(response.getToken());
        assertNull(response.getMessage());
    }

    @Test
    void testAuthResponseAllArgsConstructor() {
        AuthResponse response = new AuthResponse("jwt-token-here", "Login successful");

        assertEquals("jwt-token-here", response.getToken());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void testAuthResponseSetters() {
        AuthResponse response = new AuthResponse();

        response.setToken("new-jwt-token");
        response.setMessage("Updated message");

        assertEquals("new-jwt-token", response.getToken());
        assertEquals("Updated message", response.getMessage());
    }
}
