package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserDailyMissionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDailyMissionProgressRepository extends JpaRepository<UserDailyMissionProgress, Long> {
    List<UserDailyMissionProgress> findByUserId(Long userId);

    Optional<UserDailyMissionProgress> findByUserIdAndDailyMissionId(Long userId, Long dailyMissionId);
}
