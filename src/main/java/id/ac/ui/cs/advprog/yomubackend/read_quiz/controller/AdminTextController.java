package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.TextMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.TextRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.TextNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/texts")
public class AdminTextController {

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TextMapper textMapper;

    private User getAuthenticatedUser(@AuthenticationPrincipal UserDetails principal) {
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public ResponseEntity<TextDto> createText(@RequestBody TextDto dto, @AuthenticationPrincipal UserDetails principal) {
        User user = getAuthenticatedUser(principal);

        Text t = Text.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .createdBy(user)
                .build();

        t = textRepository.save(t);
        TextDto res = textMapper.toDto(t, false);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TextDto> updateText(@PathVariable Long id, @RequestBody TextDto dto) {
        Text t = textRepository.findById(id).orElseThrow(() -> new TextNotFoundException(id));
        if (dto.getTitle() != null) t.setTitle(dto.getTitle());
        if (dto.getContent() != null) t.setContent(dto.getContent());
        if (dto.getCategory() != null) t.setCategory(dto.getCategory());

        t = textRepository.save(t);
        TextDto res = textMapper.toDto(t, false);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteText(@PathVariable Long id) {
        if (!textRepository.existsById(id)) {
            throw new TextNotFoundException(id);
        }
        textRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
