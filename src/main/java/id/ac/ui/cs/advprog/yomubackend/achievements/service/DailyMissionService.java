package id.ac.ui.cs.advprog.yomubackend.achievements.service;

import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionClaimResponse;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionDto;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.DailyMissionRequest;
import id.ac.ui.cs.advprog.yomubackend.achievements.dto.UserDailyMissionProgressDto;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface DailyMissionService {
    DailyMissionDto createDailyMission(DailyMissionRequest request);

    List<DailyMissionDto> getAllDailyMissions();

    DailyMissionDto updateDailyMission(Long id, DailyMissionRequest request);

    void deleteDailyMission(Long id);

    List<UserDailyMissionProgressDto> getActiveMissionsForUser(User user, LocalDate activeDate);

    DailyMissionClaimResponse claimMission(User user, Long missionId);

    void updateProgressForQuizCompleted(User user, Integer score, LocalDate completedDate);
}
