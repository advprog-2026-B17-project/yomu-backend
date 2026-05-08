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
                .progressPercent(calculateProgressPercent(
                        userAchievement.getProgress(),
                        userAchievement.getAchievement().getTargetValue()
                ))
                .isCompleted(userAchievement.getIsCompleted())
                .achievementDate(userAchievement.getAchievementDate())
                .showcased(userAchievement.getShowcased())
                .showcaseOrder(userAchievement.getShowcaseOrder())
                .build();
    }

    public UserAchievementDto toUserAchievementProgressDto(
            Long userId,
            Achievement achievement,
            UserAchievement userAchievement) {
        if (achievement == null) {
            return null;
        }

        int progress = userAchievement == null ? 0 : userAchievement.getProgress();
        return UserAchievementDto.builder()
                .id(userAchievement == null ? null : userAchievement.getId())
                .userId(userId)
                .achievement(toDto(achievement))
                .progress(progress)
                .progressPercent(calculateProgressPercent(progress, achievement.getTargetValue()))
                .isCompleted(userAchievement != null && userAchievement.getIsCompleted())
                .achievementDate(userAchievement == null ? null : userAchievement.getAchievementDate())
                .showcased(userAchievement != null && userAchievement.getShowcased())
                .showcaseOrder(userAchievement == null ? null : userAchievement.getShowcaseOrder())
                .build();
    }

    private int calculateProgressPercent(Integer progress, Integer targetValue) {
        if (progress == null || targetValue == null || targetValue <= 0) {
            return 0;
        }
        return Math.min(100, Math.max(0, progress * 100 / targetValue));
    }
}
