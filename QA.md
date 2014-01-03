# AgileSites 1.8 test procedure
# Known issues

- You may need to impor the demo site twice - looks to be an issue in CSDT - investigating
- You need to remove existing jars from Shared/agilesites/ if you have installed it before
- confusing error message if you are using the wrong version of java 

# Procedure

Install JSK 11.1.1.8 (it should work on 11.1.1.6 too). 
You need Java 1.7 also.

Get the latest bug fix release:

```
git clone https://github.com/sciabarra/AgileSites.git
cd AgileSites 
git checkout remotes/origin/1.8
```

Launch agilesites.bat or agilesites.sh A configurator will popup.

Just say where your JSK is and click on configure button.

When asked if you want to create a site, answer `MySite`

Then it will download all the jars and compile.

Shut down JSK and install it with the command:

```
wcs-setup
```

(it will warn you if JSK is running)

Now start JSK. Test if you can connect with the command

```
wcs-hello
```

Wait until you get an answer.

# Generate a site with the scaffolds
 
Run the following commands, accept all the defaults:

```
wcs-dt import #MySite-11.8 @ALL
wcs-generate site
wcs-generate template
wcs-generate layout
wcs-generate cselement
wcs-deploy
```

Then you should be able to see the site and run all the tests successfully

```
http://localhost:9080/cs/Satellite/mysite
http://localhost:9080/cs/ContentServer?pagename=MySite_Tester
```

# Import the demo

```
wcs-dt import #Demo-11.8 @ALL
```

Test if you can see the demo site with `http://localhost:9080/cs/Satellite/demo`

**NOTE** If you get a Page not found error or a blank page, import it again

(NOTE: sometimes CSDT has a glitch and you may need to import the demo twice).

You can run all the tests successfully with

`http://localhost:9080/cs/ContentServer?pagename=Demo_Tester`


Please report any failure in the procedure.
