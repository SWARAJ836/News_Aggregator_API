package com.swaraj.News._Aggregator_API.controller;

import com.swaraj.News._Aggregator_API.dto.PreferenceRequest;
import com.swaraj.News._Aggregator_API.entity.User;
import com.swaraj.News._Aggregator_API.repository.UserRepository;
import com.swaraj.News._Aggregator_API.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PreferencesController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    JwtService jwtService;

    private String getUsernameFromRequest(HttpServletRequest req) {
        String token = req.getHeader("Authorization").replace("Bearer ", "");
        return jwtService.getUsername(token);
    }

    @GetMapping("/preferences")
    public ResponseEntity<String> getPreferences(HttpServletRequest request) {
        User user = userRepo.findByUsername(getUsernameFromRequest(request)).orElseThrow();
        return ResponseEntity.ok(user.getPreferences());
    }

    @PutMapping("/preferences")
    public ResponseEntity<String> updatePreferences(@Valid @RequestBody PreferenceRequest req, HttpServletRequest request) {
        User user = userRepo.findByUsername(getUsernameFromRequest(request)).orElseThrow();
        user.setPreferences(req.getPreferences());
        userRepo.save(user);
        return ResponseEntity.ok("Preferences updated");
    }

}
