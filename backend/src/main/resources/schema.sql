-- PostgresSQL

DROP OWNED BY benesphere_mast;


CREATE TABLE IF NOT EXISTS Users(
    username TEXT
        CHECK (NOT (username LIKE '%@%'))
        PRIMARY KEY,
    email TEXT
        NOT NULL
        UNIQUE,
    userPassword TEXT
        NOT NULL -- "password" reserved, so using userPassword instead.
);

CREATE TABLE IF NOT EXISTS Administrators(
    adminUser TEXT
        PRIMARY KEY
        REFERENCES Users(username)
            ON DELETE RESTRICT,
    adminLevel INT
        NOT NULL
        CHECK (adminLevel IN (1, 2, 3))
);

CREATE TABLE IF NOT EXISTS Charities(
    orgId TEXT
        PRIMARY KEY,
    insertTime TIMESTAMP
        NOT NULL 
        DEFAULT CURRENT_TIMESTAMP    
);

CREATE TABLE IF NOT EXISTS PausedCharities(
    orgId TEXT
        PRIMARY KEY
        REFERENCES Charities(orgId)
            ON DELETE CASCADE,
    adminUser TEXT
        NOT NULL
        REFERENCES Administrators(adminUser)
            ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS CharityScores(
    ratingUser TEXT
        NOT NULL
        DEFAULT '<deleted>'
        REFERENCES Users(username)
            ON DELETE SET DEFAULT,
    charity TEXT
        NOT NULL
        REFERENCES Charities(orgId)
            ON DELETE CASCADE,
    vote BOOLEAN
        NOT NULL,
    insertTime TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(ratingUser, charity)
);

CREATE TABLE IF NOT EXISTS Comments(
    commentId INT
        NOT NULL,
    charity TEXT
        NOT NULL
        REFERENCES Charities(orgId)
            ON DELETE CASCADE,
    comment JSON
        NOT NULL,
    commentUser TEXT
        NOT NULL
        DEFAULT '<deleted>'
        REFERENCES Users(username)
            ON DELETE SET DEFAULT,
    insertTime TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(commentId, charity)
);

CREATE TABLE IF NOT EXISTS CommentScores(
    comment INT
        NOT NULL,
    charity TEXT
        NOT NULL,
    scoreUser TEXT
        NOT NULL
        DEFAULT '<deleted>'
        REFERENCES Users(username)
            ON DELETE SET DEFAULT,
    upDown BOOLEAN
        NOT NULL,
    PRIMARY KEY (comment, charity, scoreUser),
    FOREIGN KEY (comment, charity)
        REFERENCES Comments(commentId, charity)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SearchedCharities(
    username TEXT
        NOT NULL
        DEFAULT '<deleted>'
        REFERENCES Users(username)
            ON DELETE SET DEFAULT,
    charity TEXT
        NOT NULL
        REFERENCES Charities(orgId)
            ON DELETE NO ACTION,
    visited BOOLEAN
        NOT NULL
        DEFAULT FALSE,
    insertTime TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(username, charity)
);

CREATE TABLE IF NOT EXISTS CommentBlame(
    comment INT
        NOT NULL,
    charity TEXT
        NOT NULL,
    reporter TEXT
        NOT NULL
        DEFAULT '<deleted>'
        REFERENCES Users(username)
            ON DELETE SET DEFAULT,
    reason TEXT -- TODO
        NOT NULL,
    PRIMARY KEY(comment, charity, reporter)
);

CREATE TABLE IF NOT EXISTS CharityBlame(
    charity TEXT
        NOT NULL
        REFERENCES Charities(orgId)
            ON DELETE CASCADE,
    reporter TEXT
        NOT NULL
        DEFAULT '<deleted>'
        REFERENCES Users(username)
            ON DELETE SET DEFAULT,
    reason TEXT -- TODO
        NOT NULL,
    PRIMARY KEY (charity, reporter)
);

CREATE TABLE IF NOT EXISTS CharityClasses(
    className TEXT
        PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS CharityClassifications(
    charity TEXT
        REFERENCES Charities(orgId),
    class TEXT
        NOT NULL
        REFERENCES CharityClasses(className),
    PRIMARY KEY (charity, class)
);

CREATE TABLE IF NOT EXISTS CharityInfo(
    charity TEXT
        PRIMARY KEY
        REFERENCES Charities(orgId)
            ON DELETE CASCADE,
    humanName TEXT
        NOT NULL UNIQUE,
    homePageUrl TEXT
        NOT NULL,
    charityDescriptionFile TEXT
        NOT NULL,
    charityImageFile TEXT
        NOT NULL
);

CREATE OR REPLACE VIEW CharityPositiveScores AS SELECT
    c1.orgId AS charity,
    COALESCE(SUM(CASE WHEN vote THEN 1 ELSE 0 END), 0) AS score
    FROM
        Charities c1
    LEFT JOIN
        CharityScores c2 ON (c1.orgId=c2.charity)
    GROUP BY c1.orgId;

CREATE OR REPLACE VIEW CharityNegativeScores AS SELECT
    c1.orgId AS charity,
    COALESCE(SUM(CASE WHEN vote THEN 0 ELSE 1 END), 0) AS score
    FROM
        Charities c1
    LEFT JOIN
        CharityScores c2 ON (c1.orgId=c2.charity)
    GROUP BY c1.orgId;

CREATE OR REPLACE VIEW CharityClassificationJson AS SELECT
    c.orgId AS charity,
    COALESCE(JSON_AGG(cc.class), JSON('[]')) AS classes
    FROM
        Charities c
    LEFT JOIN
        CharityClassifications cc
    ON (c.orgId=cc.charity)
    GROUP BY c.orgId;

CREATE OR REPLACE VIEW CharityData AS SELECT
        ci.charity AS charity,
        humanName,
        homePageUrl,
        charityDescriptionFile,
        charityImageFile,
        cps.score AS positiveScore,
        cns.score AS negativeScore,
        (cps.score - cns.score) AS totalScore,
        ccjs.classes AS classes
    FROM
        CharityInfo ci
    LEFT JOIN
        CharityPositiveScores cps
        ON (ci.charity=cps.charity) 
    LEFT JOIN
        CharityNegativeScores cns
        ON (ci.charity=cns.charity)
    LEFT JOIN
        CharityClassificationJson ccjs
        ON (ci.charity=ccjs.charity);

CREATE OR REPLACE VIEW NextCommentId AS SELECT
        ca.orgId AS charity,
        COALESCE(MAX(com.commentId), 0) + 1 AS nextId
    FROM
        Charities ca
    LEFT JOIN
        Comments com
    ON (ca.orgId = com.charity)
    GROUP BY ca.orgId;
