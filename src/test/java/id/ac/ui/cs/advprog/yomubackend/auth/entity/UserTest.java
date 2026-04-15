package id.ac.ui.cs.advprog.yomubackend.auth.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class UserTest {

    @Test
    void testUserConstructor() {
        User user = new User("testuser", "Test User", null, "081234567890", "password123");

        assertEquals("testuser", user.getUsername());
        assertEquals("Test User", user.getDisplayName());
        assertEquals("081234567890", user.getPhoneNumber());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void testUserSettersAndGetters() {
        User user = new User();

        user.setId(1L);
        user.setUsername("testuser");
        user.setDisplayName("Test User");
        user.setEmail("test@example.com");
        user.setPhoneNumber("081234567890");
        user.setPassword("password123");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("Test User", user.getDisplayName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("081234567890", user.getPhoneNumber());
        assertEquals("password123", user.getPassword());
        assertEquals("USER", user.getRole());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), user.getCreatedAt());
    }

    @Test
    void testUserNoArgsConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getDisplayName());
        assertNull(user.getEmail());
        assertNull(user.getPhoneNumber());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }

    @Test
    void testUserEqualsAndHashCode() {
        User user1 = new User("testuser", "Test User", null, "081234567890", "password123");
        user1.setId(1L);

        User user2 = new User("testuser", "Test User", null, "081234567890", "password123");
        user2.setId(1L);

        User user3 = new User("otheruser", "Other User", null, "089876543210", "password456");
        user3.setId(2L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);
    }

    @Test
    void testUserToString() {
        User user = new User("testuser", "Test User", null, "081234567890", "password123");
        user.setId(1L);

        String toString = user.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("testuser"));
    }
}
