package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GradingTest {

    @Test
    void testGradingBuilder() {
        Grading grading = Grading.builder()
                .id(1L)
                .isCorrect(true)
                .score(1)
                .feedback("Correct!")
                .build();

        assertNotNull(grading);
        assertEquals(1L, grading.getId());
        assertTrue(grading.getIsCorrect());
        assertEquals(1, grading.getScore());
        assertEquals("Correct!", grading.getFeedback());
    }

    @Test
    void testGradingBuilderIncorrect() {
        Grading grading = Grading.builder()
                .id(1L)
                .isCorrect(false)
                .score(0)
                .feedback("Incorrect")
                .build();

        assertNotNull(grading);
        assertFalse(grading.getIsCorrect());
        assertEquals(0, grading.getScore());
    }

    @Test
    void testGradingNoArgsConstructor() {
        Grading grading = new Grading();
        assertNotNull(grading);
    }

    @Test
    void testGradingAllArgsConstructor() {
        Grading grading = new Grading(1L, null, true, 1, "Good job!");
        assertNotNull(grading);
        assertTrue(grading.getIsCorrect());
    }

    @Test
    void testGradingSetters() {
        Grading grading = new Grading();
        grading.setId(1L);
        grading.setIsCorrect(false);
        grading.setScore(0);
        grading.setFeedback("Wrong answer");

        assertEquals(1L, grading.getId());
        assertFalse(grading.getIsCorrect());
        assertEquals(0, grading.getScore());
        assertEquals("Wrong answer", grading.getFeedback());
    }
}