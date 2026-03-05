package id.ac.ui.cs.advprog.yomubackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.ac.ui.cs.advprog.yomubackend.auth.controller.AuthController;
import id.ac.ui.cs.advprog.yomubackend.auth.dto.LoginRequest;
import id.ac.ui.cs.advprog.yomubackend.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.yomubackend.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.yomubackend.auth.utils.JwtUtils;
import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testRegister_Success() throws Exception {
        RegisterRequest request = new RegisterRequest("gerry_test", "gerry@test.com", "pass123");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Harapannya 200 OK
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testRegister_Fail_UsernameTaken() throws Exception {
        RegisterRequest request = new RegisterRequest("gerry_ada", "ada@test.com", "pass123");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()) // Harapannya 400 Bad Request
                .andExpect(content().string("Username already taken!"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        LoginRequest loginReq = new LoginRequest("gerry_test", "pass123");
        User mockUser = new User("gerry_test", "gerry@test.com", "encodedPassword");

        when(userRepository.findByUsername(loginReq.getUsername())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(loginReq.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(mockUser.getUsername())).thenReturn("dummy-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-jwt-token"));
    }

    @Test
    public void testLogin_Fail_UserNotFound() throws Exception {
        LoginRequest loginReq = new LoginRequest("hantu", "pass123");

        when(userRepository.findByUsername("hantu")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isNotFound());
    }
}