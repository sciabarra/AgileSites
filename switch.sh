#!/bin/bash
if test -z "$1" -o ! -f "$1.build"
then 
	echo "usage: \"switch <config>\""
	echo "where config must be a valid build.sbt file"
	echo "named \"<config>.build\""
else 
	cp $1.build build.sbt
	echo "shellPrompt in ThisBuild := { x => \"$1> \" }">>build.sbt
	shift
	sh agilesites.sh "$*"
fi