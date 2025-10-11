-- \set ON_ERROR_STOP ON

INSERT INTO Users VALUES
    ('<deleted>', 'benesphere@blackhole.mx', '2bh1jkb34334'),
    ('<moderator>', 'moderator.user@benesphere.org', '23njk3533543'),
    ('<admin>', 'admin.user@benesphere.org', 'nf4i433jrk3'),
    ('<god>', 'god.user@benesphere.org', '2u45j4b5b454');

INSERT INTO Administrators VALUES
    ('<moderator>', 1),
    ('<admin>', 2),
    ('<god>', 3);

INSERT INTO Users VALUES
    ('John Doe', 'john.doe@mailserver.com', '13r9wkmf3fmk2'),
    ('Jane Doe', 'jane.doe@mailserver.com', '1934rjnt244t4'),
    ('Alice Cooper', 'alice.cooper@gmail.com', '2rn23tgb3h4'),
    ('Lisa Simpson', 'lisa.simpson@springfield-elementary.com', '2124fgg354'),
    ('Bart Simpson', 'bart.simpson@'', ''hahaha''); DROP TABLE Users CASCADE; --', 'as3344h5rni'),
    ('682', 'scp-682@scp.net', '214h3t3tbuj3');

INSERT INTO CharityClasses VALUES
    ('Medicine'),
    ('Humanitarian'),
    ('Equality'),
    ('Economics');

-- Cancer research UK
INSERT INTO Charities VALUES
    ('1089464'),
--- Action for Children
    ('1097940'),
-- The Big Give
    ('1136547');

INSERT INTO CharityInfo VALUES
    ('1089464', 'Cancer research UK', 'https://www.cancerresearchuk.org/', 'cancer-research-uk.txt', 'cancer-research-uk.png'),
    ('1097940', 'Action for children', 'https://www.actionforchildren.org/', 'action-for-children.txt', 'action-for-children.svg'),
    ('1136547', 'The Big Give', 'https://www.thebiggivesa.org/', 'the-big-give.txt', 'the-big-give.svg');

INSERT INTO CharityClassifications VALUES
    ('1089464', 'Medicine'),
    ('1089464', 'Humanitarian'),
    ('1097940', 'Humanitarian'),
    ('1136547', 'Humanitarian'),
    ('1136547', 'Economics'),
    ('1136547', 'Equality');

INSERT INTO PausedCharities VALUES ('1136547', '<admin>');

INSERT INTO CharityScores VALUES
    ('John Doe', '1097940', TRUE),
    ('Lisa Simpson', '1097940', TRUE),
    ('John Doe', '1136547', FALSE),
    ('Bart Simpson', '1136547', TRUE),
    ('682', '1089464', FALSE),
    ('682', '1097940', FALSE);

INSERT INTO Comments VALUES
    (1, '1089464', '{ "contents": "''; INSERT INTO Administrators VALUES (''Bart Simpson'', 3); --" }', 'Bart Simpson'),
    (2, '1089464', '{ "contents": "I love what they do!" }', 'Lisa Simpson'),
    (3, '1089464', '{ "contents": "Don''t much care for it." }', '682'),
    (1, '1136547', '{ "contents": "Absolutely great!" }', 'John Doe'),
    (2, '1136547', '{ "contents": "A guy that works there took my sandwich." }', '682'),
    (3, '1136547', '{ "contents": "Nothing to say about this one." }', 'Bart Simpson'),
    (4, '1136547', '{ "contents": "Great. I give about 30$ each month, never regretted it." }', 'Jane Doe'),
    (1, '1097940', '{ "contents": "True to their name!" }', 'Jane Doe'),
    (2, '1097940', '{ "contents": "Good charity." }', 'John Doe');

INSERT INTO CommentScores VALUES
    (1, '1089464', 'Lisa Simpson', FALSE),
    (4, '1136547', 'John Doe', TRUE),
    (4, '1136547', 'Lisa Simpson', TRUE),
    (4, '1136547', '682', FALSE);

INSERT INTO CommentBlame VALUES
    (1, '1090464', 'Lisa Simpson', 'inappropriate'),
    (4, '1136547', '682', 'bad language');

INSERT INTO CharityBlame VALUES
 ('1097940', '682', 'other'),
 ('1097940', 'Bart Simpson', 'injust');
