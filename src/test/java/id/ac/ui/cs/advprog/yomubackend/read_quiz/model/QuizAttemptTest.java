package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class QuizAttemptTest {

    private QuizAttempt quizAttempt;
    private User user;
    private Quiz quiz;
    private Text text;

    @BeforeEach
    void setUp() {
        // Create a mock User
        user = new User("testuser", "test@example.com", "password");
        user.setId(1L);

        // Create a mock Text
        text = Text.builder()
                .id(1L)
                .title("Sample Text")
                .content("This is sample content")
                .category("technology")
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();

        // Create a mock Quiz
        quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Test Quiz")
                .build();

        // Create a mock QuizAttempt
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime submittedAt = LocalDateTime.of(2024, 1, 1, 10, 30);
        
        quizAttempt = QuizAttempt.builder()
                .id(1L)
                .user(user)
                .quiz(quiz)
                .score(85)
                .startedAt(startedAt)
                .submittedAt(submittedAt)
                .build();
    }

    @Test
    void testQuizAttemptBuilder() {
        assertNotNull(quizAttempt);
        assertEquals(1L, quizAttempt.getId());
        assertEquals(85, quizAttempt.getScore());
        assertEquals(user, quizAttempt.getUser());
        assertEquals(quiz, quizAttempt.getQuiz());
        assertNotNull(quizAttempt.getStartedAt());
        assertNotNull(quizAttempt.getSubmittedAt());
    }

    @Test
    void testQuizAttemptNoArgsConstructor() {
        QuizAttempt emptyAttempt = new QuizAttempt();
        assertNull(emptyAttempt.getId());
        assertNull(emptyAttempt.getScore());
        assertNull(emptyAttempt.getUser());
        assertNull(emptyAttempt.getQuiz());
        assertNull(emptyAttempt.getStartedAt());
        assertNull(emptyAttempt.getSubmittedAt());
    }

    @Test
    void testQuizAttemptAllArgsConstructor() {
        LocalDateTime startedAt = LocalDateTime.now();
        LocalDateTime submittedAt = LocalDateTime.now().plusHours(1);
        
        QuizAttempt allArgsAttempt = new QuizAttempt(2L, user, quiz, 90, startedAt, submittedAt);
        
        assertEquals(2L, allArgsAttempt.getId());
        assertEquals(90, allArgsAttempt.getScore());
        assertEquals(user, allArgsAttempt.getUser());
        assertEquals(quiz, allArgsAttempt.getQuiz());
        assertEquals(startedAt, allArgsAttempt.getStartedAt());
        assertEquals(submittedAt, allArgsAttempt.getSubmittedAt());
    }

    @Test
    void testQuizAttemptSetters() {
        LocalDateTime newStartedAt = LocalDateTime.of(2024, 2, 1, 12, 0);
        LocalDateTime newSubmittedAt = LocalDateTime.of(2024, 2, 1, 12, 45);
        
        quizAttempt.setId(2L);
        quizAttempt.setScore(95);
        quizAttempt.setStartedAt(newStartedAt);
        quizAttempt.setSubmittedAt(newSubmittedAt);

        assertEquals(2L, quizAttempt.getId());
        assertEquals(95, quizAttempt.getScore());
        assertEquals(newStartedAt, quizAttempt.getStartedAt());
        assertEquals(newSubmittedAt, quizAttempt.getSubmittedAt());
    }

    @Test
    void testQuizAttemptEqualsAndHashCode() {
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime submittedAt = LocalDateTime.of(2024, 1, 1, 10, 30);
        
        QuizAttempt attempt1 = QuizAttempt.builder()
                .id(1L)
                .score(85)
                .startedAt(startedAt)
                .submittedAt(submittedAt)
                .build();
        
        QuizAttempt attempt2 = QuizAttempt.builder()
                .id(1L)
                .score(85)
                .startedAt(startedAt)
                .submittedAt(submittedAt)
                .build();
        
        QuizAttempt attempt3 = QuizAttempt.builder()
                .id(2L)
                .score(90)
                .build();

        assertEquals(attempt1, attempt2);
        assertEquals(attempt1.hashCode(), attempt2.hashCode());
        assertNotEquals(attempt1, attempt3);
    }

    @Test
    void testQuizAttemptToString() {
        String toStringResult = quizAttempt.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("QuizAttempt"));
        assertTrue(toStringResult.contains("id=1"));
    }

    @Test
    void testQuizAttemptUserRelationship() {
        assertNotNull(quizAttempt.getUser());
        assertEquals("testuser", quizAttempt.getUser().getUsername());
    }

    @Test
    void testQuizAttemptQuizRelationship() {
        assertNotNull(quizAttempt.getQuiz());
        assertEquals("Test Quiz", quizAttempt.getQuiz().getTitle());
    }

    @Test
    void testQuizAttemptWithNullScore() {
        QuizAttempt attemptNoScore = QuizAttempt.builder()
                .id(3L)
                .user(user)
                .quiz(quiz)
                .build();
        assertNull(attemptNoScore.getScore());
    }

    @Test
    void testQuizAttemptWithNullUser() {
        QuizAttempt attemptNoUser = QuizAttempt.builder()
                .id(4L)
                .quiz(quiz)
                .score(80)
                .build();
        assertNull(attemptNoUser.getUser());
    }

    @Test
    void testQuizAttemptWithNullQuiz() {
        QuizAttempt attemptNoQuiz = QuizAttempt.builder()
                .id(5L)
                .user(user)
                .score(80)
                .build();
        assertNull(attemptNoQuiz.getQuiz());
    }

    @Test
    void testQuizAttemptWithNullDates() {
        QuizAttempt attemptNoDates = QuizAttempt.builder()
                .id(6L)
                .user(user)
                .quiz(quiz)
                .score(75)
                .build();
        assertNull(attemptNoDates.getStartedAt());
        assertNull(attemptNoDates.getSubmittedAt());
    }

    @Test
    void testQuizAttemptWithZeroScore() {
        QuizAttempt attemptZeroScore = QuizAttempt.builder()
                .id(7L)
                .user(user)
                .quiz(quiz)
                .score(0)
                .build();
        assertEquals(0, attemptZeroScore.getScore());
    }

    @Test
    void testQuizAttemptWithMaxScore() {
        QuizAttempt attemptMaxScore = QuizAttempt.builder()
                .id(8L)
                .user(user)
                .quiz(quiz)
                .score(100)
                .build();
        assertEquals(100, attemptMaxScore.getScore());
    }
}
