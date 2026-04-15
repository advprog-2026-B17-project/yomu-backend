package id.ac.ui.cs.advprog.yomubackend.auth.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void testUserResponseNoArgsConstructor() {
        UserResponse response = new UserResponse();

        assertNull(response.getId());
        assertNull(response.getUsername());
        assertNull(response.getDisplayName());
        assertNull(response.getEmail());
        assertNull(response.getPhoneNumber());
        assertNull(response.getRole());
    }

    @Test
    void testUserResponseAllArgsConstructor() {
        UserResponse response = new UserResponse(
                1L,
                "testuser",
                "Test User",
                "test@example.com",
                "081234567890",
                "USER"
        );

        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getDisplayName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("081234567890", response.getPhoneNumber());
        assertEquals("USER", response.getRole());
    }

    @Test
    void testUserResponseSetters() {
        UserResponse response = new UserResponse();

        response.setId(1L);
        response.setUsername("testuser");
        response.setDisplayName("Test User");
        response.setEmail("test@example.com");
        response.setPhoneNumber("081234567890");
        response.setRole("ADMIN");

        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getDisplayName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("081234567890", response.getPhoneNumber());
        assertEquals("ADMIN", response.getRole());
    }
}
