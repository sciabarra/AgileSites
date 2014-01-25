# How to install Sites within AgileSites 

Note: it is work-in-progress and currently only works for hsqldb in windows. Tested only for 11.1.1.6.1

1. download agilesites (currently 1.8-akka branch)

2. download Sites installer from the oracle website. 

3. Unzip the main package, locate inside the  WCS_Sites*.zip file in the folder wcs within AgileSites. If done correctlym you should find an `AgileSite-*/wcs/Sites/csinstall.bat` file

3. execute the `wcs/setup.bat` script. It will install ask for a custom Site, this step is optional, you can do it later.

4. when the installer stops, run the Sites/propeditor.bat and change the db to hsqldb. Details: open futuretense.ini, on the options menu select hsqldb, save and exit

5. now run the agilesites.cmd main command. Wait until it downloads and compiles everything.

6. execute `wcs-setup` to install AgileSites

7. execute `wcs-serve start`

8. execute `wcs-hello` and wait for an answer

9. go back to the installer and complete the installation, untio you get a message "Installation Successful"

10. type `wcs-dt import #Demo-11.8 !Demo` to import the demo site

11. optionally generate a site with `wcs-generate site`, and then create that site in Sites.

12. complete it all with `wcs-deploy`

You are done! 
