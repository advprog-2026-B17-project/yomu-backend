package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAchievementController {

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @GetMapping("/achievements")
    public ResponseEntity<List<UserAchievement>> getAllAchievements() {
        List<UserAchievement> achievements = userAchievementRepository.findAll();
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/users/{id}/achievements")
    public ResponseEntity<List<UserAchievement>> getUserAchievements(@PathVariable Long id) {
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(id);
        return ResponseEntity.ok(userAchievements);
    }
}