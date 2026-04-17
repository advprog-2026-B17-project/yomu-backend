package id.ac.ui.cs.advprog.yomubackend.read_quiz.service;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.*;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.QuizNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.QuizAttemptNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.QuestionMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.*;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private GradingRepository gradingRepository;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private id.ac.ui.cs.advprog.yomubackend.event.EventPublisher eventPublisher;

    @InjectMocks
    private QuizServiceImpl quizService;

    private User user;
    private Quiz quiz;
    private Question question;
    private QuizAttempt quizAttempt;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "Test User", "test@example.com", null, "password");
        user.setId(1L);

        Text text = Text.builder()
                .id(1L)
                .title("Test Text")
                .content("Content")
                .build();

        quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Test Quiz")
                .build();

        question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("multiple_choice")
                .questionText("What is 2+2?")
                .options("[\"A\",\"B\",\"C\",\"D\"]")
                .correctAnswer("B")
                .build();

        quizAttempt = QuizAttempt.builder()
                .id(1L)
                .user(user)
                .quiz(quiz)
                .startedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetQuizByIdFound() {
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(questionRepository.findByQuizId(1L)).thenReturn(Arrays.asList(question));
        when(questionMapper.toDto(any(Question.class))).thenReturn(
                QuestionDto.builder().id(1L).kind("multiple_choice").questionText("What is 2+2?").build()
        );

        QuizDto result = quizService.getQuizById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Quiz", result.getTitle());
        verify(quizRepository).findById(1L);
    }

    @Test
    void testGetQuizByIdNotFound() {
        when(quizRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(QuizNotFoundException.class, () -> {
            quizService.getQuizById(999L);
        });

        verify(quizRepository).findById(999L);
    }

    @Test
    void testStartQuiz() {
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(invocation -> {
            QuizAttempt attempt = invocation.getArgument(0);
            attempt.setId(1L);
            return attempt;
        });

        QuizAttemptResultDto result = quizService.startQuiz(1L, user);

        assertNotNull(result);
        assertEquals(1L, result.getAttemptId());
        assertNotNull(result.getStartedAt());
        assertNull(result.getScore());
        verify(quizAttemptRepository).save(any(QuizAttempt.class));
    }

    @Test
    void testStartQuizNotFound() {
        when(quizRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(QuizNotFoundException.class, () -> {
            quizService.startQuiz(999L, user);
        });
    }

    @Test
    void testSubmitQuiz() {
        when(quizAttemptRepository.findById(1L)).thenReturn(Optional.of(quizAttempt));
        when(questionRepository.findByQuizId(1L)).thenReturn(Arrays.asList(question));
        when(answerRepository.save(any(Answer.class))).thenAnswer(invocation -> {
            Answer answer = invocation.getArgument(0);
            answer.setId(1L);
            return answer;
        });
        when(gradingRepository.save(any(Grading.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QuizSubmissionDto submission = QuizSubmissionDto.builder()
                .answers(Arrays.asList(
                        AnswerDto.builder().questionId(1L).userAnswer("B").build()
                ))
                .build();

        QuizAttemptResultDto result = quizService.submitQuiz(1L, submission, user);

        assertNotNull(result);
        assertEquals(1L, result.getAttemptId());
        assertEquals(1, result.getScore());
        assertNotNull(result.getSubmittedAt());
        assertEquals(1, result.getGradingResults().size());
    }

    @Test
    void testSubmitQuizIncorrectAnswer() {
        when(quizAttemptRepository.findById(1L)).thenReturn(Optional.of(quizAttempt));
        when(questionRepository.findByQuizId(1L)).thenReturn(Arrays.asList(question));
        when(answerRepository.save(any(Answer.class))).thenAnswer(invocation -> {
            Answer answer = invocation.getArgument(0);
            answer.setId(1L);
            return answer;
        });
        when(gradingRepository.save(any(Grading.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QuizSubmissionDto submission = QuizSubmissionDto.builder()
                .answers(Arrays.asList(
                        AnswerDto.builder().questionId(1L).userAnswer("A").build()
                ))
                .build();

        QuizAttemptResultDto result = quizService.submitQuiz(1L, submission, user);

        assertNotNull(result);
        assertEquals(0, result.getScore());
    }

    @Test
    void testSubmitQuizAlreadySubmitted() {
        quizAttempt.setSubmittedAt(LocalDateTime.now());
        when(quizAttemptRepository.findById(1L)).thenReturn(Optional.of(quizAttempt));

        QuizSubmissionDto submission = QuizSubmissionDto.builder()
                .answers(Arrays.asList(
                        AnswerDto.builder().questionId(1L).userAnswer("B").build()
                ))
                .build();

        assertThrows(IllegalStateException.class, () -> {
            quizService.submitQuiz(1L, submission, user);
        });
    }

    @Test
    void testSubmitQuizUnauthorized() {
        User otherUser = new User("otheruser", "Other User", "other@example.com", null, "password");
        otherUser.setId(2L);
        when(quizAttemptRepository.findById(1L)).thenReturn(Optional.of(quizAttempt));

        QuizSubmissionDto submission = QuizSubmissionDto.builder()
                .answers(Arrays.asList(
                        AnswerDto.builder().questionId(1L).userAnswer("B").build()
                ))
                .build();

        assertThrows(SecurityException.class, () -> {
            quizService.submitQuiz(1L, submission, otherUser);
        });
    }

    @Test
    void testGetAttemptResult() {
        quizAttempt.setScore(1);
        quizAttempt.setSubmittedAt(LocalDateTime.now());
        when(quizAttemptRepository.findById(1L)).thenReturn(Optional.of(quizAttempt));
        when(answerRepository.findByQuizAttempt(quizAttempt)).thenReturn(Arrays.asList(
                Answer.builder()
                        .id(1L)
                        .quizAttempt(quizAttempt)
                        .question(question)
                        .userAnswer("B")
                        .build()
        ));
        when(gradingRepository.findByAnswer(any(Answer.class))).thenReturn(Arrays.asList(
                Grading.builder()
                        .id(1L)
                        .isCorrect(true)
                        .score(1)
                        .feedback("Correct!")
                        .build()
        ));

        QuizAttemptResultDto result = quizService.getAttemptResult(1L, user);

        assertNotNull(result);
        assertEquals(1L, result.getAttemptId());
        assertEquals(1, result.getScore());
    }

    @Test
    void testGetAttemptResultNotFound() {
        when(quizAttemptRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(QuizAttemptNotFoundException.class, () -> {
            quizService.getAttemptResult(999L, user);
        });
    }

    @Test
    void testGetAttemptResultUnauthorized() {
        User otherUser = new User("otheruser", "Other User", "other@example.com", null, "password");
        otherUser.setId(2L);
        when(quizAttemptRepository.findById(1L)).thenReturn(Optional.of(quizAttempt));

        assertThrows(SecurityException.class, () -> {
            quizService.getAttemptResult(1L, otherUser);
        });
    }
}