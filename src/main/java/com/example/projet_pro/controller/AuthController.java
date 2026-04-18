package com.example.projet_pro.controller;

import com.example.projet_pro.configuration.JwtUtils;
import com.example.projet_pro.entity.User;
import com.example.projet_pro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (user.getPassword() == null || user.getConfirmPassword() == null) {
            return ResponseEntity.badRequest().body("Password et confirmation sont obligatoires");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Les mots de passe ne correspondent pas");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("L'email est obligatoire");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body("Format email invalide");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        if (user.getPrenom() == null || user.getPrenom().isEmpty()) {
            return ResponseEntity.badRequest().body("Le prénom est obligatoire");
        }

        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username déjà utilisé");
        }

        // Rôle valide parmi les 3 autorisés, sinon ROLE_USER par défaut
        List<String> rolesAutorises = List.of("ROLE_USER", "ROLE_EXPERT", "ROLE_ADMIN");
        if (user.getRole() == null || !rolesAutorises.contains(user.getRole())) {
            user.setRole("ROLE_USER");
        }

        user.setConfirmPassword(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                User userFromDb = userRepository.findByUsername(user.getUsername());

                Map<String, Object> authData = new HashMap<>();
                authData.put("token", jwtUtils.generateToken(user.getUsername(), userFromDb.getRole()));
                authData.put("type", "Bearer");
                authData.put("role", userFromDb.getRole());
                return ResponseEntity.ok(authData);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");

        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}