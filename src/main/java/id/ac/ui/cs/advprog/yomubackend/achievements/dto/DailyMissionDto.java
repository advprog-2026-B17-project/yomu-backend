package id.ac.ui.cs.advprog.yomubackend.achievements.dto;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyMissionDto {
    private Long id;
    private String title;
    private String description;
    private ConditionType conditionType;
    private Integer targetValue;
    private Integer rewardPoints;
    private LocalDate activeDate;
    private Boolean active;
}
