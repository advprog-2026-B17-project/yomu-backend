package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    Page<UserAchievement> findByUserId(Long userId, Pageable pageable);

    List<UserAchievement> findAllByUserId(Long userId);

    Optional<UserAchievement> findByUserIdAndAchievementId(Long userId, Long achievementId);
}
