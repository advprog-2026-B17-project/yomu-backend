package id.ac.ui.cs.advprog.yomubackend.achievements.service;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionClaimResponse;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserDailyMissionProgressDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.DailyMission;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserDailyMissionProgress;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.achievements.exception.AchievementConflictException;
import id.ac.ui.cs.advprog.yomubackend.achievements.mapper.DailyMissionMapper;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserDailyMissionProgressRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyMissionServiceImplTest {
    @Mock
    private DailyMissionRepository dailyMissionRepository;

    @Mock
    private UserDailyMissionProgressRepository progressRepository;

    private DailyMissionServiceImpl service;
    private User user;

    @BeforeEach
    void setUp() {
        service = new DailyMissionServiceImpl(
                dailyMissionRepository,
                progressRepository,
                new DailyMissionMapper()
        );
        user = new User();
        user.setId(1L);
        user.setUsername("reader");
    }

    @Test
    void createDailyMission_SavesTrimmedMission() {
        LocalDate activeDate = LocalDate.of(2026, 5, 8);
        DailyMissionRequest request = DailyMissionRequest.builder()
                .title(" Read Today ")
                .description(" Complete one quiz ")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .rewardPoints(10)
                .activeDate(activeDate)
                .build();
        when(dailyMissionRepository.save(any(DailyMission.class))).thenAnswer(invocation -> {
            DailyMission mission = invocation.getArgument(0);
            mission.setId(2L);
            return mission;
        });

        DailyMissionDto result = service.createDailyMission(request);

        assertEquals(2L, result.getId());
        assertEquals("Read Today", result.getTitle());
        assertTrue(result.getActive());
        ArgumentCaptor<DailyMission> captor = ArgumentCaptor.forClass(DailyMission.class);
        verify(dailyMissionRepository).save(captor.capture());
        assertEquals("Complete one quiz", captor.getValue().getDescription());
    }

    @Test
    void createDailyMission_RejectsNegativeReward() {
        DailyMissionRequest request = DailyMissionRequest.builder()
                .title("Read")
                .description("Complete")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(1)
                .rewardPoints(-1)
                .activeDate(LocalDate.of(2026, 5, 8))
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.createDailyMission(request));
        verify(dailyMissionRepository, never()).save(any());
    }

    @Test
    void deleteDailyMission_SoftDeletesMission() {
        DailyMission mission = DailyMission.builder()
                .id(2L)
                .title("Read")
                .description("Complete")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(1)
                .rewardPoints(10)
                .activeDate(LocalDate.of(2026, 5, 8))
                .active(true)
                .build();
        when(dailyMissionRepository.findById(2L)).thenReturn(Optional.of(mission));

        service.deleteDailyMission(2L);

        assertFalse(mission.getActive());
        verify(dailyMissionRepository).save(mission);
    }

    @Test
    void getActiveMissionsForUser_CreatesMissingProgress() {
        LocalDate activeDate = LocalDate.of(2026, 5, 8);
        DailyMission mission = sampleMission(activeDate, ConditionType.QUIZ_COUNT, 2);
        when(dailyMissionRepository.findByActiveTrueAndActiveDate(activeDate)).thenReturn(List.of(mission));
        when(progressRepository.findByUserIdAndDailyMissionId(1L, 2L)).thenReturn(Optional.empty());
        when(progressRepository.save(any(UserDailyMissionProgress.class))).thenAnswer(invocation -> {
            UserDailyMissionProgress progress = invocation.getArgument(0);
            progress.setId(3L);
            return progress;
        });

        List<UserDailyMissionProgressDto> result = service.getActiveMissionsForUser(user, activeDate);

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getProgress());
        assertFalse(result.get(0).getCompleted());
    }

    @Test
    void claimMission_MarksCompletedMissionClaimed() {
        DailyMission mission = sampleMission(LocalDate.of(2026, 5, 8), ConditionType.QUIZ_COUNT, 2);
        UserDailyMissionProgress progress = UserDailyMissionProgress.builder()
                .id(3L)
                .user(user)
                .dailyMission(mission)
                .progress(2)
                .completed(true)
                .claimed(false)
                .build();
        when(progressRepository.findByUserIdAndDailyMissionId(1L, 2L)).thenReturn(Optional.of(progress));
        when(progressRepository.save(progress)).thenReturn(progress);

        DailyMissionClaimResponse result = service.claimMission(user, 2L);

        assertTrue(progress.getClaimed());
        assertEquals(10, result.getRewardPoints());
    }

    @Test
    void claimMission_RejectsIncompleteMission() {
        DailyMission mission = sampleMission(LocalDate.of(2026, 5, 8), ConditionType.QUIZ_COUNT, 2);
        UserDailyMissionProgress progress = UserDailyMissionProgress.builder()
                .id(3L)
                .user(user)
                .dailyMission(mission)
                .progress(1)
                .completed(false)
                .claimed(false)
                .build();
        when(progressRepository.findByUserIdAndDailyMissionId(1L, 2L)).thenReturn(Optional.of(progress));

        assertThrows(AchievementConflictException.class, () -> service.claimMission(user, 2L));
    }

    @Test
    void updateProgressForQuizCompleted_CompletesQuizCountMission() {
        LocalDate activeDate = LocalDate.of(2026, 5, 8);
        DailyMission mission = sampleMission(activeDate, ConditionType.QUIZ_COUNT, 2);
        UserDailyMissionProgress progress = UserDailyMissionProgress.builder()
                .id(3L)
                .user(user)
                .dailyMission(mission)
                .progress(1)
                .completed(false)
                .build();
        when(dailyMissionRepository.findByActiveTrueAndActiveDate(activeDate)).thenReturn(List.of(mission));
        when(progressRepository.findByUserIdAndDailyMissionId(1L, 2L)).thenReturn(Optional.of(progress));

        service.updateProgressForQuizCompleted(user, 1, activeDate);

        assertEquals(2, progress.getProgress());
        assertTrue(progress.getCompleted());
        verify(progressRepository).save(progress);
    }

    private DailyMission sampleMission(
            LocalDate activeDate,
            ConditionType conditionType,
            int targetValue) {
        return DailyMission.builder()
                .id(2L)
                .title("Read")
                .description("Complete")
                .conditionType(conditionType)
                .targetValue(targetValue)
                .rewardPoints(10)
                .activeDate(activeDate)
                .active(true)
                .build();
    }
}
