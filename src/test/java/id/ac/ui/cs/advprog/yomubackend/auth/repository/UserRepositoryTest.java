package id.ac.ui.cs.advprog.yomubackend.auth.repository;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "Test User", "test@example.com", "081234567890", "password123");
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    void testFindByUsername() {
        Optional<User> found = userRepository.findByUsername("testuser");

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void testFindByUsernameNotFound() {
        Optional<User> found = userRepository.findByUsername("nonexistent");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindByPhoneNumber() {
        Optional<User> found = userRepository.findByPhoneNumber("081234567890");

        assertTrue(found.isPresent());
        assertEquals("081234567890", found.get().getPhoneNumber());
    }

    @Test
    void testFindByPhoneNumberNotFound() {
        Optional<User> found = userRepository.findByPhoneNumber("089999999999");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindByEmail() {
        Optional<User> found = userRepository.findByEmail("test@example.com");

        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("notexist@example.com");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindByEmailOrPhoneNumber_WithEmail() {
        Optional<User> found = userRepository.findByEmailOrPhoneNumber("test@example.com");

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void testFindByEmailOrPhoneNumber_WithPhoneNumber() {
        Optional<User> found = userRepository.findByEmailOrPhoneNumber("081234567890");

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void testFindByEmailOrPhoneNumber_NotFound() {
        Optional<User> found = userRepository.findByEmailOrPhoneNumber("nonexistent");

        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByUsername() {
        assertTrue(userRepository.existsByUsername("testuser"));
        assertFalse(userRepository.existsByUsername("nonexistent"));
    }

    @Test
    void testExistsByPhoneNumber() {
        assertTrue(userRepository.existsByPhoneNumber("081234567890"));
        assertFalse(userRepository.existsByPhoneNumber("089999999999"));
    }

    @Test
    void testExistsByEmail() {
        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("notexist@example.com"));
    }
}
