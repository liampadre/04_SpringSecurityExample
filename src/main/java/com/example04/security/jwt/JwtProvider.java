package com.example04.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example04.model.UserEntity;

@Log
@Component
public class JwtProvider {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String TOKEN_TYPE = "JWT";

    // Necesita un secret del 512 bits como mínimo
    @Value("${jwt.secret:12345678901234567890123456789012345678901234567890123456789012345678901234567890}")
    private String secret;

    @Value("${jwt.ttl:864000}")
    private Long ttl;

    public String generateToken(Authentication authentication) {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Date tokenExpDate = new Date(System.currentTimeMillis() + (ttl * 1000));

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(tokenExpDate)
                .setHeaderParam("typ", TOKEN_PREFIX)
                .setSubject(Long.toString(userEntity.getId()))
                .claim("fullName", userEntity.getUsername())
                .claim("roles", userEntity.getRoles().stream().map(Enum::name).collect(Collectors.joining(",")))
                .compact();
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .parseClaimsJws(token);
            return true;
        } catch(SignatureException se) {
            log.info("Error in sign of jwt token: " + se.getMessage());
        } catch(MalformedJwtException mje) {
            log.info("Malformed token: " + mje.getMessage());
        } catch(ExpiredJwtException eje) {
            log.info("Expired token: " + eje.getMessage());
        } catch(UnsupportedJwtException uje) {
            log.info("Unsupported token: " + uje.getMessage());
        } catch(IllegalArgumentException iae) {
            log.info("Illegal JWT argument: " + iae.getMessage());
        }

        return false;
    }
}
