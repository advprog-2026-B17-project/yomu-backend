package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.AchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserAchievementDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.service.AchievementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
}