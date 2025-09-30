package com.backend.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="pausedcharities")
public class PausedCharity {
    
    @Id
    @Column(name="orgid")
    private String orgId;

    @Column(name="adminuser")
    private String adminUser;

    public PausedCharity(String orgId, String adminUser) {
        assert null != orgId;
        assert null != adminUser;
        this.orgId = orgId;
        this.adminUser = adminUser;
    }

    public String getOrgId() { return orgId; }
    public String getAdmin() { return adminUser; }
}
