-- PostgreSQL

CREATE TABLE Users(
    username TEXT
        PRIMARY KEY,
    email TEXT
        NOT NULL
        UNIQUE,
    userPassword TEXT
        NOT NULL -- "password" reserved, so using userPassword instead.
);

CREATE TABLE Administrators(
    user TEXT
        PRIMARY KEY
        REFERENCES(Users),
    adminLevel INT
        NOT NULL
        CHECK(IN (1, 2, 3))
);

CREATE TABLE Charities(
    orgId TEXT
        PRIMARY KEY,
    insertTime TIMESTAMP 
        NOT NULL 
        DEFAULT CURRENT_TIMESTAMP    
);

CREATE TABLE PausedCharities(
    orgId TEXT
        PRIMARY KEY
        REFERENCES(Charities),
    adminUser TEXT
        NOT NULL
        REFERENCES(Administrators)
);

CREATE TABLE Likes(
    user TEXT
        NOT NULL
        REFERENCES(Users),
    charity TEXT
        NOT NULL
        REFERENCES(Charities),
    insertTime TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(user, charity)
);

CREATE TABLE Comments(
    commentId INT
        NOT NULL,
    charity TEXT
        NOT NULL
        REFERENCES(Charities),
    comment JSON
        NOT NULL
    insertTime TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    user TEXT
        NOT NULL
        REFERENCES(Users),
    PRIMARY KEY(commentId, charity)
);

CREATE TABLE CommentScores(
    comment INT
        NOT NULL,
    charity TEXT
        NOT NULL,
    user TEXT
        NOT NULL
        REFERENCES(Users),
    upDown BOOLEAN
        NOT NULL,
    PRIMARY KEY (comment, charity),
    FOREIGN KEY (comment, charity)
        REFERENCES(Comments.commentId, Comments.charity)
);

CREATE TABLE SearchedCharities(
    username TEXT
        NOT NULL
        REFERENCES(Users),
    charity TEXT
        NOT NULL
        REFERENCES(Charities),
    insertTime TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(username, charity)
);

CREATE TYPE COMMENT_BLAME_REASON AS ENUM(
    'other',
    'inappropriate',
    'discriminatory',
    'fallacy'
);

CREATE TABLE CommentBlame(
    comment INT
        NOT NULL,
    charity TEXT
        NOT NULL,
    reporter TEXT
        NOT NULL
        REFERENCES(Users),
    reason COMMENT_BLAME_REASON
        NOT NULL,
    PRIMARY KEY(comment, charity)
);

CREATE TYPE CHARITY_BLAME_REASON AS ENUM(
    'other',
    'distrustful',
    'discrimination'
    -- more ...
);

CREATE TABLE CharityBlame(
    charity TEXT
        NOT NULL
        REFERENCES(Charities),
    reporter TEXT
        NOT NULL
        REFERENCES(Users),
    reason CHARITY_BLAME_REASON
        NOT NULL,
    PRIMARY KEY (charity, reporter)
);
