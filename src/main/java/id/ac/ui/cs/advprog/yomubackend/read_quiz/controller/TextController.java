package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextSummaryDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.service.TextService;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.QuizAttemptRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.QuizAttempt;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.TextStatsDto;
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

    // repository used to compute simple statistics per text
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @Autowired
    public void setQuizAttemptRepository(QuizAttemptRepository quizAttemptRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
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

    /**
     * Statistik singkat untuk teks (jumlah attempts yang disubmit dan rata-rata skor)
     * GET /api/texts/{id}/stats
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<TextStatsDto> getTextStats(@PathVariable("id") Long id) {
        if (quizAttemptRepository == null) {
            // repository not available (e.g., in some test wiring), return zeroed stats
            return ResponseEntity.ok(TextStatsDto.builder().attempts(0).avgScore(0.0).build());
        }

        java.util.List<QuizAttempt> attempts = quizAttemptRepository.findSubmittedByTextId(id);
        long count = attempts == null ? 0 : attempts.size();
        double avg = 0.0;
        if (count > 0) {
            avg = attempts.stream()
                    .filter(a -> a.getScore() != null)
                    .mapToInt(a -> a.getScore())
                    .average()
                    .orElse(0.0);
        }

        TextStatsDto dto = TextStatsDto.builder()
                .attempts(count)
                .avgScore(avg)
                .build();

        return ResponseEntity.ok(dto);
    }
}
