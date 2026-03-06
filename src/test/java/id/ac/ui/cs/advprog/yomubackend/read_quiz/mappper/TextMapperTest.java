package id.ac.ui.cs.advprog.yomubackend.read_quiz.mappper;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextSummaryDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.QuestionMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.QuizMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.TextMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TextMapperTest {

    private TextMapper textMapper;
    private QuestionMapper questionMapper;
    private QuizMapper quizMapper;

    @BeforeEach
    void setUp() {
        questionMapper = new QuestionMapper();
        quizMapper = new QuizMapper(questionMapper);
        textMapper = new TextMapper(quizMapper);
    }

    @Test
    void testToSummaryWithValidText() {
        // Create a mock User
        User user = new User("testuser", "test@example.com", "password");
        user.setId(1L);

        // Create Text with content > 200 chars to test excerpt
        StringBuilder longContentBuilder = new StringBuilder();
        for (int i = 0; i < 250; i++) {
            longContentBuilder.append("a");
        }
        String longContent = longContentBuilder.toString();

        Text text = Text.builder()
                .id(1L)
                .title("Test Text")
                .content(longContent)
                .category("technology")
                .createdBy(user)
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .quizzes(new ArrayList<>())
                .build();

        TextSummaryDto dto = textMapper.toSummary(text);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Text", dto.getTitle());
        assertEquals("technology", dto.getCategory());
        assertNotNull(dto.getExcerpt());
        assertTrue(dto.getExcerpt().endsWith("..."));
        // Should be truncated to 200 chars + "..."
        assertEquals(203, dto.getExcerpt().length());
        assertEquals(1L, dto.getCreatedById());
        assertEquals("testuser", dto.getCreatedByName());
    }

    @Test
    void testToSummaryWithNullText() {
        TextSummaryDto dto = textMapper.toSummary(null);
        assertNull(dto);
    }

    @Test
    void testToSummaryWithShortContent() {
        User user = new User("testuser", "test@example.com", "password");
        
        Text text = Text.builder()
                .id(1L)
                .title("Short Text")
                .content("Short content")
                .category("general")
                .createdBy(user)
                .build();

        TextSummaryDto dto = textMapper.toSummary(text);

        assertNotNull(dto);
        assertEquals("Short content", dto.getExcerpt()); // Should not have "..."
    }

    @Test
    void testToSummaryWithNullContent() {
        User user = new User("testuser", "test@example.com", "password");
        
        Text text = Text.builder()
                .id(1L)
                .title("Null Content Text")
                .content(null)
                .createdBy(user)
                .build();

        TextSummaryDto dto = textMapper.toSummary(text);

        assertNotNull(dto);
        assertEquals("", dto.getExcerpt());
    }

    @Test
    void testToSummaryWithNullCreatedBy() {
        Text text = Text.builder()
                .id(1L)
                .title("No Creator Text")
                .content("Content")
                .category("tech")
                .createdBy(null)
                .build();

        TextSummaryDto dto = textMapper.toSummary(text);

        assertNotNull(dto);
        assertNull(dto.getCreatedById());
        assertNull(dto.getCreatedByName());
    }

    @Test
    void testToDtoWithQuizMetadata() {
        User user = new User("testuser", "test@example.com", "password");
        user.setId(1L);

        // Create quizzes
        List<Quiz> quizzes = Arrays.asList(
                Quiz.builder().id(1L).title("Quiz 1").questions(new ArrayList<>()).build(),
                Quiz.builder().id(2L).title("Quiz 2").questions(new ArrayList<>()).build()
        );

        Text text = Text.builder()
                .id(1L)
                .title("Test Text")
                .content("Content")
                .category("technology")
                .createdBy(user)
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .quizzes(quizzes)
                .build();

        TextDto dto = textMapper.toDto(text, true);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Text", dto.getTitle());
        assertEquals("technology", dto.getCategory());
        assertEquals("Content", dto.getContent());
        assertNotNull(dto.getQuizzes());
        assertEquals(2, dto.getQuizzes().size());
    }

    @Test
    void testToDtoWithoutQuizMetadata() {
        User user = new User("testuser", "test@example.com", "password");
        
        Text text = Text.builder()
                .id(1L)
                .title("Test Text")
                .content("Content")
                .category("tech")
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .quizzes(new ArrayList<>())
                .build();

        TextDto dto = textMapper.toDto(text, false);

        assertNotNull(dto);
        assertNull(dto.getQuizzes()); // Should be null when false
    }

    @Test
    void testToDtoWithNullText() {
        TextDto dto = textMapper.toDto(null, true);
        assertNull(dto);
    }

    @Test
    void testToDtoWithNullQuizzes() {
        User user = new User("testuser", "test@example.com", "password");
        
        Text text = Text.builder()
                .id(1L)
                .title("Test")
                .content("Content")
                .createdBy(user)
                .quizzes(null)
                .build();

        TextDto dto = textMapper.toDto(text, true);

        assertNotNull(dto);
        assertNull(dto.getQuizzes());
    }

    @Test
    void testToDtoWithNullCreatedBy() {
        Text text = Text.builder()
                .id(1L)
                .title("No Creator Text")
                .content("Content")
                .category("tech")
                .createdBy(null)
                .build();

        TextDto dto = textMapper.toDto(text, false);

        assertNotNull(dto);
        assertNull(dto.getCreatedById());
        assertNull(dto.getCreatedByName());
    }

    @Test
    void testToDtoWithEmptyQuizzesList() {
        User user = new User("testuser", "test@example.com", "password");
        
        Text text = Text.builder()
                .id(1L)
                .title("Test")
                .content("Content")
                .createdBy(user)
                .quizzes(new ArrayList<>())
                .build();

        TextDto dto = textMapper.toDto(text, true);

        assertNotNull(dto);
        assertNotNull(dto.getQuizzes());
        assertTrue(dto.getQuizzes().isEmpty());
    }

    @Test
    void testToDtoContentField() {
        User user = new User("testuser", "test@example.com", "password");
        
        String fullContent = "This is the full content of the text.";
        
        Text text = Text.builder()
                .id(1L)
                .title("Test")
                .content(fullContent)
                .createdBy(user)
                .build();

        TextDto dto = textMapper.toDto(text, false);

        assertNotNull(dto);
        assertEquals(fullContent, dto.getContent());
    }
}
