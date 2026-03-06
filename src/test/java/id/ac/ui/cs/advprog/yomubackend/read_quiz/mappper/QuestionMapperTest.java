package id.ac.ui.cs.advprog.yomubackend.read_quiz.mappper;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.QuestionDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.QuestionMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Question;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class QuestionMapperTest {

    private QuestionMapper questionMapper;

    @BeforeEach
    void setUp() {
        questionMapper = new QuestionMapper();
    }

    @Test
    void testToDtoWithValidQuestion() {
        // Create a mock Quiz
        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Test Quiz")
                .build();

        // Create a Question with JSON options
        Question question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("multiple_choice")
                .questionText("What is the capital of France?")
                .options("[\"Paris\", \"London\", \"Berlin\", \"Madrid\"]")
                .correctAnswer("Paris")
                .build();

        QuestionDto dto = questionMapper.toDto(question);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("multiple_choice", dto.getKind());
        assertEquals("What is the capital of France?", dto.getQuestionText());
        assertNotNull(dto.getOptions());
        assertEquals(4, dto.getOptions().size());
        assertEquals("Paris", dto.getOptions().get(0));
    }

    @Test
    void testToDtoWithNullQuestion() {
        QuestionDto dto = questionMapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void testToDtoWithNullOptions() {
        Quiz quiz = Quiz.builder().id(1L).build();
        
        Question question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("short_answer")
                .questionText("What is 2+2?")
                .options(null)
                .correctAnswer("4")
                .build();

        QuestionDto dto = questionMapper.toDto(question);

        assertNotNull(dto);
        assertNull(dto.getOptions());
    }

    @Test
    void testToDtoWithEmptyOptions() {
        Quiz quiz = Quiz.builder().id(1L).build();
        
        Question question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("short_answer")
                .questionText("Test?")
                .options("")
                .build();

        QuestionDto dto = questionMapper.toDto(question);

        assertNotNull(dto);
        assertNull(dto.getOptions());
    }

    @Test
    void testToDtoWithBlankOptions() {
        Quiz quiz = Quiz.builder().id(1L).build();
        
        Question question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("short_answer")
                .questionText("Test?")
                .options("   ")
                .build();

        QuestionDto dto = questionMapper.toDto(question);

        assertNotNull(dto);
        assertNull(dto.getOptions());
    }

    @Test
    void testToDtoWithInvalidJsonOptions() {
        Quiz quiz = Quiz.builder().id(1L).build();
        
        // This is not valid JSON, should fall back to singleton list
        Question question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("short_answer")
                .questionText("Test?")
                .options("Invalid JSON")
                .build();

        QuestionDto dto = questionMapper.toDto(question);

        assertNotNull(dto);
        assertNotNull(dto.getOptions());
        assertEquals(1, dto.getOptions().size());
        assertEquals("Invalid JSON", dto.getOptions().get(0));
    }

    @Test
    void testToDtoWithSingleElementJsonArray() {
        Quiz quiz = Quiz.builder().id(1L).build();
        
        Question question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("multiple_choice")
                .questionText("Test?")
                .options("[\"Single Option\"]")
                .build();

        QuestionDto dto = questionMapper.toDto(question);

        assertNotNull(dto);
        assertEquals(1, dto.getOptions().size());
        assertEquals("Single Option", dto.getOptions().get(0));
    }

    @Test
    void testToDtoWithTrueFalseOptions() {
        Quiz quiz = Quiz.builder().id(1L).build();
        
        Question question = Question.builder()
                .id(1L)
                .quiz(quiz)
                .kind("true_false")
                .questionText("Is the sky blue?")
                .options("[\"True\", \"False\"]")
                .build();

        QuestionDto dto = questionMapper.toDto(question);

        assertNotNull(dto);
        assertEquals(2, dto.getOptions().size());
        assertEquals("True", dto.getOptions().get(0));
        assertEquals("False", dto.getOptions().get(1));
    }

    @Test
    void testToDtoWithQuizDetails() {
        Quiz quiz = Quiz.builder()
                .id(2L)
                .title("Math Quiz")
                .build();
        
        Question question = Question.builder()
                .id(5L)
                .quiz(quiz)
                .kind("multiple_choice")
                .questionText("What is 5*5?")
                .options("[\"20\", \"25\", \"30\", \"35\"]")
                .build();

        QuestionDto dto = questionMapper.toDto(question);

        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals("What is 5*5?", dto.getQuestionText());
    }
}
