package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "gradings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "score")
    private Integer score;

    @Column(columnDefinition = "text")
    private String feedback;
}
