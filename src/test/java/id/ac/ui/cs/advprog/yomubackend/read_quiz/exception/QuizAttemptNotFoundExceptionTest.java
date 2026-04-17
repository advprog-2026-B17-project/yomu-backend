package id.ac.ui.cs.advprog.yomubackend.read_quiz.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class QuizAttemptNotFoundExceptionTest {

    @Test
    void testQuizAttemptNotFoundExceptionMessage() {
        QuizAttemptNotFoundException exception = new QuizAttemptNotFoundException(1L);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("1"));
        assertTrue(exception.getMessage().contains("attempt"));
    }

    @Test
    void testQuizAttemptNotFoundExceptionMessageWithDifferentId() {
        QuizAttemptNotFoundException exception = new QuizAttemptNotFoundException(999L);
        
        assertNotNull(exception);
        assertEquals("Quiz attempt not found with id: 999", exception.getMessage());
    }

    @Test
    void testQuizAttemptNotFoundExceptionIsRuntimeException() {
        QuizAttemptNotFoundException exception = new QuizAttemptNotFoundException(1L);
        
        assertTrue(exception instanceof RuntimeException);
    }
}