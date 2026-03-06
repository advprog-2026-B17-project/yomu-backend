package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class QuestionTest {

    private Question question;
    private Quiz quiz;

    @BeforeEach
    void setUp() {
        // Create a mock Quiz first
        quiz = Quiz.builder()
                .id(1L)
                .title("Test Quiz")
                .questions(new ArrayList<>())
                .build();

        question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("multiple_choice")
                .questionText("What is the capital of France?")
                .options("[\"Paris\", \"London\", \"Berlin\", \"Madrid\"]")
                .correctAnswer("Paris")
                .build();
    }

    @Test
    void testQuestionBuilder() {
        assertNotNull(question);
        assertEquals(1L, question.getId());
        assertEquals("multiple_choice", question.getKind());
        assertEquals("What is the capital of France?", question.getQuestionText());
        assertEquals("[\"Paris\", \"London\", \"Berlin\", \"Madrid\"]", question.getOptions());
        assertEquals("Paris", question.getCorrectAnswer());
    }

    @Test
    void testQuestionNoArgsConstructor() {
        Question emptyQuestion = new Question();
        assertNull(emptyQuestion.getId());
        assertNull(emptyQuestion.getKind());
        assertNull(emptyQuestion.getQuestionText());
        assertNull(emptyQuestion.getOptions());
        assertNull(emptyQuestion.getCorrectAnswer());
    }

    @Test
    void testQuestionAllArgsConstructor() {
        Question allArgsQuestion = new Question(2L, quiz, "short_answer", "What is 2+2?", null, "4");
        assertEquals(2L, allArgsQuestion.getId());
        assertEquals(quiz, allArgsQuestion.getQuiz());
        assertEquals("short_answer", allArgsQuestion.getKind());
        assertEquals("What is 2+2?", allArgsQuestion.getQuestionText());
        assertEquals("4", allArgsQuestion.getCorrectAnswer());
    }

    @Test
    void testQuestionSetters() {
        question.setId(2L);
        question.setKind("true_false");
        question.setQuestionText("Is the sky blue?");
        question.setOptions("[\"True\", \"False\"]");
        question.setCorrectAnswer("True");

        assertEquals(2L, question.getId());
        assertEquals("true_false", question.getKind());
        assertEquals("Is the sky blue?", question.getQuestionText());
        assertEquals("[\"True\", \"False\"]", question.getOptions());
        assertEquals("True", question.getCorrectAnswer());
    }

    @Test
    void testQuestionEqualsAndHashCode() {
        Question question1 = Question.builder()
                .id(1L)
                .kind("multiple_choice")
                .build();
        
        Question question2 = Question.builder()
                .id(1L)
                .kind("multiple_choice")
                .build();
        
        Question question3 = Question.builder()
                .id(2L)
                .kind("short_answer")
                .build();

        assertEquals(question1, question2);
        assertEquals(question1.hashCode(), question2.hashCode());
        assertNotEquals(question1, question3);
    }

    @Test
    void testQuestionToString() {
        String toStringResult = question.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Question"));
        assertTrue(toStringResult.contains("id=1"));
    }

    @Test
    void testQuestionQuizRelationship() {
        assertNotNull(question.getQuiz());
        assertEquals("Test Quiz", question.getQuiz().getTitle());
    }

    @Test
    void testQuestionWithNullQuiz() {
        Question questionNoQuiz = Question.builder()
                .id(3L)
                .kind("short_answer")
                .questionText("Test")
                .build();
        assertNull(questionNoQuiz.getQuiz());
    }

    @Test
    void testQuestionWithNullOptions() {
        Question questionNoOptions = Question.builder()
                .id(4L)
                .kind("short_answer")
                .questionText("Test question")
                .options(null)
                .correctAnswer("Answer")
                .build();
        assertNull(questionNoOptions.getOptions());
    }

    @Test
    void testQuestionWithEmptyOptions() {
        Question questionEmptyOptions = Question.builder()
                .id(5L)
                .kind("short_answer")
                .questionText("Test question")
                .options("")
                .correctAnswer("Answer")
                .build();
        assertEquals("", questionEmptyOptions.getOptions());
    }
}
