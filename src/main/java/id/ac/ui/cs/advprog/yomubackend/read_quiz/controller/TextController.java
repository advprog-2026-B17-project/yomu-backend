package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextSummaryDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller untuk endpoint terkait Texts (bacaan).
 * - GET /api/texts
 * - GET /api/texts/{id}
 *
 * Catatan:
 * - Paging disediakan via Spring Data Pageable.
 * - Parameter "includeQuizMetadata" pada GET /{id} mengontrol apakah metadata quiz disertakan.
 */
@RestController
@RequestMapping("/api/texts")
public class TextController {

    private final TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    /**
     * Ambil daftar teks (summary) dengan pagination dan optional filter.
     *
     * Contoh:
     * GET /api/texts?page=0&size=10&sort=createdAt,desc&category=politik&q=hoax
     *
     * @param pageable paging & sorting otomatis (default size dikendalikan oleh @PageableDefault)
     * @param category optional category filter
     * @param q optional search query terhadap title
     * @return halaman TextSummaryDto
     */
    @GetMapping
    public ResponseEntity<Page<TextSummaryDto>> listTexts(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "q", required = false) String q
    ) {
        Page<TextSummaryDto> page = textService.listTexts(pageable, category, q);
        return ResponseEntity.ok(page);
    }

    /**
     * Ambil detail teks berdasarkan id.
     *
     * Contoh:
     * GET /api/texts/123?includeQuizMetadata=true
     *
     * @param id id teks
     * @param includeQuizMetadata jika true, sertakan metadata quiz (optional)
     * @return TextDto (detail)
     */
    @GetMapping("/{id}")
    public ResponseEntity<TextDto> getTextById(
            @PathVariable("id") Long id,
            @RequestParam(name = "includeQuizMetadata", required = false, defaultValue = "false") boolean includeQuizMetadata
    ) {
        TextDto dto = textService.getTextById(id, includeQuizMetadata);
        return ResponseEntity.ok(dto);
    }
}
