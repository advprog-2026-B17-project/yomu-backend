package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class QuizTest {

    private Quiz quiz;
    private Text text;
    private User user;

    @BeforeEach
    void setUp() {
        // Create a mock User - using constructor since User doesn't have builder
        user = new User("testuser", "test@example.com", "password");
        user.setId(1L);

        // Create a mock Text
        text = Text.builder()
                .id(1L)
                .title("Sample Text")
                .content("This is sample content for testing")
                .category("technology")
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .quizzes(new ArrayList<>())
                .build();

        // Create a mock Quiz
        List<Question> questions = new ArrayList<>();
        Question q1 = Question.builder()
                .id(1L)
                .quiz(null) // Will be set by relationship
                .kind("multiple_choice")
                .questionText("Question 1")
                .build();
        questions.add(q1);

        quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Test Quiz")
                .questions(questions)
                .build();
    }

    @Test
    void testQuizBuilder() {
        assertNotNull(quiz);
        assertEquals(1L, quiz.getId());
        assertEquals("Test Quiz", quiz.getTitle());
        assertNotNull(quiz.getQuestions());
        assertEquals(1, quiz.getQuestions().size());
    }

    @Test
    void testQuizNoArgsConstructor() {
        Quiz emptyQuiz = new Quiz();
        assertNull(emptyQuiz.getId());
        assertNull(emptyQuiz.getTitle());
        assertNull(emptyQuiz.getText());
        assertNull(emptyQuiz.getQuestions());
    }

    @Test
    void testQuizAllArgsConstructor() {
        Quiz allArgsQuiz = new Quiz(2L, text, "All Args Quiz", new ArrayList<>());
        assertEquals(2L, allArgsQuiz.getId());
        assertEquals("All Args Quiz", allArgsQuiz.getTitle());
        assertEquals(text, allArgsQuiz.getText());
    }

    @Test
    void testQuizSetters() {
        quiz.setId(2L);
        quiz.setTitle("Updated Quiz");
        
        List<Question> newQuestions = new ArrayList<>();
        quiz.setQuestions(newQuestions);

        assertEquals(2L, quiz.getId());
        assertEquals("Updated Quiz", quiz.getTitle());
        assertEquals(0, quiz.getQuestions().size());
    }

    @Test
    void testQuizEqualsAndHashCode() {
        Quiz quiz1 = Quiz.builder()
                .id(1L)
                .title("Quiz")
                .build();
        
        Quiz quiz2 = Quiz.builder()
                .id(1L)
                .title("Quiz")
                .build();
        
        Quiz quiz3 = Quiz.builder()
                .id(2L)
                .title("Different Quiz")
                .build();

        assertEquals(quiz1, quiz2);
        assertEquals(quiz1.hashCode(), quiz2.hashCode());
        assertNotEquals(quiz1, quiz3);
    }

    @Test
    void testQuizToString() {
        String toStringResult = quiz.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Quiz"));
        assertTrue(toStringResult.contains("id=1"));
    }

    @Test
    void testQuizTextRelationship() {
        assertNotNull(quiz.getText());
        assertEquals("Sample Text", quiz.getText().getTitle());
    }

    @Test
    void testQuizWithNullText() {
        Quiz quizNoText = Quiz.builder()
                .id(3L)
                .title("No Text Quiz")
                .build();
        assertNull(quizNoText.getText());
    }

    @Test
    void testQuizWithNullQuestions() {
        Quiz quizNoQuestions = Quiz.builder()
                .id(4L)
                .title("No Questions Quiz")
                .questions(null)
                .build();
        assertNull(quizNoQuestions.getQuestions());
    }

    @Test
    void testQuizWithEmptyQuestionsList() {
        Quiz quizEmptyQuestions = Quiz.builder()
                .id(5L)
                .title("Empty Questions Quiz")
                .questions(new ArrayList<>())
                .build();
        assertNotNull(quizEmptyQuestions.getQuestions());
        assertTrue(quizEmptyQuestions.getQuestions().isEmpty());
    }
}
