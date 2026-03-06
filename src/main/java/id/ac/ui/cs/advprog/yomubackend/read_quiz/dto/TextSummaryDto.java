package id.ac.ui.cs.advprog.yomubackend.read_quiz.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextSummaryDto {
    private Long id;
    private String title;
    private String category;
    private String excerpt;        // ringkasan pendek (mis. first 200 chars)
    private Long createdById;      // id pembuat (opsional jika tersedia)
    private String createdByName;  // nama pembuat (opsional)
}