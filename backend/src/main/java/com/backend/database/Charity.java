package com.backend.database;

import jakarta.persistence.*;

/**
 * Represents an entry in the Charity table
 * @author Ludvighygrell
 * @version 1.0
 * @since 2025-09-18
 */

@Entity
@Table(name="Charities")
public class Charity {
    @Id
    @Column(name = "orgID")
    private String orgID;

    protected Charity() {}

    /**
     * Create a new Charity.
     * @param orgID The organization ID of the charity.
     */
    public Charity(String orgID){
        assert null != orgID;
        this.orgID = orgID;
    }

    @Override
    public String toString(){
        return String.format("Charity(orgId=%s)", orgID);
    }

    public void setOrgID(String orgID){ this.orgID = orgID;} //Unnecessary?

    public String getOrgID() {return this.orgID;}

}
