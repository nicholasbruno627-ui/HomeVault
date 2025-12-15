package com.homevault.server.auth;

import com.homevault.server.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

	//secret key
    private static final String SECRET_KEY = "gG93Fv0*C19k@5!pQ7s$D3r9Jf82LmXwZcVbErTnFhPgQwXeRsTyUiOpAsDfGhJ";

    //HMAC-SHA hash
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    //generate JWT for current user
    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        long expiry = now + 1000L * 60 * 60 * 24; //token expires in 24 hours

        return Jwts.builder()
                //store the user's ID as the subject
                .setSubject(user.getId().toString())
                //store email inside the token
                .claim("email", user.getEmail())
                //store display name inside the token
                .claim("displayName", user.getDisplayName())
                //issued at timestamp
                .setIssuedAt(new Date(now))
                //expiration timestamp
                .setExpiration(new Date(expiry))
                //token is signed with HMAC-SHA256 string
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                //builds final JWT string
                .compact();
    }

    
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        //parse the JWT and validate its signature using signing key
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        //gets everything inside JWT token
        return resolver.apply(claims);
    }

    
    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            //token is valid only if its expiration time is after current time
            return claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            //any exception means invalid token
            return false;
        }
    }

   
    public String extractEmail(String token) {
        //use the generic extractClaim helper and casts the "email" claim to a string
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

}
