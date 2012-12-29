# Simplified development procedure

After the installation is complete, start the agilewcs shell and type

<pre>
 ~ wcs-deploy
</pre>

Then you can open eclipse and start editing code

The tool will continuosly monitor changes and deploy all the file.


# Optimized development procedure

Using the wcs-deploy procedure in continuos mode is safe, however it can be sometimes slow if you have a lot of resource files.

The command wcs-deploy actually has  3 steps that can be invoked individually:

- wcs-package-jar
- wcs-copy-static
- wcs-update-model

# Code Changes

If you are only changing the code (not changing resources or static files), you can instead simply type

<pre>
~ wcs-package-jar
</pre>

This will continuosly build and deploy the code jar only.

# Static files change

If you are only editing static files, you can use instead

<pre>
~ wcs-copy-static
</pre>

This will continuosly copy changed static files.

# Updating the model

Finally, if you change the setup files adding new template and cselements, you can update them calling

<pre>
wcs-update-model
</pre> 

# Exporting Assets with csdt

The shell integrates CSDT and provides a simplified to it.

Here there are the relevant commands:

<pre>
wcs-dt
</pre>

This will list the assets in the selected sites (
