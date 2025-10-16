package com.backend.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "charityinfo")
public class CharityInfo {
    @Id
    @Column(name="charity", nullable = false)
    private String charity;

    @Column(name="humanname", nullable = false)
    private String humanName;

    @Column(name="class", nullable = false)
    private String charityClass;

    @Column(name="homepageurl", nullable = false)
    private String homePageUrl;

    @Column(name="charitydescriptionfile", nullable = false)
    private String charityDescriptionFile;

    @Column(name="activitiesdirectory")
    private String activitiesDirectory;

    protected CharityInfo() {}

    public CharityInfo(String charity, String humanName, String charityClass, String homePageUrl, String charityDescriptionFile, String activitiesDirectory) {
        assert null != charity;
        assert null != humanName;
        assert null != charityClass;
        assert null != homePageUrl;
        assert null != charityDescriptionFile;
        assert null != activitiesDirectory;
        this.charity = charity;
        this.humanName = humanName;
        this.charityClass = charityClass;
        this.homePageUrl = homePageUrl;
        this.charityDescriptionFile = charityDescriptionFile;
        this.activitiesDirectory = activitiesDirectory;
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

    public String getActivitiesDirectory() {
        return activitiesDirectory;
    }

    public void setActivitiesDirectory(String activitiesDirectory) {
        this.activitiesDirectory = activitiesDirectory;
    }
}
