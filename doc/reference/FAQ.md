## I have compilations errors.

AgileSites 1.8 requires Java 7. If you have Java 6 in your path the code won't compile since it is using features found only in JDK version 7 or greater. 

## Where are the binaries? I see only the sources in the download page

The framework is, by design, source only. The build will actually create 2 jars, one is the core and will be deployed (by the integrated installer) inside the WebCenter Sites webapp. 

Another jar is built and deployed in the shared folder and contains your application logic. This jar is continously built and deployed by the `wcs-package-jar` command. Both the jar are built automatically by an heavily tested automated build.

The framework lets you create one eclipse project, typing `eclipse` and importing the `app` folder in eclipse as an Eclipse project.

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
val addFilterSetup = "junit*" ||
```

and change as:

```
val addFilterSetup = "jfreechart*" || "junit*" ||
```

This filter is used to select the jars to add to the Sites webapp, so you use only the initial part of name of the jar without the version.

**NOTE** if a jar has additional dependent jars that are required to run it, you need to add those to the filter, too.

Finally you can use them. Shut down the application server, enter in the shell and execute:

```
reload
update
eclipse
wcs-setup
```

Restart the application server, refresh your eclipse project and enjoy using your new library both in the project and in the deployment.

## How can I delete a template, layout or a cselement created by the generator?

The `wcs-generate` for templates, cselements and layouts generates normally just 2 classes, one is the actual element class and another is the test class. All you need is to remove those 2 classes.

## The compilation has some wierd behaviours

Sometimes the compilation system get confused by intensive changes done to the source code. If the behaviour appears illogical, you can try exiting from the shell cleaning up everything running the `clean.bat` or the `clean.sh` script.

**Warning**: this will remove all the jars and a somewhat time consuming rebuild will be then performed when you access the shell again.

## I have an error saying "agilesites-core" is a missing dependency

When you start the shell, the script will build the core library if it is not already there. However, if you are not connected to internet (for example you are behind a proxy without direct access to internet) the initial build may file and the core library ends up not being build.

Ensure java can access the internet (for example configuring a proxy for Java), exit the agile shell, execute the `clean` batch/shell script and then run again the `agileshell`.  After those steps the build should rebuild correctly.

## No Such Resource when importing a site

Check the workspace actually exists. See below how to do it.

## No Such Site when importing a site

Check the site is actually in the Workspace.

## How can I know which workspaces  I have?

Just type `wcs-dt` the help command will display the available workspaces.
