package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminQuestionDto {
    private Long id;
    private String kind;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
}
