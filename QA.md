# How to test AgileSites 1.8

Install JSK 11.1.1.8 (it should work on 11.1.1.6 too). 
You need Java 1.7 also.

Get the latest bug fix release:

```
git clone https://github.com/sciabarra/AgileSites.git
cd AgileSites 
git checkout 1.8
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

Now you can import the demo and deploy it

```
wcs-dt import #Demo-11.8 @ALL
wcs-deploy
```

Test if you can see the demo site with `http://localhost:9080/cs/Satellite/demo`

(NOTE: sometimes CSDT has a glitch and you may need to import the demo twice).

You can run all the tests successfully with

`http://localhost:9080/cs/ContentServer?pagename=Demo_Tester`

Test the scaffold generation (accept all the defaults)

```
wcs-dt import #MySite-11.8 @ALL
wcs-generate site
wcs-generate template
wcs-generate layout
wcs-generate cselement
wcs-deploy
```

You can now create a page in the site MySite, with the following values

name: Home

description: Home Page

Title: Home Page Title

Text: Home Page Text

Then you should be able to see the site and run all the tests successfully

```
http://localhost:9080/cs/Satellite/mysite
http://localhost:9080/cs/ContentServer?pagename=MySite_Tester
```

Please report any failure in the procedure.
