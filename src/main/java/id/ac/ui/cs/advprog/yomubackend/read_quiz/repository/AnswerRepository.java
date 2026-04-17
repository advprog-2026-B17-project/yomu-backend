package id.ac.ui.cs.advprog.yomubackend.read_quiz.repository;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Answer;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuizAttempt(QuizAttempt quizAttempt);
    List<Answer> findByQuizAttemptId(Long attemptId);
}
