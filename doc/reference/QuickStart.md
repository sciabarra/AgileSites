This page is a quick start guide to install Sites, AgileSites, the Demo site and a starting site to work with.

- (1) Download the WebCenterSites distribution from the Oracle website, either 11.8 (11.1.1.8.0) or 11.6 (11.1.1.6.1)

```
http://www.oracle.com/technetwork/middleware/webcenter/sites/downloads/index.html
```

- (2) Unzip the distribution package, then locate the `WCS_Sites*.zip` archive file (it is usually in a subdirectory).

- (3) Unzip the content of the `WCS_Sites*.zip` file inside the `wcs` folder. After unarchiving there should be 3 new folders in your distribution:

     - `wcs/Sites`
     - `wcs/SitesExplorer`
     - `wcs/misc`

- (4) Launch the `wcs\setup.cmd` batch (on Windows) or `wcs/setup.sh` shell script (on Linux/OSX). Answer "MySite" when you asked for you new site. Now the installer will start and work for a while.

- (5) When the installer stops asking to restart your application server, open a new terminal folder and launch the `agilesites.cmd` (on Windows) or the `agilesites.sh` (on Linux/OSX). Then the system will start downloading jars from internet and compiling source. Wait patiently.

- (6) Once you get the prompt, type:

    - `wcs-setup`
    - `wcs-serve start`

- Now WebCenter Sites will start.  Wait for the message showing the welcome of webcenter sites.  You will see a number of messages. Those messages overwrite the prompt so you may not be sure when the startup is complete.

- As a test, press enter. If you get the `>` prompt then you are ready.
Note installation is not yet complete.

- (7) Come back to the installer and press enter. Wait for the installation to complete.

- (8) When you see the "Installation Successfull" message, test it accessing with username `fwadmin` and password `xceladmin` in

```
http://localhost:8181/cs
```

- (9) Import the `Demo` site and the empty `MySite`. On 11.6:

    - `wcs-dt import #Demo-11.6 !Demo @ALL`
    - `wcs-dt import #MySite-11.6 !MySite @ALL`

- On 11.8:

    - `wcs-dt import #Demo-11.8 !Demo @ALL`
    - `wcs-dt import #MySite-11.8 !MySite @ALL`


- (10) Generate a new site and deploy it all:

    - `wcs-generate` (accept the default in the popup - MySite)
    - `wcs-deploy`

- You should be able to see the Demo and an empty MySite with

    - `http://localhost:8181/cs/Satellite/demo`
    - `http://localhost:8181/cs/Satellite/mysite`

- You can control now the start of sites from the agilesites shell with:

    - `wcs-serve status` to check execution status
    - `wcs-serve stop` to stop a running tomcat
    - `wcs-serve start` to start a running tomcat

**Note**: you can quickly shutdown a running tomcat if you exited the shell with http://localhost:8182
