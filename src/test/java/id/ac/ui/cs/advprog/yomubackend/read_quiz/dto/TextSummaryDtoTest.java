package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextSummaryDtoTest {

    @Test
    void testTextSummaryDtoBuilder() {
        TextSummaryDto dto = TextSummaryDto.builder()
                .id(1L)
                .title("Test Summary")
                .category("technology")
                .excerpt("This is a short excerpt of the text...")
                .createdById(1L)
                .createdByName("testuser")
                .build();

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Summary", dto.getTitle());
        assertEquals("technology", dto.getCategory());
        assertEquals("This is a short excerpt of the text...", dto.getExcerpt());
        assertEquals(1L, dto.getCreatedById());
        assertEquals("testuser", dto.getCreatedByName());
    }

    @Test
    void testTextSummaryDtoNoArgsConstructor() {
        TextSummaryDto dto = new TextSummaryDto();
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getCategory());
        assertNull(dto.getExcerpt());
        assertNull(dto.getCreatedById());
        assertNull(dto.getCreatedByName());
    }

    @Test
    void testTextSummaryDtoAllArgsConstructor() {
        TextSummaryDto dto = new TextSummaryDto(
                2L,
                "All Args Summary",
                "science",
                "Full excerpt here...",
                1L,
                "user"
        );
        
        assertEquals(2L, dto.getId());
        assertEquals("All Args Summary", dto.getTitle());
        assertEquals("science", dto.getCategory());
        assertEquals("Full excerpt here...", dto.getExcerpt());
        assertEquals(1L, dto.getCreatedById());
        assertEquals("user", dto.getCreatedByName());
    }

    @Test
    void testTextSummaryDtoSetters() {
        TextSummaryDto dto = TextSummaryDto.builder().build();
        
        dto.setId(3L);
        dto.setTitle("Updated Summary");
        dto.setCategory("education");
        dto.setExcerpt("Updated excerpt");
        dto.setCreatedById(2L);
        dto.setCreatedByName("newuser");

        assertEquals(3L, dto.getId());
        assertEquals("Updated Summary", dto.getTitle());
        assertEquals("education", dto.getCategory());
        assertEquals("Updated excerpt", dto.getExcerpt());
        assertEquals(2L, dto.getCreatedById());
        assertEquals("newuser", dto.getCreatedByName());
    }

    @Test
    void testTextSummaryDtoEqualsAndHashCode() {
        TextSummaryDto dto1 = TextSummaryDto.builder()
                .id(1L)
                .title("Summary")
                .category("tech")
                .excerpt("Excerpt...")
                .build();
        
        TextSummaryDto dto2 = TextSummaryDto.builder()
                .id(1L)
                .title("Summary")
                .category("tech")
                .excerpt("Excerpt...")
                .build();
        
        TextSummaryDto dto3 = TextSummaryDto.builder()
                .id(2L)
                .title("Different")
                .category("science")
                .excerpt("Different excerpt...")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testTextSummaryDtoToString() {
        TextSummaryDto dto = TextSummaryDto.builder()
                .id(1L)
                .title("Test Summary")
                .build();

        String toStringResult = dto.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("TextSummaryDto"));
    }

    @Test
    void testTextSummaryDtoWithNullCategory() {
        TextSummaryDto dto = TextSummaryDto.builder()
                .id(1L)
                .title("Test")
                .category(null)
                .build();

        assertNull(dto.getCategory());
    }

    @Test
    void testTextSummaryDtoWithNullExcerpt() {
        TextSummaryDto dto = TextSummaryDto.builder()
                .id(1L)
                .title("Test")
                .excerpt(null)
                .build();

        assertNull(dto.getExcerpt());
    }

    @Test
    void testTextSummaryDtoWithEmptyExcerpt() {
        TextSummaryDto dto = TextSummaryDto.builder()
                .id(1L)
                .title("Test")
                .excerpt("")
                .build();

        assertEquals("", dto.getExcerpt());
    }

    @Test
    void testTextSummaryDtoWithNullCreatedBy() {
        TextSummaryDto dto = TextSummaryDto.builder()
                .id(1L)
                .title("Test")
                .createdById(null)
                .createdByName(null)
                .build();

        assertNull(dto.getCreatedById());
        assertNull(dto.getCreatedByName());
    }

    @Test
    void testTextSummaryDtoWithLongExcerpt() {
        StringBuilder longExcerpt = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            longExcerpt.append("a");
        }
        
        TextSummaryDto dto = TextSummaryDto.builder()
                .id(1L)
                .title("Long Excerpt Test")
                .excerpt(longExcerpt.toString())
                .build();

        assertEquals(300, dto.getExcerpt().length());
    }
}
