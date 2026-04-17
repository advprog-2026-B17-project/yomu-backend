package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AnswerDtoTest {

    @Test
    void testAnswerDtoBuilder() {
        AnswerDto dto = AnswerDto.builder()
                .questionId(1L)
                .userAnswer("B")
                .build();

        assertNotNull(dto);
        assertEquals(1L, dto.getQuestionId());
        assertEquals("B", dto.getUserAnswer());
    }

    @Test
    void testAnswerDtoNoArgsConstructor() {
        AnswerDto dto = new AnswerDto();
        assertNotNull(dto);
    }

    @Test
    void testAnswerDtoAllArgsConstructor() {
        AnswerDto dto = new AnswerDto(1L, "C");
        assertNotNull(dto);
        assertEquals(1L, dto.getQuestionId());
        assertEquals("C", dto.getUserAnswer());
    }

    @Test
    void testAnswerDtoSetters() {
        AnswerDto dto = new AnswerDto();
        dto.setQuestionId(2L);
        dto.setUserAnswer("D");

        assertEquals(2L, dto.getQuestionId());
        assertEquals("D", dto.getUserAnswer());
    }
}