#!/bin/bash

set -e

# config

source $(dirname $0)/config.conf

function liquibase() {
  java -jar liquibase/lib/liquibase-core-3.5.3.jar \
    --changeLogFile="liquibase/changeLog-master.xml" \
    --username="$dbuser" \
    --password="$dbpassword" \
    --url="$dburl" \
    --classpath="liquibase/lib/mysql-connector-java-5.1.38.jar" \
    --driver="com.mysql.jdbc.Driver" \
    --logLevel="info" \
    $*
}

function execute() {
  dburl="jdbc:mysql://$dbhost:3306/$dbname?useSSL=false&nullNamePatternMatchesAll=true&serverTimezone=UTC"

  echo "   ENV: $env"
  echo "   DB_USER: $dbuser"
  echo "   DB_URL: $dburl"
  liquibase $*
}

set -e
cd $(dirname $0)/..

if [ $# -lt 2 ]; then
  echo "Usage: $0 <local|remote> <liquibase command>"
  echo "Example: $0 local update"
  echo "Example: $0 remote rollbackCount 1"
  exit 1
fi

env="$1"
shift

case "$env" in
local)
  if [ -z "${LOCAL_DB_USER}" -o -z "${LOCAL_DB_PASSWORD}" -o -z "${LOCAL_DB_SCHEMA}" -o -z "${LOCAL_DB_HOST}" ]; then
    echo "Please specify all the following in config file: LOCAL_DB_USER, LOCAL_DB_PASSWORD, LOCAL_DB_SCHEMA, LOCAL_DB_HOST"
    exit 1
  fi

  dbname="$LOCAL_DB_SCHEMA"
  dbuser="$LOCAL_DB_USER"
  dbpassword="$LOCAL_DB_PASSWORD"
  dbhost="$LOCAL_DB_HOST"

  execute $*

  dbname="${LOCAL_DB_SCHEMA}_test"

  execute $*
  ;;
remote)
  if [ -z "${REMOTE_DB_USER}" -o -z "${REMOTE_DB_PASSWORD}" -o -z "${REMOTE_DB_SCHEMA}" -o -z "${REMOTE_DB_HOST}" ]; then
    echo "Please specify all the following in config file: REMOTE_DB_USER, REMOTE_DB_PASSWORD, REMOTE_DB_SCHEMA, REMOTE_DB_HOST"
    exit 1
  fi

  dbname="$REMOTE_DB_SCHEMA"
  dbuser="$REMOTE_DB_USER"
  dbpassword="$REMOTE_DB_PASSWORD"
  dbhost="$REMOTE_DB_HOST"

  execute $*
  ;;
*)
  echo "Environment name '$env' is not supported"
  exit 1
  ;;
esac
