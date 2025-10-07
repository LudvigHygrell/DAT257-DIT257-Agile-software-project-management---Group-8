package com.backend.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "CharityInfo")
public class CharityInfo {
    @Id
    @Column(name="charity", nullable = false)
    private String charity;

    @Id
    @Column(name="humanName", nullable = false)
    private String humanName;

    @Id
    @Column(name="class", nullable = false)
    private String charityClass;

    @Id
    @Column(name="homePageUrl", nullable = false)
    private String homePageUrl;

    @Id
    @Column(name="charityDescriptionFile", nullable = false)
    private String charityDescriptionFile;

    public CharityInfo(String charity, String humanName, String charityClass, String homePageUrl, String charityDescriptionFile) {
        assert null != charity;
        assert null != humanName;
        assert null != charityClass;
        assert null != homePageUrl;
        assert null != charityDescriptionFile;
        this.charity = charity;
        this.humanName = humanName;
        this.charityClass = charityClass;
        this.homePageUrl = homePageUrl;
        this.charityDescriptionFile = charityDescriptionFile;
    }

    public String getCharity() {
        return charity;
    }

    public void setCharity(String charity) {
        this.charity = charity;
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public String getCharityClass() {
        return charityClass;
    }

    public void setCharityClass(String charityClass) {
        this.charityClass = charityClass;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public String getCharityDescriptionFile() {
        return charityDescriptionFile;
    }

    public void setCharityDescriptionFile(String charityDescriptionFile) {
        this.charityDescriptionFile = charityDescriptionFile;
    }
}
