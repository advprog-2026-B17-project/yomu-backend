package id.ac.ui.cs.advprog.yomubackend.achievements.exception;

public class UserAchievementAccessException extends RuntimeException {
    public UserAchievementAccessException(Long userId) {
        super("User with id " + userId + " not found");
    }
}
