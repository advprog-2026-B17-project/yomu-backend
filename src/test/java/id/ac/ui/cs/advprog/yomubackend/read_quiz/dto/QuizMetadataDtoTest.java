package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class QuizMetadataDtoTest {

    @Test
    void testQuizMetadataDtoBuilder() {
        List<QuestionDto> sampleQuestions = Arrays.asList(
                QuestionDto.builder().id(1L).kind("multiple_choice").questionText("Q1").build(),
                QuestionDto.builder().id(2L).kind("multiple_choice").questionText("Q2").build()
        );
        
        QuizMetadataDto dto = QuizMetadataDto.builder()
                .id(1L)
                .title("Test Quiz")
                .totalQuestions(10)
                .sampleQuestions(sampleQuestions)
                .build();

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Quiz", dto.getTitle());
        assertEquals(10, dto.getTotalQuestions());
        assertEquals(2, dto.getSampleQuestions().size());
    }

    @Test
    void testQuizMetadataDtoNoArgsConstructor() {
        QuizMetadataDto dto = new QuizMetadataDto();
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getTotalQuestions());
        assertNull(dto.getSampleQuestions());
    }

    @Test
    void testQuizMetadataDtoAllArgsConstructor() {
        List<QuestionDto> questions = Arrays.asList(
                QuestionDto.builder().id(1L).build()
        );
        
        QuizMetadataDto dto = new QuizMetadataDto(2L, "All Args Quiz", 5, questions);
        
        assertEquals(2L, dto.getId());
        assertEquals("All Args Quiz", dto.getTitle());
        assertEquals(5, dto.getTotalQuestions());
        assertEquals(1, dto.getSampleQuestions().size());
    }

    @Test
    void testQuizMetadataDtoSetters() {
        QuizMetadataDto dto = QuizMetadataDto.builder().build();
        
        dto.setId(3L);
        dto.setTitle("Updated Quiz");
        dto.setTotalQuestions(15);
        dto.setSampleQuestions(Collections.emptyList());

        assertEquals(3L, dto.getId());
        assertEquals("Updated Quiz", dto.getTitle());
        assertEquals(15, dto.getTotalQuestions());
        assertTrue(dto.getSampleQuestions().isEmpty());
    }

    @Test
    void testQuizMetadataDtoEqualsAndHashCode() {
        QuizMetadataDto dto1 = QuizMetadataDto.builder()
                .id(1L)
                .title("Quiz")
                .totalQuestions(5)
                .build();
        
        QuizMetadataDto dto2 = QuizMetadataDto.builder()
                .id(1L)
                .title("Quiz")
                .totalQuestions(5)
                .build();
        
        QuizMetadataDto dto3 = QuizMetadataDto.builder()
                .id(2L)
                .title("Different")
                .totalQuestions(10)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testQuizMetadataDtoToString() {
        QuizMetadataDto dto = QuizMetadataDto.builder()
                .id(1L)
                .title("Test Quiz")
                .totalQuestions(10)
                .build();

        String toStringResult = dto.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("QuizMetadataDto"));
    }

    @Test
    void testQuizMetadataDtoWithNullSampleQuestions() {
        QuizMetadataDto dto = QuizMetadataDto.builder()
                .id(1L)
                .title("Quiz")
                .totalQuestions(10)
                .sampleQuestions(null)
                .build();

        assertNull(dto.getSampleQuestions());
    }

    @Test
    void testQuizMetadataDtoWithZeroTotalQuestions() {
        QuizMetadataDto dto = QuizMetadataDto.builder()
                .id(1L)
                .title("Empty Quiz")
                .totalQuestions(0)
                .build();

        assertEquals(0, dto.getTotalQuestions());
    }

    @Test
    void testQuizMetadataDtoWithLargeTotalQuestions() {
        QuizMetadataDto dto = QuizMetadataDto.builder()
                .id(1L)
                .title("Large Quiz")
                .totalQuestions(1000)
                .build();

        assertEquals(1000, dto.getTotalQuestions());
    }
}
