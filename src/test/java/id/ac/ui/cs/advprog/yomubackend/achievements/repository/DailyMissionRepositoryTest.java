package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.DailyMission;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DailyMissionRepositoryTest {

    @Autowired
    private DailyMissionRepository dailyMissionRepository;

    @Test
    void findByActiveTrueAndActiveDate_ReturnsOnlyActiveMissionsForDate() {
        LocalDate today = LocalDate.of(2026, 5, 8);

        DailyMission activeToday = DailyMission.builder()
                .title("Active Today")
                .description("Complete one quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .rewardPoints(10)
                .activeDate(today)
                .active(true)
                .build();
        DailyMission inactiveToday = DailyMission.builder()
                .title("Inactive Today")
                .description("Complete five quizzes")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(5)
                .rewardPoints(25)
                .activeDate(today)
                .active(false)
                .build();
        DailyMission activeTomorrow = DailyMission.builder()
                .title("Active Tomorrow")
                .description("Score high")
                .conditionType(ConditionType.SCORE_ABOVE)
                .targetValue(90)
                .rewardPoints(15)
                .activeDate(today.plusDays(1))
                .active(true)
                .build();

        dailyMissionRepository.save(activeToday);
        dailyMissionRepository.save(inactiveToday);
        dailyMissionRepository.save(activeTomorrow);

        List<DailyMission> result = dailyMissionRepository.findByActiveTrueAndActiveDate(today);

        assertEquals(1, result.size());
        assertEquals("Active Today", result.get(0).getTitle());
    }
}
