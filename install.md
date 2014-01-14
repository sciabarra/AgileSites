---
layout: page
title: Install
---
## Download 

AgileSites is distributed as a source zip from GitHub.

You need to download a stable release of AgileSites from GitHub. Stable releases are [listed here](http://www.agilesites.org/download.html)

It is recommended you pick the latest stable version, 
unzip it in a folder of your choice where you put your projects. Then read on to learn how to configure and install it.

## Configuration

Configuration is now fully automated.

Once downloaded and extracted you just need to execute the `agilesites.cmd` or `agilesites.sh` script.

A GUI will popup asking for the main installation directory. It will detect automatically the configuration.

Sometimes, since CSDT is not always included in the installation, you may need to specify manually where the csdt jar is located (in those cases when it is not found automatically).

The configuration will then ask for the password of the user `fwadmin`. The default value is  `xceladmin` unless you changed it in your installation. 

Last but not least, the configurator will ask for  the name of the site you are going to use for development (in the tutorial samples it is called `MySite`).

It is strongly advised you specify it because the setup will then configure the Satellite for accepting the selected site with the url assembler.

### Editing the `build.sbt`

The configurator reads the file `build.sbt.dist` and creates a `build.sbt`, takig the values from the Sites configuration files. The resulting build.sbt is good for working immediately with AgileSites.

However if you decide to add a new Sites site to be used with the framework, or the site you want to use is different from the one you said in configuration, then you have to edit the `build.sbt`, changing the `wcsSites` setting value.


**NOTE** If you change the value of `wcsSites` then you have to repeat the `wcs-setup` procedure. 

Additional manual configurations is needed for Satellite and Apache since their configuration cannot be detected manually. You can read about Satellite and Apache front end installation in the [deployment reference](http://www.agilesites.org/reference/Deployment.html).

You can read details of the configuration in the [configuration reference](http://www.agilesites.org/reference/Configuration.html) to understand each parameter if in doubt.

## Installation 

After the configuration you can proceed with the installation. Ensure you have a connection to the internet, then  start the AgileSites shell running either `agilesites.bat` if using Windows, or `agilesites.sh` if using Linux/OSX.

The first time the shell is started, it will take some time (a few minutes) before you get a prompt, since it will have to download a number of libraries from internet and build the core library. 

**HINT** If you are performing an installation using an existing code base and you experience weird behaviours, you can force the repositories to a safe state before running the `clean.bat` or `clean.sh` script  before starting the shell.  After the clean the build will take some time like the first time because everything will have to be downloaded again.

The installation requires 3 simple steps

- offline setup (command `wcs-setup`)
- optionally import a demo site 
- online deploy (command `wcs-deploy`)

## Setup (offline)

Once you get the prompt `>`, stop the application server with Sites and execute:

`wcs-setup`

The setup command checks that Sites is not running and will stop if it finds a running Sites.

After the setup you must start the application server and wait until it is ready.

Since it can takes some time before the application server is active, it is useful to wait until you get an answer, invoking the command `wcs-hello`. 

This command will return when Sites is up and running, and print the version of Java that it is using.

This command also checks if AgileSites is using the same Java version as Sites (because using different versions can cause problems).

##  Importing and deploy the Demo site  (Optional)

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

![Successful import](/img/snap1188.png)

Once you have installed the core library and eventually imported the demo site, you can complete the installation running

```
wcs-deploy
```

If you have imported the demo site, you can check if it is now up and running with (note the port can be different):

> http://localhost:9080/cs/Satellite/demo

Change the port according your actual installed host, port and path.

Run tests accessing to  

> http://localhost:8080/cs/ContentServer?pagename=Demo_Tester

then click on "Run All Tests" and expect 0 tests failed.

This will complete the installation of the framework.

### What is next

You can now follow the steps in the [tutorial](http://www.agilesites.org/tutorial.html) to create a new site.

The installation for development is complete but for live deployment you need to configure also Satellite Server and Apache.

You can learn how to configure a Satellite and Apache front end installation in the [deployment reference](http://www.agilesites.org/reference/Deployment.html).

