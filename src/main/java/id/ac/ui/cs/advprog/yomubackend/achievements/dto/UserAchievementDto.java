package id.ac.ui.cs.advprog.yomubackend.achievements.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAchievementDto {
    private Long id;
    private Long userId;
    private AchievementDto achievement;
    private Integer progress;
    private Boolean isCompleted;
    private LocalDateTime achievementDate;
}