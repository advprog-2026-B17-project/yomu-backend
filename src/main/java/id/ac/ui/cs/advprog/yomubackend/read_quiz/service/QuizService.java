package id.ac.ui.cs.advprog.yomubackend.read_quiz.service;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.*;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;

public interface QuizService {
    QuizDto getQuizById(Long id);
    QuizAttemptResultDto startQuiz(Long quizId, User user);
    QuizAttemptResultDto submitQuiz(Long attemptId, QuizSubmissionDto submission, User user);
    QuizAttemptResultDto getAttemptResult(Long attemptId, User user);
}
