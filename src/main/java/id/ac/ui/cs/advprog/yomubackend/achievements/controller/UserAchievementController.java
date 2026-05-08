package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementCreateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementUpdateRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserAchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.service.AchievementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserAchievementController {

    private final AchievementService achievementService;

    @Autowired
    public UserAchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping("/achievements")
    public ResponseEntity<Page<AchievementDto>> getAllAchievements(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(achievementService.getAllAchievements(pageable));
    }

    @GetMapping("/achievements/{id}")
    public ResponseEntity<AchievementDto> getAchievementById(@PathVariable Long id) {
        return ResponseEntity.ok(achievementService.getAchievementById(id));
    }

    @GetMapping("/users/{id}/achievements")
    public ResponseEntity<Page<UserAchievementDto>> getUserAchievements(
            @PathVariable Long id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(achievementService.getUserAchievementProgress(id, pageable));
    }

    @PostMapping("/admin/achievements")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AchievementDto> createAchievement(
            @RequestBody AchievementCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(achievementService.createAchievement(request));
    }

    @PutMapping("/admin/achievements/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AchievementDto> updateAchievement(
            @PathVariable Long id,
            @RequestBody AchievementUpdateRequest request) {
        return ResponseEntity.ok(achievementService.updateAchievement(id, request));
    }

    @DeleteMapping("/admin/achievements/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}
