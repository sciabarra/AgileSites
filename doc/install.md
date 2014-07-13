## Download 

AgileSites is distributed as a source zip from GitHub.

You need to download a stable release of AgileSites from GitHub. 
Stable releases are [listed here](http://www.agilesites.org/download.html)

It is recommended you pick the latest stable version,  unzip it in a folder of your choice where you put your projects. 

Then read on to learn how to configure and install it.

AgileSites can be used with an existing instance of Sites or with Jump Start Kit and must be configured to use it.

AgileSites can also install a *local instance* of the **full WebCenter Sites** for you and let you manage it from its integrated shell.

## Installing Sites in the AgileSites shell

If you DO NOT HAVE JumpStart Kit and you wanto to install Sites together with AgileSites read this section, otherwise skip this and go to the next section.

AgileSites now features an embedded installer to easily install Sites within AgileSites.

The installer is meant for development *ONLY*, not for production, so:

- the port and the host used is fixed (localhost:8181)
- it uses only HSQLDB
- you can easily shutdown the server with just http://localhost:8182  (not desiderable for production)
- passwords are default (fwadmin/xceladmin)

### Installation steps

- (1) Download the WebCenterSites distribution from the Oracle website, either 11.8 (11.1.1.8.0) or 11.6 (11.1.1.6.1)

```
http://www.oracle.com/technetwork/middleware/webcenter/sites/downloads/index.html
```

- (2) Unzip the distribution package, then locate inside the `WCS_Sites*.zip` file, Then unzip again the content of the `WCS_Sites*.zip` archive in the `wcs` subfolder of the folder where you have AgileSites. Before unarchiving you should see in the folder the files `setup.cmd`, `setup.sh`, the folder home and other folders. Do not change or remove anything, just add Sites installation files.

- After unarchiving there should be 3 *additional* folders. Check there are the following folders:

    - `Sites`
    - `SitesExplorer`
    - `misc`

- (3) Launch the script `wcs\setup.cmd` on Windows or `wcs/setup.sh` on Linux/OSX. 

It will ask you if you want to create a site for work.

It is recommended you answer "MySite" when you asked for you new site (or the name of another site you may want to create for your own development purposes). 

- (4) When the installer stops, asking to deploy the app and restart your application server, open a new terminal folder and launch the `agilesites.cmd` (on Windows) or the `agilesites.sh` (on Linux/OSX).

Then the shell will start downloading required jars from internet and compling code.

- Wait patiently until the download and the compilation is complete, then type:

    - `wcs-setup`
    - `wcs-serve start`

Then Sites will start. Wait until the startup is complete. 
As a test, to be sure Sites is started just press enter. 
If you get the ">" prompt then you are ready.

Note installation is not yet complete.

- (5) Come back to the installer windows and press enter. 
Then wait for the installation to complete

- When you see the "Installation Successfull" message, test it accessing in 

```
http://localhost:8181/cs
```

with username `fwadmin` and password `xceladmin`

If you can login, then you are done.

## Installing AgileSites in an existing instance of Sites

If you have JumpStart Kit and you want to use AgileSites with this JumpStart Kit installation (or with an existing instance of Sites) read this section.
If you instead already installed it following directions in the previous section, skip it.

Configuration with an existing instance is fully automated.

Once downloaded and extracted AgileSites, you just need to execute the `agilesites.cmd` or `agilesites.sh` script.

A GUI will popup asking for the main installation directory. It will detect automatically the configuration.

Sometimes, since CSDT is not always included in the installation (it is usually left zipped in a csdt.zip file), you may need to manually unzip the csdt.zip and specify manually where the csdt jar is located (in those cases when it is not found automatically).

The configuration will then ask for the password of the user `fwadmin`. The default value is  `xceladmin` unless you changed it in your installation. 

Last but not least, the configurator will ask for  the name of the site you are going to use for development (in the tutorial samples it is called `MySite`).

It is strongly advised you specify it because the setup will then configure the Satellite for accepting the selected site with the url assembler.



### Additional Configuration editing the `build.sbt`

The configurator reads the file `build.sbt.dist` and creates a `build.sbt`, 
taking the values from the Sites configuration files. 
The resulting build.sbt is good for working immediately with AgileSites.

However if you decide to add a new Sites site to be used with the framework, or the site you want to use is different from the one you said in configuration, then you have to edit the `build.sbt`, changing the `wcsSites` setting value.

**NOTE** If you change the value of `wcsSites` then you have to repeat the `wcs-setup` procedure. 

Additional manual configurations is needed for Satellite and Apache since their configuration cannot be detected manually. You can read about Satellite and Apache front end installation in the [deployment reference](http://www.agilesites.org/reference/Deployment.html).

You can read details of the configuration in the [configuration reference](http://www.agilesites.org/reference/Configuration.html) to understand each parameter if in doubt.

### Completing the installation

Ensure you have a connection to the internet, then  start the AgileSites shell running either `agilesites.bat` if using Windows, or `agilesites.sh` if using Linux/OSX.

The first time the shell is started, it will take some time (a few minutes) before you get a prompt, since it will have to download a number of libraries from internet and build the core library. 

**HINT** If you are performing an installation using an existing code base and you experience weird behaviours, you can force the repositories to a safe state before running the `clean.bat` or `clean.sh` script  before starting the shell.  After the clean the build will take some time like the first time because everything will have to be downloaded again.

When the propmpt '>' appear, type: `wcs-setup`


## Importing the Demo site (optional)

The following steps are valid both for AgileSites installed within an existing Sites, and Sites installed within AgileSites. The step is optional but it is strongly advised, since the Demo site is a handy code reference and it is very small.
 
After the setup you must start the application server and wait until it is ready.
If you have installed Sites within AgileSites you need the command `wcs-serve start`.
Otherwise start you Jump Start Kit.

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
