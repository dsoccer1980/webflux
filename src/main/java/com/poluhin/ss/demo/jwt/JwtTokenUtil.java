package com.poluhin.ss.demo.jwt;

import com.poluhin.ss.demo.domain.model.AuthResponse;
import com.poluhin.ss.demo.service.CacheService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
    private final CacheService cacheService;
    @Value("${jwt.signing.key.secret}")
    private String secretKey;

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported", ex);
        }
        return false;
    }

    public String generateAccessToken(UserDetails user) {
        System.out.println("generateAccessToken for user: " + user.getUsername());
        String token = Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
        cacheService.write(new AuthResponse(user.getUsername(), token));
        return token;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }
}
