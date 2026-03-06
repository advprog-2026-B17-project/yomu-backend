package id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.QuestionDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.QuizMetadataDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper untuk Quiz -> QuizMetadataDto
 */
@Component
public class QuizMapper {

    private final QuestionMapper questionMapper;

    @Autowired
    public QuizMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    /**
     * Map Quiz ke QuizMetadataDto.
     * @param quiz sumber
     * @param includeSampleQuestions jika true, sertakan mappings untuk sampleQuestions (bisa dikosongkan untuk milestone 25%)
     */
    public QuizMetadataDto toMetadata(Quiz quiz, boolean includeSampleQuestions) {
        if (quiz == null) return null;

        List<Question> questions = quiz.getQuestions();
        int totalQuestions = questions == null ? 0 : questions.size();

        List<QuestionDto> sample = Collections.emptyList();
        if (includeSampleQuestions && questions != null && !questions.isEmpty()) {
            // ambil maksimal 2 sample questions
            sample = questions.stream()
                    .limit(2)
                    .map(questionMapper::toDto)
                    .collect(Collectors.toList());
        }

        return QuizMetadataDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .totalQuestions(totalQuestions)
                .sampleQuestions(sample)
                .build();
    }
}