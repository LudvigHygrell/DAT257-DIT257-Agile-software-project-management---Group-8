package com.backend.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="PausedCharities")
public class PausedCharity {
    
    @Id
    @Column(name="orgId")
    private String orgId;

    @Column(name="adminUser")
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
