package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.AdminQuestionDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.AdminQuizDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.QuizNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.TextNotFoundException;
// import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Question;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.QuestionRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.QuizRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.TextRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminQuizControllerTest {

    @Mock
    private TextRepository textRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizService quizService;

    @InjectMocks
    private AdminQuizController adminQuizController;

    private Text text;
    private Quiz quiz;

    @BeforeEach
    void setUp() {
        text = Text.builder().id(1L).title("T").build();
        quiz = Quiz.builder().id(2L).title("Q").text(text).questions(new ArrayList<>()).build();
    }

    @Test
    void createQuiz_Succeeds() {
        when(textRepository.findById(1L)).thenReturn(Optional.of(text));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(inv -> {
            Quiz q = inv.getArgument(0);
            q.setId(2L);
            return q;
        });
        when(quizService.getQuizById(2L)).thenReturn(null);

        AdminQuizDto dto = AdminQuizDto.builder().title("New Quiz").questions(List.of(AdminQuestionDto.builder().kind("multiple_choice").questionText("Q?").options(List.of("A","B")).correctAnswer("A").build())).build();

        ResponseEntity<?> resp = adminQuizController.createQuiz(1L, dto);
        assertEquals(201, resp.getStatusCodeValue());
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void createQuiz_TextNotFound_Throws() {
        when(textRepository.findById(9L)).thenReturn(Optional.empty());
        AdminQuizDto dto = AdminQuizDto.builder().title("New Quiz").build();
        assertThrows(TextNotFoundException.class, () -> adminQuizController.createQuiz(9L, dto));
    }

    @Test
    void updateQuiz_Succeeds() {
        when(quizRepository.findById(2L)).thenReturn(Optional.of(quiz));
        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);
        when(quizService.getQuizById(2L)).thenReturn(null);

        AdminQuizDto dto = AdminQuizDto.builder().title("Updated").build();
        ResponseEntity<?> resp = adminQuizController.updateQuiz(2L, dto);
        assertEquals(200, resp.getStatusCodeValue());
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void updateQuiz_NotFound_Throws() {
        when(quizRepository.findById(99L)).thenReturn(Optional.empty());
        AdminQuizDto dto = AdminQuizDto.builder().title("X").build();
        assertThrows(QuizNotFoundException.class, () -> adminQuizController.updateQuiz(99L, dto));
    }

    @Test
    void deleteQuiz_Succeeds() {
        when(quizRepository.existsById(2L)).thenReturn(true);
        ResponseEntity<Void> resp = adminQuizController.deleteQuiz(2L);
        assertEquals(204, resp.getStatusCodeValue());
        verify(quizRepository).deleteById(2L);
    }

    @Test
    void deleteQuiz_NotFound_Throws() {
        when(quizRepository.existsById(99L)).thenReturn(false);
        assertThrows(QuizNotFoundException.class, () -> adminQuizController.deleteQuiz(99L));
    }
}
