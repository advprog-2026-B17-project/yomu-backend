package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementCreateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementUpdateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.achievements.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    private UserAchievementController controller;
    private AchievementDto achievementDto;

    @BeforeEach
    void setUp() {
        controller = new UserAchievementController(achievementService);
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
