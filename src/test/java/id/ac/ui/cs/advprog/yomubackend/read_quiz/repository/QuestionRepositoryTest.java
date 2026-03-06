package id.ac.ui.cs.advprog.yomubackend.read_quiz.repository;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Question;
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
class QuestionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    private Quiz quiz;

    @BeforeEach
    void setUp() {
        // Create a User
        User user = new User("testuser", "test@example.com", "password");
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

        // Create Questions
        Question q1 = Question.builder()
                .quiz(quiz)
                .kind("multiple_choice")
                .questionText("Q1")
                .options("[\"A\",\"B\"]")
                .correctAnswer("A")
                .build();
        entityManager.persist(q1);

        Question q2 = Question.builder()
                .quiz(quiz)
                .kind("short_answer")
                .questionText("Q2")
                .correctAnswer("B")
                .build();
        entityManager.persist(q2);

        entityManager.flush();
    }

    @Test
    void testFindByQuizId() {
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());

        assertNotNull(questions);
        assertEquals(2, questions.size());
    }

    @Test
    void testFindByQuizIdWithInvalidId() {
        List<Question> questions = questionRepository.findByQuizId(999L);

        assertNotNull(questions);
        assertTrue(questions.isEmpty());
    }
}
