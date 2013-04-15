# How to install AgileSites

(work in progress - still an incomplete page)

## Install Sites JSK or a local install

## Download a stable release of AgileSites

Download.

Unzip somewhere.

## Configure

copy build.sbt.dist to build.sbt and edit it

To the mininimum you need to provide paths where your installation is.

If you want the Demo site up and running, leave the site Demo in the wcsSites, otherwise remove it.

## AgileShell

Start agileshell

## Offline Install

Shutdown Sites.

``core/clean
core/publish-local
wcs-setup-offline``

## Online Install

Start Sites

``wcs-setup-online``

## Optionally, import the Demo site

if you want also the demo site, ensure it is in the wcsSites property in the build.sbt

Then import the site and deploy the code this way

``wcs-import 
wcs-deploy
``

Check the site is up and running with http://localhost:8080/cs/Satellite/demo

## What's next

Follow the [Tutorial](Tutorial.md) for building your site
