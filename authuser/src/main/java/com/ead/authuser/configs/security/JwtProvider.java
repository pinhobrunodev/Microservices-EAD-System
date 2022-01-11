package com.ead.authuser.configs.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    public String generateJwt(Authentication authentication){
        // Extract the UserDetails of the Authentication
        UserDetails userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder() // Build the token with the UserDetails information that were extracted by the authentication.
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }


}
