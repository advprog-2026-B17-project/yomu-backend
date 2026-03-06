package id.ac.ui.cs.advprog.yomubackend.read_quiz.service;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextSummaryDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.TextNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.TextMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.TextRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TextServiceImpl implements TextService {

    private final TextRepository textRepository;
    private final TextMapper textMapper;

    @Autowired
    public TextServiceImpl(TextRepository textRepository, TextMapper textMapper) {
        this.textRepository = textRepository;
        this.textMapper = textMapper;
    }

    @Override
    public Page<TextSummaryDto> listTexts(Pageable pageable, String category, String q) {
        Page<Text> page;
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasQuery = q != null && !q.isBlank();

        if (hasCategory && hasQuery) {
            page = textRepository.findByCategoryAndTitleContainingIgnoreCase(category, q, pageable);
        } else if (hasCategory) {
            page = textRepository.findByCategory(category, pageable);
        } else if (hasQuery) {
            page = textRepository.findByTitleContainingIgnoreCase(q, pageable);
        } else {
            page = textRepository.findAll(pageable);
        }

        return page.map(textMapper::toSummary);
    }

    @Override
    public TextDto getTextById(Long id, boolean includeQuizMetadata) {
        Text text = textRepository.findById(id)
                .orElseThrow(() -> new TextNotFoundException(id));
        return textMapper.toDto(text, includeQuizMetadata);
    }
}
