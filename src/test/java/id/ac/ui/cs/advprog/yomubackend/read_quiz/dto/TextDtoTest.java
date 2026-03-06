package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TextDtoTest {

    @Test
    void testTextDtoBuilder() {
        List<QuizMetadataDto> quizzes = Arrays.asList(
                QuizMetadataDto.builder().id(1L).title("Quiz 1").totalQuestions(5).build(),
                QuizMetadataDto.builder().id(2L).title("Quiz 2").totalQuestions(10).build()
        );
        
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        TextDto dto = TextDto.builder()
                .id(1L)
                .title("Test Text")
                .category("technology")
                .content("This is test content")
                .createdById(1L)
                .createdByName("testuser")
                .createdAt(createdAt)
                .quizzes(quizzes)
                .build();

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Text", dto.getTitle());
        assertEquals("technology", dto.getCategory());
        assertEquals("This is test content", dto.getContent());
        assertEquals(1L, dto.getCreatedById());
        assertEquals("testuser", dto.getCreatedByName());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(2, dto.getQuizzes().size());
    }

    @Test
    void testTextDtoNoArgsConstructor() {
        TextDto dto = new TextDto();
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getCategory());
        assertNull(dto.getContent());
        assertNull(dto.getCreatedById());
        assertNull(dto.getCreatedByName());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getQuizzes());
    }

    @Test
    void testTextDtoAllArgsConstructor() {
        LocalDateTime createdAt = LocalDateTime.now();
        
        TextDto dto = new TextDto(
                2L,
                "All Args Text",
                "category",
                "content",
                1L,
                "user",
                createdAt,
                Collections.emptyList()
        );
        
        assertEquals(2L, dto.getId());
        assertEquals("All Args Text", dto.getTitle());
        assertEquals("category", dto.getCategory());
        assertEquals("content", dto.getContent());
        assertEquals(1L, dto.getCreatedById());
        assertEquals("user", dto.getCreatedByName());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void testTextDtoSetters() {
        TextDto dto = TextDto.builder().build();
        
        LocalDateTime newCreatedAt = LocalDateTime.of(2024, 2, 1, 12, 0);
        
        dto.setId(3L);
        dto.setTitle("Updated Text");
        dto.setCategory("science");
        dto.setContent("Updated content");
        dto.setCreatedById(2L);
        dto.setCreatedByName("newuser");
        dto.setCreatedAt(newCreatedAt);
        dto.setQuizzes(Collections.emptyList());

        assertEquals(3L, dto.getId());
        assertEquals("Updated Text", dto.getTitle());
        assertEquals("science", dto.getCategory());
        assertEquals("Updated content", dto.getContent());
        assertEquals(2L, dto.getCreatedById());
        assertEquals("newuser", dto.getCreatedByName());
        assertEquals(newCreatedAt, dto.getCreatedAt());
        assertTrue(dto.getQuizzes().isEmpty());
    }

    @Test
    void testTextDtoEqualsAndHashCode() {
        TextDto dto1 = TextDto.builder()
                .id(1L)
                .title("Text")
                .category("tech")
                .build();
        
        TextDto dto2 = TextDto.builder()
                .id(1L)
                .title("Text")
                .category("tech")
                .build();
        
        TextDto dto3 = TextDto.builder()
                .id(2L)
                .title("Different")
                .category("science")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testTextDtoToString() {
        TextDto dto = TextDto.builder()
                .id(1L)
                .title("Test Text")
                .build();

        String toStringResult = dto.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("TextDto"));
    }

    @Test
    void testTextDtoWithNullQuizzes() {
        TextDto dto = TextDto.builder()
                .id(1L)
                .title("Test Text")
                .quizzes(null)
                .build();

        assertNull(dto.getQuizzes());
    }

    @Test
    void testTextDtoWithNullCategory() {
        TextDto dto = TextDto.builder()
                .id(1L)
                .title("Test Text")
                .category(null)
                .build();

        assertNull(dto.getCategory());
    }

    @Test
    void testTextDtoWithNullContent() {
        TextDto dto = TextDto.builder()
                .id(1L)
                .title("Test Text")
                .content(null)
                .build();

        assertNull(dto.getContent());
    }

    @Test
    void testTextDtoWithNullCreatedBy() {
        TextDto dto = TextDto.builder()
                .id(1L)
                .title("Test Text")
                .createdById(null)
                .createdByName(null)
                .build();

        assertNull(dto.getCreatedById());
        assertNull(dto.getCreatedByName());
    }
}
