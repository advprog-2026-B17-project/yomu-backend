package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionClaimResponse;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserDailyMissionProgressDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.achievements.service.DailyMissionService;
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

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyMissionControllerTest {
    @Mock
    private DailyMissionService dailyMissionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails principal;

    private DailyMissionController controller;
    private DailyMissionDto missionDto;

    @BeforeEach
    void setUp() {
        controller = new DailyMissionController(dailyMissionService, userRepository);
        missionDto = DailyMissionDto.builder()
                .id(1L)
                .title("Read")
                .description("Complete")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(2)
                .rewardPoints(10)
                .activeDate(LocalDate.of(2026, 5, 8))
                .active(true)
                .build();
    }

    @Test
    void createDailyMission_ReturnsCreatedMission() {
        DailyMissionRequest request = DailyMissionRequest.builder()
                .title("Read")
                .description("Complete")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(2)
                .rewardPoints(10)
                .activeDate(LocalDate.of(2026, 5, 8))
                .build();
        when(dailyMissionService.createDailyMission(request)).thenReturn(missionDto);

        ResponseEntity<DailyMissionDto> response = controller.createDailyMission(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(missionDto, response.getBody());
    }

    @Test
    void getActiveDailyMissions_UsesAuthenticatedUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("reader");
        UserDailyMissionProgressDto progress = UserDailyMissionProgressDto.builder()
                .userId(1L)
                .dailyMission(missionDto)
                .progress(1)
                .completed(false)
                .claimed(false)
                .build();
        when(principal.getUsername()).thenReturn("reader");
        when(userRepository.findByUsername("reader")).thenReturn(Optional.of(user));
        when(dailyMissionService.getActiveMissionsForUser(eq(user), any(LocalDate.class)))
                .thenReturn(List.of(progress));

        ResponseEntity<List<UserDailyMissionProgressDto>> response =
                controller.getActiveDailyMissions(principal);

        assertEquals(1, response.getBody().size());
        assertEquals(1, response.getBody().get(0).getProgress());
    }

    @Test
    void claimDailyMission_ReturnsClaimResponse() {
        User user = new User();
        user.setId(1L);
        user.setUsername("reader");
        DailyMissionClaimResponse claimResponse = DailyMissionClaimResponse.builder()
                .rewardPoints(10)
                .build();
        when(principal.getUsername()).thenReturn("reader");
        when(userRepository.findByUsername("reader")).thenReturn(Optional.of(user));
        when(dailyMissionService.claimMission(user, 1L)).thenReturn(claimResponse);

        ResponseEntity<DailyMissionClaimResponse> response =
                controller.claimDailyMission(1L, principal);

        assertEquals(10, response.getBody().getRewardPoints());
        verify(dailyMissionService).claimMission(user, 1L);
    }

    @Test
    void adminMutationEndpoints_AreProtectedWithAdminRole() throws NoSuchMethodException {
        Method create = DailyMissionController.class.getMethod(
                "createDailyMission",
                DailyMissionRequest.class
        );
        Method getAll = DailyMissionController.class.getMethod("getAllDailyMissions");
        Method update = DailyMissionController.class.getMethod(
                "updateDailyMission",
                Long.class,
                DailyMissionRequest.class
        );
        Method delete = DailyMissionController.class.getMethod("deleteDailyMission", Long.class);

        assertEquals("hasRole('ADMIN')", create.getAnnotation(PreAuthorize.class).value());
        assertEquals("hasRole('ADMIN')", getAll.getAnnotation(PreAuthorize.class).value());
        assertEquals("hasRole('ADMIN')", update.getAnnotation(PreAuthorize.class).value());
        assertEquals("hasRole('ADMIN')", delete.getAnnotation(PreAuthorize.class).value());
    }
}
