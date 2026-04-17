package id.ac.ui.cs.advprog.yomubackend.read_quiz.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class QuizNotFoundExceptionTest {

    @Test
    void testQuizNotFoundExceptionMessage() {
        QuizNotFoundException exception = new QuizNotFoundException(1L);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("1"));
        assertTrue(exception.getMessage().contains("Quiz"));
    }

    @Test
    void testQuizNotFoundExceptionMessageWithDifferentId() {
        QuizNotFoundException exception = new QuizNotFoundException(999L);
        
        assertNotNull(exception);
        assertEquals("Quiz not found with id: 999", exception.getMessage());
    }

    @Test
    void testQuizNotFoundExceptionIsRuntimeException() {
        QuizNotFoundException exception = new QuizNotFoundException(1L);
        
        assertTrue(exception instanceof RuntimeException);
    }
}