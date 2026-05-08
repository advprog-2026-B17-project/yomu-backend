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
import id.ac.ui.cs.advprog.yomubackend.achievements.mapper.AchievementMapper;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.AchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.ProcessedQuizCompletedEventRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserAchievementStatsRepository;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserAchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.yomubackend.event.QuizCompletedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
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
    private ProcessedQuizCompletedEventRepository processedEventRepository;

    @Mock
    private UserAchievementStatsRepository userAchievementStatsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DailyMissionService dailyMissionService;

    private AchievementServiceImpl achievementService;

    @BeforeEach
    void setUp() {
        achievementService = new AchievementServiceImpl(
                achievementRepository,
                userAchievementRepository,
                processedEventRepository,
                userAchievementStatsRepository,
                userRepository,
                new AchievementMapper(),
                dailyMissionService
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
                .active(null)
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
        assertTrue(result.getActive());

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
        Achievement existing = Achievement.builder()
                .id(1L)
                .name("Old")
                .description("Old description")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(5)
                .active(true)
                .build();
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(existing));

        achievementService.deleteAchievement(1L);

        assertFalse(existing.getActive());
        verify(achievementRepository).save(existing);
    }

    @Test
    void deleteAchievement_ThrowsWhenNotFound() {
        when(achievementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AchievementNotFoundException.class,
                () -> achievementService.deleteAchievement(99L));
        verify(achievementRepository, never()).save(any());
    }

    @Test
    void getUserAchievementProgress_ReturnsAllCatalogAchievementsWithProgress() {
        Achievement firstQuiz = Achievement.builder()
                .id(10L)
                .name("First Quiz")
                .description("Complete first quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .build();
        Achievement fiveQuizzes = Achievement.builder()
                .id(11L)
                .name("Five Quizzes")
                .description("Complete five quizzes")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(5)
                .build();
        User user = new User();
        user.setId(1L);
        UserAchievement existingProgress = UserAchievement.builder()
                .id(20L)
                .user(user)
                .achievement(fiveQuizzes)
                .progress(2)
                .isCompleted(false)
                .build();
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userAchievementRepository.findAllByUserId(1L)).thenReturn(List.of(existingProgress));
        when(achievementRepository.findByActiveTrue(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(firstQuiz, fiveQuizzes)));

        Page<UserAchievementDto> result = achievementService.getUserAchievementProgress(
                1L,
                PageRequest.of(0, 10)
        );

        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getContent().get(0).getProgress());
        assertEquals(40, result.getContent().get(1).getProgressPercent());
    }

    @Test
    void getPublicUserAchievements_ReturnsCompletedShowcasedAchievementsOnly() {
        User user = new User();
        user.setId(1L);
        Achievement firstQuiz = Achievement.builder()
                .id(10L)
                .name("First Quiz")
                .description("Complete first quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .active(true)
                .build();
        UserAchievement showcased = UserAchievement.builder()
                .id(20L)
                .user(user)
                .achievement(firstQuiz)
                .progress(1)
                .isCompleted(true)
                .showcased(true)
                .showcaseOrder(1)
                .build();
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userAchievementRepository
                .findAllByUserIdAndIsCompletedTrueAndShowcasedTrueOrderByShowcaseOrderAsc(1L))
                .thenReturn(List.of(showcased));

        List<UserAchievementDto> result = achievementService.getPublicUserAchievements(1L);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getShowcased());
        assertTrue(result.get(0).getIsCompleted());
    }

    @Test
    void updateShowcase_PersistsCompletedAchievementsInOrder() {
        User user = new User();
        user.setId(1L);
        Achievement firstQuiz = Achievement.builder()
                .id(10L)
                .name("First Quiz")
                .description("Complete first quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .build();
        Achievement highScore = Achievement.builder()
                .id(11L)
                .name("High Score")
                .description("Score high")
                .conditionType(ConditionType.SCORE_ABOVE)
                .targetValue(90)
                .build();
        UserAchievement previousShowcase = UserAchievement.builder()
                .id(19L)
                .user(user)
                .achievement(firstQuiz)
                .progress(1)
                .isCompleted(true)
                .showcased(true)
                .showcaseOrder(1)
                .build();
        UserAchievement newShowcase = UserAchievement.builder()
                .id(20L)
                .user(user)
                .achievement(highScore)
                .progress(95)
                .isCompleted(true)
                .showcased(false)
                .build();
        AchievementShowcaseUpdateRequest request = AchievementShowcaseUpdateRequest.builder()
                .achievementIds(List.of(11L, 10L))
                .build();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userAchievementRepository.findAllByUserId(1L))
                .thenReturn(List.of(previousShowcase, newShowcase));
        when(userAchievementRepository.findByUserIdAndAchievementId(1L, 11L))
                .thenReturn(Optional.of(newShowcase));
        when(userAchievementRepository.findByUserIdAndAchievementId(1L, 10L))
                .thenReturn(Optional.of(previousShowcase));
        when(userAchievementRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<UserAchievementDto> result = achievementService.updateShowcase(1L, request);

        assertEquals(2, result.size());
        assertEquals(1, newShowcase.getShowcaseOrder());
        assertEquals(2, previousShowcase.getShowcaseOrder());
        assertTrue(result.stream().allMatch(UserAchievementDto::getShowcased));
    }

    @Test
    void updateShowcase_RejectsIncompleteAchievement() {
        User user = new User();
        user.setId(1L);
        Achievement fiveQuizzes = Achievement.builder()
                .id(11L)
                .name("Five Quizzes")
                .description("Complete five quizzes")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(5)
                .build();
        UserAchievement incomplete = UserAchievement.builder()
                .id(20L)
                .user(user)
                .achievement(fiveQuizzes)
                .progress(2)
                .isCompleted(false)
                .build();
        AchievementShowcaseUpdateRequest request = AchievementShowcaseUpdateRequest.builder()
                .achievementIds(List.of(11L))
                .build();
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userAchievementRepository.findAllByUserId(1L)).thenReturn(List.of(incomplete));
        when(userAchievementRepository.findByUserIdAndAchievementId(1L, 11L))
                .thenReturn(Optional.of(incomplete));

        assertThrows(AchievementConflictException.class, () -> achievementService.updateShowcase(1L, request));
    }

    @Test
    void updateShowcase_RejectsDuplicateAchievementIds() {
        AchievementShowcaseUpdateRequest request = AchievementShowcaseUpdateRequest.builder()
                .achievementIds(List.of(11L, 11L))
                .build();
        when(userRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> achievementService.updateShowcase(1L, request));
    }

    @Test
    void processQuizCompletedEvent_IgnoresDuplicateAttemptId() {
        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .attemptId(100L)
                .userId(1L)
                .score(3)
                .completedAt(LocalDateTime.now())
                .build();
        when(processedEventRepository.existsByAttemptId(100L)).thenReturn(true);

        boolean result = achievementService.processQuizCompletedEvent(event);

        assertFalse(result);
        verify(userAchievementStatsRepository, never()).save(any());
        verify(userAchievementRepository, never()).save(any());
        verify(dailyMissionService, never()).updateProgressForQuizCompleted(any(), any(), any());
    }

    @Test
    void processQuizCompletedEvent_UpdatesStatsAndUnlocksMatchingAchievements() {
        User user = new User();
        user.setId(1L);
        Achievement firstQuiz = Achievement.builder()
                .id(10L)
                .name("First Quiz")
                .description("Complete first quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .build();
        Achievement fiveQuizzes = Achievement.builder()
                .id(11L)
                .name("Five Quizzes")
                .description("Complete five quizzes")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(5)
                .build();
        Achievement highScore = Achievement.builder()
                .id(12L)
                .name("High Score")
                .description("Score high")
                .conditionType(ConditionType.SCORE_ABOVE)
                .targetValue(2)
                .build();
        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .attemptId(100L)
                .userId(1L)
                .score(3)
                .completedAt(LocalDateTime.now())
                .build();

        when(processedEventRepository.existsByAttemptId(100L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userAchievementStatsRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(achievementRepository.findByActiveTrue()).thenReturn(List.of(firstQuiz, fiveQuizzes, highScore));
        when(userAchievementRepository.findByUserIdAndAchievementId(eq(1L), any()))
                .thenReturn(Optional.empty());

        boolean result = achievementService.processQuizCompletedEvent(event);

        assertTrue(result);

        ArgumentCaptor<UserAchievementStats> statsCaptor = ArgumentCaptor.forClass(UserAchievementStats.class);
        verify(userAchievementStatsRepository).save(statsCaptor.capture());
        assertEquals(1, statsCaptor.getValue().getTotalQuizzesCompleted());
        assertEquals(3, statsCaptor.getValue().getBestScore());

        ArgumentCaptor<ProcessedQuizCompletedEvent> eventCaptor =
                ArgumentCaptor.forClass(ProcessedQuizCompletedEvent.class);
        verify(processedEventRepository).save(eventCaptor.capture());
        assertEquals(100L, eventCaptor.getValue().getAttemptId());
        verify(dailyMissionService).updateProgressForQuizCompleted(
                eq(user),
                eq(3),
                eq(event.getCompletedAt().toLocalDate())
        );

        ArgumentCaptor<UserAchievement> achievementCaptor = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepository, times(3)).save(achievementCaptor.capture());
        List<UserAchievement> savedAchievements = achievementCaptor.getAllValues();
        assertTrue(savedAchievements.stream()
                .anyMatch(userAchievement -> userAchievement.getAchievement().getId().equals(10L)
                        && userAchievement.getIsCompleted()));
        assertTrue(savedAchievements.stream()
                .anyMatch(userAchievement -> userAchievement.getAchievement().getId().equals(11L)
                        && !userAchievement.getIsCompleted()
                        && userAchievement.getProgress().equals(1)));
        assertTrue(savedAchievements.stream()
                .anyMatch(userAchievement -> userAchievement.getAchievement().getId().equals(12L)
                        && userAchievement.getIsCompleted()));
    }
}
