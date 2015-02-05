#!/bin/bash
BASE="$(dirname $0)"
cd "$BASE"
BASE="$(pwd)"
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
cp Sites/omii.ini home/ominstallinfo/omii.ini
java -cp ../bin/wcs.jar wcs.Replacer .. "$BASE" <context.xml >webapps/cs/META-INF/context.xml
java -cp ../bin/wcs.jar wcs.Unzip Sites/csdt.zip home
cd ..
java -cp bin/wcs.jar wcs.Configurator wcs
cd wcs
cd Sites
chmod +x csInstall.sh
./csInstall.sh -silent
