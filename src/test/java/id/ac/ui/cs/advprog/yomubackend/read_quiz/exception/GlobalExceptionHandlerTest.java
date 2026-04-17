package id.ac.ui.cs.advprog.yomubackend.read_quiz.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleTextNotFound_Returns404() {
        TextNotFoundException exception = new TextNotFoundException(1L);
        
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleTextNotFound(exception);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("Text"));
    }

    @Test
    void handleQuizNotFound_Returns404() {
        QuizNotFoundException exception = new QuizNotFoundException(1L);
        
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleQuizNotFound(exception);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("Quiz"));
    }

    @Test
    void handleQuizAttemptNotFound_Returns404() {
        QuizAttemptNotFoundException exception = new QuizAttemptNotFoundException(1L);
        
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleQuizAttemptNotFound(exception);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("attempt"));
    }

    @Test
    void handleSecurityException_Returns403() {
        SecurityException exception = new SecurityException("Unauthorized");
        
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleSecurityException(exception);
        
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody().get("error"));
    }

    @Test
    void handleIllegalState_Returns400() {
        IllegalStateException exception = new IllegalStateException("Already submitted");
        
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleIllegalState(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Already submitted", response.getBody().get("error"));
    }

    @Test
    void handleQuizNotFound_WithDifferentId() {
        QuizNotFoundException exception = new QuizNotFoundException(999L);
        
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleQuizNotFound(exception);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("999"));
    }

    @Test
    void handleQuizAttemptNotFound_WithDifferentId() {
        QuizAttemptNotFoundException exception = new QuizAttemptNotFoundException(123L);
        
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleQuizAttemptNotFound(exception);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("123"));
    }

    @Test
    void globalExceptionHandler_IsControllerAdvice() {
        assertTrue(GlobalExceptionHandler.class.isAnnotationPresent(ControllerAdvice.class));
    }

    @Test
    void handleTextNotFound_HasExceptionHandler() throws NoSuchMethodException {
        var method = GlobalExceptionHandler.class.getMethod("handleTextNotFound", TextNotFoundException.class);
        assertTrue(method.isAnnotationPresent(ExceptionHandler.class));
    }

    @Test
    void handleQuizNotFound_HasExceptionHandler() throws NoSuchMethodException {
        var method = GlobalExceptionHandler.class.getMethod("handleQuizNotFound", QuizNotFoundException.class);
        assertTrue(method.isAnnotationPresent(ExceptionHandler.class));
    }

    @Test
    void handleQuizAttemptNotFound_HasExceptionHandler() throws NoSuchMethodException {
        var method = GlobalExceptionHandler.class.getMethod("handleQuizAttemptNotFound", QuizAttemptNotFoundException.class);
        assertTrue(method.isAnnotationPresent(ExceptionHandler.class));
    }
}