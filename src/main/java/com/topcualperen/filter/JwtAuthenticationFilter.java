package com.topcualperen.filter;

import com.topcualperen.entity.User;
import com.topcualperen.service.UserService;
import com.topcualperen.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Request'ten Authorization headerını al
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Authorization header'ı var mı ve "Bearer " ile başlıyor mu kontrol et
        if (authHeader != null && authHeader.startsWith("Bearer ")){

            //Bearer kısmını çıkarıp sadece token'ı alacağız
            token = authHeader.substring(7);
            try {
                // Token'dan username i çıkar
                username = jwtUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                logger.error("JWT Token'dan username alınamadı: " + e.getMessage());
            }
        }

        // username ve Spring Security de authentication var mı kontrol et
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> userOptional = userService.findByUsername(username);

            if (userOptional.isPresent() && jwtUtil.validateToken(token)) {
                User user = userOptional.get();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // SecurityContext'e authentication'ı set et
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Filter Chain'i devam ettir (Bir sonraki filter'a geç)
        filterChain.doFilter(request, response);
    }
}
