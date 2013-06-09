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

A library can be used for 2 purposes: compilation and runtime. 

XXXX

## The compilation has some wierd behaviours

Sometimes the compilation system get confused by intensive changes done to the source code. If the behavious appears illogical, you can try exiting from the shell cleaning up everything running the `clean.bat` or the `clean.sh` script.

**Warning**: this will remove all the jars and a somewhat time consuming rebuild will be then performed when you access the shell again.

## I have an error saying "agilesites-core" is a missing dependency

When you start the shell, the script will build the core library if it is not already there. However, if you are not connected to internet (for example you are behind a proxy without direct access to internet) the initial build may file and the core library ends up not being build.

Ensure java can access the internet (for example configuring a proxy for Java), exit the agile shell, execute the clean batch/shell script and then run again the `agileshell`. Those steps should rebuild properly

## Why not use just GST-Foundation? Why another framework?

While there are some similarities (both provides a MVC framework and a customizable url assembler) there are also many differences.

The key technical choice of GST is using Groovy for writing the controller (using AssetAPI to retrieve the content) and keep JSP (with the addition of the JSTL) to render the view. 

AgileSites instead moves away from JSP, offers pure Java coding for the controller and uses pure HTML views with a jQuery-style replacement logic.

We believe this way you do not need to learn Groovy (although it is similar to Java is not Java), you can keep the HTML in the original form instead of changin it in HTML-with-code, and most importantly you can deal with a much simple API that the tag API/Asset API you still need to use with GST.

However, the key priority for AgileSites is to make Agile development possible, providing those functionality current day developers feel should be out-of-the-box. 
There is a lot of emphasis on simplification: simplifying installation, simplification the deployment, simplification of the base API for becoming quickly productive, simplification of test writing and so on.

We believe that GST-Foundation priorities are different and also it offers a different feature set.  So if needed, we think you can use both the frameworks in a project, like any other java project where you use different libraries from different sources. 
