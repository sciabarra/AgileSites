# How to install AgileSites

## Prerequisites

Install Sites or the JSK, version 11gR1 

## Download 

Download a stable release of AgileSites (0.5 currently)

Unzip it somewhere.

## Configure

Copy build.sbt.dist to build.sbt and edit it

To the mininimum you need to provide paths where your installation is.

If you want the Demo site up and running, leave the site Demo in the wcsSites, otherwise remove it.

## Install 

Start the agileshell running either agilesites.bat or agilesites.sh

Ensure sites is not running and run

`wcs-setup-offline``

If you get exceptions, review the paths carefully and repeat

Now start Sites/JSK amd run

``wcs-setup-online``

## Import

First import the site with CSDT with

``wcs-dt import``

Then deploy the code with

``wcs-deploy``

Check the site is up and running with http://localhost:8080/cs/Satellite/demo

Run tests with http://localhost:8080/cs/ContentServer?pagename=Demo/DmTester
