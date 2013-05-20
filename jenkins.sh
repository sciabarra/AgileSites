#!/bin/bash
# jenkins build script
CS=http://localhost:8080/cs
TEST=Demo/DmTester
umask 0
cp jenkins.build.sbt build.sbt
echo Hello
curl "$CS/HelloCS"
echo Setup
sh agilesites.sh "wcs-setup-online silent"
echo Importing
sh agilesites.sh "wcs-dt import"
echo Deploying
sh agilesites.sh "app/clean"
sh agilesites.sh "api/clean"
sh agilesites.sh "wcs-deploy"
echo Testing
curl -o ${WORKSPACE:-.}/report.xml "$CS/ContentServer?xml=1&cs.contenttype=text/xml&pagename=$TEST"
