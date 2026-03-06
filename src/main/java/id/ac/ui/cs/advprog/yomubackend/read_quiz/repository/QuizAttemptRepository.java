package id.ac.ui.cs.advprog.yomubackend.read_quiz.repository;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.QuizAttempt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Optional<QuizAttempt> findByUserAndQuizId(User user, Quiz quiz);
    Page<QuizAttempt> findByUser(User user, Pageable pageable);
    boolean existsByUserAndQuiz(User user, Quiz quiz);
}