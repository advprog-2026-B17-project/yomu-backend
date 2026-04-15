package id.ac.ui.cs.advprog.yomubackend.auth.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testLoginRequestNoArgsConstructor() {
        LoginRequest request = new LoginRequest();

        assertNull(request.getIdentifier());
        assertNull(request.getPassword());
    }

    @Test
    void testLoginRequestAllArgsConstructor() {
        LoginRequest request = new LoginRequest("081234567890", "password123");

        assertEquals("081234567890", request.getIdentifier());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void testLoginRequestSetters() {
        LoginRequest request = new LoginRequest();

        request.setIdentifier("test@example.com");
        request.setPassword("password123");

        assertEquals("test@example.com", request.getIdentifier());
        assertEquals("password123", request.getPassword());
    }
}
