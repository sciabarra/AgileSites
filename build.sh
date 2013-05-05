#!/bin/bash
cp build.sbt.ci build.sbt
echo Hello
curl "http://localhost:8080/cs/HelloCS"
echo Setup
sh agilesites.sh "wcs-setup-online silent"
echo Importing
sh agilesites.sh "wcs-dt import"
echo Deploying
sh agilesites.sh "app/clean"
sh agilesites.sh "api/clean"
sh agilesites.sh "wcs-deploy"
echo Testing
curl -o report.xml "http://localhost:8080/cs/ContentServer?xml=1&cs.contenttype=text/xml&pagename=Demo/DmTester"
