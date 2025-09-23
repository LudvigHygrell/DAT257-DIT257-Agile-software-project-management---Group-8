-- Note:
--      Run with psql -U postgres -d benespheredb
--
-- To create the database, run bash ./sql/create
--

\set ON_ERROR_STOP ON

\i setup.sql

-- Create the BeneSphere user (and grant proper permissions) if it does not exist.
DO $$ BEGIN

IF NOT EXISTS (SELECT 1 FROM pg_user WHERE usename='benesphere') THEN
    CREATE USER BeneSphere
        WITH PASSWORD 'J2-3/5=941Adv';

    GRANT SELECT, INSERT, DELETE, UPDATE
        ON TABLE Users
        TO BeneSphere;

    GRANT SELECT
        ON TABLE Administrators
        TO BeneSphere;

    GRANT SELECT
        ON TABLE Charities
        TO BeneSphere;

    GRANT SELECT
        ON TABLE PausedCharities
        TO BeneSphere;

    GRANT SELECT, INSERT, DELETE, UPDATE
        ON TABLE CharityScores
        TO BeneSphere;

    GRANT SELECT, INSERT, UPDATE
        ON TABLE Comments
        TO BeneSphere;

    GRANT SELECT, INSERT, UPDATE, DELETE
        ON TABLE CommentScores
        TO BeneSphere;

    GRANT SELECT, INSERT
        ON TABLE SearchedCharities
        TO BeneSphere;

    GRANT SELECT, INSERT, UPDATE, DELETE
        ON TABLE CommentBlame
        TO BeneSphere;

    GRANT SELECT, INSERT, UPDATE, DELETE
        ON TABLE CharityBlame
        TO BeneSphere;
END IF;

END $$;

\i mockdata.sql
