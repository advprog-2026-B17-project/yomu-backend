package id.ac.ui.cs.advprog.yomubackend.achievements.mapper;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserAchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.Achievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievement;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {

    public AchievementDto toDto(Achievement achievement) {
        if (achievement == null)
            return null;

        return AchievementDto.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .description(achievement.getDescription())
                .conditionType(achievement.getConditionType())
                .targetValue(achievement.getTargetValue())
                .iconUrl(achievement.getIconUrl())
                .build();
    }

    public UserAchievementDto toUserAchievementDto(UserAchievement userAchievement) {
        if (userAchievement == null)
            return null;

        return UserAchievementDto.builder()
                .id(userAchievement.getId())
                .userId(userAchievement.getUser().getId())
                .achievement(toDto(userAchievement.getAchievement()))
                .progress(userAchievement.getProgress())
                .isCompleted(userAchievement.getIsCompleted())
                .achievementDate(userAchievement.getAchievementDate())
                .build();
    }
}
