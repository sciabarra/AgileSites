#!/bin/bash
BASE="$(dirname $0)"
cd "$BASE"
BASE="$(pwd)"
#11.6.0 compat
if test -e ContentServer/csInstall.sh
then mv ContentServer Sites
fi
if ! test -e  Sites/csInstall.sh 
then 
	echo Sites installer not found.
	echo Please download it from the Oracle website, 
	echo then unzip the WCS_Sites\*.zip in the wcs folder.
	echo It is required the Sites installer in the wcs/Sites folder
fi
if test -n "$JAVA_HOME"
then export PATH="$JAVA_HOME/bin:$PATH"
fi
export PATH="$BASE/wcs/home/bin:$PATH"
if ! java -version
then echo java not found. Please install JDK and set JAVA_HOME ; exit
fi
rm home/*.done
touch home/hsql.flag
touch home/setup.flag
java -cp ../bin/wcs.jar wcs.Silent "$BASE" misc/silentinstaller/generic_omii.ini Sites/install.ini Sites/omii.ini
java -cp ../bin/wcs.jar wcs.Replacer .. "$BASE" <context.xml >webapps/cs/META-INF/context.xml
java -cp ../bin/wcs.jar wcs.Unzip Sites/csdt.zip home
cd ..
if ! test -e build.sbt
then java -cp bin/wcs.jar wcs.Configurator wcs $1
fi
cd wcs
cd Sites
echo "" >log.out
chmod +x csInstall.sh
(java -cp ../../bin/wcs.jar wcs.PressEnterSock | ./csInstall.sh -silent >log.out)&
cd ../..
java -cp bin/wcs.jar wcs.WaitUntil wcs/Sites/log.out ENTER.
./agilesites.sh "wcs-serve stop" "wcs-setup" "wcs-serve start"
java -cp bin/wcs.jar wcs.PressEnterSock now
java -cp bin/wcs.jar wcs.WaitUntil wcs/Sites/log.out "Installation Finished Successfully"
