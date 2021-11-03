#!/bin/bash

set -e

# config

source $(dirname $0)/config.conf

# check all properties are set

if [ -z "${LOCAL_DB_USER}" -o -z "${LOCAL_DB_PASSWORD}" -o -z "${LOCAL_DB_SCHEMA}" ]; then
  echo "Please insert parameters in config.conf file under the same directory"
  echo "File must contain: LOCAL_DB_USER, LOCAL_DB_PASSWORD, LOCAL_DB_SCHEMA"
  exit 1
fi

function do_mysql() {
  mysql -h $LOCAL_DB_HOST -uroot $*
}

function create_db() {
  db=$1
  changeLogFile=$2
  seed=$3

  echo "drop database if exists $db" | do_mysql
  echo "create database $db" | do_mysql
  echo "CREATED DATABASE '$db'"
  echo " granting permissions..."

  echo "grant all on $db.* to '$LOCAL_DB_USER'@'%' identified by '$LOCAL_DB_PASSWORD';" | do_mysql
  echo "grant all on $db.* to '$LOCAL_DB_USER'@'$LOCAL_DB_HOST' identified by '$LOCAL_DB_PASSWORD';" | do_mysql
  echo "PERMISSIONS GRANTED FOR USER '$LOCAL_DB_USER' AND DATABASE '$db' "

  java -jar liquibase/lib/liquibase-core-3.5.3.jar \
    --changeLogFile="$changeLogFile" \
    --username="$LOCAL_DB_USER" \
    --password="$LOCAL_DB_PASSWORD" \
    --url="jdbc:mysql://$LOCAL_DB_HOST:3306/$db?useSSL=false" \
    --classpath="liquibase/lib/mysql-connector-java-5.1.38.jar" \
    --driver="com.mysql.jdbc.Driver" \
    --logLevel="warning" \
    update

  if [ "$seed" != "" ]; then
    echo " loading seed data..."
    cat $seed | do_mysql $db
  fi
}

cd "$(dirname $0)/.."
if [ "$MYSQL_PWD" == "" ]; then
  read -s -p "mysql root password? (type return for no password) " MYSQL_PWD
  export MYSQL_PWD
  echo ""
fi

create_db $LOCAL_DB_SCHEMA liquibase/changeLog-master.xml liquibase/seed.sql
create_db ${LOCAL_DB_SCHEMA}_test liquibase/changeLog-master.xml liquibase/seed.sql
