package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class QuizDtoTest {

    @Test
    void testQuizDtoBuilder() {
        QuestionDto questionDto = QuestionDto.builder()
                .id(1L)
                .kind("multiple_choice")
                .questionText("What is 2+2?")
                .build();

        QuizDto dto = QuizDto.builder()
                .id(1L)
                .title("Test Quiz")
                .questions(Collections.singletonList(questionDto))
                .build();

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Quiz", dto.getTitle());
        assertEquals(1, dto.getQuestions().size());
    }

    @Test
    void testQuizDtoNoArgsConstructor() {
        QuizDto dto = new QuizDto();
        assertNotNull(dto);
    }

    @Test
    void testQuizDtoAllArgsConstructor() {
        List<QuestionDto> questions = Arrays.asList(
                QuestionDto.builder().id(1L).kind("q1").questionText("Q1").build()
        );
        QuizDto dto = new QuizDto(1L, "Quiz", questions);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Quiz", dto.getTitle());
    }

    @Test
    void testQuizDtoSetters() {
        QuizDto dto = new QuizDto();
        dto.setId(1L);
        dto.setTitle("Test Quiz");

        assertEquals(1L, dto.getId());
        assertEquals("Test Quiz", dto.getTitle());
    }

    @Test
    void testQuizDtoEmptyQuestions() {
        QuizDto dto = QuizDto.builder()
                .id(1L)
                .title("Empty Quiz")
                .questions(Collections.emptyList())
                .build();

        assertNotNull(dto);
        assertTrue(dto.getQuestions().isEmpty());
    }
}