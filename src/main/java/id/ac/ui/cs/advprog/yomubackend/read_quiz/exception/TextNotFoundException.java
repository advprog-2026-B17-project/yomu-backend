package id.ac.ui.cs.advprog.yomubackend.read_quiz.exception;

public class TextNotFoundException extends RuntimeException {
    public TextNotFoundException(Long id) {
        super("Text with id " + id + " not found");
    }
}
