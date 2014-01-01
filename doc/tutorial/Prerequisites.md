##### Prev:  [Tutorial Index](/tutorial.html) Next: [New Site](NewSite.md)

In this section we will see what is needed in order to use this framework.

## Prerequisites 

The framework **requires** you develop in your local machine with a local installation of Sites.  You are expected to share code and content model through a version control system. 

However this is not a limitation, since you can easily install the enviroment and the JumpStartKit for development on your system. 

### You need Java Development Kit  7

The framework currently requires a java development kit version 7 or greater. You need to compile code so an actual Java Development Kit is needed, not just a java runtime

### A local instance of WebCenter Sites is required

We stress here that the framework assumes you are going (and willing) to use a local installation of Sites that is unique to each developer.

It is opinion of the author that working on a shared instance is against good development practices, so the framework is assuming that you may not want to do this, but instead you prefer to work in your local machine and share changes with other through a version control system.

## Install JumpStart Kit

The JumpStartKit (a version of Sites easy to install, with its own embedded application server and database) is available from Oracle Support to customers. Just access to support.oracle.home and search for Jump Start Kit (JSK).

JSK It is convenient because it does not requires too many resources and can be installed in a developer machine. It includes everything can be needed for development.

**NOTE** It is possible to [install the full Sites in your computer](http://www.sciabarra.com/fatwire/2012/04/09/download-and-install-a-development-fatwire-instance-also-on-mac/) for development purposes. You can install it with free Oracle XE or  the free Microsoft SQLServer. It is even possible to install it with the simple, in-memory database Hypersonic SQL (note, only using **version 1.8.0**) 

## Verify Java Versions

It is important that you verify the java version used by the JumpStart Kit is the same that will be used by AgileSites. AgileSites will refuse to deploy anything if the version of the JDK used in Sites is different from the version used by the AgileSites shell.

### Checking which Java version are in use

Start Jump Start Kit then access the `HelloCS` servlet within you Sites webapp to check the version (typically you use the url `http://localhost:8080/cs/HelloCS`). It displays the version of Java in use.

Once done you can go to the command prompt and type: `java -version` and then `javac -version` 

Both the used java version and the compiler version must match the version used by the JSK.

![Java Version](../img/snap5135.png)

### Fixing java versions 

On Windows, if the `java` or `javac` is not found you must the variable JAVA_HOME and the PATH as described [here](http://stackoverflow.com/questions/11161248/setting-java-home).

A windows JDK is installed together with  JSK by default in `C:\Oracle\WebCenter\Sites\11gR1\windowsJDK` (change accordingly your installation choices) so you use this JDK as JAVA_HOME. Remeber also to the the PATH variable adding at the end of the PATH: `;"%JAVA_HOME\bin"`

On Mac, you need to edit the file `~/.bash_profile` followinging [these directions](http://stackoverflow.com/questions/6588390/where-is-java-home-on-osx-lion-10-7-or-mountain-lion-10-8). Then you should also fix the path adding `export PATH="$JAVA_HOME/bin":$PATH`

On Linux the solution is the same as the Mac solution except that the actual location of your JDK depends on your distribution (and there are many of them), so definitely you have to figure out by yourself where java is installed.

**NOTE** Be aware that changing the Java Home may affect other applications using  Java in your machine.

## Go on with the installation

Once you installed Sites, follows the [installation procedure](../install.md) to install AgileSites. You do not require the installation of the demo site, although it is convenient to have it installed during development.
