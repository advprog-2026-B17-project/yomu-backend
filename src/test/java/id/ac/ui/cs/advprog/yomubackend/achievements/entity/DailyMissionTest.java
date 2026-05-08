package id.ac.ui.cs.advprog.yomubackend.achievements.entity;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DailyMissionTest {

    @Test
    void builder_SetsDailyMissionFields() {
        LocalDate activeDate = LocalDate.of(2026, 5, 8);

        DailyMission mission = DailyMission.builder()
                .id(1L)
                .title("Complete a Quiz")
                .description("Complete one quiz today")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .rewardPoints(10)
                .activeDate(activeDate)
                .active(true)
                .build();

        assertEquals(1L, mission.getId());
        assertEquals("Complete a Quiz", mission.getTitle());
        assertEquals("Complete one quiz today", mission.getDescription());
        assertEquals(ConditionType.FIRST_QUIZ_COMPLETED, mission.getConditionType());
        assertEquals(1, mission.getTargetValue());
        assertEquals(10, mission.getRewardPoints());
        assertEquals(activeDate, mission.getActiveDate());
        assertTrue(mission.getActive());
    }

    @Test
    void builder_DefaultsActiveToTrue() {
        DailyMission mission = DailyMission.builder().build();

        assertTrue(mission.getActive());
    }
}
