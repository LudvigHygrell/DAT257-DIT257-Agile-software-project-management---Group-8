package com.backend.database.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="charitydata")
public class CharityData {
    
    @Id
    @Column(name="charity")
    private String charity;

    @Column(name="humanname")
    private String humanName;

    @Column(name="homepageurl")
    private String homePageUrl;

    @Column(name="charitydescriptionfile")
    private String charityDescriptionFile;

    @Column(name="charityimagefile")
    private String charityImageFile;

    @Column(name="positivescore")
    private long positiveScore;

    @Column(name="negativescore")
    private long negativeScore;

    @Column(name="totalscore")
    private long totalScore;

    protected CharityData() {}

    public String getCharity() {
        return charity;
    }

    public String getHumanName() {
        return humanName;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public String getCharityDescriptionFile() {
        return charityDescriptionFile;
    }

    public String getCharityImageFile() {
        return charityImageFile;
    }

    public long getPositiveScore() {
        return positiveScore;
    }

    public long getNegativeScore() {
        return negativeScore;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public JsonNode toJson() {
        return JsonNodeFactory.instance.objectNode()
            .put("charity", getCharity())
            .put("humanName", getHumanName())
            .put("homePageUrl", getHomePageUrl())
            .put("charityDescritpionFile", getCharityDescriptionFile())
            .put("charityImageFile", getCharityImageFile())
            .put("positiveScore", getPositiveScore())
            .put("negativeScore", getNegativeScore())
            .put("totalScore", getTotalScore());
    }
}
