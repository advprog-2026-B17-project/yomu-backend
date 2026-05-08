package id.ac.ui.cs.advprog.yomubackend.achievements.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementShowcaseUpdateRequest {
    private List<Long> achievementIds;
}
