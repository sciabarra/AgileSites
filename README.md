# AgileSites 0.5 (Rick Deckard)

Warning - still work in progress - do not download yet.

-----

Welcome to **AgileSites**, the framework for Agile development with Oracle WebCenter Sites (formerly Fatwire ContentServer).

# Developing for Oracle  WebCenter Sites ?

If you ever developed with Fatwire or Sites, you may have found it  is not very developer friendly and have a wish list.
I had a wish list too, and this is my own. If your wish list has similar items to my own, you may be interested in this project.

# Do you want

## Use a __real__ [MVC pattern](/doc/Features.md#MVC)  

### thus separating presentation (view) from logic (control) and content (model) ?

## Avoid [unreadable JSPs](/doc/Features.md#NoJSP) 

### coding all your logic in plain Java (using full Java oop and not crippled JSP Java and awkward library CSElements) ?

## Keep the [HTML design](/doc/Features.md#HTML) in his original form 

### so they can be updated easily by Web Designer ?

## Code with a [better API](/doc/Features.md#API)

### possiblysimpler, less verbose, consistent and preventing you from doing silly mistakes?

## Save yourself from a [deployment hell](/doc/Features.md#Deploy) with a simple build and an installer

### avoiding manual  css/javascript to copy, jars to deploy, templates to publish, tables to catalog move, properties to edit and so on?

## Use a real [version control system](/doc/Features.md#VCS) 

### to track your work and share it with other developers?

## Write easily *real* [unit tests](/doc/Features.md#UnitTest)?

### so you can develop in Test Driven way and keep a test suite to avoid regression bugs?

## Run a [continous integration server](/doc/Features.md#CI) 

### integrating and verifying the work of your team, allowing each developer to work in isolation with the JSK?

## Deploy your code as a [single jar](/doc/Features.md#Jar) 

### that can be easily shared, tracked, compared and deployed?

##  HotDeploy your changes, [Not having to restart](/doc/Features.md#HotDeploy) the application server 

### when you do any change, even small, to your Java code?

## Use your [preferred IDE](/doc/Features.md#IDE) and not CSExplorer or a mandatory Eclipse-only plugin

### either  Eclipse, Netbeans IntellijIdea or even Notepad or VI if you like ?

## Keep your [existing work](/doc/Features.md#Compatible) 

### while developing new code using this framework?

## Be 100% compliant to [standard practices](/doc/Features.md#Support) 

### so you won't lose Oracle Support?

*If any of above is in your wish list*, the this framework may be suitable for you.

# Ok, I am sold, now what?

- [Install](/doc/Install.md) the framework
- Follow this [Tutorial](/doc/Tutorial.md) 
- Keep this [API Reference](/doc/Reference.md) handy when you develop
- Code it!

Beins a commercial friendly (Apache) open source license, there is no license to pay.

If you need support you may consider commercial support from [Sciabarra srl](http://www.sciabarra.com/fatwire)

# A note of caution

This framework is changing the rules of the game in Fatwire/Sites Development. 

It tries to modernize Oracle WebCenter Site development providing a layer that introduces a lot of features for developing in a modern, easier and agile way. 

This framework is *strongly opinionated*. It is built based on the following assumptions (or opinions). 

Incidentally those opinions are very similar to those advocates by the Agile Development Movinment

- developers should work in isolation, using either a jump start kit or a local install of the product, sharing their code through a version control system
- you *must*  write unit tests for every single piece of code you write
- you need a build system that must be run by a continuous integration system checking the work
- being able to write your code in straight Java (leveraging Java OOP and code reuse) is muh better that writing a lot of Java logic inside a JSP (losing on average 70% of the power of Java)
- HTML should be left in  original form to be easily updatable by Web Designer 
- You ought to use your standard tool without needing strange plugins or special tool to access the code
- The code must be deployed in jars, not published. Dependencies in code are much more complex than they can be managed by publishing. You should forget about publishing templates and welcome deploying your application jar (that does NOT need restart of the application server).

However, while changing all the rules of the game, it tries to play fair:

- Only documented API is used for implementing the Framework. 
- It does not break any contract so the whole framework appear to the product like normal user code implementing a site
- There is a clear separation with the Framework and the product. The framework is invoked by standard JSP and produces a string, handled by Fatwire JSP

As a result you need not to worry of incompatible changes would break the site, since they would affect any other site.
Oracle has not reason to refuse support as long as the question are not specific to the internals of the framework. 
And for those questions there is our [commercial support](http:/www.sciabarra.com/fatwire/)

