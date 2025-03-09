package com.virtualbookstore.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Ensure this secret key is at least 32 bytes (before Base64 encoding)
    private final String JWT_SECRET = "dxtTdrPxRrPXzg6/SbnT2zhhCjhM9297GbtgZLIbrWg=";
    private final long JWT_EXPIRATION = 86400000; // 1 day in milliseconds

    // Create a SecretKey using the Base64-decoded secret
    private final SecretKey key = Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(JWT_SECRET));

    /**
     * Generate a JWT token for the given user details.
     */
    @SuppressWarnings("deprecation")
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(key)
                .compact();
    }

    /**
     * Extract the username (subject) from the token.
     */
    public String getUsernameFromToken(String token) {
        try {
            // Create a JwtParser using the legacy parser() method with verifyWith(key)
            JwtParser parser = Jwts.parser().verifyWith(key).build();
            // Use parseSignedClaims() and then call getBody() to retrieve the Claims
            @SuppressWarnings("deprecation")
            Claims claims = parser.parseSignedClaims(token).getBody();
            return claims.getSubject();
        } catch (Exception ex) {
            // Log the error if needed
            return null; // Handle token parsing errors
        }
    }

    /**
     * Validate the token against the provided user details.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Check whether the token is expired.
     */
    private boolean isTokenExpired(String token) {
        try {
            JwtParser parser = Jwts.parser().verifyWith(key).build();
            @SuppressWarnings("deprecation")
            Date expiration = parser.parseSignedClaims(token).getBody().getExpiration();
            return expiration.before(new Date());
        } catch (Exception ex) {
            return true; // If parsing fails, assume the token is expired
        }
    }
}
