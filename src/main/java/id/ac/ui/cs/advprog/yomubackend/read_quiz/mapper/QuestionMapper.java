package id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.QuestionDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Question;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Mapper untuk Question -> QuestionDto
 */
@Component
public class QuestionMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.QuestionDto toDto(Question q) {
        if (q == null) return null;

        List<String> options = parseOptions(q.getOptions());
        return QuestionDto.builder()
                .id(q.getId())
                .kind(q.getKind())
                .questionText(q.getQuestionText())
                .options(options)
                .build();
    }

    private List<String> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) return null;
        try {
            // expecting JSON array of strings, e.g. ["A", "B", "C"]
            return objectMapper.readValue(optionsJson, new TypeReference<List<String>>() {});
        } catch (Exception ex) {
            // fallback: return single-element list containing the raw string
            return Collections.singletonList(optionsJson);
        }
    }
}