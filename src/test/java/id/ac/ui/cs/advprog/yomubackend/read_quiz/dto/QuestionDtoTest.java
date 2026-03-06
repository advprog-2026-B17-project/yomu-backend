package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class QuestionDtoTest {

    @Test
    void testQuestionDtoBuilder() {
        List<String> options = Arrays.asList("Paris", "London", "Berlin", "Madrid");
        
        QuestionDto dto = QuestionDto.builder()
                .id(1L)
                .kind("multiple_choice")
                .questionText("What is the capital of France?")
                .options(options)
                .build();

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("multiple_choice", dto.getKind());
        assertEquals("What is the capital of France?", dto.getQuestionText());
        assertEquals(4, dto.getOptions().size());
        assertEquals("Paris", dto.getOptions().get(0));
    }

    @Test
    void testQuestionDtoNoArgsConstructor() {
        QuestionDto dto = new QuestionDto();
        assertNull(dto.getId());
        assertNull(dto.getKind());
        assertNull(dto.getQuestionText());
        assertNull(dto.getOptions());
    }

    @Test
    void testQuestionDtoAllArgsConstructor() {
        List<String> options = Arrays.asList("A", "B", "C", "D");
        
        QuestionDto dto = new QuestionDto(2L, "short_answer", "What is 2+2?", options);
        
        assertEquals(2L, dto.getId());
        assertEquals("short_answer", dto.getKind());
        assertEquals("What is 2+2?", dto.getQuestionText());
        assertEquals(4, dto.getOptions().size());
    }

    @Test
    void testQuestionDtoSetters() {
        QuestionDto dto = QuestionDto.builder().build();
        
        dto.setId(3L);
        dto.setKind("true_false");
        dto.setQuestionText("Is the sky blue?");
        dto.setOptions(Arrays.asList("True", "False"));

        assertEquals(3L, dto.getId());
        assertEquals("true_false", dto.getKind());
        assertEquals("Is the sky blue?", dto.getQuestionText());
        assertEquals(2, dto.getOptions().size());
    }

    @Test
    void testQuestionDtoEqualsAndHashCode() {
        List<String> options1 = Arrays.asList("A", "B");
        List<String> options2 = Arrays.asList("A", "B");
        
        QuestionDto dto1 = QuestionDto.builder()
                .id(1L)
                .kind("test")
                .options(options1)
                .build();
        
        QuestionDto dto2 = QuestionDto.builder()
                .id(1L)
                .kind("test")
                .options(options2)
                .build();
        
        QuestionDto dto3 = QuestionDto.builder()
                .id(2L)
                .kind("different")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testQuestionDtoToString() {
        QuestionDto dto = QuestionDto.builder()
                .id(1L)
                .kind("multiple_choice")
                .questionText("Test?")
                .build();

        String toStringResult = dto.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("QuestionDto"));
    }

    @Test
    void testQuestionDtoWithNullOptions() {
        QuestionDto dto = QuestionDto.builder()
                .id(1L)
                .kind("short_answer")
                .questionText("What is 2+2?")
                .options(null)
                .build();

        assertNull(dto.getOptions());
    }

    @Test
    void testQuestionDtoWithEmptyOptions() {
        QuestionDto dto = QuestionDto.builder()
                .id(1L)
                .kind("short_answer")
                .questionText("Test?")
                .options(Arrays.asList())
                .build();

        assertNotNull(dto.getOptions());
        assertTrue(dto.getOptions().isEmpty());
    }

    @Test
    void testQuestionDtoWithSingleOption() {
        QuestionDto dto = QuestionDto.builder()
                .id(1L)
                .kind("short_answer")
                .questionText("Test?")
                .options(Arrays.asList("Single Option"))
                .build();

        assertEquals(1, dto.getOptions().size());
        assertEquals("Single Option", dto.getOptions().get(0));
    }
}
