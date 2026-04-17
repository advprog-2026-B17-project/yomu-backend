package id.ac.ui.cs.advprog.yomubackend.read_quiz.repository;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.QuizAttempt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Optional<QuizAttempt> findByUserAndQuizId(User user, Quiz quiz);
    Page<QuizAttempt> findByUser(User user, Pageable pageable);
    boolean existsByUserAndQuiz(User user, Quiz quiz);
    
    @Query("SELECT CASE WHEN COUNT(qa) > 0 THEN true ELSE false END " +
           "FROM QuizAttempt qa JOIN qa.quiz q JOIN q.text t " +
           "WHERE qa.user = :user AND t.id = :textId AND qa.submittedAt IS NOT NULL")
    boolean existsByUserAndTextIdSubmitted(@Param("user") User user, @Param("textId") Long textId);
}