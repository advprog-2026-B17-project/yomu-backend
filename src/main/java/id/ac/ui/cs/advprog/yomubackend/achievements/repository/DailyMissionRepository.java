package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.DailyMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyMissionRepository extends JpaRepository<DailyMission, Long> {
    List<DailyMission> findByActiveTrueAndActiveDate(LocalDate activeDate);
}
