package com.jwt.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Encoders.BASE64.encode(key.getEncoded());
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", base64Key);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 10000);
    }

    @Test
    void generateAndValidateToken() {
        String username = "testuser";
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Encoders.BASE64.encode(key.getEncoded());
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, base64Key)
                .compact();
        Claims claims = Jwts.parser().setSigningKey(base64Key).parseClaimsJws(token).getBody();
        assertEquals(username, claims.getSubject());
    }
}
