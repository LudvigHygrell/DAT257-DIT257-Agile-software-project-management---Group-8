@ECHO OFF

:: Windows batch file.
:: Creates a new database and then runs the mock initializer script.

psql -U postgres -c "CREATE DATABASE BeneSphereDB;"
psql -U postgres -c "\i %~dp0/init-mock.sql"
