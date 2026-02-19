package id.ac.ui.cs.advprog.yomubackend.controller;

import id.ac.ui.cs.advprog.yomubackend.model.User;
import id.ac.ui.cs.advprog.yomubackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // SANGAT PENTING: Agar Next.js (port 3000) diizinkan mengambil data dari sini (port 8080)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Endpoint untuk MENGAMBIL semua data user (Method: GET)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Endpoint untuk MENAMBAH user baru (Method: POST)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
