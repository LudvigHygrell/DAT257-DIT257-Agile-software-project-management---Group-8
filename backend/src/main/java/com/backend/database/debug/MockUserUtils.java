package com.backend.database.debug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.database.adapters.UserAdapter;

/**
 * Utilities for managing the mock user.
 */
@Service
public class MockUserUtils {

    @Autowired
    private UserAdapter userAdapter;

    /**
     * Username of the mock user.
     */
    public static final String MOCK_USER_USERNAME = "mockingjay";

    /**
     * Password of the mock user.
     */
    public static final String MOCK_USER_PASSWORD = "Let the games begin";
    
    /**
     * Email of the mock user.
     */
    public static final String MOCK_USER_EMAIL = "mock@user.com";

    /**
     * Ensures that the mock user is registered.
     */
    public void ensureMockUserRegistered() {
        if (userAdapter.isUsername("mockingjay"))
            return;
        userAdapter.register(MOCK_USER_USERNAME, MOCK_USER_EMAIL, MOCK_USER_PASSWORD);
    }
}
