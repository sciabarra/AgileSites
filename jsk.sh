#!/bin/sh
# change this to jsk home
export JSK_HOME=/Users/msciab/Work/product/ScalaWCS
# change this to change parameters
#export CATALINA_OPTS="-Dwcs.core.debug -Dwcs.tag.debug"
# end changes
if ! test -d $JSK_HOME/App_Server ; then 
	echo Please install JumpStartKit and edit this script JSK_HOME
	exit
fi
cd $JSK_HOME/App_Server/apache-tomcat-*/bin/
sh catalina.sh.org run
cd -
