package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionClaimResponse;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserDailyMissionProgressDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.service.DailyMissionService;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DailyMissionController {
    private final DailyMissionService dailyMissionService;
    private final UserRepository userRepository;

    public DailyMissionController(
            DailyMissionService dailyMissionService,
            UserRepository userRepository) {
        this.dailyMissionService = dailyMissionService;
        this.userRepository = userRepository;
    }

    @PostMapping("/admin/daily-missions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DailyMissionDto> createDailyMission(
            @RequestBody DailyMissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dailyMissionService.createDailyMission(request));
    }

    @GetMapping("/admin/daily-missions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DailyMissionDto>> getAllDailyMissions() {
        return ResponseEntity.ok(dailyMissionService.getAllDailyMissions());
    }

    @PutMapping("/admin/daily-missions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DailyMissionDto> updateDailyMission(
            @PathVariable Long id,
            @RequestBody DailyMissionRequest request) {
        return ResponseEntity.ok(dailyMissionService.updateDailyMission(id, request));
    }

    @DeleteMapping("/admin/daily-missions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDailyMission(@PathVariable Long id) {
        dailyMissionService.deleteDailyMission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/daily-missions/active")
    public ResponseEntity<List<UserDailyMissionProgressDto>> getActiveDailyMissions(
            @AuthenticationPrincipal UserDetails principal) {
        User user = getAuthenticatedUser(principal);
        return ResponseEntity.ok(dailyMissionService.getActiveMissionsForUser(user, LocalDate.now()));
    }

    @PostMapping("/daily-missions/{id}/claim")
    public ResponseEntity<DailyMissionClaimResponse> claimDailyMission(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        User user = getAuthenticatedUser(principal);
        return ResponseEntity.ok(dailyMissionService.claimMission(user, id));
    }

    private User getAuthenticatedUser(UserDetails principal) {
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
