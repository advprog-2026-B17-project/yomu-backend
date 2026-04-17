package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GradingResultDtoTest {

    @Test
    void testGradingResultDtoBuilder() {
        GradingResultDto dto = GradingResultDto.builder()
                .questionId(1L)
                .isCorrect(true)
                .score(1)
                .feedback("Correct!")
                .correctAnswer("B")
                .build();

        assertNotNull(dto);
        assertEquals(1L, dto.getQuestionId());
        assertTrue(dto.getIsCorrect());
        assertEquals(1, dto.getScore());
        assertEquals("Correct!", dto.getFeedback());
        assertEquals("B", dto.getCorrectAnswer());
    }

    @Test
    void testGradingResultDtoIncorrect() {
        GradingResultDto dto = GradingResultDto.builder()
                .questionId(1L)
                .isCorrect(false)
                .score(0)
                .feedback("Incorrect")
                .correctAnswer("B")
                .build();

        assertFalse(dto.getIsCorrect());
        assertEquals(0, dto.getScore());
    }

    @Test
    void testGradingResultDtoNoArgsConstructor() {
        GradingResultDto dto = new GradingResultDto();
        assertNotNull(dto);
    }

    @Test
    void testGradingResultDtoAllArgsConstructor() {
        GradingResultDto dto = new GradingResultDto(1L, true, 1, "Good job!", "A", "A");
        assertNotNull(dto);
        assertEquals(1L, dto.getQuestionId());
    }

    @Test
    void testGradingResultDtoSetters() {
        GradingResultDto dto = new GradingResultDto();
        dto.setQuestionId(2L);
        dto.setIsCorrect(false);
        dto.setScore(0);
        dto.setFeedback("Wrong");
        dto.setCorrectAnswer("C");

        assertEquals(2L, dto.getQuestionId());
        assertFalse(dto.getIsCorrect());
        assertEquals(0, dto.getScore());
        assertEquals("Wrong", dto.getFeedback());
        assertEquals("C", dto.getCorrectAnswer());
    }
}