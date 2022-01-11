package com.ead.authuser.configs.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Intercept the request and verify if everything is ok... (jwt provides methods)
@Log4j2
public class AuthenticationJwtFilter extends OncePerRequestFilter {        // OncePerRequestFilter : 1 Execution by 1 request on Dispatcher
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    // GET the token -> Extract the username -> LoadUserByUsername ->  Set that information on Authentication -> Authenticate the user request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtStr = getTokenHeader(request); // Get the token of the request that is inside the header of requisition
            if (jwtStr != null && jwtProvider.validateJwt(jwtStr)) {
                String username = jwtProvider.getUsername(jwtStr);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Cannot set User Authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }


    // Get the Token that is on HEADER OF REQUEST
    private String getTokenHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization"); // Token will be on Authorization
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}