package com.topcualperen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.topcualperen.entity.User;
import com.topcualperen.repository.UserRepository;
import com.topcualperen.dto.RegisterRequest;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest registerRequest){

        // Username daha öncesinde kullanılıp kullanılmadığına bakıyoruz
        if (userRepository.existsByUsername(registerRequest.getUsername())){
            throw new RuntimeException("Kullanıcı adı(username) zaten kullanılıyor");
        }

        // Email daha öncesinde kullanılıp kulanılmadığına bakıyoruz
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("Email zaten kullanılıyor");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    // Kullanıcı giriş doğrulama metotu
    public boolean validateUser(String username, String password){
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) return false;

        // Kullanıcıyı Optionaldan çıkarırız
        User user = userOpt.get();

        return passwordEncoder.matches(password, user.getPassword());

    }
}
