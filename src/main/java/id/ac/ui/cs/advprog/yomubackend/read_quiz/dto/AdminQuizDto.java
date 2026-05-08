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
public class AdminQuizDto {
    private Long id;
    private String title;
    private List<AdminQuestionDto> questions;
}
