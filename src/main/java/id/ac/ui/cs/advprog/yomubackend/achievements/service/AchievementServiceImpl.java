package id.ac.ui.cs.advprog.yomubackend.achievements.service;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementCreateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementShowcaseUpdateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementUpdateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserAchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.Achievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.ProcessedQuizCompletedEvent;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievementStats;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.achievements.exception.AchievementConflictException;
import id.ac.ui.cs.advprog.yomubackend.achievements.exception.AchievementNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.achievements.exception.UserAchievementAccessException;
import id.ac.ui.cs.advprog.yomubackend.achievements.mapper.AchievementMapper;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.AchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.ProcessedQuizCompletedEventRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserAchievementStatsRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserAchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.yomubackend.event.QuizCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final ProcessedQuizCompletedEventRepository processedEventRepository;
    private final UserAchievementStatsRepository userAchievementStatsRepository;
    private final UserRepository userRepository;
    private final AchievementMapper achievementMapper;
    private final DailyMissionService dailyMissionService;

    @Autowired
    public AchievementServiceImpl(
            AchievementRepository achievementRepository,
            UserAchievementRepository userAchievementRepository,
            ProcessedQuizCompletedEventRepository processedEventRepository,
            UserAchievementStatsRepository userAchievementStatsRepository,
            UserRepository userRepository,
            AchievementMapper achievementMapper,
            DailyMissionService dailyMissionService) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.processedEventRepository = processedEventRepository;
        this.userAchievementStatsRepository = userAchievementStatsRepository;
        this.userRepository = userRepository;
        this.achievementMapper = achievementMapper;
        this.dailyMissionService = dailyMissionService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AchievementDto> getAllAchievements(Pageable pageable) {
        return achievementRepository.findByActiveTrue(pageable)
                .map(achievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAchievementDto> getUserAchievementProgress(Long userId, Pageable pageable) {
        ensureUserExists(userId);
        Map<Long, UserAchievement> progressByAchievementId = userAchievementRepository
                .findAllByUserId(userId)
                .stream()
                .collect(Collectors.toMap(
                        userAchievement -> userAchievement.getAchievement().getId(),
                        Function.identity()
                ));

        return achievementRepository.findByActiveTrue(pageable)
                .map(achievement -> achievementMapper.toUserAchievementProgressDto(
                        userId,
                        achievement,
                        progressByAchievementId.get(achievement.getId())
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAchievementDto> getPublicUserAchievements(Long userId) {
        ensureUserExists(userId);
        return userAchievementRepository
                .findAllByUserIdAndIsCompletedTrueAndShowcasedTrueOrderByShowcaseOrderAsc(userId)
                .stream()
                .filter(userAchievement -> userAchievement.getAchievement().getActive())
                .map(achievementMapper::toUserAchievementDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AchievementDto getAchievementById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new AchievementNotFoundException(id));
        return achievementMapper.toDto(achievement);
    }

    @Override
    public AchievementDto createAchievement(AchievementCreateRequest request) {
        validateCreateRequest(request);

        Achievement achievement = Achievement.builder()
                .name(request.getName().trim())
                .description(request.getDescription().trim())
                .conditionType(request.getConditionType())
                .targetValue(request.getTargetValue())
                .iconUrl(normalizeOptionalText(request.getIconUrl()))
                .active(request.getActive() == null || request.getActive())
                .build();

        return achievementMapper.toDto(achievementRepository.save(achievement));
    }

    @Override
    public AchievementDto updateAchievement(Long id, AchievementUpdateRequest request) {
        validateUpdateRequest(request);

        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new AchievementNotFoundException(id));

        achievement.setName(request.getName().trim());
        achievement.setDescription(request.getDescription().trim());
        achievement.setConditionType(request.getConditionType());
        achievement.setTargetValue(request.getTargetValue());
        achievement.setIconUrl(normalizeOptionalText(request.getIconUrl()));
        achievement.setActive(request.getActive() == null || request.getActive());

        return achievementMapper.toDto(achievementRepository.save(achievement));
    }

    @Override
    public void deleteAchievement(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new AchievementNotFoundException(id));
        achievement.setActive(false);
        achievementRepository.save(achievement);
    }

    @Override
    public void evaluateAndUnlockAchievements(Long userId, Integer score, Integer totalQuizzesCompleted) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserAchievementAccessException(userId));

        List<Achievement> allAchievements = achievementRepository.findByActiveTrue();

        for (Achievement achievement : allAchievements) {
            int progressValue = getProgressValue(achievement, score, totalQuizzesCompleted);
            upsertAchievementProgress(user, achievement, progressValue);
        }
    }

    @Override
    public boolean processQuizCompletedEvent(QuizCompletedEvent event) {
        validateQuizCompletedEvent(event);

        if (processedEventRepository.existsByAttemptId(event.getAttemptId())) {
            return false;
        }

        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new UserAchievementAccessException(event.getUserId()));

        UserAchievementStats stats = userAchievementStatsRepository.findByUserId(user.getId())
                .orElseGet(() -> UserAchievementStats.builder()
                        .user(user)
                        .build());
        int score = Math.max(event.getScore(), 0);
        stats.setTotalQuizzesCompleted(stats.getTotalQuizzesCompleted() + 1);
        stats.setBestScore(Math.max(stats.getBestScore(), score));
        userAchievementStatsRepository.save(stats);

        processedEventRepository.save(ProcessedQuizCompletedEvent.builder()
                .attemptId(event.getAttemptId())
                .user(user)
                .score(score)
                .completedAt(event.getCompletedAt())
                .build());

        evaluateAndUnlockAchievements(user.getId(), stats.getBestScore(), stats.getTotalQuizzesCompleted());
        dailyMissionService.updateProgressForQuizCompleted(user, score, resolveCompletedDate(event));
        return true;
    }

    @Override
    public List<UserAchievementDto> updateShowcase(Long userId, AchievementShowcaseUpdateRequest request) {
        ensureUserExists(userId);
        validateShowcaseRequest(request);

        List<UserAchievement> currentAchievements = userAchievementRepository.findAllByUserId(userId);
        for (UserAchievement userAchievement : currentAchievements) {
            userAchievement.setShowcased(false);
            userAchievement.setShowcaseOrder(null);
        }
        userAchievementRepository.saveAll(currentAchievements);

        List<UserAchievement> showcasedAchievements = request.getAchievementIds()
                .stream()
                .map(achievementId -> findCompletedUserAchievement(userId, achievementId))
                .toList();

        for (int index = 0; index < showcasedAchievements.size(); index++) {
            UserAchievement userAchievement = showcasedAchievements.get(index);
            userAchievement.setShowcased(true);
            userAchievement.setShowcaseOrder(index + 1);
        }

        return userAchievementRepository.saveAll(showcasedAchievements)
                .stream()
                .map(achievementMapper::toUserAchievementDto)
                .toList();
    }

    private LocalDate resolveCompletedDate(QuizCompletedEvent event) {
        if (event.getCompletedAt() == null) {
            return LocalDate.now();
        }
        return event.getCompletedAt().toLocalDate();
    }

    private int getProgressValue(Achievement achievement, Integer score, Integer totalQuizzesCompleted) {
        return switch (achievement.getConditionType()) {
            case FIRST_QUIZ_COMPLETED, QUIZ_COUNT -> safeProgress(totalQuizzesCompleted);
            case SCORE_ABOVE -> safeProgress(score);
        };
    }

    private void upsertAchievementProgress(User user, Achievement achievement, Integer progress) {
        Optional<UserAchievement> existing = userAchievementRepository
                .findByUserIdAndAchievementId(user.getId(), achievement.getId());
        boolean completed = progress >= achievement.getTargetValue();

        if (existing.isPresent()) {
            UserAchievement ua = existing.get();
            ua.setProgress(Math.max(ua.getProgress(), progress));
            if (!ua.getIsCompleted() && completed) {
                ua.setIsCompleted(true);
                ua.setAchievementDate(LocalDateTime.now());
            }
            userAchievementRepository.save(ua);
        } else {
            UserAchievement newUserAchievement = UserAchievement.builder()
                    .user(user)
                    .achievement(achievement)
                    .progress(progress)
                    .isCompleted(completed)
                    .achievementDate(completed ? LocalDateTime.now() : null)
                    .build();
            userAchievementRepository.save(newUserAchievement);
        }
    }

    private int safeProgress(Integer value) {
        return value == null ? 0 : Math.max(value, 0);
    }

    private void validateQuizCompletedEvent(QuizCompletedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Quiz completed event is required");
        }
        if (event.getAttemptId() == null) {
            throw new IllegalArgumentException("Quiz completed event attemptId is required");
        }
        if (event.getUserId() == null) {
            throw new IllegalArgumentException("Quiz completed event userId is required");
        }
        if (event.getScore() == null) {
            throw new IllegalArgumentException("Quiz completed event score is required");
        }
        if (event.getScore() < 0 || event.getScore() > 100) {
            throw new IllegalArgumentException("Quiz completed event score must be between 0 and 100");
        }
    }

    private void validateShowcaseRequest(AchievementShowcaseUpdateRequest request) {
        if (request == null || request.getAchievementIds() == null) {
            throw new IllegalArgumentException("Achievement showcase request is required");
        }

        Set<Long> uniqueAchievementIds = new HashSet<>();
        for (Long achievementId : request.getAchievementIds()) {
            if (achievementId == null) {
                throw new IllegalArgumentException("Achievement showcase id is required");
            }
            if (!uniqueAchievementIds.add(achievementId)) {
                throw new IllegalArgumentException("Achievement showcase cannot contain duplicates");
            }
        }
    }

    private UserAchievement findCompletedUserAchievement(Long userId, Long achievementId) {
        UserAchievement userAchievement = userAchievementRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new AchievementConflictException(
                        "Completed achievement not found: " + achievementId
                ));

        if (!userAchievement.getIsCompleted()) {
            throw new AchievementConflictException("Achievement is not completed: " + achievementId);
        }
        return userAchievement;
    }

    private void ensureUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserAchievementAccessException(userId);
        }
    }

    private void validateCreateRequest(AchievementCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Achievement request is required");
        }
        validateAchievementFields(
                request.getName(),
                request.getDescription(),
                request.getConditionType(),
                request.getTargetValue()
        );
    }

    private void validateUpdateRequest(AchievementUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Achievement request is required");
        }
        validateAchievementFields(
                request.getName(),
                request.getDescription(),
                request.getConditionType(),
                request.getTargetValue()
        );
    }

    private void validateAchievementFields(
            String name,
            String description,
            ConditionType conditionType,
            Integer targetValue) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Achievement name is required");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Achievement description is required");
        }
        if (conditionType == null) {
            throw new IllegalArgumentException("Achievement conditionType is required");
        }
        if (targetValue == null || targetValue <= 0) {
            throw new IllegalArgumentException("Achievement targetValue must be greater than 0");
        }
    }

    private String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
