#!/usr/bin/env bash

#
# bash script file.
#
# Creates a new database and then runs the mock initializer script.
#

psql -U postgres -c 'CREATE DATABASE BeneSphereDB;'
psql -U postgres -c "\i $(dirname "$0")/init-mock.sql"
