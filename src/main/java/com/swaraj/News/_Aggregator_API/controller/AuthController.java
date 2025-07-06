package com.swaraj.News._Aggregator_API.controller;

import com.swaraj.News._Aggregator_API.dto.JwtResponse;
import com.swaraj.News._Aggregator_API.dto.LoginRequest;
import com.swaraj.News._Aggregator_API.dto.RegisterRequest;
import com.swaraj.News._Aggregator_API.entity.User;
import com.swaraj.News._Aggregator_API.repository.UserRepository;
import com.swaraj.News._Aggregator_API.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

}
