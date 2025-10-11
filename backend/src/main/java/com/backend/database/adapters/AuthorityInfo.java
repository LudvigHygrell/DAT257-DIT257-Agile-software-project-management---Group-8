package com.backend.database.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.database.repositories.AdministratorsRepository;
import com.backend.jwt.user.UserUtil;

/**
 * Service for getting information about the current user's authority.
 */
@Service
public class AuthorityInfo {
    
    @Autowired
    private AdministratorsRepository adminRepo;

    /**
     * Gets the authority level of the user of the current session.
     */
    public int getAuthority() {
        if (!UserUtil.isAuthenticated())
            return -1;
        return adminRepo.findById(UserUtil.getUsername())
            .map(v -> v.getLevel())
            .orElse(0);
    }

    /**
     * Tests whether the user of the current session outranks the given user.
     * @param user User to validate rank against.
     * @return True if the current user outranks the given user. 
     * Always false if the current user is not an administrator.
     */
    public boolean outranksUser(String user) {
        int auth = getAuthority();
        if (auth < 1)
            return false;
        return auth > adminRepo.findById(user)
            .map(v -> v.getLevel())
            .orElse(0);
    }

    /**
     * Checks if the current user has modify permissions on data owned by the given user.
     * @param user User to check modify rights on.
     * @return True if the current user can modify data owned by the specified user.
     */
    public boolean hasModifyPermissionOnUser(String user) {
        return UserUtil.getUsername().equals(user) || outranksUser(user);
    }
}
