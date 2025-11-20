package com.poseidon.app.security;


import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.poseidon.app.domain.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Service for generating, parsing, and validating JSON Web Tokens (JWT) for authentication.
 *
 * <p>This service uses a symmetric HMAC key provided via application properties
 * to sign and verify JWT tokens. The token stores the username as the subject
 * and the user's role as a claim.</p>
 *
 * <p>The JWT expiration is set to 10 minutes from the time of creation.</p>
 */

@Service
public class JwtService {
    
    
    private final SecretKey key;
        
    /**
     * Constructs a JwtService using the secret key from application properties.
     *
     * @param jwtSecret the secret key used for signing JWT tokens
     */
    
    public JwtService(@Value("${jwt.secret}") String jwtSecret) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    /**
     * Creates a JWT token for the given user.
     *
     * <p>The token includes the username as subject and the role as a claim prefixed with "ROLE_".
     * The token is signed using HS256 algorithm and expires in 10 minutes.</p>
     *
     * @param user the user for whom the token is created
     * @return a signed JWT token as a String
     */
    
    public String createToken(User user) {
        Date now = new Date();
        
        
        System.out.println("Create token for user " + user.getUsername() + " with role : " + user.getRole());

        return Jwts.builder()
                .claim("role", "ROLE_" + user.getRole())
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + 60 * 10 * 1000))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
                
    }
    
    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token
     * @return the username contained in the token
     */
    
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    /**
     * Extracts the role claim from the given JWT token.
     *
     * @param token the JWT token
     * @return the role stored in the token, or null if not present
     */
    
    public String extractRole(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
    
    /**
     * Validates the given JWT token by verifying its signature and structure.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
