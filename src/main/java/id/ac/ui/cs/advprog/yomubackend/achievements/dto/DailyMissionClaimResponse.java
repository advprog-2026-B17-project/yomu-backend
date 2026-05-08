package id.ac.ui.cs.advprog.yomubackend.achievements.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyMissionClaimResponse {
    private UserDailyMissionProgressDto progress;
    private Integer rewardPoints;
}
