package id.ac.ui.cs.advprog.yomubackend.read_quiz.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import id.ac.ui.cs.advprog.yomubackend.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "texts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(length = 100)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Column(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "text", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes;
    
}
