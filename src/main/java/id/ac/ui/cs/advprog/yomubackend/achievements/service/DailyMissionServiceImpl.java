package id.ac.ui.cs.advprog.yomubackend.achievements.service;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionClaimResponse;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserDailyMissionProgressDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.DailyMission;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserDailyMissionProgress;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.achievements.exception.AchievementConflictException;
import id.ac.ui.cs.advprog.yomubackend.achievements.exception.DailyMissionNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.achievements.mapper.DailyMissionMapper;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserDailyMissionProgressRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DailyMissionServiceImpl implements DailyMissionService {
    private final DailyMissionRepository dailyMissionRepository;
    private final UserDailyMissionProgressRepository progressRepository;
    private final DailyMissionMapper dailyMissionMapper;

    public DailyMissionServiceImpl(
            DailyMissionRepository dailyMissionRepository,
            UserDailyMissionProgressRepository progressRepository,
            DailyMissionMapper dailyMissionMapper) {
        this.dailyMissionRepository = dailyMissionRepository;
        this.progressRepository = progressRepository;
        this.dailyMissionMapper = dailyMissionMapper;
    }

    @Override
    public DailyMissionDto createDailyMission(DailyMissionRequest request) {
        validateRequest(request);

        DailyMission mission = DailyMission.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .conditionType(request.getConditionType())
                .targetValue(request.getTargetValue())
                .rewardPoints(request.getRewardPoints())
                .activeDate(request.getActiveDate())
                .active(request.getActive() == null || request.getActive())
                .build();

        return dailyMissionMapper.toDto(dailyMissionRepository.save(mission));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyMissionDto> getAllDailyMissions() {
        return dailyMissionRepository.findAll().stream()
                .map(dailyMissionMapper::toDto)
                .toList();
    }

    @Override
    public DailyMissionDto updateDailyMission(Long id, DailyMissionRequest request) {
        validateRequest(request);

        DailyMission mission = findMissionById(id);
        mission.setTitle(request.getTitle().trim());
        mission.setDescription(request.getDescription().trim());
        mission.setConditionType(request.getConditionType());
        mission.setTargetValue(request.getTargetValue());
        mission.setRewardPoints(request.getRewardPoints());
        mission.setActiveDate(request.getActiveDate());
        mission.setActive(request.getActive() == null || request.getActive());

        return dailyMissionMapper.toDto(dailyMissionRepository.save(mission));
    }

    @Override
    public void deleteDailyMission(Long id) {
        DailyMission mission = findMissionById(id);
        mission.setActive(false);
        dailyMissionRepository.save(mission);
    }

    @Override
    public List<UserDailyMissionProgressDto> getActiveMissionsForUser(User user, LocalDate activeDate) {
        LocalDate date = activeDate == null ? LocalDate.now() : activeDate;
        return dailyMissionRepository.findByActiveTrueAndActiveDate(date).stream()
                .map(mission -> getOrCreateProgress(user, mission))
                .map(dailyMissionMapper::toProgressDto)
                .toList();
    }

    @Override
    public DailyMissionClaimResponse claimMission(User user, Long missionId) {
        UserDailyMissionProgress progress = progressRepository
                .findByUserIdAndDailyMissionId(user.getId(), missionId)
                .orElseThrow(() -> new DailyMissionNotFoundException(missionId));

        if (!progress.getCompleted()) {
            throw new AchievementConflictException("Daily mission is not completed");
        }
        if (progress.getClaimed()) {
            throw new AchievementConflictException("Daily mission reward already claimed");
        }

        progress.setClaimed(true);
        progress.setClaimedAt(LocalDateTime.now());
        UserDailyMissionProgress savedProgress = progressRepository.save(progress);

        return DailyMissionClaimResponse.builder()
                .progress(dailyMissionMapper.toProgressDto(savedProgress))
                .rewardPoints(savedProgress.getDailyMission().getRewardPoints())
                .build();
    }

    @Override
    public void updateProgressForQuizCompleted(User user, Integer score, LocalDate completedDate) {
        LocalDate missionDate = completedDate == null ? LocalDate.now() : completedDate;
        List<DailyMission> activeMissions = dailyMissionRepository
                .findByActiveTrueAndActiveDate(missionDate);

        for (DailyMission mission : activeMissions) {
            UserDailyMissionProgress progress = getOrCreateProgress(user, mission);
            if (progress.getCompleted()) {
                continue;
            }

            int nextProgress = calculateNextProgress(progress, mission, score);
            progress.setProgress(Math.min(nextProgress, mission.getTargetValue()));
            if (progress.getProgress() >= mission.getTargetValue()) {
                progress.setCompleted(true);
                progress.setCompletedAt(LocalDateTime.now());
            }
            progressRepository.save(progress);
        }
    }

    private UserDailyMissionProgress getOrCreateProgress(User user, DailyMission mission) {
        return progressRepository.findByUserIdAndDailyMissionId(user.getId(), mission.getId())
                .orElseGet(() -> progressRepository.save(UserDailyMissionProgress.builder()
                        .user(user)
                        .dailyMission(mission)
                        .build()));
    }

    private int calculateNextProgress(
            UserDailyMissionProgress progress,
            DailyMission mission,
            Integer score) {
        ConditionType conditionType = mission.getConditionType();
        if (conditionType == ConditionType.SCORE_ABOVE) {
            return Math.max(progress.getProgress(), safeValue(score));
        }
        return progress.getProgress() + 1;
    }

    private DailyMission findMissionById(Long id) {
        return dailyMissionRepository.findById(id)
                .orElseThrow(() -> new DailyMissionNotFoundException(id));
    }

    private void validateRequest(DailyMissionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Daily mission request is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Daily mission title is required");
        }
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new IllegalArgumentException("Daily mission description is required");
        }
        if (request.getConditionType() == null) {
            throw new IllegalArgumentException("Daily mission conditionType is required");
        }
        if (request.getTargetValue() == null || request.getTargetValue() <= 0) {
            throw new IllegalArgumentException("Daily mission targetValue must be greater than 0");
        }
        if (request.getRewardPoints() == null || request.getRewardPoints() < 0) {
            throw new IllegalArgumentException("Daily mission rewardPoints cannot be negative");
        }
        if (request.getActiveDate() == null) {
            throw new IllegalArgumentException("Daily mission activeDate is required");
        }
    }

    private int safeValue(Integer value) {
        return value == null ? 0 : Math.max(value, 0);
    }
}
