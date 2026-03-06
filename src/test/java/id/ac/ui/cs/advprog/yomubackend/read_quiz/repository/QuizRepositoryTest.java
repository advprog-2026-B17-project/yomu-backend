package id.ac.ui.cs.advprog.yomubackend.read_quiz.repository;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class QuizRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuizRepository quizRepository;

    private Text text;

    @BeforeEach
    void setUp() {
        // Create a User
        User user = new User("testuser", "test@example.com", "password");
        entityManager.persist(user);

        // Create a Text (required for Quiz)
        text = Text.builder()
                .title("Test Text")
                .content("Content")
                .category("tech")
                .createdBy(user)
                .build();
        entityManager.persist(text);

        // Create Quizzes
        Quiz q1 = Quiz.builder()
                .text(text)
                .title("Quiz 1")
                .build();
        entityManager.persist(q1);

        Quiz q2 = Quiz.builder()
                .text(text)
                .title("Quiz 2")
                .build();
        entityManager.persist(q2);

        entityManager.flush();
    }

    @Test
    void testFindByTextId() {
        List<Quiz> quizzes = quizRepository.findByTextId(text.getId());

        assertNotNull(quizzes);
        assertEquals(2, quizzes.size());
    }

    @Test
    void testFindByTextIdWithInvalidId() {
        List<Quiz> quizzes = quizRepository.findByTextId(999L);

        assertNotNull(quizzes);
        assertTrue(quizzes.isEmpty());
    }
}
