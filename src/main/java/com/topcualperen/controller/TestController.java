package com.topcualperen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
// Base URL'i belirt
@RequestMapping("/api/test")
// CORS'u tüm origin'ler için aç
// @CrossOrigin(origins = "*")
public class TestController {

    // Herkese açık test endpoint'i - GET /api/test/public
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        // Herkese açık mesaj döndür
        return ResponseEntity.ok("Bu endpoint herkese açık!");
    }

    // JWT token gerektiren korumalı endpoint - GET /api/test/protected
    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        // SecurityContext'ten authentication bilgisini al
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Giriş yapmış kullanıcının username'ini al
        String username = authentication.getName();

        // Kullanıcıya özel mesaj döndür
        return ResponseEntity.ok("Merhaba " + username + "! Bu korumalı bir endpoint.");
    }

    // Kullanıcı bilgilerini döndüren endpoint - GET /api/test/user-info
    @GetMapping("/user-info")
    public ResponseEntity<String> getUserInfo() {
        // SecurityContext'ten authentication bilgisini al
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kullanıcının giriş yapıp yapmadığını kontrol et
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body("Kullanıcı giriş yapmamış!");
        }

        // Kullanıcı adını al
        String username = authentication.getName();

        // Kullanıcı bilgilerini JSON formatında döndür
        return ResponseEntity.ok("{ \"username\": \"" + username + "\", \"authenticated\": true }");
    }
}