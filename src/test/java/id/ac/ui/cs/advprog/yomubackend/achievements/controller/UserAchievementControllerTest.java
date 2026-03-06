package id.ac.ui.cs.advprog.yomubackend.achievements.controller;

import id.ac.ui.cs.advprog.yomubackend.achievements.entity.UserAchievement;
import id.ac.ui.cs.advprog.yomubackend.achievements.repository.UserAchievementRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.auth.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAchievementController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserAchievementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserAchievementRepository userAchievementRepository;

    @MockitoBean
    private JwtUtils jwtUtils;

    private User user;
    private UserAchievement achievement1;
    private UserAchievement achievement2;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        achievement1 = new UserAchievement();
        achievement1.setId(1L);
        achievement1.setName("Beginner");
        achievement1.setMilestone("Read 1 Text");
        achievement1.setUser(user);

        achievement2 = new UserAchievement();
        achievement2.setId(2L);
        achievement2.setName("Intermediate");
        achievement2.setMilestone("Read 10 Texts");
        achievement2.setUser(user);
    }

    @Test
    public void testGetAllAchievements() throws Exception {
        List<UserAchievement> achievements = Arrays.asList(achievement1, achievement2);
        when(userAchievementRepository.findAll()).thenReturn(achievements);

        mockMvc.perform(get("/achievements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Beginner"))
                .andExpect(jsonPath("$[1].name").value("Intermediate"));
    }

    @Test
    public void testGetUserAchievements() throws Exception {
        List<UserAchievement> achievements = Arrays.asList(achievement1, achievement2);
        when(userAchievementRepository.findByUserId(1L)).thenReturn(achievements);

        mockMvc.perform(get("/users/1/achievements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Beginner"))
                .andExpect(jsonPath("$[1].name").value("Intermediate"));
    }
}