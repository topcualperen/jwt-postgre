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

            return ResponseEntity.ok(new MessageResponse("Kullanıcı başarıyla kaydedildi"));
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

        String token = jwtUtil.generateToken(loginRequest.getUsername());

        return ResponseEntity.ok(new JwtResponse(token, loginRequest.getUsername()));
    }
}
