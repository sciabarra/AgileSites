# AgileSites 1.8 QA & Test

## Known issues

Please report any failure in the procedure.

## Procedure

Install JSK 11.1.1.8 or JSK  11.1.1.6.1


You need a JDK version 1.7.x

Get the latest 1.8 release:

```
git clone https://github.com/sciabarra/AgileSites.git
cd AgileSites
git branch 
```

It should show it is the branch 1.8 (it is the default)

Launch agilesites.bat or agilesites.sh. A configurator window will show up.

Select where is your JSK, then click on configure button.

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


If you have 11.1.1.8.0 execute:


```
wcs-dt import #Demo-11.8 !Demo @ALL
```

If you have 11.1.1.6.x execute:


```
wcs-dt import #Demo-11.6 !Demo @ALL
```

Then execute:


```
wcs-deploy
```

Verify the Demo site works and all the tests pass.


```
http://localhost:9080/cs/Satellite/demo
http://localhost:9080/cs/ContentServer?pagename=Demo_Tester

```

Note the port may be different according your installation choices.


# Generate a site with the scaffolds 

If you have 11.1.1.8.0 execute:


```
wcs-dt import #Demo-11.8 !Demo @ALL
```

If you have 11.1.1.6.x execute:


```
wcs-dt import #Demo-11.6 !Demo @ALL
```

Run the following commands, accepting all the defaults:


```
wcs-generate site
wcs-generate template
wcs-generate layout
wcs-generate cselement
wcs-deploy

```

Verify now all tests run.


```
http://localhost:9080/cs/Satellite/mysite
http://localhost:9080/cs/ContentServer?pagename=MySite_Tester
```

Note the port may be different according your installation choices.
