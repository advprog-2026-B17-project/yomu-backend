package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.Achievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.entity.types.ConditionType;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByConditionType(ConditionType conditionType);

    Optional<Achievement> findByConditionTypeAndTargetValue(ConditionType conditionType, Integer targetValue);

    Page<Achievement> findByActiveTrue(Pageable pageable);

    List<Achievement> findByActiveTrue();
}
