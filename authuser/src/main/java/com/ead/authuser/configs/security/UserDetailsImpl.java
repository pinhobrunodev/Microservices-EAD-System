package com.ead.authuser.configs.security;

import com.ead.authuser.models.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// Remove responsibility of UserModel
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    // username,password,roles => mandatory

    private UUID userId;
    private String fullName;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private Collection<? extends GrantedAuthority> authorities; // Collection of Any Type that extends GrantedAuthority

    // UserModel -> From Database
    public static UserDetailsImpl build(UserModel userModel) {
        // Extract the list of authorities for a specific userModel that came from the Database... to Spring analyze and see what role the user have.
        List<GrantedAuthority> authorities = userModel.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
        return new UserDetailsImpl( // -> Conversion UserModel to UserDetailsImpl
                userModel.getUserId(),
                userModel.getFullName(),
                userModel.getUsername(),
                userModel.getPassword(),
                userModel.getEmail(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
