package com.backend.database.entities;

import com.backend.database.JpaToJsonConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

    @Column(name="classes")
    @Convert(converter=JpaToJsonConverter.class)
    private JsonNode classes;

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

    public JsonNode getClasses() {
        return classes;
    }

    public void setClasses(ArrayNode classes) {
        this.classes = classes;
    }

    public JsonNode toJson() {
        return JsonNodeFactory.instance.objectNode()
            .put("charity", getCharity())
            . <ObjectNode> set("humanName", JsonNodeFactory.instance.textNode(getHumanName()))
            . <ObjectNode> set("homePageUrl", JsonNodeFactory.instance.textNode(getHomePageUrl()))
            . <ObjectNode> set("charityDescritpionFile", JsonNodeFactory.instance.textNode(getCharityDescriptionFile()))
            . <ObjectNode> set("charityImageFile", JsonNodeFactory.instance.textNode(getCharityImageFile()))
            . <ObjectNode> set("positiveScore", JsonNodeFactory.instance.numberNode(getPositiveScore()))
            . <ObjectNode> set("negativeScore", JsonNodeFactory.instance.numberNode(getNegativeScore()))
            . <ObjectNode> set("totalScore", JsonNodeFactory.instance.numberNode(getTotalScore()))
            . <ObjectNode> set("classes", getClasses());
    }
}
