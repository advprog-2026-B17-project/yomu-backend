package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextDto {
    private Long id;
    private String title;
    private String category;
    private String content;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private List<QuizMetadataDto> quizzes; // metadata quiz (optional, bisa kosong)
}