package id.ac.ui.cs.advprog.yomubackend.achievements.service;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementCreateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementUpdateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserAchievementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AchievementService {
    Page<AchievementDto> getAllAchievements(Pageable pageable);

    Page<UserAchievementDto> getUserAchievementProgress(Long userId, Pageable pageable);

    AchievementDto getAchievementById(Long id);

    AchievementDto createAchievement(AchievementCreateRequest request);

    AchievementDto updateAchievement(Long id, AchievementUpdateRequest request);

    void deleteAchievement(Long id);

    void evaluateAndUnlockAchievements(Long userId, Integer score, Integer totalQuizzesCompleted);
}
