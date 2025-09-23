package com.backend.jwt.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

public class UserDetail implements UserDetails {
    private String username;
    private String password;
    private ArrayList<GrantedAuthority> roles;

    public UserDetail(String username, String password, ArrayList<GrantedAuthority> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserDetail(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public ArrayList<GrantedAuthority> getAuthorities() {
        return roles;
    }
}
