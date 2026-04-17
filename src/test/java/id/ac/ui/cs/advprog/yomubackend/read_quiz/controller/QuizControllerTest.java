package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.*;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.QuizNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.QuizAttemptNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class QuizControllerTest {

    @Mock
    private QuizService quizService;

    @Mock
    private id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository userRepository;

    @InjectMocks
    private QuizController quizController;

    private User mockUser;
    private UserDetails mockPrincipal;
    private QuizDto sampleQuizDto;
    private QuizAttemptResultDto sampleAttemptResult;
    private QuizSubmissionDto sampleSubmission;

    @BeforeEach
    void setUp() {
        mockUser = new User("testuser", "Test User", "test@example.com", null, "password");
        mockUser.setId(1L);

        mockPrincipal = mock(UserDetails.class);
        lenient().when(mockPrincipal.getUsername()).thenReturn("testuser");
        lenient().when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        sampleQuizDto = QuizDto.builder()
                .id(1L)
                .title("Sample Quiz")
                .questions(Collections.emptyList())
                .build();

        sampleAttemptResult = QuizAttemptResultDto.builder()
                .attemptId(1L)
                .score(100)
                .startedAt(LocalDateTime.now())
                .submittedAt(LocalDateTime.now())
                .gradingResults(Collections.emptyList())
                .build();

        sampleSubmission = QuizSubmissionDto.builder()
                .answers(Arrays.asList(
                        AnswerDto.builder().questionId(1L).userAnswer("A").build()
                ))
                .build();
    }

    @Test
    void getQuizById_ReturnsQuizDto_WhenQuizExists() {
        when(quizService.getQuizById(1L)).thenReturn(sampleQuizDto);

        ResponseEntity<QuizDto> response = quizController.getQuizById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sample Quiz", response.getBody().getTitle());
        verify(quizService, times(1)).getQuizById(1L);
    }

    @Test
    void getQuizById_ThrowsException_WhenQuizNotFound() {
        when(quizService.getQuizById(999L)).thenThrow(new QuizNotFoundException(999L));

        assertThrows(QuizNotFoundException.class, () -> {
            quizController.getQuizById(999L);
        });
    }

    @Test
    void startQuiz_ReturnsAttemptResult_WhenQuizExists() {
        when(quizService.startQuiz(1L, mockUser)).thenReturn(sampleAttemptResult);

        ResponseEntity<QuizAttemptResultDto> response = quizController.startQuiz(1L, mockPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getAttemptId());
        verify(quizService, times(1)).startQuiz(1L, mockUser);
    }

    @Test
    void startQuiz_ThrowsException_WhenQuizNotFound() {
        when(quizService.startQuiz(999L, mockUser)).thenThrow(new QuizNotFoundException(999L));

        assertThrows(QuizNotFoundException.class, () -> {
            quizController.startQuiz(999L, mockPrincipal);
        });
    }

    @Test
    void submitQuiz_ReturnsResult_WhenSubmissionIsValid() {
        when(quizService.submitQuiz(eq(1L), any(QuizSubmissionDto.class), eq(mockUser))).thenReturn(sampleAttemptResult);

        ResponseEntity<QuizAttemptResultDto> response = quizController.submitQuiz(1L, sampleSubmission, mockPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100, response.getBody().getScore());
        verify(quizService, times(1)).submitQuiz(eq(1L), any(QuizSubmissionDto.class), eq(mockUser));
    }

    @Test
    void submitQuiz_ThrowsException_WhenAttemptNotFound() {
        when(quizService.submitQuiz(eq(999L), any(QuizSubmissionDto.class), eq(mockUser)))
            .thenThrow(new QuizAttemptNotFoundException(999L));

        assertThrows(QuizAttemptNotFoundException.class, () -> {
            quizController.submitQuiz(999L, sampleSubmission, mockPrincipal);
        });
    }

    @Test
    void getAttemptResult_ReturnsResult_WhenAttemptExists() {
        when(quizService.getAttemptResult(1L, mockUser)).thenReturn(sampleAttemptResult);

        ResponseEntity<QuizAttemptResultDto> response = quizController.getAttemptResult(1L, mockPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(quizService, times(1)).getAttemptResult(1L, mockUser);
    }

    @Test
    void getAttemptResult_ThrowsException_WhenAttemptNotFound() {
        when(quizService.getAttemptResult(999L, mockUser)).thenThrow(new QuizAttemptNotFoundException(999L));

        assertThrows(QuizAttemptNotFoundException.class, () -> {
            quizController.getAttemptResult(999L, mockPrincipal);
        });
    }

    @Test
    void getQuizById_ReturnsQuizWithQuestions() {
        QuestionDto questionDto = QuestionDto.builder()
                .id(1L)
                .kind("multiple_choice")
                .questionText("What is 2+2?")
                .build();
        QuizDto quizWithQuestions = QuizDto.builder()
                .id(1L)
                .title("Math Quiz")
                .questions(Collections.singletonList(questionDto))
                .build();
        when(quizService.getQuizById(1L)).thenReturn(quizWithQuestions);

        ResponseEntity<QuizDto> response = quizController.getQuizById(1L);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getQuestions().size());
    }

    @Test
    void startQuiz_SetsStartedAtTimestamp() {
        when(quizService.startQuiz(1L, mockUser)).thenReturn(sampleAttemptResult);

        ResponseEntity<QuizAttemptResultDto> response = quizController.startQuiz(1L, mockPrincipal);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getStartedAt());
    }

    @Test
    void submitQuiz_ReturnsGradingResults() {
        GradingResultDto gradingResult = GradingResultDto.builder()
                .questionId(1L)
                .isCorrect(true)
                .score(1)
                .feedback("Correct!")
                .correctAnswer("A")
                .build();
        QuizAttemptResultDto resultWithGrading = QuizAttemptResultDto.builder()
                .attemptId(1L)
                .score(1)
                .startedAt(LocalDateTime.now())
                .submittedAt(LocalDateTime.now())
                .gradingResults(Collections.singletonList(gradingResult))
                .build();
        when(quizService.submitQuiz(eq(1L), any(QuizSubmissionDto.class), eq(mockUser)))
            .thenReturn(resultWithGrading);

        ResponseEntity<QuizAttemptResultDto> response = quizController.submitQuiz(1L, sampleSubmission, mockPrincipal);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getGradingResults().size());
    }

    @Test
    void constructor_InjectsQuizService() {
        QuizController controller = new QuizController(quizService, userRepository);
        assertNotNull(controller);
    }
}