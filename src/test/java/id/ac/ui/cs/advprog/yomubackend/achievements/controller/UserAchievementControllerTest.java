package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementCreateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementShowcaseUpdateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementUpdateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserAchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.achievements.service.AchievementService;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails principal;

    private UserAchievementController controller;
    private AchievementDto achievementDto;

    @BeforeEach
    void setUp() {
        controller = new UserAchievementController(achievementService, userRepository);
        achievementDto = AchievementDto.builder()
                .id(1L)
                .name("First Quiz")
                .description("Complete your first quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .iconUrl("https://example.com/icon.png")
                .build();
    }

    @Test
    void createAchievement_ReturnsCreatedAchievement() {
        AchievementCreateRequest request = AchievementCreateRequest.builder()
                .name("First Quiz")
                .description("Complete your first quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .build();
        when(achievementService.createAchievement(request)).thenReturn(achievementDto);

        ResponseEntity<AchievementDto> response = controller.createAchievement(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(achievementDto, response.getBody());
        verify(achievementService).createAchievement(request);
    }

    @Test
    void updateAchievement_ReturnsUpdatedAchievement() {
        AchievementUpdateRequest request = AchievementUpdateRequest.builder()
                .name("First Quiz Updated")
                .description("Complete your first quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .build();
        when(achievementService.updateAchievement(1L, request)).thenReturn(achievementDto);

        ResponseEntity<AchievementDto> response = controller.updateAchievement(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(achievementDto, response.getBody());
        verify(achievementService).updateAchievement(1L, request);
    }

    @Test
    void deleteAchievement_ReturnsNoContent() {
        ResponseEntity<Void> response = controller.deleteAchievement(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(achievementService).deleteAchievement(1L);
    }

    @Test
    void getUserAchievements_ReturnsPublicShowcaseOnly() {
        UserAchievementDto publicAchievement = UserAchievementDto.builder()
                .userId(2L)
                .achievement(achievementDto)
                .isCompleted(true)
                .showcased(true)
                .showcaseOrder(1)
                .build();
        when(achievementService.getPublicUserAchievements(2L)).thenReturn(List.of(publicAchievement));

        ResponseEntity<List<UserAchievementDto>> response = controller.getUserAchievements(2L);

        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getShowcased());
        verify(achievementService).getPublicUserAchievements(2L);
    }

    @Test
    void getMyAchievementProgress_UsesAuthenticatedUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("reader");
        UserAchievementDto progress = UserAchievementDto.builder()
                .userId(1L)
                .achievement(achievementDto)
                .progress(0)
                .progressPercent(0)
                .isCompleted(false)
                .build();
        PageRequest pageable = PageRequest.of(0, 10);
        when(principal.getUsername()).thenReturn("reader");
        when(userRepository.findByUsername("reader")).thenReturn(Optional.of(user));
        when(achievementService.getUserAchievementProgress(1L, pageable))
                .thenReturn(new PageImpl<>(List.of(progress)));

        ResponseEntity<?> response = controller.getMyAchievementProgress(pageable, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(achievementService).getUserAchievementProgress(1L, pageable);
    }

    @Test
    void updateMyAchievementShowcase_UsesAuthenticatedUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("reader");
        AchievementShowcaseUpdateRequest request = AchievementShowcaseUpdateRequest.builder()
                .achievementIds(List.of(1L))
                .build();
        UserAchievementDto showcasedAchievement = UserAchievementDto.builder()
                .userId(1L)
                .achievement(achievementDto)
                .progress(1)
                .progressPercent(100)
                .isCompleted(true)
                .showcased(true)
                .showcaseOrder(1)
                .build();
        when(principal.getUsername()).thenReturn("reader");
        when(userRepository.findByUsername("reader")).thenReturn(Optional.of(user));
        when(achievementService.updateShowcase(1L, request)).thenReturn(List.of(showcasedAchievement));

        ResponseEntity<List<UserAchievementDto>> response =
                controller.updateMyAchievementShowcase(request, principal);

        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getShowcased());
        verify(achievementService).updateShowcase(1L, request);
    }

    @Test
    void adminMutationEndpoints_AreProtectedWithAdminRole() throws NoSuchMethodException {
        Method create = UserAchievementController.class.getMethod(
                "createAchievement",
                AchievementCreateRequest.class
        );
        Method update = UserAchievementController.class.getMethod(
                "updateAchievement",
                Long.class,
                AchievementUpdateRequest.class
        );
        Method delete = UserAchievementController.class.getMethod("deleteAchievement", Long.class);

        assertEquals("hasRole('ADMIN')", create.getAnnotation(PreAuthorize.class).value());
        assertEquals("hasRole('ADMIN')", update.getAnnotation(PreAuthorize.class).value());
        assertEquals("hasRole('ADMIN')", delete.getAnnotation(PreAuthorize.class).value());
    }
}
