package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AnswerTest {

    @Test
    void testAnswerBuilder() {
        Answer answer = Answer.builder()
                .id(1L)
                .userAnswer("B")
                .build();

        assertNotNull(answer);
        assertEquals(1L, answer.getId());
        assertEquals("B", answer.getUserAnswer());
    }

    @Test
    void testAnswerNoArgsConstructor() {
        Answer answer = new Answer();
        assertNotNull(answer);
    }

    @Test
    void testAnswerAllArgsConstructor() {
        Answer answer = new Answer(1L, null, null, "A");
        assertNotNull(answer);
        assertEquals("A", answer.getUserAnswer());
    }

    @Test
    void testAnswerSetters() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setUserAnswer("C");

        assertEquals(1L, answer.getId());
        assertEquals("C", answer.getUserAnswer());
    }
}