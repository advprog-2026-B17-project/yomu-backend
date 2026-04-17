package id.ac.ui.cs.advprog.yomubackend.achievements.service;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserAchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.Achievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.exception.AchievementNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.achievements.mapper.AchievementMapper;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.AchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserAchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserRepository userRepository;
    private final AchievementMapper achievementMapper;

    @Autowired
    public AchievementServiceImpl(
            AchievementRepository achievementRepository,
            UserAchievementRepository userAchievementRepository,
            UserRepository userRepository,
            AchievementMapper achievementMapper) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.userRepository = userRepository;
        this.achievementMapper = achievementMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AchievementDto> getAllAchievements(Pageable pageable) {
        return achievementRepository.findAll(pageable)
                .map(achievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAchievementDto> getUserAchievementProgress(Long userId, Pageable pageable) {
        return userAchievementRepository.findByUserId(userId, pageable)
                .map(achievementMapper::toUserAchievementDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AchievementDto getAchievementById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new AchievementNotFoundException(id));
        return achievementMapper.toDto(achievement);
    }

    @Override
    public void evaluateAndUnlockAchievements(Long userId, Integer score, Integer totalQuizzesCompleted) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<Achievement> allAchievements = achievementRepository.findAll();

        for (Achievement achievement : allAchievements) {
            if (evaluateCondition(achievement, score, totalQuizzesCompleted)) {
                int progressValue = getProgressValue(achievement, score, totalQuizzesCompleted);
                unlockAchievementIfNew(user, achievement, progressValue);
            }
        }
    }

    private boolean evaluateCondition(Achievement achievement, Integer score, Integer totalQuizzesCompleted) {
        return switch (achievement.getConditionType()) {
            case FIRST_QUIZ_COMPLETED -> totalQuizzesCompleted != null
                    && totalQuizzesCompleted >= achievement.getTargetValue();
            case QUIZ_COUNT -> totalQuizzesCompleted != null
                    && totalQuizzesCompleted >= achievement.getTargetValue();
            case SCORE_ABOVE -> score != null
                    && score >= achievement.getTargetValue();
        };
    }

    private int getProgressValue(Achievement achievement, Integer score, Integer totalQuizzesCompleted) {
        return switch (achievement.getConditionType()) {
            case FIRST_QUIZ_COMPLETED, QUIZ_COUNT -> totalQuizzesCompleted;
            case SCORE_ABOVE -> score;
        };
    }

    private void unlockAchievementIfNew(User user, Achievement achievement, Integer progress) {
        Optional<UserAchievement> existing = userAchievementRepository
                .findByUserIdAndAchievementId(user.getId(), achievement.getId());

        if (existing.isPresent()) {
            UserAchievement ua = existing.get();
            if (!ua.getIsCompleted()) {
                ua.setProgress(progress);
                ua.setIsCompleted(true);
                ua.setAchievementDate(LocalDateTime.now());
                userAchievementRepository.save(ua);
            }
        } else {
            UserAchievement newUserAchievement = UserAchievement.builder()
                    .user(user)
                    .achievement(achievement)
                    .progress(progress)
                    .isCompleted(true)
                    .achievementDate(LocalDateTime.now())
                    .build();
            userAchievementRepository.save(newUserAchievement);
        }
    }
}