package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class TextTest {

    private Text text;
    private User user;

    @BeforeEach
    void setUp() {
        // Create a mock User
        user = new User("testuser", "test@example.com", "password");
        user.setId(1L);

        // Create a mock Text
        text = Text.builder()
                .id(1L)
                .title("Sample Text Title")
                .content("This is a sample content for testing purposes. " +
                        "It contains enough text to test excerpt generation.")
                .category("technology")
                .createdBy(user)
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .quizzes(new ArrayList<>())
                .build();
    }

    @Test
    void testTextBuilder() {
        assertNotNull(text);
        assertEquals(1L, text.getId());
        assertEquals("Sample Text Title", text.getTitle());
        assertEquals("technology", text.getCategory());
        assertEquals(user, text.getCreatedBy());
        assertNotNull(text.getCreatedAt());
        assertNotNull(text.getQuizzes());
    }

    @Test
    void testTextNoArgsConstructor() {
        Text emptyText = new Text();
        assertNull(emptyText.getId());
        assertNull(emptyText.getTitle());
        assertNull(emptyText.getContent());
        assertNull(emptyText.getCategory());
        assertNull(emptyText.getCreatedBy());
        assertNull(emptyText.getCreatedAt());
        assertNull(emptyText.getQuizzes());
    }

    @Test
    void testTextAllArgsConstructor() {
        LocalDateTime createdAt = LocalDateTime.now();
        
        Text allArgsText = new Text(
                2L,
                "All Args Title",
                "Content here",
                "science",
                user,
                createdAt,
                new ArrayList<>()
        );
        
        assertEquals(2L, allArgsText.getId());
        assertEquals("All Args Title", allArgsText.getTitle());
        assertEquals("Content here", allArgsText.getContent());
        assertEquals("science", allArgsText.getCategory());
        assertEquals(user, allArgsText.getCreatedBy());
        assertEquals(createdAt, allArgsText.getCreatedAt());
    }

    @Test
    void testTextSetters() {
        text.setId(2L);
        text.setTitle("Updated Title");
        text.setContent("Updated content");
        text.setCategory("education");
        
        assertEquals(2L, text.getId());
        assertEquals("Updated Title", text.getTitle());
        assertEquals("Updated content", text.getContent());
        assertEquals("education", text.getCategory());
    }

    @Test
    void testTextEqualsAndHashCode() {
        Text text1 = Text.builder()
                .id(1L)
                .title("Test")
                .build();
        
        Text text2 = Text.builder()
                .id(1L)
                .title("Test")
                .build();
        
        Text text3 = Text.builder()
                .id(2L)
                .title("Different")
                .build();

        assertEquals(text1, text2);
        assertEquals(text1.hashCode(), text2.hashCode());
        assertNotEquals(text1, text3);
    }

    @Test
    void testTextToString() {
        String toStringResult = text.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Text"));
        assertTrue(toStringResult.contains("id=1"));
    }

    @Test
    void testTextCreatedByRelationship() {
        assertNotNull(text.getCreatedBy());
        assertEquals("testuser", text.getCreatedBy().getUsername());
    }

    @Test
    void testTextWithNullCategory() {
        Text textNoCategory = Text.builder()
                .id(3L)
                .title("No Category")
                .content("Content")
                .category(null)
                .createdBy(user)
                .build();
        assertNull(textNoCategory.getCategory());
    }

    @Test
    void testTextWithNullContent() {
        Text textNoContent = Text.builder()
                .id(4L)
                .title("No Content")
                .content(null)
                .createdBy(user)
                .build();
        assertNull(textNoContent.getContent());
    }

    @Test
    void testTextWithNullCreatedBy() {
        Text textNoCreator = Text.builder()
                .id(5L)
                .title("No Creator")
                .content("Content")
                .build();
        assertNull(textNoCreator.getCreatedBy());
    }

    @Test
    void testTextWithNullQuizzes() {
        Text textNoQuizzes = Text.builder()
                .id(6L)
                .title("No Quizzes")
                .content("Content")
                .quizzes(null)
                .build();
        assertNull(textNoQuizzes.getQuizzes());
    }

    @Test
    void testTextWithEmptyQuizzesList() {
        Text textEmptyQuizzes = Text.builder()
                .id(7L)
                .title("Empty Quizzes")
                .content("Content")
                .quizzes(new ArrayList<>())
                .build();
        assertNotNull(textEmptyQuizzes.getQuizzes());
        assertTrue(textEmptyQuizzes.getQuizzes().isEmpty());
    }

    @Test
    void testTextWithLongContent() {
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            longContent.append("a");
        }
        
        Text textLongContent = Text.builder()
                .id(8L)
                .title("Long Content")
                .content(longContent.toString())
                .createdBy(user)
                .build();
        
        assertNotNull(textLongContent.getContent());
        assertEquals(500, textLongContent.getContent().length());
    }

    @Test
    void testTextWithSpecialCharactersInTitle() {
        Text textSpecialTitle = Text.builder()
                .id(9L)
                .title("Special Characters: <>&\"'Test")
                .content("Content with special chars: @#$%^&*()")
                .createdBy(user)
                .build();
        
        assertEquals("Special Characters: <>&\"'Test", textSpecialTitle.getTitle());
    }

    @Test
    void testTextCreatedAtField() {
        assertNotNull(text.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), text.getCreatedAt());
    }
}
