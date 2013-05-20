---
layout: page
title: Install
---
## Download 

Download a stable release of AgileSites from GitHub. 

Stable releases are [listed here](http://www.agilesites.org/download.html)

Unzip them somewhere and read on to configure it.

## Configuration

Copy `build.sbt.dist` to `build.sbt` and edit it.

You need to provide details on your installation.

At least you need to configure paths, so the AgileSites installer can locate your Sites files.

You also need to decide for a front end web server, usually running Apache and a Satellite server in front of your sites and behind you web server. 

For development those components are optional, so you just need to ensure the ``wcsUrl`` is pointing to your local development environment.

See the [configuration reference](http://www.agilesites.org/reference/Configuration.html) for details.

## Installation 

Start the AgileShell running either `agilesites.bat` or `agilesites.sh`

Ensure sites is ***not running*** and execute

`wcs-setup-offline``

If you get exceptions, review the paths carefully and repeat.

Now start Sites/JSK and run

``wcs-setup-online``

This will complete the installation of the framework.

You can now create a new site following the [tutorial](http://www.agilesites.org/tuttorial.html) or go on this document to learn how to import the demo site.

**NOTE**: You can use `wcs-setup-online silent` and `wcs-setup-online silent` to skip informative GUI messages (if you are installing in a server without a GUI)

## Import

To import a site, you need to import the content model and then deploy the site.

The site imported is the one pointed by the `wcsSites` variable in the 

First import the site using the CSDT with

``wcs-dt import``

Then deploy the code with

``wcs-deploy``

Check the site is up and running with:

> http://localhost:8080/cs/Satellite/demo

Run tests with 

> http://localhost:8080/cs/ContentServer?pagename=Demo/DmTester


## Satellite

TODO
