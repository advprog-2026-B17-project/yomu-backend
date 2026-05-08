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
public class UserDailyMissionProgressDto {
    private Long id;
    private Long userId;
    private DailyMissionDto dailyMission;
    private Integer progress;
    private Boolean completed;
    private Boolean claimed;
    private LocalDateTime completedAt;
    private LocalDateTime claimedAt;
}
