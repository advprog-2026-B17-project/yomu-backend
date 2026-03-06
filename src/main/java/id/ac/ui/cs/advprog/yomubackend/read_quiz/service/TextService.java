package id.ac.ui.cs.advprog.yomubackend.read_quiz.service;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextSummaryDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TextService {
    /**
     * Mengambil daftar teks (summary) dengan pagination.
     * @param pageable pagination/ sorting
     * @param category optional filter kategori (nullable)
     * @param q optional search query terhadap title (nullable)
     * @return page of TextSummaryDto
     */
    Page<TextSummaryDto> listTexts(Pageable pageable, String category, String q);

    /**
     * Mengambil detail teks by id.
     * @param id id teks
     * @param includeQuizMetadata jika true sertakan metadata quiz
     * @return TextDto
     * @throws id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.TextNotFoundException jika tidak ditemukan
     */
    TextDto getTextById(Long id, boolean includeQuizMetadata);
}