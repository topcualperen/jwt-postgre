package com.topcualperen.controller;

import com.topcualperen.dto.JwtResponse;
import com.topcualperen.dto.LoginRequest;
import com.topcualperen.dto.MessageResponse;
import com.topcualperen.dto.RegisterRequest;
import com.topcualperen.entity.User;
import com.topcualperen.service.UserService;
import com.topcualperen.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerUser(registerRequest);
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            return ResponseEntity.ok(new JwtResponse(token, user.getUsername()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (!isValid) {
            return ResponseEntity.badRequest().body(new MessageResponse("Geçersiz Kullanıcı adı veya şifresi"));
        }

        Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Kullanıcı bulunamadı"));
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(new JwtResponse(token, user.getUsername()));
    }
}
