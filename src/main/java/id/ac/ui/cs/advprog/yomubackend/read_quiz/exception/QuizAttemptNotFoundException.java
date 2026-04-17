package id.ac.ui.cs.advprog.yomubackend.read_quiz.exception;

public class QuizAttemptNotFoundException extends RuntimeException {
    public QuizAttemptNotFoundException(Long id) {
        super("Quiz attempt not found with id: " + id);
    }
}
