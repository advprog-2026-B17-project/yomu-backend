package id.ac.ui.cs.advprog.yomubackend.read_quiz.repository;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TextRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TextRepository textRepository;

    private User user;

    @BeforeEach
    void setUp() {
        // Create a User
        user = new User("testuser", "test@example.com", "password");
        entityManager.persist(user);

        // Create Texts
        Text t1 = Text.builder()
                .title("Technology Article")
                .content("Content about technology")
                .category("technology")
                .createdBy(user)
                .build();
        entityManager.persist(t1);

        Text t2 = Text.builder()
                .title("Science Article")
                .content("Content about science")
                .category("science")
                .createdBy(user)
                .build();
        entityManager.persist(t2);

        Text t3 = Text.builder()
                .title("Tech News")
                .content("More tech content")
                .category("technology")
                .createdBy(user)
                .build();
        entityManager.persist(t3);

        entityManager.flush();
    }

    @Test
    void testFindByCategory() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Text> page = textRepository.findByCategory("technology", pageable);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void testFindByCategoryWithNoResults() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Text> page = textRepository.findByCategory("nonexistent", pageable);

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Text> page = textRepository.findByTitleContainingIgnoreCase("Article", pageable);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void testFindByTitleContainingIgnoreCaseWithNoResults() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Text> page = textRepository.findByTitleContainingIgnoreCase("xyz123", pageable);

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
    }

    @Test
    void testFindByCategoryAndTitleContainingIgnoreCase() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Text> page = textRepository.findByCategoryAndTitleContainingIgnoreCase("technology", "Article", pageable);

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals("Technology Article", page.getContent().get(0).getTitle());
    }

    @Test
    void testFindByCategoryAndTitleContainingIgnoreCaseWithNoResults() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Text> page = textRepository.findByCategoryAndTitleContainingIgnoreCase("science", "Tech", pageable);

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
    }

    @Test
    void testFindByCategoryWithPagination() {
        PageRequest pageable = PageRequest.of(0, 1);
        Page<Text> page = textRepository.findByCategory("technology", pageable);

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void testFindByTitleContainingIgnoreCaseWithSorting() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("title").descending());
        Page<Text> page = textRepository.findByTitleContainingIgnoreCase("Article", pageable);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
    }
}
