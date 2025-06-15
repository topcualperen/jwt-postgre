package com.topcualperen.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Base64;

@Component
public class JwtUtil {    
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Secret Key Oluşturan Metot
    private SecretKey getSigninKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username){


       // Şu anki zamanı al
        Date now = new Date();
        // Token'ın ne zaman sona ereceğini hesapla
        Date expiryDate = new Date(now.getTime() + expiration);

        // Jwt Token Oluştur ve Döndür
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigninKey())
                .compact();
    }

    // JWT Token'dan username i Çıkaran Metot
    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())  // İmza doğrulama için secret key'i belirt
                .build()
                .parseClaimsJws(token)          // Token'ı parse et
                .getBody()                      // Token'ın body kısmını al
                .getSubject();                  // Subject'i (username) döndür

    }

    // JWT token'ın geçerli olup olmadığını kontrol eden metot
    public boolean validateToken(String token){
        try{
            // Token'ı parse etmeye çalış
            Jwts.parserBuilder()
                    .setSigningKey(getSigninKey()) // imza doğrulaması için secret key'i belirt - NOT: Bu satır, token’ın doğruluğunu kontrol edebilmesi için bir referans imza anahtarı sağlar.
                    .build()
                    .parseClaimsJws(token); // token'ı parse et - NOT: Burada hem imza doğrulaması yapar hem de token süresi yani expiration süresine bakar
            // Hata yoksa token geçerlidir
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Hata varsa token geçersizdir
            return false;
        }
    }

    // Jwt token'ın süresinin dolup dolmadığını kontrol ederiz bu metot ile ama bu metotun yaptığını parseClaimsJws() metotu da yapar. Bu yazdığımız metotun amacı token süresinin dolup dolmadığını true-false ile almak istememizdir yoksa bu expired parseClaimsJws() metotu ile kontrol edilir
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
