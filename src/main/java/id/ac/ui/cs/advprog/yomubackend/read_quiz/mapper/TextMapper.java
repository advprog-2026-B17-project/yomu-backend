package id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.QuizMetadataDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextSummaryDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper untuk Text -> TextDto / TextSummaryDto
 */
@Component
public class TextMapper {

    private final QuizMapper quizMapper;
    private static final int EXCERPT_LENGTH = 200;

    @Autowired
    public TextMapper(QuizMapper quizMapper) {
        this.quizMapper = quizMapper;
    }

    public TextSummaryDto toSummary(Text t) {
        if (t == null) return null;

        String excerpt = makeExcerpt(t.getContent());
        Long createdById = null;
        String createdByName = null;
        if (t.getCreatedBy() != null) {
            createdById = t.getCreatedBy().getId();
            createdByName = t.getCreatedBy().getUsername();
        }

        return TextSummaryDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .category(t.getCategory())
                .excerpt(excerpt)
                .createdById(createdById)
                .createdByName(createdByName)
                .build();
    }

    public TextDto toDto(Text t, boolean includeQuizMetadata) {
        if (t == null) return null;

        Long createdById = null;
        String createdByName = null;
        if (t.getCreatedBy() != null) {
            createdById = t.getCreatedBy().getId();
            createdByName = t.getCreatedBy().getUsername();
        }

        List<Quiz> quizzes = t.getQuizzes();
        List<QuizMetadataDto> quizMetadata = null;
        if (includeQuizMetadata && quizzes != null) {
            quizMetadata = quizzes.stream()
                    .map(q -> quizMapper.toMetadata(q, false)) // milestone 25%: sampleQuestions optional
                    .collect(Collectors.toList());
        }

        return TextDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .category(t.getCategory())
                .content(t.getContent())
                .createdAt(t.getCreatedAt())
                .createdById(createdById)
                .createdByName(createdByName)
                .quizzes(quizMetadata)
                .build();
    }

    private String makeExcerpt(String content) {
        if (content == null) return "";
        if (content.length() <= EXCERPT_LENGTH) return content;
        return content.substring(0, EXCERPT_LENGTH).trim() + "...";
    }
}