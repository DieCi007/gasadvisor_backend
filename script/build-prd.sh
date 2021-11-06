#!/bin/bash

set -e

function usage() {
  echo "Usage: $0 [-u <db_username>] [-p <db_password>]"
  exit 1
}

while getopts ":u:p:" o; do
  case "${o}" in
  u)
    dbuser=${OPTARG}
    ;;
  p)
    dbpassword=${OPTARG}
    ;;
  *)
    usage
    ;;
  esac
done
shift $((OPTIND - 1))

if [ -z $dbuser -o -z $dbpassword ]; then
  usage
fi

source $(dirname $0)/config.conf

dbhost="$REMOTE_DB_HOST"
dbname="$REMOTE_DB_SCHEMA"

if [ -z "${dbhost}" -o -z "${dbname}" ]; then
  echo "Please specify 'REMOTE_DB_HOST' and 'REMOTE_DB_SCHEMA' in config.conf file under same directory"
  exit 1
fi

cd $(dirname $0)/..

echo building with user $dbuser
datasource="jdbc:mysql://$dbhost:3306/$dbname?serverTimezone=UTC\\&useSSL=false"
echo building with datasource $datasource

resourcesPath=./src/main/resources
defaultPropertiesPath="${resourcesPath}/application.properties"
cp $defaultPropertiesPath "${defaultPropertiesPath}.ignore"

# add new properties to config
awk '/^spring.profiles.active=/{sub(".*","spring.profiles.active=prd", $0)}1' $defaultPropertiesPath >>"${defaultPropertiesPath}.temp" &&
  mv "${defaultPropertiesPath}.temp" $defaultPropertiesPath

awk -v replace="spring.datasource.username=${dbuser}" '/^spring.datasource.username=/{sub(".*",replace, $0)}1' $defaultPropertiesPath >>"${defaultPropertiesPath}.temp" &&
  mv "${defaultPropertiesPath}.temp" $defaultPropertiesPath

awk -v replace="spring.datasource.password=${dbpassword}" '/^spring.datasource.password=/{sub(".*",replace, $0)}1' $defaultPropertiesPath >>"${defaultPropertiesPath}.temp" &&
  mv "${defaultPropertiesPath}.temp" $defaultPropertiesPath

sed -i -e "s|spring.datasource.url.*|spring.datasource.url=$datasource|g" $defaultPropertiesPath

logbackPath="${resourcesPath}/logback.xml"
mv $logbackPath "${logbackPath}.ignore"

# build application
./gradlew clean build -x test --stacktrace --console plain

#mv $defaultPropertiesPath "${defaultPropertiesPath}.test"
rm $defaultPropertiesPath
rm "${defaultPropertiesPath}-e"
mv "${logbackPath}.ignore" $logbackPath
mv "${defaultPropertiesPath}.ignore" $defaultPropertiesPath
