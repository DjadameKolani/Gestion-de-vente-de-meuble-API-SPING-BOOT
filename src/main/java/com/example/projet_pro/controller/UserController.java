package com.example.projet_pro.controller;

import com.example.projet_pro.entity.User;
import com.example.projet_pro.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id,
                                           @RequestBody User user) {
        return userRepository.findById(id)
                .map(existing -> {
                    user.setId(id);
                    // Ne pas écraser le mot de passe si non fourni
                    if (user.getPassword() == null || user.getPassword().isBlank()) {
                        user.setPassword(existing.getPassword());
                    }
                    return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return userRepository.findById(id)
                .map(existing -> {
                    userRepository.deleteById(id);
                    return new ResponseEntity<>("Utilisateur supprimé", HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.NOT_FOUND));
    }
}