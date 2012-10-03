# Setup

To install ScalaWCS you need to [[download|Download]] and [[configure|Configuration]] it first.

Assuming you have configured it, you are ready to run the setup, entering in the sbt shell.

You can enter in the sbt tool executing the ``sbt.sh`` or ``sbt.bat`` batch file.

If you see the prompt ``>`` then you can perform the following 3 steps

## Step 1: build the core

Enter in sbt and type: ``core/publish-local``

## Step 2: deploy the core

Ensure WCS is  WCS **NOT RUNNING** before perform the following steps:

<pre>
wcs-setup
</pre>

## Step 3: import the demo site (optional, if you need it)

Start now WCS and execute:

<pre>
wcs-csdt import
</pre>

to import the demo site.

## Step 4: deploy the static content (optional, if you need it)

You can copy your static resources in the webapp folder with the command

<pre>
wcs-copy-static
</pre>


## Upgrading the app (both for development and deploy)

You need to perform the previous steps only once.

If you work only in your application, to deploy it you only need to run from sbt:

<pre>
wcs-deploy
</pre>

Application is then hot reloaded without any need to restart WCS

During development, assuming you have deployed it on a local jump start, you can do it continuosly with the command

<pre>
~ wcs-deploy
</pre>

Every change to your code will trigger the deploy of the jar, that will picked up immediately without any need to restart the application server.

**Note:*** currently there is NOT support for intelligent invalidation of the cache when code is changed
This support will be added soon.

For now if you update you application, you need to clean the cache to avoid inconsistencies 
( the code is changed in the Jar but this change is not currently detected by the system - it will be in a future release)