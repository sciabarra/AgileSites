# AgileSites 0.5 (Rick)

Current Relese: 0.5-beta1 

This project is an Agile Development framework for Oracle WebCenter Sites.

The framework is tested currently  with WCS JSK 11gR1 
Sample sites can be imported *ONLY* in WCS 11gR1


If you are in a hurry this readme includes a (succing) introduction to the whole framework.

Check the [[AgileSites.pdf|AgileSites book]] for the complete documentation.

# Before you start

This framework is a game changing. It tries to modernize Oracle WCS development providing a layer that introduces a lot of features for developing in a modern, easier and agile way. 

This framework is *strongly opinionated*. It is built based on the following assumptions (or opinions). Incidentally those opinions are very similar to those advocates by the Agile Development Movinment

- developers should work in isolation, share their code through a version control system
- you need to be able to write unit tests for every single piece of code you write
- you need a build system that must be run by a continuous integration system checking the work
- being able to code in pure Java (leveraging Java OOP and code reuse) s better that write a lot of Java logic inside a JSP (forgetting 2/3 of what you can do in Java)
- HTML should be HTML, and must be kept in is original form to be easily modifiable by designer who must not need to understand JSP
- You ought to use your favorite tool without strange plugins (either pure eclipse, or Netbeans, IntelliJ or even notepad if you like)
- The code must be deployed in jars, not published. Dependencies in code are much more complex than they can be managed by publishing

However, while changing all the rules of the game, it plays fair:

Only documented API is used for implementing the Framework. It does not break any contract so the whole framework appear as a standard implementation of a site.
As a result you need not to worry of incompatible changes would break the site since they would affect any other site.


# Installation

## Prerequisite: 

-JDK 1.6+, 
- Eclipse Juno+


## Installation

- Download the framework from GitHub (click on the link on top of this document for a stable release)/
- Unzip it, go to the terminal and cd to the main directory
- Copy build.sbt.dist to build.sbt and edit it, providing the correct values (read the comments)
- Start the agilesite shell
- Create the core library running core/publish-local
- Stop WCS, and install the library with wcs-setup
- Start WCS, and import standard code with wcs-cm import_all

## Create the site MySite

- Edit build.sbt and set "wcsSites := MySite"
- Start the agilesites shell and ensure WCS is running
- Create a new site with wcs-gen site (site=MySite, prefix=My)
- Deploy it with wcs-deploy
- Run a test invoking http://localhost:8080/cs/ContentServer?pagename=MySite/MyTester
- Generate an eclipse project with the command "eclipse" the import it in eclipse itself

## Normal coding
- copy your static mockup under src/main/static
- generate a new template with "wcs-gen template" and a new CSElement with "wcs-gen cselement"
- Remember to redeply (invoking wcs-deploy) if you add a template or a cselement or you change the html
- otherwise just code in Java in eclipse keeping the 



