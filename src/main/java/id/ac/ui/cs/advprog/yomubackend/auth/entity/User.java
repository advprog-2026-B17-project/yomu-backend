package id.ac.ui.cs.advprog.yomubackend.auth.entity;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievement;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.*;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username or displayname
    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 10)
    private String role;

     /* Relasi ke entity lain (lazy-loaded agar tidak eager fetch semua):
       - texts: teks yang dibuat oleh user (creator)
       - quizAttempts: percobaan kuis oleh user
       - clansLed: clan yang dipimpin user
       - clanMemberships: keanggotaan user di clans
       - userAchievements: achievements milik user
       
       NOTE: mappedBy harus sesuai nama field ManyToOne di entity target.
    */

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Text> texts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<QuizAttempt> quizAttempts;

    // @OneToMany(mappedBy = "leader", fetch = FetchType.LAZY)
    // private List<Clan> clansLed;

    // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    // private List<ClanMember> clanMemberships;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserAchievement> userAchievements;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
