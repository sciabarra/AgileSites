# AgileSites 0.5 (Rick Deckard)

Welcome to **AgileSites**, the framework for Agile development with Oracle WebCenter Sites (formerly Fatwire ContentServer).

# Developing for Oracle  WebCenter Sites ?

If you ever developed with Fatwire or Sites, you may have a wish list, including items like:

## Use a __real__ [MVC pattern](/doc/Features#MVC)  

thus separating presentation (view) from logic (control) and content (model) ?

## Avoid [unreadable JSPs](/doc/Features#NoJSP) 

coding all your logic in plain Java (or Scala) and keeping views in simple HTML ?

## Keep the [HTML design](/doc/Features#HTML) in his original form 
so they can be updated easily by Web Designer ?

## Code with a [better API](/doc/Features#API)

that is both simpler, shorter, consistent and preventing you from doing silly mistakes?

## Save yourselg from a [deployment hell](/doc/Features#Deploy)

with css/javascript to copy, jars to deploy, templates to publish and so on?

## Use a real [version control system](/doc/Features#VCS) 

to manage your work and share it with other developers?

## Write easily [unit tests|(/doc/Features#UnitTest)

so you can develop in Test Driven way and keep a test suite to verify consistency.

## Run a [continous integration server](/doc/Features#CI) 

integrating and verifying the work of your team, allowing each developer to work in isolation with the JSK?

## Deploy your code as a [single jar](/doc/Features#Jar) 

that can be easily shared, tracked and compared?

##  [Not having to restart](/doc/Features#HotDeploy) the application server 

when change  your Java code?

## Use your [preferred IDE](/doc/Features#IDE)

either  Eclipse, Netbeans IntellijIdea or even Notepad or VI if you like ?

## Keep your [existing work](/doc/Features#Compatible) 

while developing new code using this framework?

## Be 100% compliant to [[standard practices|Feature-Standard]] 

so you won't lose Oracle Support?

*If any of above is of interest*, the this framework may be suitable for you.

# Ok, I am sold, now what?

- Install the framework
- Read this tutorial 
- Check this reference reference for the API guide

# A note of caution

This framework is changing the rules of the game in Sites Development. It tries to modernize Oracle WebCenter Site development providing a layer that introduces a lot of features for developing in a modern, easier and agile way. 

This framework is *strongly opinionated*. It is built based on the following assumptions (or opinions). Incidentally those opinions are very similar to those advocates by the Agile Development Movinment

- developers should work in isolation, using either a jump start kit or a local install of the product, sharing their code through a version control system
- you *must* and need to be able to write unit tests for every single piece of code you write
- you need a build system that must be run by a continuous integration system checking the work
- being able to write your code in straight Java (leveraging Java OOP and code reuse) is muh better that writing a lot of Java logic inside a JSP (losing on average 70% of the power of Java)
- HTML should be HTML, and must be kept in is original form to be easily modifiable by designer 
- You ought to use your favorite tool without strange plugins (either pure eclipse, or Netbeans, IntelliJ or even notepad if you like)
- The code must be deployed in jars, not published. Dependencies in code are much more complex than they can be managed by publishing. So forget about publishing templates and welcome deploying your application jar (that does NOT need restart of the application server)

However, while changing all the rules of the game, it tries to play fair:

- Only documented API is used for implementing the Framework. 
- It does not break any contract so the whole framework appear to the product like normal user code implementing a 

As a result you need not to worry of incompatible changes would break the site, since they would affect any other site. And Oracle has not reason to refuse support as long as the question are not specific to the internals of the framework. 

The framework is commercially supported by Sciabarra srl, a company working with Fatwire (originally OpenMarket ContentServer) since 1999. 


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



