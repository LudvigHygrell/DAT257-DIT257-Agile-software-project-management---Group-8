package com.backend.database.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.*;

/**
 * Represents an entry in the Charity table
 * @author Ludvighygrell
 * @version 1.0
 * @since 2025-09-18
 */

@Entity
@Table(name="charities")
public class Charity {
    
    @Id
    @Column(name = "orgid")
    private String orgId;

    protected Charity() {}

    /**
     * Create a new Charity.
     * @param orgID The organization ID of the charity.
     */
    public Charity(String orgID){
        assert null != orgID;
        this.orgId = orgID;
    }

    @Override
    public String toString(){
        return String.format("Charity(orgId=%s)", orgId);
    }

    public void setOrgID(String orgID){ this.orgId = orgID; }

    public String getOrgID() { return this.orgId; }

    public JsonNode toJson() {
        return JsonNodeFactory.instance.objectNode()
            .put("orgId", orgId);
    }
}
