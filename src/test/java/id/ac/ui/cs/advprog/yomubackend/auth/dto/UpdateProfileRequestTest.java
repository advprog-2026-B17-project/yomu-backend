package id.ac.ui.cs.advprog.yomubackend.auth.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UpdateProfileRequestTest {

    @Test
    void testUpdateProfileRequestNoArgsConstructor() {
        UpdateProfileRequest request = new UpdateProfileRequest();

        assertNull(request.getDisplayName());
        assertNull(request.getEmail());
        assertNull(request.getPhoneNumber());
        assertNull(request.getPassword());
    }

    @Test
    void testUpdateProfileRequestAllArgsConstructor() {
        UpdateProfileRequest request = new UpdateProfileRequest(
                "Updated Name",
                "updated@example.com",
                "081234567890",
                "newpassword"
        );

        assertEquals("Updated Name", request.getDisplayName());
        assertEquals("updated@example.com", request.getEmail());
        assertEquals("081234567890", request.getPhoneNumber());
        assertEquals("newpassword", request.getPassword());
    }

    @Test
    void testUpdateProfileRequestSetters() {
        UpdateProfileRequest request = new UpdateProfileRequest();

        request.setDisplayName("New Name");
        request.setEmail("new@example.com");
        request.setPhoneNumber("089876543210");
        request.setPassword("secret");

        assertEquals("New Name", request.getDisplayName());
        assertEquals("new@example.com", request.getEmail());
        assertEquals("089876543210", request.getPhoneNumber());
        assertEquals("secret", request.getPassword());
    }
}
