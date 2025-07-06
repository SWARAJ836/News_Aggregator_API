package com.swaraj.News._Aggregator_API.controller;

import com.swaraj.News._Aggregator_API.entity.User;
import com.swaraj.News._Aggregator_API.repository.UserRepository;
import com.swaraj.News._Aggregator_API.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class NewsController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${gnews.api.key}")
    private String API_KEY;
    // fetch value from application.properties
    private static final String GNEWS_API_URL = "https://gnews.io/api/v4/search";


    @GetMapping("/news")
    public Mono<ResponseEntity<String>> getNews(HttpServletRequest request) {

        System.out.println("🟡 /api/news endpoint called");
        String token = extractToken(request);
        String username = jwtService.getUsername(token);
        System.out.println("🟢 Extracted username from token: " + username);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String prefs = user.getPreferences();
        if (prefs == null || prefs.trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body("User preferences are not set."));
        }

        String url = GNEWS_API_URL + "?q=" + prefs + "&apikey=" + API_KEY;

        System.out.println("-------> Generated-URL  :  " + url );

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .body("Failed to fetch news: " + ex.getResponseBodyAsString())))
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity
                                .internalServerError()
                                .body("Unexpected error: " + ex.getMessage())));
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        return header.replace("Bearer ", "");
    }

    @GetMapping("/news/search/{keyword}")
    public Mono<ResponseEntity<String>> searchNews(@PathVariable String keyword,
                                                   HttpServletRequest request) {
        System.out.println("🔎 /api/news/search called with keyword: " + keyword);

        String token = extractToken(request);
        String username = jwtService.getUsername(token);

        System.out.println("🔐 User: " + username + " is searching for: " + keyword);

        String url = GNEWS_API_URL + "?q=" + keyword + "&apikey=" + API_KEY;
        System.out.println("-------> Generated-URL  :  " + url );

        return webClientBuilder.build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .body("Failed to fetch news: " + ex.getResponseBodyAsString())))
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity
                                .internalServerError()
                                .body("Unexpected error: " + ex.getMessage())));
    }


}
