package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.DailyMission;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserDailyMissionProgress;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserDailyMissionProgressRepositoryTest {

    @Autowired
    private UserDailyMissionProgressRepository progressRepository;

    @Autowired
    private DailyMissionRepository dailyMissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserIdAndDailyMissionId_ReturnsProgress() {
        User user = userRepository.save(new User("reader", "Reader", "reader@example.com", null, "password"));
        DailyMission mission = dailyMissionRepository.save(DailyMission.builder()
                .title("Complete a Quiz")
                .description("Complete one quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .rewardPoints(10)
                .activeDate(LocalDate.of(2026, 5, 8))
                .active(true)
                .build());
        UserDailyMissionProgress progress = UserDailyMissionProgress.builder()
                .user(user)
                .dailyMission(mission)
                .progress(1)
                .completed(true)
                .build();
        progressRepository.save(progress);

        Optional<UserDailyMissionProgress> result = progressRepository
                .findByUserIdAndDailyMissionId(user.getId(), mission.getId());
        List<UserDailyMissionProgress> userProgress = progressRepository.findByUserId(user.getId());

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getProgress());
        assertEquals(1, userProgress.size());
    }
}
