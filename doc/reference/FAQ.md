## How can I delete a template, layout or a cselement created by the generator?

The `wcs-generate` for templates, cselements and layouts generates normally just 2 classes, one is the actual element class and another is the test class. In addition, they add the class name in the list of classes to be deployed and the test name in the list of tests to be run.

So removing a class for example with name SampleLayout for site `mysite` involves removing the 2 files

- `app/src/main/java/mysite/element/SampleLayout`
- `app/src/main/java/mysite/element/SampleLayout`

Then editing the 2 files

- `app/src/main/resources/mysite/elements.txt`
- `app/src/main/resources/mysite/tests`

and removing the entries for `mysite.element.SampleLatyout` and `mysite.tests.SampleLayoutTest`respectively.
 
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

## The compilation has some wierd behaviours

Sometimes the compilation system get confused by intensive changes done to the source code. If the behavious appears illogical, you can try exiting from the shell cleaning up everything running the `clean.bat` or the `clean.sh` script.

**Warning**: this will remove all the jars and a somewhat time consuming rebuild will be then performed when you access the shell again.

## I have an error saying "agilesites-core" is a missing dependency

When you start the shell, the script will build the core library if it is not already there. However, if you are not connected to internet (for example you are behind a proxy without direct access to internet) the initial build may file and the core library ends up not being build.

Ensure java can access the internet (for example configuring a proxy for Java), exit the agile shell, execute the `clean` batch/shell script and then run again the `agileshell`.  After those steps the build should rebuild correctly.
