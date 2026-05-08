package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminTextControllerTest {

    @Mock
    private TextRepository textRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TextMapper textMapper;

    @InjectMocks
    private AdminTextController adminTextController;

    private User user;
    private Text text;
    private TextDto textDto;

    @BeforeEach
    void setUp() {
        user = new User("admin", "admin", "admin@example.com", null, "pass");
        user.setId(10L);

        text = Text.builder()
                .id(1L)
                .title("T1")
                .content("C")
                .category("cat")
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();

        textDto = TextDto.builder()
                .id(1L)
                .title("T1")
                .content("C")
                .category("cat")
                .createdById(10L)
                .createdByName("admin")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createText_Succeeds() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(textRepository.save(any(Text.class))).thenReturn(text);
        when(textMapper.toDto(any(Text.class), eq(false))).thenReturn(textDto);

        UserDetails principal = mock(UserDetails.class);
        when(principal.getUsername()).thenReturn("admin");

        ResponseEntity<TextDto> resp = adminTextController.createText(textDto, principal);
        assertNotNull(resp);
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals(1L, resp.getBody().getId());
        verify(textRepository).save(any(Text.class));
    }

    @Test
    void updateText_Succeeds() {
        when(textRepository.findById(1L)).thenReturn(Optional.of(text));
        when(textRepository.save(any(Text.class))).thenReturn(text);
        TextDto updatedDto = TextDto.builder().id(1L).title("T1-upd").build();
        when(textMapper.toDto(any(Text.class), eq(false))).thenReturn(updatedDto);

        TextDto update = TextDto.builder().title("T1-upd").build();
        ResponseEntity<TextDto> resp = adminTextController.updateText(1L, update);

        assertNotNull(resp);
        assertEquals("T1-upd", resp.getBody().getTitle());
        verify(textRepository).save(any(Text.class));
    }

    @Test
    void updateText_NotFound_Throws() {
        when(textRepository.findById(999L)).thenReturn(Optional.empty());
        TextDto update = TextDto.builder().title("X").build();
        assertThrows(TextNotFoundException.class, () -> adminTextController.updateText(999L, update));
    }

    @Test
    void deleteText_Succeeds() {
        when(textRepository.existsById(1L)).thenReturn(true);
        ResponseEntity<Void> resp = adminTextController.deleteText(1L);
        assertEquals(204, resp.getStatusCodeValue());
        verify(textRepository).deleteById(1L);
    }

    @Test
    void deleteText_NotFound_Throws() {
        when(textRepository.existsById(99L)).thenReturn(false);
        assertThrows(TextNotFoundException.class, () -> adminTextController.deleteText(99L));
    }
}
