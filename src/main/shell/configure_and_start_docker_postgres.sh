#!/bin/bash

host=db
port=5432
user=postgres
password=
db=userrequestlog

#start postgres
/docker-entrypoint.sh postgres &

#just check that postgres is up and running and accepting connections
/shell_scripts/check_docker_postgres_up.sh

if [ "$?" -ne 0 ]; then
    echo "Could not verify that postgres is running. Exiting!"
    exit "$?"
fi

#create dbs
psql -h "${host}" -U "${user}" -c "drop database if exists \"${db}\";"
createdb -h "${host}" -U "${user}" "${db}"

#create schema/tables
psql -h "${host}" -U "${user}" "${db}" -f /sql_scripts/userrequestlog_db_ddl.sql

#hold here or container will exit
tail -f /dev/null
