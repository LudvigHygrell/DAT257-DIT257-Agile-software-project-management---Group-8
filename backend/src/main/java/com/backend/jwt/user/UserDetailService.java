package com.backend.jwt.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.backend.database.adapters.UserAdapter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserAdapter userAdapter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userAdapter.isUsername(username))
            return new UserDetail(username, userAdapter
                    .getPassword(username)
                    .orElseThrow(
                            () -> new UsernameNotFoundException(
                                    String.format("User %s not found.", username)))
                    .toString());
        throw new UsernameNotFoundException("User not found: " + username);
    }
    
}
