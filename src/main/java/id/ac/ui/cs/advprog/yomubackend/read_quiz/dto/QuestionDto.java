package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private Long id;
    private String kind;            // e.g. "multiple_choice", "short_answer"
    private String questionText;
    private List<String> options;   // jika multiple choice; null otherwise
    // jangan sertakan correctAnswer di DTO response publik
}
