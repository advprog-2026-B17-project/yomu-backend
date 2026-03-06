package id.ac.ui.cs.advprog.yomubackend.read_quiz.service;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextSummaryDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.TextNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.TextMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.TextRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextServiceImplTest {

    @Mock
    private TextRepository textRepository;

    @Mock
    private TextMapper textMapper;

    @InjectMocks
    private TextServiceImpl textService;

    private User user;
    private Text text;
    private TextSummaryDto textSummaryDto;
    private TextDto textDto;

    @BeforeEach
    void setUp() {
        // Create a mock User
        user = new User("testuser", "test@example.com", "password");
        user.setId(1L);

        // Create a mock Text
        text = Text.builder()
                .id(1L)
                .title("Test Text")
                .content("Content")
                .category("technology")
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();

        // Create a mock TextSummaryDto
        textSummaryDto = TextSummaryDto.builder()
                .id(1L)
                .title("Test Text")
                .category("technology")
                .excerpt("Content")
                .createdById(1L)
                .createdByName("testuser")
                .build();

        // Create a mock TextDto
        textDto = TextDto.builder()
                .id(1L)
                .title("Test Text")
                .content("Content")
                .category("technology")
                .createdById(1L)
                .createdByName("testuser")
                .build();
    }

    @Test
    void testListTextsWithNoFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Text> textPage = new PageImpl<>(Arrays.asList(text));
        
        when(textRepository.findAll(pageable)).thenReturn(textPage);
        when(textMapper.toSummary(any(Text.class))).thenReturn(textSummaryDto);

        Page<TextSummaryDto> result = textService.listTexts(pageable, null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(textRepository).findAll(pageable);
    }

    @Test
    void testListTextsWithCategoryFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Text> textPage = new PageImpl<>(Arrays.asList(text));
        
        when(textRepository.findByCategory(eq("technology"), any(Pageable.class))).thenReturn(textPage);
        when(textMapper.toSummary(any(Text.class))).thenReturn(textSummaryDto);

        Page<TextSummaryDto> result = textService.listTexts(pageable, "technology", null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(textRepository).findByCategory(eq("technology"), any(Pageable.class));
    }

    @Test
    void testListTextsWithSearchQuery() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Text> textPage = new PageImpl<>(Arrays.asList(text));
        
        when(textRepository.findByTitleContainingIgnoreCase(eq("Test"), any(Pageable.class))).thenReturn(textPage);
        when(textMapper.toSummary(any(Text.class))).thenReturn(textSummaryDto);

        Page<TextSummaryDto> result = textService.listTexts(pageable, null, "Test");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(textRepository).findByTitleContainingIgnoreCase(eq("Test"), any(Pageable.class));
    }

    @Test
    void testListTextsWithCategoryAndSearchQuery() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Text> textPage = new PageImpl<>(Arrays.asList(text));
        
        when(textRepository.findByCategoryAndTitleContainingIgnoreCase(eq("technology"), eq("Test"), any(Pageable.class)))
                .thenReturn(textPage);
        when(textMapper.toSummary(any(Text.class))).thenReturn(textSummaryDto);

        Page<TextSummaryDto> result = textService.listTexts(pageable, "technology", "Test");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(textRepository).findByCategoryAndTitleContainingIgnoreCase(eq("technology"), eq("Test"), any(Pageable.class));
    }

    @Test
    void testListTextsWithEmptyCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Text> textPage = new PageImpl<>(Arrays.asList(text));
        
        when(textRepository.findAll(pageable)).thenReturn(textPage);
        when(textMapper.toSummary(any(Text.class))).thenReturn(textSummaryDto);

        Page<TextSummaryDto> result = textService.listTexts(pageable, "", null);

        assertNotNull(result);
        verify(textRepository).findAll(pageable);
    }

    @Test
    void testListTextsWithBlankCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Text> textPage = new PageImpl<>(Arrays.asList(text));
        
        when(textRepository.findAll(pageable)).thenReturn(textPage);
        when(textMapper.toSummary(any(Text.class))).thenReturn(textSummaryDto);

        Page<TextSummaryDto> result = textService.listTexts(pageable, "   ", null);

        assertNotNull(result);
        verify(textRepository).findAll(pageable);
    }

    @Test
    void testListTextsWithEmptySearchQuery() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Text> textPage = new PageImpl<>(Arrays.asList(text));
        
        when(textRepository.findAll(pageable)).thenReturn(textPage);
        when(textMapper.toSummary(any(Text.class))).thenReturn(textSummaryDto);

        Page<TextSummaryDto> result = textService.listTexts(pageable, null, "");

        assertNotNull(result);
        verify(textRepository).findAll(pageable);
    }

    @Test
    void testListTextsWithBlankSearchQuery() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Text> textPage = new PageImpl<>(Arrays.asList(text));
        
        when(textRepository.findAll(pageable)).thenReturn(textPage);
        when(textMapper.toSummary(any(Text.class))).thenReturn(textSummaryDto);

        Page<TextSummaryDto> result = textService.listTexts(pageable, null, "   ");

        assertNotNull(result);
        verify(textRepository).findAll(pageable);
    }

    @Test
    void testGetTextByIdFound() {
        when(textRepository.findById(1L)).thenReturn(Optional.of(text));
        when(textMapper.toDto(text, true)).thenReturn(textDto);

        TextDto result = textService.getTextById(1L, true);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Text", result.getTitle());
        verify(textRepository).findById(1L);
    }

    @Test
    void testGetTextByIdNotFound() {
        when(textRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TextNotFoundException.class, () -> {
            textService.getTextById(999L, true);
        });

        verify(textRepository).findById(999L);
    }

    @Test
    void testGetTextByIdWithQuizMetadata() {
        when(textRepository.findById(1L)).thenReturn(Optional.of(text));
        when(textMapper.toDto(text, true)).thenReturn(textDto);

        TextDto result = textService.getTextById(1L, true);

        assertNotNull(result);
        verify(textMapper).toDto(text, true);
    }

    @Test
    void testGetTextByIdWithoutQuizMetadata() {
        when(textRepository.findById(1L)).thenReturn(Optional.of(text));
        when(textMapper.toDto(text, false)).thenReturn(textDto);

        TextDto result = textService.getTextById(1L, false);

        assertNotNull(result);
        verify(textMapper).toDto(text, false);
    }
}
