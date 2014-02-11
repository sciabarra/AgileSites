## Download 

AgileSites is distributed as a source zip from GitHub.

You need to download a stable release of AgileSites from GitHub. 
Stable releases are [listed here](http://www.agilesites.org/download.html)

It is recommended you pick the latest stable version,  unzip it in a folder of your choice where you put your projects. 

Then read on to learn how to configure and install it.

AgileSites can be used with an existing instance of Sites or with Jump Start Kit and must be configured to use it.

AgileSites can be also install Sites for you and let you manage it from its integrated shell.

## Configuring with an existing instance of Sites

If you want to use AgileSites with JumpStart Kit or an existing instance of Sites read this section.

If you instead want to install also Sites within AgileSites, go to the next section. 

Configuration with an existing instance is now fully automated.

Once downloaded and extracted AgileSites, you just need to execute the `agilesites.cmd` or `agilesites.sh` script.

A GUI will popup asking for the main installation directory. It will detect automatically the configuration.

Sometimes, since CSDT is not always included in the installation, you may need to specify manually where the csdt jar is located (in those cases when it is not found automatically).

The configuration will then ask for the password of the user `fwadmin`. The default value is  `xceladmin` unless you changed it in your installation. 

Last but not least, the configurator will ask for  the name of the site you are going to use for development (in the tutorial samples it is called `MySite`).

It is strongly advised you specify it because the setup will then configure the Satellite for accepting the selected site with the url assembler.

## Installing Sites within AgileSites

AgileSites now features an embedded installer to easily install Sites within AgileSites.

The installer is meant for development *ONLY*, not for production, so:

- the ports are fixed (8181)
- it uses only HSQLDB
- you can easily shutdown the server with http://localhost:8182  (not desiderable for production)
- passwords are default (fwadmin/xceladmin)

Installation steps:

1. Download the WebCenterSites distribution from the Oracle website, either 11.8 (11.1.1.8.0) or 11.6 (11.1.1.6.1)

```
http://www.oracle.com/technetwork/middleware/webcenter/sites/downloads/index.html
```

2. Unzip the distribution package, then locate inside the `WCS_Sites*.zip` file

3. Unzip the content of the `WCS_Sites*.zip` archive in the `wcs` subfolder of the folder where you unarchived AgileSites.

Before unarchiving you should see in the folder the files `setup.cmd`, `setup.sh`, the folder home and others.
Do not change or remove anything, just a
After unarchiving there should be 3 *additional* folders. Check there are the following folders:

- `Sites`
- `SitesExplorer
- `misc`


4. Launch the script `wcs\setup.cmd` on Windows or `wcs/setup.sh` on Linux/OSX. 

It is recommended you answer "MySite" when you asked for you new site (or the name of another site you may want to create for your own development purposes).

5. when the installer stops  asking to deploy the app and restart your application server, open a new terminal folder and launch the `agilesites.cmd` (on Windows) or the `agilesites.sh` (on Linux/OSX).

6. wait the download and compile complete, then type:

- `wcs-serve start`

Wait for the message showing the welcome of webcenter sites. 
As a test, press enter. If you get the ">" prompt then you are ready

7. Come back to the installer windows and press enter. Then wait for the installation to complete

8. When you see the "Installation Successfull" message, test it accessing in 


```
http://localhost:8181/cs
```

with username `fwadmin` and password `xceladmin`

### Additional Configuration editing the `build.sbt`

The configurator reads the file `build.sbt.dist` and creates a `build.sbt`, 
taking the values from the Sites configuration files. 
The resulting build.sbt is good for working immediately with AgileSites.

However if you decide to add a new Sites site to be used with the framework, or the site you want to use is different from the one you said in configuration, then you have to edit the `build.sbt`, changing the `wcsSites` setting value.

**NOTE** If you change the value of `wcsSites` then you have to repeat the `wcs-setup` procedure. 

Additional manual configurations is needed for Satellite and Apache since their configuration cannot be detected manually. You can read about Satellite and Apache front end installation in the [deployment reference](http://www.agilesites.org/reference/Deployment.html).

You can read details of the configuration in the [configuration reference](http://www.agilesites.org/reference/Configuration.html) to understand each parameter if in doubt.

## Installing AgileSites and the Demo Sites 

The following steps are valid both for an existing instance and for Sites installed within AgileSites
 
Ensure you have a connection to the internet, then  start the AgileSites shell running either `agilesites.bat` if using Windows, or `agilesites.sh` if using Linux/OSX.

The first time the shell is started, it will take some time (a few minutes) before you get a prompt, since it will have to download a number of libraries from internet and build the core library. 

**HINT** If you are performing an installation using an existing code base and you experience weird behaviours, you can force the repositories to a safe state before running the `clean.bat` or `clean.sh` script  before starting the shell.  After the clean the build will take some time like the first time because everything will have to be downloaded again.

The installation requires 3 simple steps

- offline setup (command `wcs-setup`)
- optionally import a demo site 
- online deploy (command `wcs-deploy`)

### Setup (offline)

Once you get the prompt `>`, stop the application server with Sites.

If you have installed Sites within AgileSites, you can do it with the command `wcs-serve stop` 

Now execute (note that if you already did it in the insteallation you do not need to repeat this step):

`wcs-setup`

The setup command checks that Sites is not running and will stop if it finds a running Sites.

After the setup you must start the application server and wait until it is ready.

If you have installed Sites within AgileSites you need the command `wcs-serve start`.

If you are using an external application server, since it can takes some time before the application server is active, it is useful to wait until you get an answer, invoking the command `wcs-hello`. 

This command will return when Sites is up and running, and print the version of Java that it is using.

This command also checks if AgileSites is using the same Java version as Sites (because using different versions can cause problems).

Note that the integrated shell already does the check and surely uses the same java version.

###  Importing and deploy the Demo site  (Optional)

If you are going to do some development, you may want to import the Demo site in the CMS. You can skip step but if do it, there will be nothing visible of the framework inside the CMS, so you will have to create your site as described in the [tutorial](http://www.agilesites.org/tutorial.html). 

The import of a site is performed using the CSDT development tool integrated in the AgileSites shell.

If you are using WebCenter Sites 11.1.1.8.x to import the demo site all you need to run is the command:

```
wcs-dt import #Demo-11.8 !Demo @ALL
```

If instead you are using WebCenter Sites 11.1.1.6.x to import the demo site all you need to run is the command:

```
wcs-dt import #Demo-11.6 !Demo @ALL
```

Once you have installed the core library and eventually imported the demo site, you can complete the installation running

```
wcs-deploy
```

If you have imported the demo site, you can check if it is now up and running with (note the port can be different):

> http://localhost:9080/cs/Satellite/demo

Change the port according your actual installed host, port and path.

![Demo Site](../img/snap0469.png)

Run tests accessing to  

> http://localhost:8080/cs/ContentServer?pagename=Demo_Tester

then click on "Run All Tests" and expect 0 tests failed.

This will complete the installation of the framework.

### What is next

You can now follow the steps in the [tutorial](http://www.agilesites.org/tutorial.html) to create a new site.

The installation for development is complete but for live deployment you need to configure also Satellite Server and Apache.

You can learn how to configure a Satellite and Apache front end installation in the [deployment reference](http://www.agilesites.org/reference/Deployment.html).
