package id.ac.ui.cs.advprog.yomubackend.achievements.entity;


import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserAchievementTest {

    @Test
    public void testUserAchievementCreation() {
        User user = new User("testuser", "test@test.com", "password123");
        user.setId(1L);

        UserAchievement achievement = UserAchievement.builder()
                .id(1L)
                .name("First Login")
                .milestone("Login for the first time")
                .user(user)
                .build();

        assertNotNull(achievement);
        assertEquals(1L, achievement.getId());
        assertEquals("First Login", achievement.getName());
        assertEquals("Login for the first time", achievement.getMilestone());
    }
}