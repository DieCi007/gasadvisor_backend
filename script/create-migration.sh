#!/bin/bash

set -e

cd "$(dirname $0)/../liquibase"
master_file="changeLog-master.xml"
changes_dir="changeSets"

# if number of params is not 1
if [ $# -ne 1 ]; then
  echo "Usage: $0 name-of-the-migration"
  exit 1
fi

migration_name=$1
migration_id="$(date +%Y%m%d%H%M%S)-$migration_name"
migration_file="$migration_id.xml"
migration_path="$changes_dir/$migration_file"
temp_file=/tmp/create-migration.$$

closing_tag="<\/databaseChangeLog>"
changelog_tag=" <include file='$migration_path' relativeToChangelogFile='true'/>"

# add migration name to changelog
awk "
/$closing_tag/ { print \"$changelog_tag\" }
               { print \$0 }
" $master_file >$temp_file

mv $temp_file $master_file

# add migration file to /changeSets
cat >"$migration_path" <<EOF
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext" xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd">
  <changeSet author="$USER" id="$migration_id">
    <sql>
      MIGRATION CODE GOES HERE
    </sql>
    <rollback>
      ROLLBACK CODE GOES HERE
    </rollback>
  </changeSet>
</databaseChangeLog>
EOF
