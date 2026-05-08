package id.ac.ui.cs.advprog.yomubackend.achievements.exception;

public class DailyMissionNotFoundException extends RuntimeException {
    public DailyMissionNotFoundException(Long id) {
        super("Daily mission with id " + id + " not found");
    }
}
