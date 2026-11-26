package com.example.VozUrbana.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.example.VozUrbana.enums.ProfileEnum;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioSpringSecurity implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final Set<ProfileEnum> profiles;

    public UsuarioSpringSecurity(
            Integer id,
            String username,
            String password,
            Set<ProfileEnum> profiles) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.profiles = profiles == null
                ? new HashSet<>()
                : new HashSet<>(profiles);
    }

    public Integer getId() {
        return id;
    }

    public Set<ProfileEnum> getProfiles() {
        return new HashSet<>(profiles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return profiles.stream()
                .map(profile -> new SimpleGrantedAuthority(profile.getDescricao()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public boolean hasRole(ProfileEnum profile) {
        return profiles.contains(profile);
    }
}
