# How to install Sites within AgileSites 

(currently only works for hsqldb in windwos)

1. download agilesites (currently 1.8-akka branch)

2. download Sites installer from the oracle website. 

3. Unzip the main package, locate inside the  WCS_Sites*.zip file in the folder wcs within AgileSites.

(you must have AgileSite-*/wcs/Sites/csinstall.bat)

3. execute the wcs/setup.bat script. It will install sites.

4. when the installer stops, run the Sites/propeditor.bat and change the db to hsqldb. Details:

4.1 open futuretense.ini

4.2 on the options menu select hsqldb
 
4.3 save

5. now run the agilesites.cmd main command. Wait until it download and compiles everything

6. execute wcs-setup to install AgileSites

7. execute wcs-serve start

8. execute wcs-hello and wait for an answer

9. go back to the installer

10. type "wcs-dt import #Demo-11.8 !Demo" to import the demo site

11. optionally generate a site with "wcs-generate site"

12. complete it all with "wcs-deploy"

You are done! 
