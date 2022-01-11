package com.ead.authuser.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity // -> Turn off wall security config default
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
// -> This class that will be set the global configuration of AuthenticationManager
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    // Endpoint that don't need authentication
    private static final String[] AUTH_WHITELIST = {
            "/auth/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint) // If have some error during authentication will throw exception to UNAUTHORIZED.
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll() // Free the Endpoint ( don't need authentication )
                .antMatchers(HttpMethod.GET,"/users/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .csrf().disable();

    }


    @Override  // The way that authenticationManager will authenticate... userDetailsService return an Obj with UserDetails that have the attributes to authentication.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // The way that will see the password = passwordEncode
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    // Used to authenticate the user
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
