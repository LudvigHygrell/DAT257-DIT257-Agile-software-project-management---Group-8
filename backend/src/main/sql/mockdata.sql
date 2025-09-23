\set ON_ERROR_STOP ON

-- Iterate over all public schema tables and delete their contents.
DO $$
DECLARE
    tblName NAME;
BEGIN
    -- Temporarily disable triggers.
    SET session_replication_role = replica;
    FOR tblName IN
        SELECT tablename
            FROM pg_catalog.pg_tables
            WHERE schemaname='public'
    LOOP
        EXECUTE FORMAT('DELETE FROM %I;', tblName);
    END LOOP;
    -- Re-enable triggers.
    SET session_replication_role = DEFAULT;
END $$;

INSERT INTO Users VALUES ('<deleted>', 'benesphere@blackhole.mx', '2bh1jkb34334');
INSERT INTO Users VALUES ('<moderator>', 'moderator.user@benesphere.org', '23njk3533543');
INSERT INTO Users VALUES ('<admin>', 'admin.user@benesphere.org', 'nf4i433jrk3');
INSERT INTO Users VALUES ('<god>', 'god.user@benesphere.org', '2u45j4b5b454');

INSERT INTO Administrators VALUES ('<moderator>', 1);
INSERT INTO Administrators VALUES ('<admin>', 2);
INSERT INTO Administrators VALUES ('<god>', 3);

INSERT INTO Users VALUES ('John Doe', 'john.doe@mailserver.com', '13r9wkmf3fmk2');
INSERT INTO Users VALUES ('Jane Doe', 'jane.doe@mailserver.com', '1934rjnt244t4');
INSERT INTO Users VALUES ('Alice Cooper', 'alice.cooper@gmail.com', '2rn23tgb3h4');
INSERT INTO Users VALUES ('Lisa Simpson', 'lisa.simpson@springfield-elementary.com', '2124fgg354');
INSERT INTO Users VALUES ('Bart Simpson', 'bart.simpson@'', ''hahaha''); DROP TABLE Users CASCADE; --', 'as3344h5rni');
INSERT INTO Users VALUES ('682', 'scp-682@scp.net', '214h3t3tbuj3');

-- Cancer research UK
INSERT INTO Charities VALUES ('1089464');

--- Action for Children
INSERT INTO Charities VALUES ('1097940');

-- The Big Give
INSERT INTO Charities VALUES ('1136547');

INSERT INTO PausedCharities VALUES ('1136547', '<admin>');

INSERT INTO CharityScores VALUES ('John Doe', '1097940', TRUE);
INSERT INTO CharityScores VALUES ('Lisa Simpson', '1097940', TRUE);

INSERT INTO Comments VALUES (1,
 '1089464',
 '{ "contents": "''; INSERT INTO Administrators VALUES (''Bart Simpson'', 3); --" }',
 CURRENT_TIMESTAMP,
 'Bart Simpson');

INSERT INTO CommentScores VALUES (1, '1089464', 'Lisa Simpson', FALSE);
INSERT INTO CommentBlame VALUES (1, '1090464', 'Lisa Simpson', 'inappropriate');

INSERT INTO CharityBlame VALUES ('1097940', '682', 'other');
