#!/bin/bash

host=db
port=5432
user=postgres
password=

db=$1
query=$2

#check postgres is up
tries=0
retries=20
wait=10

while [ true ]
do
  echo "Attempting to verify that docker postgres is up and accepting connections at ${host}:${port}"

  #if db and query passed in, use those in health check, otherwise ignore
  if [ -z "$db" ]; then
    pg_isready -h "${host}" -U "${user}" -p "${port}"
  else
    echo "Running test query to verify postgres is ready:" "${query}"
    psql -h "${host}" -U "${user}" -p "${port}" "${db}" -c "${query}"
  fi

  if [ "$?" -eq 0 ]; then
    exit 0
  fi

  if [ "$tries" -ge "$retries" ]; then
    break
  fi

  echo "Could not connect to docker postgres at ${host}:${port} Will retry $((retries-tries)) times after waiting $wait seconds"
  sleep "$wait"
  tries=$((tries+1))
done

echo "Could not connect to docker postgres at ${host}:${port} after trying $tries times"
exit 1