package com.jyr.DailyLog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@PropertySource("classpath:application-JWT.properties")
public class JwtUtil {
    private final Key key;
    private final long expireTime = 1000 * 60 * 60;

    public JwtUtil(@Value("${jwt.secret.key}") String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException e){
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    public String getUsername(String token){
        return getClaims(token).getSubject();
    }

    public boolean isExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(String username, List<String> roles){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setSubject(username)
                .claim("role",roles)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
