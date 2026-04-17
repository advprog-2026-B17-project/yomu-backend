package id.ac.ui.cs.advprog.yomubackend.achievements.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AchievementNotFoundException extends RuntimeException {
    public AchievementNotFoundException(Long id) {
        super("Achievement with id " + id + " not found");
    }
}
