package id.ac.ui.cs.advprog.yomubackend.achievements.dto;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementDto {
    private Long id;
    private String name;
    private String description;
    private ConditionType conditionType;
    private Integer targetValue;
}