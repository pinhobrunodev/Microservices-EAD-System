package com.ead.serviceregistry.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ead.serviceRegistry.username}")
    private String username;
    @Value("${ead.serviceRegistry.password}")
    private String password;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic() // Defining that we will use "Basic"
                .and()
                .authorizeRequests()              //  Any requests need to be authenticated
                .anyRequest().authenticated()   // Example : Some endpoint I need to be public... just put permitAll()
                .and()
                .csrf().disable()  // Because Client Eureka dont have valid Token
                .formLogin(); // Show the form to login we need this formLogin()

    }


    // Defining the type of authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // Setting the information to connect on Service Registry
                .withUser(username)
                .password(passwordEncoder().encode(password))
                .roles("ADMIN");
    }


}
