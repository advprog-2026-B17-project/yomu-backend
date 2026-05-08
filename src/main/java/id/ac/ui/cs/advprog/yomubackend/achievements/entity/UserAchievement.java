package id.ac.ui.cs.advprog.yomubackend.achievements.entity;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_achievements", uniqueConstraints = @UniqueConstraint(name = "uk_user_achievement_user_achievement", columnNames = {
        "user_id", "achievement_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Column(nullable = false)
    @Builder.Default
    private Integer progress = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCompleted = false;

    private LocalDateTime achievementDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean showcased = false;

    @Column(name = "showcase_order")
    private Integer showcaseOrder;
}
