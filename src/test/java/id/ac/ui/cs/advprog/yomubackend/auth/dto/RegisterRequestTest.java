package id.ac.ui.cs.advprog.yomubackend.auth.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void testRegisterRequestNoArgsConstructor() {
        RegisterRequest request = new RegisterRequest();

        assertNull(request.getUsername());
        assertNull(request.getDisplayName());
        assertNull(request.getEmail());
        assertNull(request.getPhoneNumber());
        assertNull(request.getPassword());
    }

    @Test
    void testRegisterRequestAllArgsConstructor() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "Test User",
                "test@example.com",
                "081234567890",
                "password123"
        );

        assertEquals("testuser", request.getUsername());
        assertEquals("Test User", request.getDisplayName());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("081234567890", request.getPhoneNumber());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void testRegisterRequestSetters() {
        RegisterRequest request = new RegisterRequest();

        request.setUsername("testuser");
        request.setDisplayName("Test User");
        request.setEmail("test@example.com");
        request.setPhoneNumber("081234567890");
        request.setPassword("password123");
        request.setRole("USER");

        assertEquals("testuser", request.getUsername());
        assertEquals("Test User", request.getDisplayName());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("081234567890", request.getPhoneNumber());
        assertEquals("password123", request.getPassword());
        assertEquals("USER", request.getRole());
    }
}
