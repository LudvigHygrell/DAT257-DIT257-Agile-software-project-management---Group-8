

GRANT USAGE ON SCHEMA public TO benesphere;

GRANT SELECT, INSERT, DELETE, UPDATE
    ON TABLE public.Users
    TO benesphere;

GRANT SELECT
    ON TABLE public.Administrators
    TO benesphere;

GRANT SELECT
    ON TABLE public.Charities
    TO benesphere;

GRANT SELECT
    ON TABLE public.PausedCharities
    TO benesphere;

GRANT SELECT, INSERT, DELETE, UPDATE
    ON TABLE public.CharityScores
    TO benesphere;

GRANT SELECT, INSERT, UPDATE
    ON TABLE public.Comments
    TO benesphere;

GRANT SELECT, INSERT, UPDATE, DELETE
    ON TABLE public.CommentScores
    TO benesphere;

GRANT SELECT, INSERT
    ON TABLE public.SearchedCharities
    TO benesphere;

GRANT SELECT, INSERT, UPDATE, DELETE
    ON TABLE public.CommentBlame
    TO benesphere;

GRANT SELECT, INSERT, UPDATE, DELETE
    ON TABLE public.CharityBlame
    TO benesphere;
