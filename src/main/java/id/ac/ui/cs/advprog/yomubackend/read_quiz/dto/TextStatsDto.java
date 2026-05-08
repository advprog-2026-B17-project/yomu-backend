package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextStatsDto {
    private long attempts;
    private double avgScore;
}
