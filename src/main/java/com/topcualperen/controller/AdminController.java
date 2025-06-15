package com.topcualperen.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Admin Dashboard - Sadece ADMIN rolüne sahip kullanıcılar erişebilir";
    }

    @GetMapping("/users")
    public String userList() {
        return "Kullanıcı Listesi - Sadece ADMIN rolüne sahip kullanıcılar erişebilir";
    }
}
