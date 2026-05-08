package id.ac.ui.cs.advprog.yomubackend.achievements.entity;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserDailyMissionProgressTest {

    @Test
    void builder_SetsProgressFields() {
        User user = new User();
        user.setId(1L);
        DailyMission mission = DailyMission.builder().id(2L).build();
        LocalDateTime completedAt = LocalDateTime.of(2026, 5, 8, 10, 0);
        LocalDateTime claimedAt = LocalDateTime.of(2026, 5, 8, 10, 5);

        UserDailyMissionProgress progress = UserDailyMissionProgress.builder()
                .id(3L)
                .user(user)
                .dailyMission(mission)
                .progress(1)
                .completed(true)
                .claimed(true)
                .completedAt(completedAt)
                .claimedAt(claimedAt)
                .build();

        assertEquals(3L, progress.getId());
        assertEquals(user, progress.getUser());
        assertEquals(mission, progress.getDailyMission());
        assertEquals(1, progress.getProgress());
        assertTrue(progress.getCompleted());
        assertTrue(progress.getClaimed());
        assertEquals(completedAt, progress.getCompletedAt());
        assertEquals(claimedAt, progress.getClaimedAt());
    }

    @Test
    void builder_DefaultsProgressBooleansAndCount() {
        UserDailyMissionProgress progress = UserDailyMissionProgress.builder().build();

        assertEquals(0, progress.getProgress());
        assertFalse(progress.getCompleted());
        assertFalse(progress.getClaimed());
    }
}
