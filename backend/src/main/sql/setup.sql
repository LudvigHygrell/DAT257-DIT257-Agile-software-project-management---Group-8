-- PostgresSQL

CREATE TABLE IF NOT EXISTS Users(
    username TEXT
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
        REFERENCES Users(username),
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
        REFERENCES Charities(orgId),
    adminUser TEXT
        NOT NULL
        REFERENCES Administrators(adminUser)
);

CREATE TABLE IF NOT EXISTS CharityScores(
    ratingUser TEXT
        NOT NULL
        REFERENCES Users(username),
    charity TEXT
        NOT NULL
        REFERENCES Charities(orgId),
    vote BOOLEAN
        NOT NULL,
    insertTime TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(likeUser, charity)
);

CREATE TABLE IF NOT EXISTS Comments(
    commentId INT
        NOT NULL,
    charity TEXT
        NOT NULL
        REFERENCES Charities(orgId),
    comment JSON
        NOT NULL,
    commentUser TEXT
        NOT NULL
        REFERENCES Users(username),
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
        REFERENCES Users(username),
    upDown BOOLEAN
        NOT NULL,
    PRIMARY KEY (comment, charity),
    FOREIGN KEY (comment, charity)
        REFERENCES Comments(commentId, charity)
);

CREATE TABLE IF NOT EXISTS SearchedCharities(
    username TEXT
        NOT NULL
        REFERENCES Users(username),
    charity TEXT
        NOT NULL
        REFERENCES Charities(orgId),
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
        REFERENCES Users(username),
    reason TEXT -- TODO
        NOT NULL,
    PRIMARY KEY(comment, charity, reporter)
);

CREATE TABLE IF NOT EXISTS CharityBlame(
    charity TEXT
        NOT NULL
        REFERENCES Charities(orgId),
    reporter TEXT
        NOT NULL
        REFERENCES Users(username),
    reason TEXT -- TODO
        NOT NULL,
    PRIMARY KEY (charity, reporter)
);

CREATE VIEW NextCommendId(charity) AS SELECT MAX(commentId)+1
    FROM Comments
    WHERE Comments.charity=NextCommendId.charity;
