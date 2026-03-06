package id.ac.ui.cs.advprog.yomubackend.read_quiz.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextNotFoundExceptionTest {

    @Test
    void testTextNotFoundExceptionWithId() {
        Long textId = 1L;
        
        TextNotFoundException exception = new TextNotFoundException(textId);
        
        assertNotNull(exception);
        assertEquals("Text with id 1 not found", exception.getMessage());
    }

    @Test
    void testTextNotFoundExceptionWithDifferentId() {
        Long textId = 999L;
        
        TextNotFoundException exception = new TextNotFoundException(textId);
        
        assertEquals("Text with id 999 not found", exception.getMessage());
    }

    @Test
    void testTextNotFoundExceptionWithZeroId() {
        Long textId = 0L;
        
        TextNotFoundException exception = new TextNotFoundException(textId);
        
        assertEquals("Text with id 0 not found", exception.getMessage());
    }

    @Test
    void testTextNotFoundExceptionWithNegativeId() {
        Long textId = -1L;
        
        TextNotFoundException exception = new TextNotFoundException(textId);
        
        assertEquals("Text with id -1 not found", exception.getMessage());
    }

    @Test
    void testTextNotFoundExceptionExtendsRuntimeException() {
        TextNotFoundException exception = new TextNotFoundException(1L);
        
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testTextNotFoundExceptionCanBeCaught() {
        try {
            throw new TextNotFoundException(1L);
        } catch (TextNotFoundException e) {
            assertTrue(true);
            assertEquals("Text with id 1 not found", e.getMessage());
        }
    }

    @Test
    void testTextNotFoundExceptionWithLargeId() {
        Long textId = Long.MAX_VALUE;
        
        TextNotFoundException exception = new TextNotFoundException(textId);
        
        assertEquals("Text with id 9223372036854775807 not found", exception.getMessage());
    }

    @Test
    void testTextNotFoundExceptionMessageFormat() {
        Long textId = 42L;
        
        TextNotFoundException exception = new TextNotFoundException(textId);
        String message = exception.getMessage();
        
        assertNotNull(message);
        assertTrue(message.contains("Text"));
        assertTrue(message.contains("not found"));
        assertTrue(message.contains("42"));
    }
}
