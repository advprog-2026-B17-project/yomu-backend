package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievementStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAchievementStatsRepository extends JpaRepository<UserAchievementStats, Long> {
    Optional<UserAchievementStats> findByUserId(Long userId);
}
