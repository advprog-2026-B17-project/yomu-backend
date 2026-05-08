package id.ac.ui.cs.advprog.yomubackend.achievements.service;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementCreateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementUpdateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.Achievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.achievements.exception.AchievementNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.achievements.mapper.AchievementMapper;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.AchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserAchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private UserRepository userRepository;

    private AchievementServiceImpl achievementService;

    @BeforeEach
    void setUp() {
        achievementService = new AchievementServiceImpl(
                achievementRepository,
                userAchievementRepository,
                userRepository,
                new AchievementMapper()
        );
    }

    @Test
    void createAchievement_SavesAndReturnsDto() {
        AchievementCreateRequest request = AchievementCreateRequest.builder()
                .name(" First Quiz ")
                .description(" Complete your first quiz ")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .iconUrl(" https://example.com/icon.png ")
                .build();

        when(achievementRepository.save(any(Achievement.class))).thenAnswer(invocation -> {
            Achievement achievement = invocation.getArgument(0);
            achievement.setId(1L);
            return achievement;
        });

        AchievementDto result = achievementService.createAchievement(request);

        assertEquals(1L, result.getId());
        assertEquals("First Quiz", result.getName());
        assertEquals("Complete your first quiz", result.getDescription());
        assertEquals(ConditionType.FIRST_QUIZ_COMPLETED, result.getConditionType());
        assertEquals(1, result.getTargetValue());
        assertEquals("https://example.com/icon.png", result.getIconUrl());

        ArgumentCaptor<Achievement> captor = ArgumentCaptor.forClass(Achievement.class);
        verify(achievementRepository).save(captor.capture());
        assertEquals("First Quiz", captor.getValue().getName());
    }

    @Test
    void createAchievement_RejectsInvalidTargetValue() {
        AchievementCreateRequest request = AchievementCreateRequest.builder()
                .name("Invalid")
                .description("Invalid target")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(0)
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> achievementService.createAchievement(request)
        );

        assertEquals("Achievement targetValue must be greater than 0", exception.getMessage());
        verify(achievementRepository, never()).save(any());
    }

    @Test
    void updateAchievement_UpdatesExistingAchievement() {
        Achievement existing = Achievement.builder()
                .id(1L)
                .name("Old")
                .description("Old description")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(5)
                .build();
        AchievementUpdateRequest request = AchievementUpdateRequest.builder()
                .name("Score Master")
                .description("Score at least 90")
                .conditionType(ConditionType.SCORE_ABOVE)
                .targetValue(90)
                .iconUrl("")
                .build();

        when(achievementRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(achievementRepository.save(existing)).thenReturn(existing);

        AchievementDto result = achievementService.updateAchievement(1L, request);

        assertEquals("Score Master", result.getName());
        assertEquals("Score at least 90", result.getDescription());
        assertEquals(ConditionType.SCORE_ABOVE, result.getConditionType());
        assertEquals(90, result.getTargetValue());
        assertNull(result.getIconUrl());
        verify(achievementRepository).save(existing);
    }

    @Test
    void updateAchievement_ThrowsWhenNotFound() {
        AchievementUpdateRequest request = AchievementUpdateRequest.builder()
                .name("Missing")
                .description("Missing achievement")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(2)
                .build();

        when(achievementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AchievementNotFoundException.class,
                () -> achievementService.updateAchievement(99L, request));
    }

    @Test
    void deleteAchievement_DeletesExistingAchievement() {
        when(achievementRepository.existsById(1L)).thenReturn(true);

        achievementService.deleteAchievement(1L);

        verify(achievementRepository).deleteById(1L);
    }

    @Test
    void deleteAchievement_ThrowsWhenNotFound() {
        when(achievementRepository.existsById(99L)).thenReturn(false);

        assertThrows(AchievementNotFoundException.class,
                () -> achievementService.deleteAchievement(99L));
        verify(achievementRepository, never()).deleteById(any());
    }
}
