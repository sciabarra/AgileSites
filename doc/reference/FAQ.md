## Where are the binaries? I see only the sources in the download page

The framework is, by design, source only. The build will create actually 2 jars, one is the core and will be deployed (by the integrated installer) inside the WebCenter Sites webapp. Another jar is built and deployed in the shared folder and contains you application logic. This jar is continously built and deployed by the `wcs-package-jar` command. Both the jar are built automatically by an heavily tested automated build.

The framework creates 3 projects, one (`agilesites-app`) is where you should put your user code. If you want to extend the framework (something in a sense part of the design), you should place your changes in `agilesites-api`. Consider contribuiting your changes if you extend the framework  (you will required to place your changes under the Apache License 2.0). You are not expected to change the `agilesites-core`.


##  On Mac OSX (and potentially in other systems) after the installation the JumpStart is broken (I cannot even log in)

JAVA_HOME points to java version or in the path there is a different java version than the one used by JumpStart Kit.

Typical symptom is this exception:

java.lang.NoClassDefFoundError: Could not initialize class COM.FutureTense.Servlet.ServletRequest

This problem happens mostly on  OSX Lion and Mountain Lion: if you install Oracle JDK 1.7, in the path there is java 1.7. But this may happen also in other systems.

 JumpStart up to version 11gR1 on a Mac installs using the existing java 1.6.  As a result, AgileSites build a core with JDK 1.7 but JSK will runs under 1.6 and will be unable to read classes compiled with JDK 1.7.

To solve the issue on Mac OSX, first clean everthing unsing the clean.sh  script, then edit the agilesites.sh script and add this the beginning of the script:

```
export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Home
export PATH=$JAVA_HOME/bin:$PATH 
```

Then start the shell again and repeat the installation procedure.

 
## How do I add a custom library to the framework?

You need to locate it in a repository accessible by maven, using websites like [MVN repository](http://mvnrepository.com). Assuming you want something like, for example, jfreechart, locate it and locate its sbt dependency. In the site you will find a string like this:

```
libraryDependencies += "jfree" % "jfreechart" % "1.0.13"         
```

Then open the file `project/AgileSitesBuild` and  locate the:

```
val coreDependencies = Seq(
    "javax.servlet" % "servlet-api" % "2.5",
```

add your dependency at the beginning as follows:

```
val coreDependencies = Seq(
    "jfree" % "jfreechart" % "1.0.13",
    "javax.servlet" % "servlet-api" % "2.5",
```

Note you dropped the `libraryDependencies +=` and added a comma to the end.

This is enough to download the jar and use it for compilation.  If you want also to deploy in the application server together the other jars, you need to add in the deployment filter.

Locate the 

```
val addFilterSetup = "scala-library*" ||
```

and add:

```
val addFilterSetup = "jfreechart*" || "scala-library*" ||
```

This filter is used to select the jars to add to the Sites webapp, so you use only the initial part of name of the jar without the version.

**NOTE** if a jar has additional dependent jars that are required to run it, you need to add those to the filter, too.

Finally you can use them. Shut down the application server, enter in the shell and execute:

```
reload
update
eclipse
wcs-setup-offline
```

Restart the application server, refresh your eclipse project and enjoy using your new library both in the project and in the deployment.

## How can I delete a template, layout or a cselement created by the generator?

The `wcs-generate` for templates, cselements and layouts generates normally just 2 classes, one is the actual element class and another is the test class. In addition, they add the class name in the list of classes to be deployed and the test name in the list of tests to be run.

So removing a class for example with name SampleLayout for site `mysite` involves removing the 2 files

- `app/src/main/java/mysite/element/SampleLayout`
- `app/src/main/java/mysite/element/SampleLayout`

Then editing the 2 files

- `app/src/main/resources/mysite/elements.txt`
- `app/src/main/resources/mysite/tests`

and removing the entries for `mysite.element.SampleLatyout` and `mysite.tests.SampleLayoutTest`respectively.

## The compilation has some wierd behaviours

Sometimes the compilation system get confused by intensive changes done to the source code. If the behavious appears illogical, you can try exiting from the shell cleaning up everything running the `clean.bat` or the `clean.sh` script.

**Warning**: this will remove all the jars and a somewhat time consuming rebuild will be then performed when you access the shell again.

## I have an error saying "agilesites-core" is a missing dependency

When you start the shell, the script will build the core library if it is not already there. However, if you are not connected to internet (for example you are behind a proxy without direct access to internet) the initial build may file and the core library ends up not being build.

Ensure java can access the internet (for example configuring a proxy for Java), exit the agile shell, execute the `clean` batch/shell script and then run again the `agileshell`.  After those steps the build should rebuild correctly.


## No Such Resource when importing the Demo site

Before importing, you need to run the `wcs-setup-offline` that will set the CSDT export folder to the export folder in the AgileSites directory.


In build.sbt the sites imported is defined by:

```
wcsSites := "Demo"
```

The CSDT workspace name is then given by the `wcsSites` name (plus the Sites version number)

Ensure you performed the installation (so in `futuretense.ini` the `csdt.exportfolder` has been changed to /export after wcs-setup-offilne) then check that in the expoort/envision folder there is `Demo-11g`

Note that when you change current sites, you need to repeat the `wcs-setup-offilne` because it has to change some properties.




