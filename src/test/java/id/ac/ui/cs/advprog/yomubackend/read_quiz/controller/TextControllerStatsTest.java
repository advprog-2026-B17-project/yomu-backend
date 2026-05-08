package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextStatsDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.QuizAttempt;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.QuizAttemptRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.service.TextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TextControllerStatsTest {

    @Mock
    private TextService textService;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @InjectMocks
    private TextController textController;

    private QuizAttempt a1, a2;

    @BeforeEach
    void setUp() {
        Quiz quiz = Quiz.builder().id(1L).build();
        a1 = QuizAttempt.builder().id(1L).quiz(quiz).score(80).build();
        a2 = QuizAttempt.builder().id(2L).quiz(quiz).score(90).build();
        // ensure repository is injected (controller uses setter injection in code)
        textController.setQuizAttemptRepository(quizAttemptRepository);
    }

    @Test
    void getTextStats_ReturnsCorrectAggregates() {
        when(quizAttemptRepository.findSubmittedByTextId(1L)).thenReturn(List.of(a1, a2));

        ResponseEntity<TextStatsDto> resp = textController.getTextStats(1L);
        assertNotNull(resp);
        TextStatsDto dto = resp.getBody();
        assertNotNull(dto);
        assertEquals(2L, dto.getAttempts());
        assertEquals(85.0, dto.getAvgScore(), 0.0001);
    }

    @Test
    void getTextStats_WhenNoAttempts_ReturnsZero() {
        when(quizAttemptRepository.findSubmittedByTextId(2L)).thenReturn(List.of());
        ResponseEntity<TextStatsDto> resp = textController.getTextStats(2L);
        TextStatsDto dto = resp.getBody();
        assertNotNull(dto);
        assertEquals(0L, dto.getAttempts());
        assertEquals(0.0, dto.getAvgScore(), 0.0001);
    }
}
