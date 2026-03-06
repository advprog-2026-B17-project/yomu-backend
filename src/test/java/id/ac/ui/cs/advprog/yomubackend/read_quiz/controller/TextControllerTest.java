package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextSummaryDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.TextNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.service.TextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextControllerTest {

    @Mock
    private TextService textService;

    @InjectMocks
    private TextController textController;

    private TextSummaryDto sampleTextSummary;
    private TextDto sampleTextDto;

    @BeforeEach
    void setUp() {
        sampleTextSummary = TextSummaryDto.builder()
                .id(1L)
                .title("Sample Title")
                .category("Technology")
                .excerpt("This is a sample excerpt...")
                .createdById(1L)
                .createdByName("John Doe")
                .build();

        sampleTextDto = TextDto.builder()
                .id(1L)
                .title("Sample Title")
                .category("Technology")
                .content("This is the full content of the text.")
                .createdById(1L)
                .createdByName("John Doe")
                .createdAt(LocalDateTime.now())
                .quizzes(Collections.emptyList())
                .build();
    }

    // Positive tests for listTexts
    @Test
    void listTexts_ReturnsPagedResults_WhenQueryAndCategoryAreProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<TextSummaryDto> page = new PageImpl<>(List.of(sampleTextSummary), pageable, 1);
        when(textService.listTexts(any(Pageable.class), eq("sample"), eq("Technology"))).thenReturn(page);

        // Act
        ResponseEntity<Page<TextSummaryDto>> response = textController.listTexts(pageable, "sample", "Technology");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Sample Title", response.getBody().getContent().get(0).getTitle());
        verify(textService, times(1)).listTexts(any(Pageable.class), eq("sample"), eq("Technology"));
    }

    @Test
    void listTexts_ReturnsPagedResults_WhenNoQueryOrCategoryProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<TextSummaryDto> page = new PageImpl<>(List.of(sampleTextSummary), pageable, 1);
        when(textService.listTexts(any(Pageable.class), isNull(), isNull())).thenReturn(page);

        // Act
        ResponseEntity<Page<TextSummaryDto>> response = textController.listTexts(pageable, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void listTexts_ReturnsEmptyPage_WhenNoTextsFound() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<TextSummaryDto> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(textService.listTexts(any(Pageable.class), any(), any())).thenReturn(emptyPage);

        // Act
        ResponseEntity<Page<TextSummaryDto>> response = textController.listTexts(pageable, "nonexistent", null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
        assertTrue(response.getBody().getContent().isEmpty());
    }

    @Test
    void listTexts_ReturnsMultiplePages_WhenManyTextsExist() {
        // Arrange
        Pageable pageable = PageRequest.of(1, 2); // Second page
        TextSummaryDto text2 = TextSummaryDto.builder()
                .id(2L)
                .title("Title 2")
                .category("Science")
                .excerpt("Excerpt 2...")
                .createdById(2L)
                .createdByName("Jane Doe")
                .build();
        TextSummaryDto text3 = TextSummaryDto.builder()
                .id(3L)
                .title("Title 3")
                .category("History")
                .excerpt("Excerpt 3...")
                .createdById(3L)
                .createdByName("Bob Smith")
                .build();
        Page<TextSummaryDto> page = new PageImpl<>(Arrays.asList(text2, text3), pageable, 5);
        when(textService.listTexts(any(Pageable.class), any(), any())).thenReturn(page);

        // Act
        ResponseEntity<Page<TextSummaryDto>> response = textController.listTexts(pageable, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getTotalElements());
        assertEquals(2, response.getBody().getContent().size());
    }

    // Negative tests for listTexts
    @Test
    void listTexts_HandlesServiceException() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(textService.listTexts(any(Pageable.class), any(), any()))
                .thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            textController.listTexts(pageable, null, null);
        });
    }

    // Positive tests for getTextById
    @Test
    void getTextById_ReturnsTextDto_WhenTextExists() {
        // Arrange
        when(textService.getTextById(1L, false)).thenReturn(sampleTextDto);

        // Act
        ResponseEntity<TextDto> response = textController.getTextById(1L, false);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sample Title", response.getBody().getTitle());
        assertEquals("Technology", response.getBody().getCategory());
        verify(textService, times(1)).getTextById(1L, false);
    }

    @Test
    void getTextById_ReturnsTextDto_WithQuizData() {
        // Arrange
        when(textService.getTextById(1L, true)).thenReturn(sampleTextDto);

        // Act
        ResponseEntity<TextDto> response = textController.getTextById(1L, true);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getTextById_ReturnsText_WhenIncludeQuizzesIsTrue() {
        // Arrange
        when(textService.getTextById(1L, true)).thenReturn(sampleTextDto);

        // Act
        ResponseEntity<TextDto> response = textController.getTextById(1L, true);

        // Assert
        assertNotNull(response.getBody());
        verify(textService).getTextById(1L, true);
    }

    @Test
    void getTextById_ReturnsText_WhenIncludeQuizzesIsFalse() {
        // Arrange
        when(textService.getTextById(1L, false)).thenReturn(sampleTextDto);

        // Act
        ResponseEntity<TextDto> response = textController.getTextById(1L, false);

        // Assert
        assertNotNull(response.getBody());
        verify(textService).getTextById(1L, false);
    }

    // Negative tests for getTextById
    @Test
    void getTextById_ThrowsException_WhenTextNotFound() {
        // Arrange
        when(textService.getTextById(999L, false))
                .thenThrow(new TextNotFoundException(999L));

        // Act & Assert
        assertThrows(TextNotFoundException.class, () -> {
            textController.getTextById(999L, false);
        });
    }

    @Test
    void getTextById_HandlesInvalidId() {
        // Arrange
        when(textService.getTextById(-1L, false))
                .thenThrow(new TextNotFoundException(-1L));

        // Act & Assert
        assertThrows(TextNotFoundException.class, () -> {
            textController.getTextById(-1L, false);
        });
    }

    @Test
    void getTextById_HandlesNullId() {
        // Arrange
        when(textService.getTextById(null, false))
                .thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            textController.getTextById(null, false);
        });
    }

    // Test constructor
    @Test
    void constructor_InjectsTextService() {
        // This test verifies the constructor works correctly
        TextController controller = new TextController(textService);
        assertNotNull(controller);
    }

    // Additional edge case tests
    @Test
    void listTexts_HandlesEmptyQueryString() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<TextSummaryDto> page = new PageImpl<>(List.of(sampleTextSummary), pageable, 1);
        when(textService.listTexts(any(Pageable.class), eq(""), eq(""))).thenReturn(page);

        // Act
        ResponseEntity<Page<TextSummaryDto>> response = textController.listTexts(pageable, "", "");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTextById_WithLargeId() {
        // Arrange
        Long largeId = Long.MAX_VALUE;
        when(textService.getTextById(largeId, false))
                .thenThrow(new TextNotFoundException(largeId));

        // Act & Assert
        assertThrows(TextNotFoundException.class, () -> {
            textController.getTextById(largeId, false);
        });
    }
}
