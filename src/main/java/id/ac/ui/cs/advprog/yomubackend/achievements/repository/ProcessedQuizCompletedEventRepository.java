package id.ac.ui.cs.advprog.yomubackend.achievements.repository;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.ProcessedQuizCompletedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedQuizCompletedEventRepository extends JpaRepository<ProcessedQuizCompletedEvent, Long> {
    boolean existsByAttemptId(Long attemptId);
}
