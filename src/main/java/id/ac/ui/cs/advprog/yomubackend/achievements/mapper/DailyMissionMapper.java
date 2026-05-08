package id.ac.ui.cs.advprog.yomubackend.achievements.mapper;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserDailyMissionProgressDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.DailyMission;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserDailyMissionProgress;
import org.springframework.stereotype.Component;

@Component
public class DailyMissionMapper {
    public DailyMissionDto toDto(DailyMission mission) {
        if (mission == null) {
            return null;
        }

        return DailyMissionDto.builder()
                .id(mission.getId())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .conditionType(mission.getConditionType())
                .targetValue(mission.getTargetValue())
                .rewardPoints(mission.getRewardPoints())
                .activeDate(mission.getActiveDate())
                .active(mission.getActive())
                .build();
    }

    public UserDailyMissionProgressDto toProgressDto(UserDailyMissionProgress progress) {
        if (progress == null) {
            return null;
        }

        return UserDailyMissionProgressDto.builder()
                .id(progress.getId())
                .userId(progress.getUser().getId())
                .dailyMission(toDto(progress.getDailyMission()))
                .progress(progress.getProgress())
                .completed(progress.getCompleted())
                .claimed(progress.getClaimed())
                .completedAt(progress.getCompletedAt())
                .claimedAt(progress.getClaimedAt())
                .build();
    }
}
