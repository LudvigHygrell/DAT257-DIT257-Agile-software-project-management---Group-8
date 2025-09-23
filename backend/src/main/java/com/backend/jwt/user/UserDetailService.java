package com.backend.jwt.user;

import com.backend.database.UserAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;


public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserAdapter userAdapter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userAdapter.isUsername(username))
            return new User(username, userAdapter.getPassword(username), new ArrayList<>());
        throw new UsernameNotFoundException("User not found: " + username);
    }
    
}
