#!/bin/bash
JAR="$JAVA_HOME"/bin/jar
if ! test -f $JAR
then echo "Please set JAVA_HOME enviroment variable to a JDK (not JRE) java home"
     exit
fi
if test -f ofm_sites_generic_11.1.1.8.0_disk1_1of1.zip
then
   $JAR xvf ofm_sites_generic_11.1.1.8.0_disk1_1of1.zip WebCenterSites_11.1.1.8.0/WCS_Sites/WCS_Sites.zip
   cd wcs
   $JAR xvf ../WebCenterSites_11.1.1.8.0/WCS_Sites/WCS_Sites.zip
   ./setup.sh
elif test -f ofm_sites_generic_11.1.1.6_bp1_disk1_1of1.zip 
then
   $JAR xvf ofm_sites_generic_11.1.1.6_bp1_disk1_1of1.zip WebCenterSites_11.1.1.6_bp1/WCS_Sites_11.1.1.6_bp1/WCS_Sites_11.1.1.6_bp1.zip
   cd wcs
   $JAR xvf ../WebCenterSites_11.1.1.6_bp1/WCS_Sites_11.1.1.6_bp1/WCS_Sites_11.1.1.6_bp1.zip
   ./setup.sh
else 
     echo Please download WebCenter Sites 11.1.1.6.1 or 11.1.1.8.0 and place in this folder.
     echo Please go to http://www.oracle.com/technetwork/middleware/webcenter/sites/downloads/index.html
     exit
fi

