# Installation

## Prerequisites and Configuration

1. I assume here you downloaded a stable release

2. Install WCS 11gR1 (or JumpStartKit 11gR1)

3. Copy build.sbt.dist in build.sbt, and edit it (you have to specify a number of paths)

## Offline setup

4. Ensure WCS is NOT running

5. Start the agilewcs.sh command line shell (run either agilewcs.sh or agilewcs.bat)

6. Type in the shell: 

<pre>
core/publish-local (it can take some time)
wcs-setup
</pre>

## Online setup

8. Start WCS, and ensure it is up and running.

9. Import the sample sites with the following commands:

<pre>
wcs-dt import @SITE
wcs-dt import @ASSET_TYPE
wcs-dt import @ALL_ASSETS
wcs-dt import @ALL_NONASSETS
</pre>

10. Import configuration and entry point with the following command

<pre>
wcs-cm import_all
</pre>

11. Finally deploy the code.

<pre>
wcs-deploy
</pre>

12. Build eclipse configuration files with 

<pre>
eclipse
</pre>

and then import the projects in eclipse (core, app and api).

You are done. Follow directions in DEVELOPMENT.md

