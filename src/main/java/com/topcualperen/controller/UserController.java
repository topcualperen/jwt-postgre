package com.topcualperen.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    @GetMapping("/profile")
    public String userProfile() {
        return "Kullanıcı Profili - USER ve ADMIN rolüne sahip kullanıcılar erişebilir";
    }

    @GetMapping("/settings")
    public String userSettings() {
        return "Kullanıcı Ayarları - USER ve ADMIN rolüne sahip kullanıcılar erişebilir";
    }
}
