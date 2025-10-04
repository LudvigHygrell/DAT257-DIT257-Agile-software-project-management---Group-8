
\set ON_ERROR_STOP ON

DROP DATABASE IF EXISTS benesphere_db;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname='benesphere') THEN
      DROP OWNED BY benesphere;
      DROP ROLE benesphere;
  END IF;
  IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname='benesphere_sudo') THEN
      DROP OWNED BY benesphere_sudo;
      DROP ROLE benesphere_sudo;
  END IF;
END
$$;

CREATE ROLE benesphere_sudo LOGIN PASSWORD :'benesphere_password';
CREATE ROLE benesphere LOGIN PASSWORD 'J2-3/5=941Adv';

CREATE DATABASE benesphere_db WITH OWNER = benesphere_sudo;

GRANT CONNECT ON DATABASE benesphere_db TO benesphere;

