package id.ac.ui.cs.advprog.yomubackend.read_quiz.repository;

import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<Text, Long> {
    Page<Text> findByCategory(String category, Pageable pageable);

    Page<Text> findByTitleContainingIgnoreCase(String q, Pageable pageable);

    Page<Text> findByCategoryAndTitleContainingIgnoreCase(String category, String q, Pageable pageable);
}