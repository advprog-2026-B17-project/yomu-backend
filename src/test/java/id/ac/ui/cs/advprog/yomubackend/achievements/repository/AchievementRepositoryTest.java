package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.Achievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AchievementRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AchievementRepository achievementRepository;

    private Achievement firstQuizAchievement;
    private Achievement score90Achievement;
    private Achievement fiveQuizzesAchievement;

    @BeforeEach
    void setUp() {
        firstQuizAchievement = Achievement.builder()
                .name("First Step")
                .description("Complete your first quiz")
                .conditionType(ConditionType.FIRST_QUIZ_COMPLETED)
                .targetValue(1)
                .iconUrl("/icons/first-step.png")
                .build();

        score90Achievement = Achievement.builder()
                .name("High Achiever")
                .description("Score above 90 on any quiz")
                .conditionType(ConditionType.SCORE_ABOVE)
                .targetValue(90)
                .iconUrl("/icons/high-achiever.png")
                .build();

        fiveQuizzesAchievement = Achievement.builder()
                .name("Dedicated Learner")
                .description("Complete 5 quizzes")
                .conditionType(ConditionType.QUIZ_COUNT)
                .targetValue(5)
                .iconUrl("/icons/dedicated-learner.png")
                .build();
    }

    @Test
    void save_ValidAchievement_PersistedSuccessfully() {
        Achievement saved = achievementRepository.save(firstQuizAchievement);
        entityManager.flush();
        entityManager.clear();

        assertNotNull(saved.getId());
        assertEquals("First Step", saved.getName());
        assertEquals(ConditionType.FIRST_QUIZ_COMPLETED, saved.getConditionType());
        assertEquals(1, saved.getTargetValue());
    }

    @Test
    void findById_ExistingAchievement_ReturnsAchievement() {
        entityManager.persist(firstQuizAchievement);
        entityManager.flush();

        Optional<Achievement> found = achievementRepository.findById(firstQuizAchievement.getId());

        assertTrue(found.isPresent());
        assertEquals("First Step", found.get().getName());
    }

    @Test
    void findById_NonExisting_ReturnsEmpty() {
        Optional<Achievement> found = achievementRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findByConditionType_ExistingCondition_ReturnsAchievementList() {
        entityManager.persist(firstQuizAchievement);
        entityManager.persist(score90Achievement);
        entityManager.persist(fiveQuizzesAchievement);
        entityManager.flush();

        List<Achievement> found = achievementRepository.findByConditionType(ConditionType.FIRST_QUIZ_COMPLETED);

        assertEquals(1, found.size());
        assertEquals("First Step", found.get(0).getName());
    }

    @Test
    void findByConditionType_MultipleMatches_ReturnsAll() {
        Achievement anotherScoreAchievement = Achievement.builder()
                .name("Perfect Score")
                .description("Score above 95")
                .conditionType(ConditionType.SCORE_ABOVE)
                .targetValue(95)
                .build();

        entityManager.persist(score90Achievement);
        entityManager.persist(anotherScoreAchievement);
        entityManager.flush();

        List<Achievement> found = achievementRepository.findByConditionType(ConditionType.SCORE_ABOVE);

        assertEquals(2, found.size());
    }

    @Test
    void findByConditionType_NoMatch_ReturnsEmptyList() {
        List<Achievement> found = achievementRepository.findByConditionType(ConditionType.FIRST_QUIZ_COMPLETED);

        assertTrue(found.isEmpty());
    }

    @Test
    void findByConditionTypeAndTargetValue_MatchFound_ReturnsAchievement() {
        entityManager.persist(firstQuizAchievement);
        entityManager.flush();

        Optional<Achievement> found = achievementRepository
                .findByConditionTypeAndTargetValue(ConditionType.FIRST_QUIZ_COMPLETED, 1);

        assertTrue(found.isPresent());
        assertEquals("First Step", found.get().getName());
    }

    @Test
    void findByConditionTypeAndTargetValue_NoMatch_ReturnsEmpty() {
        Optional<Achievement> found = achievementRepository
                .findByConditionTypeAndTargetValue(ConditionType.FIRST_QUIZ_COMPLETED, 999);

        assertFalse(found.isPresent());
    }

    @Test
    void findByConditionTypeAndTargetValue_WrongConditionType_ReturnsEmpty() {
        entityManager.persist(score90Achievement);
        entityManager.flush();

        Optional<Achievement> found = achievementRepository
                .findByConditionTypeAndTargetValue(ConditionType.FIRST_QUIZ_COMPLETED, 90);

        assertFalse(found.isPresent());
    }

    @Test
    void findAll_MultipleAchievements_ReturnsAll() {
        entityManager.persist(firstQuizAchievement);
        entityManager.persist(score90Achievement);
        entityManager.persist(fiveQuizzesAchievement);
        entityManager.flush();

        List<Achievement> all = achievementRepository.findAll();

        assertEquals(3, all.size());
    }

    @Test
    void delete_ExistingAchievement_Removed() {
        entityManager.persist(firstQuizAchievement);
        entityManager.flush();
        Long id = firstQuizAchievement.getId();

        achievementRepository.delete(firstQuizAchievement);
        entityManager.flush();

        Optional<Achievement> found = achievementRepository.findById(id);
        assertFalse(found.isPresent());
    }
}
