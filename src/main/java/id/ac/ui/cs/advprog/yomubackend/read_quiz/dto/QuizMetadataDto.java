package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizMetadataDto {
    private Long id;
    private String title;
    private Integer totalQuestions;
    private List<QuestionDto> sampleQuestions; // optional: untuk 25% bisa kosong atau 1-2 contoh
}
