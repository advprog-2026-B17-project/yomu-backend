package id.ac.ui.cs.advprog.yomubackend.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class QuizCompletedEventTest {

    @Test
    void testQuizCompletedEventBuilder() {
        LocalDateTime now = LocalDateTime.now();

        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .userId(1L)
                .textId(10L)
                .quizId(100L)
                .attemptId(1000L)
                .score(5)
                .completedAt(now)
                .build();

        assertEquals(1L, event.getUserId());
        assertEquals(10L, event.getTextId());
        assertEquals(100L, event.getQuizId());
        assertEquals(1000L, event.getAttemptId());
        assertEquals(5, event.getScore());
        assertEquals(now, event.getCompletedAt());
    }

    @Test
    void testQuizCompletedEventNoArgsConstructor() {
        QuizCompletedEvent event = new QuizCompletedEvent();

        assertNull(event.getUserId());
        assertNull(event.getTextId());
        assertNull(event.getQuizId());
        assertNull(event.getAttemptId());
        assertNull(event.getScore());
        assertNull(event.getCompletedAt());
    }

    @Test
    void testQuizCompletedEventAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        QuizCompletedEvent event = new QuizCompletedEvent(
                1L, 10L, 100L, 1000L, 5, now
        );

        assertEquals(1L, event.getUserId());
        assertEquals(10L, event.getTextId());
        assertEquals(100L, event.getQuizId());
        assertEquals(1000L, event.getAttemptId());
        assertEquals(5, event.getScore());
        assertEquals(now, event.getCompletedAt());
    }

    @Test
    void testQuizCompletedEventSettersAndGetters() {
        QuizCompletedEvent event = new QuizCompletedEvent();
        LocalDateTime now = LocalDateTime.now();

        event.setUserId(1L);
        event.setTextId(10L);
        event.setQuizId(100L);
        event.setAttemptId(1000L);
        event.setScore(5);
        event.setCompletedAt(now);

        assertEquals(1L, event.getUserId());
        assertEquals(10L, event.getTextId());
        assertEquals(100L, event.getQuizId());
        assertEquals(1000L, event.getAttemptId());
        assertEquals(5, event.getScore());
        assertEquals(now, event.getCompletedAt());
    }

    @Test
    void testQuizCompletedEventToString() {
        LocalDateTime now = LocalDateTime.now();
        QuizCompletedEvent event = new QuizCompletedEvent(1L, 10L, 100L, 1000L, 5, now);

        String toString = event.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("userId"));
        assertTrue(toString.contains("textId"));
        assertTrue(toString.contains("quizId"));
        assertTrue(toString.contains("attemptId"));
        assertTrue(toString.contains("score"));
        assertTrue(toString.contains("completedAt"));
    }

    @Test
    void testQuizCompletedEventEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        QuizCompletedEvent event1 = new QuizCompletedEvent(1L, 10L, 100L, 1000L, 5, now);
        QuizCompletedEvent event2 = new QuizCompletedEvent(1L, 10L, 100L, 1000L, 5, now);
        QuizCompletedEvent event3 = new QuizCompletedEvent(2L, 20L, 200L, 2000L, 3, now);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1, event3);
    }
}
