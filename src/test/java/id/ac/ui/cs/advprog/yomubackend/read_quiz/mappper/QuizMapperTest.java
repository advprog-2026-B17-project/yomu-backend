package id.ac.ui.cs.advprog.yomubackend.read_quiz.mappper;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.QuizMetadataDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.QuestionMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.QuizMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Question;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class QuizMapperTest {

    private QuizMapper quizMapper;
    private QuestionMapper questionMapper;

    @BeforeEach
    void setUp() {
        questionMapper = new QuestionMapper();
        quizMapper = new QuizMapper(questionMapper);
    }

    @Test
    void testToMetadataWithSampleQuestions() {
        // Create a mock Text
        Text text = Text.builder()
                .id(1L)
                .title("Test Text")
                .content("Content")
                .build();

        // Create questions
        List<Question> questions = Arrays.asList(
                Question.builder().id(1L).kind("multiple_choice").questionText("Q1").options("[\"A\",\"B\"]").build(),
                Question.builder().id(2L).kind("multiple_choice").questionText("Q2").options("[\"C\",\"D\"]").build()
        );

        // Create Quiz
        Quiz quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Test Quiz")
                .questions(questions)
                .build();

        QuizMetadataDto dto = quizMapper.toMetadata(quiz, true);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Quiz", dto.getTitle());
        assertEquals(2, dto.getTotalQuestions());
        assertNotNull(dto.getSampleQuestions());
        assertEquals(2, dto.getSampleQuestions().size()); // Should include up to 2 sample questions
    }

    @Test
    void testToMetadataWithoutSampleQuestions() {
        Text text = Text.builder().id(1L).title("Test").content("Content").build();
        
        List<Question> questions = Arrays.asList(
                Question.builder().id(1L).kind("multiple_choice").questionText("Q1").options("[\"A\",\"B\"]").build(),
                Question.builder().id(2L).kind("multiple_choice").questionText("Q2").options("[\"C\",\"D\"]").build()
        );

        Quiz quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Test Quiz")
                .questions(questions)
                .build();

        QuizMetadataDto dto = quizMapper.toMetadata(quiz, false);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Quiz", dto.getTitle());
        assertEquals(2, dto.getTotalQuestions());
        assertNotNull(dto.getSampleQuestions());
        assertTrue(dto.getSampleQuestions().isEmpty()); // Should be empty when false
    }

    @Test
    void testToMetadataWithNullQuiz() {
        QuizMetadataDto dto = quizMapper.toMetadata(null, true);
        assertNull(dto);
    }

    @Test
    void testToMetadataWithNullQuestions() {
        Text text = Text.builder().id(1L).title("Test").content("Content").build();
        
        Quiz quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Test Quiz")
                .questions(null)
                .build();

        QuizMetadataDto dto = quizMapper.toMetadata(quiz, true);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(0, dto.getTotalQuestions());
    }

    @Test
    void testToMetadataWithEmptyQuestions() {
        Text text = Text.builder().id(1L).title("Test").content("Content").build();
        
        Quiz quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Test Quiz")
                .questions(new ArrayList<>())
                .build();

        QuizMetadataDto dto = quizMapper.toMetadata(quiz, true);

        assertNotNull(dto);
        assertEquals(0, dto.getTotalQuestions());
        assertNotNull(dto.getSampleQuestions());
        assertTrue(dto.getSampleQuestions().isEmpty());
    }

    @Test
    void testToMetadataWithMoreThanTwoQuestions() {
        Text text = Text.builder().id(1L).title("Test").content("Content").build();
        
        List<Question> questions = Arrays.asList(
                Question.builder().id(1L).kind("q1").questionText("Q1").options("[\"A\"]").build(),
                Question.builder().id(2L).kind("q2").questionText("Q2").options("[\"B\"]").build(),
                Question.builder().id(3L).kind("q3").questionText("Q3").options("[\"C\"]").build(),
                Question.builder().id(4L).kind("q4").questionText("Q4").options("[\"D\"]").build()
        );

        Quiz quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Test Quiz")
                .questions(questions)
                .build();

        QuizMetadataDto dto = quizMapper.toMetadata(quiz, true);

        assertNotNull(dto);
        assertEquals(4, dto.getTotalQuestions());
        // Should only include max 2 sample questions
        assertTrue(dto.getSampleQuestions().size() <= 2);
    }

    @Test
    void testToMetadataWithSingleQuestion() {
        Text text = Text.builder().id(1L).title("Test").content("Content").build();
        
        List<Question> questions = Collections.singletonList(
                Question.builder().id(1L).kind("multiple_choice").questionText("Q1").options("[\"A\",\"B\"]").build()
        );

        Quiz quiz = Quiz.builder()
                .id(1L)
                .text(text)
                .title("Single Question Quiz")
                .questions(questions)
                .build();

        QuizMetadataDto dto = quizMapper.toMetadata(quiz, true);

        assertNotNull(dto);
        assertEquals(1, dto.getTotalQuestions());
        assertEquals(1, dto.getSampleQuestions().size());
    }

    @Test
    void testToMetadataWithNullText() {
        Quiz quiz = Quiz.builder()
                .id(1L)
                .text(null)
                .title("Test Quiz")
                .questions(new ArrayList<>())
                .build();

        QuizMetadataDto dto = quizMapper.toMetadata(quiz, false);

        assertNotNull(dto);
        assertEquals("Test Quiz", dto.getTitle());
    }
}
