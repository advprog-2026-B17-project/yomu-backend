package id.ac.ui.cs.advprog.yomubackend.read_quiz.repository;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.QuizAttempt;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class QuizAttemptRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    private User user;
    private Quiz quiz;

    @BeforeEach
    void setUp() {
        // Create a User
        user = new User("testuser", "test@example.com", "password");
        entityManager.persist(user);

        // Create a Text
        Text text = Text.builder()
                .title("Test Text")
                .content("Content")
                .category("tech")
                .createdBy(user)
                .build();
        entityManager.persist(text);

        // Create a Quiz
        quiz = Quiz.builder()
                .title("Test Quiz")
                .text(text)
                .build();
        entityManager.persist(quiz);
        entityManager.flush();
    }

    @Test
    void testFindByUser() {
        // Create QuizAttempts after quiz is persisted
        QuizAttempt a1 = QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .score(85)
                .startedAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .submittedAt(LocalDateTime.of(2024, 1, 1, 10, 30))
                .build();
        entityManager.persist(a1);

        QuizAttempt a2 = QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .score(90)
                .startedAt(LocalDateTime.of(2024, 1, 2, 10, 0))
                .submittedAt(LocalDateTime.of(2024, 1, 2, 10, 25))
                .build();
        entityManager.persist(a2);

        entityManager.flush();
        
        // Clear and re-fetch
        entityManager.clear();
        User managedUser = entityManager.find(User.class, user.getId());
        
        PageRequest pageable = PageRequest.of(0, 10);
        Page<QuizAttempt> page = quizAttemptRepository.findByUser(managedUser, pageable);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void testFindByUserWithPagination() {
        QuizAttempt a1 = QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .score(85)
                .startedAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .submittedAt(LocalDateTime.of(2024, 1, 1, 10, 30))
                .build();
        entityManager.persist(a1);

        QuizAttempt a2 = QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .score(90)
                .startedAt(LocalDateTime.of(2024, 1, 2, 10, 0))
                .submittedAt(LocalDateTime.of(2024, 1, 2, 10, 25))
                .build();
        entityManager.persist(a2);

        entityManager.flush();
        entityManager.clear();
        
        User managedUser = entityManager.find(User.class, user.getId());
        PageRequest pageable = PageRequest.of(0, 1);
        Page<QuizAttempt> page = quizAttemptRepository.findByUser(managedUser, pageable);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(2, page.getTotalElements());
    }
}
