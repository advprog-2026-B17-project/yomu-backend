package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(length = 50)
    private String kind; // e.g. "multiple_choice", "short_answer"

    @Column(name = "question_text", columnDefinition = "text")
    private String questionText;

    /**
     * Options stored as JSON string (array/object) to keep DB-agnostic.
     * If using PostgreSQL JSON/JSONB, change columnDefinition accordingly.
     */
    @Column(columnDefinition = "text")
    private String options;

    @Column(name = "correct_answer", columnDefinition = "text")
    private String correctAnswer;
}
