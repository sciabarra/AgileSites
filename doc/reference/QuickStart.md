This document is a quick start guide to install Sites within AgileSites shell.

- AgileSites 
- Sites within AgileSites
- the AgileSites Demo site
- a new empty site MySite for AgileSites development

1. Download the WebCenterSites distribution from the Oracle website, either 11.8 (11.1.1.8.0) or 11.6 (11.1.1.6.1)

```
http://www.oracle.com/technetwork/middleware/webcenter/sites/downloads/index.html
```

2. Unzip the distribution package, then inside locate the `WCS_Sites*.zip` file

3. Unzip the content of the `WCS_Sites*.zip` file inside the `wcs` folder. 
After the unarchiving there should be 3 new folders:

- `Sites`
- `SitesExplorer`
- `misc`

4. Launch the `wcs\setup.cmd` (on Windows) or (`wcs/setup.sh`) script on Linux/OSX. 
Answer "MySite" when you asked for you new site.

5. When the installer stops  asking to restart your application server, open a new terminal folder and launch the `agilesites.cmd` (on Windows) or the `agilesites.sh` (on Linux/OSX).

6. wait until everything is downloaded and compiled, then type:

- `wcs-setup`
- `wcs-serve start`

Wait for the message showing the welcome of webcenter sites. 
As a test, press enter. If you get the `>` prompt then you are ready.

7. Come back to the installer and press enter. Wait for the installation to complete.

8. When you see the "Installation Successfull" message, test it accessing in 

```
http://localhost:8181/cs
```

with username `fwadmin` and password `xceladmin`.

9. Import the `Demo` site and the empty `MySite`

On 11.6:
- `wcs-dt import #Demo-11.6 !Demo @ALL`
- `wcs-dt import #MySite-11.6 !MySite @ALL`

On 11.8:

- `wcs-dt import #Demo-11.8 !Demo @ALL`
- `wcs-dt import #MySite-11.8 !MySite @ALL`


10. Generate a new site and deploy it all:

- `wcs-generate` (accept the default in the popup - MySite)
- `wcs-deploy`


You should be able to see the Demo and an empty MySite with

- `http://localhost:8181/cs/Satellite/demo`
- `http://localhost:8181/cs/Satellite/mysite`


You can control now the start of sites from the agilesites shell with:

- `wcs-serve status` to check execution status
- `wcs-serve stop` to stop a running tomcat
- `wcs-serve start` to start a running tomcat

**Note**: you can quickly shutdown a running tomcat if you exited the shell with http://localhost:8182

