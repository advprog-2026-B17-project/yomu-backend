package id.ac.ui.cs.advprog.yomubackend.achievements.exception;

public class AchievementNotFoundException extends RuntimeException {
    public AchievementNotFoundException(Long id) {
        super("Achievement with id " + id + " not found");
    }
}
