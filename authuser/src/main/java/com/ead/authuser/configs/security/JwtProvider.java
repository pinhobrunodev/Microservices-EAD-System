package com.ead.authuser.configs.security;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Log4j2
@Component
public class JwtProvider {


    @Value("${ead.auth.jwtSecret}")
    private String jwtSecret;
    @Value("${ead.auth.jwtExpiration}")
    private int jwtExpirationMs;


    // Method to generate the token
    public String generateJwt(Authentication authentication) {
        // Extract the UserDetails of the Authentication
        UserDetails userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder() // Build the token with the UserDetails information that were extracted by the authentication.
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // Receive the token that was generated and catch the  username of the user that are on the token.
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    // Method that valid the token
    public boolean validateJwt(String authToken) {
        try {
            // If is ok , the token will is validated
            // Receive the jwtSecret to see if is right
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token : {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {} ", e.getMessage());
        }
        return false;
    }


}
