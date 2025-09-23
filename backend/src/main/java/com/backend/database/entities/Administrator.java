package com.backend.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Administrators")
public class Administrator {
    
    /**
     * Minimum required admin level to pause/resume a charity.
     */
    public static final int PAUSE_CHARITY_LEVEL = 1;

    @Id
    @Column(name="adminUser")
    private String adminUser;

    @Column(name="level")
    private int level;

    /**
     * Create a new admin user record.
     * @param adminUser Username of the admin.
     * @param level Access level of the user.
     */
    public Administrator(String adminUser, int level) {
        assert null != adminUser;
        this.adminUser = adminUser;
        this.level = level;
    }

    public String getAdminUser() {
        return adminUser;
    }
    
    public int getLevel() {
        return level;
    }
}
