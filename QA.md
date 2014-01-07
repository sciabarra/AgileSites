# AgileSites 1.8 QA & Test

## Known issues

- You need to remove existing jars from Shared/agilesites/ if you have installed it before
- confusing error message if you are using the wrong version of java 

Please report any failure in the procedure.

## Procedure

Install JSK 11.1.1.8 or JSK  11.1.1.6.1

You need a JDK version 1.7.x

Get the latest bug fix release:

```
git clone https://github.com/sciabarra/AgileSites.git
cd AgileSites 
git checkout remotes/origin/1.8
```

Launch agilesites.bat or agilesites.sh. A configurator will popup.

Select  where your JSK then click on configure button.

When asked if you want to create a site, answer `MySite`

It will download all the jars and compile. It can take a while.

Shut down JSK and install it with the command:

```
wcs-setup
```

(it will warn you if JSK is running).

Now start JSK. Test if you can connect with the command

```
wcs-hello
```

Wait until you get an answer.

# Import the Demo site

Import the Demo site (first inmport the site, then deploy, then import all the assets).

```
wcs-dt import #Demo-11.8 @SITE
wcs-deploy
wcs-dt import #Demo-11.8 @ALL
```

# Generate a site with the scaffolds 

Run the following commands, accepting all the defaults:

```
wcs-dt import #MySite-11.8 @SITE
wcs-generate site
wcs-generate template
wcs-generate layout
wcs-generate cselement
wcs-deploy
wcs-dt import #MySite-11.8 @ALL
```


# Verify

Then you should be able to see the Demo site and the new site MySite 

```
http://localhost:9080/cs/Satellite/demo
http://localhost:9080/cs/Satellite/mysite
```

Also all the tests should run successfully:

```
http://localhost:9080/cs/ContentServer?pagename=Demo_Tester
http://localhost:9080/cs/ContentServer?pagename=MySite_Tester
```

