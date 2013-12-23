#  AgileSites 1.8

Welcome to **AgileSites**, the Open Source framework for Agile development with Oracle WebCenter Sites (formerly Fatwire Content Server).
 
The framework is released under the commercial friendly Apache License 2.0.

Read on this page if you want to know more about this framework.

If you already know what is it, then you can:

- [Install it](http://www.agilesites.org/install.html) following the linked instructions.
- [Learn about it](http://www.agilesites.org/tutorial.html) reading a tutorial.
- [Check references](http://www.agilesites.org/reference.html) for in-depth documentation.
- [Browse the API](http://www.agilesites.org/javadoc/) reading the JavaDocs

# Developing for Oracle WebCenter Sites?

If you ever developed with Fatwire Content Server or Oracle WebCenter Sites, you may think (as many does) it is somewhat lacking friendly development tools.

As a developer you may have a wish list. I had a wish list too. At some point in time decided to fulfill some of my wishes for Fatwire and Sites development. This framework is the result of this (one year long!) effort.

If your wishes are similar to mine  you may be interested in this project. 

So, do you want...

## Use a __real__ [MVC pattern](http://www.agilesites.org/about.html#MVC)  

thus separating presentation (view) from logic (control) and content (model) ?

## Avoid [unreadable JSPs](http://www.agilesites.org/about.html#NoJSP) 

coding all your logic in plain Java (using full Java oop and not crippled JSP Java and awkward library CSElements) ?

## Keep the [HTML design](http://www.agilesites.org/about.html#HTML) in his original form 

so it can be updated easily by Web Designer without extensive recoding?

## Code with a [simpler API](http://www.agilesites.org/about.html#API)

possibly less verbose, more consistent and helpful in preventingsilly mistakes?

## Save yourself from [manual deployment](http://www.agilesites.org/about.html#Deploy) with a build and an installer

avoiding manual css/javascript to copy, jars to deploy, templates to publish, tables to catalog move, properties to edit and so on?

## Use a real [version control system](http://www.agilesites.org/about.html#VCS) 

thus tracking your work and sharing it with other developers in an healthy way?

## Write easily *real* [unit tests](http://www.agilesites.org/about.html#UnitTest)?

so you can develop in Test Driven way and keep a test suite to avoid regression bugs?

## Run a [continous integration server](http://www.agilesites.org/about.html#CI) ?

integrating and verifying the work of your team, allowing each developer to work in isolation with the JumpStartKit?

## Deploy your code as a [single jar](http://www.agilesites.org/about.html#Jar) ?

that can be easily shared, tracked, compared and deployed?

##  Hot deploy your changes, [not having to restart](http://www.agilesites.org/about.html#HotDeploy) the application server ?

when you do any change, even small, to your Java code?

## Use your [preferred IDE](http://www.agilesites.org/about.html#IDE) and not CSExplorer or a mandatory Eclipse-only plugin

either Eclipse, Netbeans, Idea or even Notepad or VI if you like ?

## Keep your [existing work](http://www.agilesites.org/about.html#Compatible) ?

while developing new code using this framework?

## Keep Oracle Support 

being 100% compliant to [standard practices](http://www.agilesites.org/about.html#Support) 

*If any of above is in your wish list*, then this framework may be suitable for you.

# A note of caution

This framework changes the rules of the game in Fatwire/Sites Development. 

It **modernises** Oracle WebCenter Site development providing a layer that introduces a lot of features allowing development in a modern, easier and agile way. 

The **inspiration** of the framework are modern and agile web frameworks like Ruby on Rails or Play! Framework.

This framework is **strongly opinionated**. It is built based on the following assumptions, or opinions if you prefer (incidentally those opinions are very similar to those advocated by the *Agile Development Moviment*):


- developers should work in isolation, using either a jump start kit or a local install of the product, sharing their code through a version control system 
- you _must not use_ a shared development server but you _must have_ instead a continuous integration server 
- you *need*  to write tests for every single piece of code you write, so writing tests should be very easy
- you need a build system so you can rebuild the entire site with a single command (the one to be run on the continuous integration server)
- being able to write your code in straight Java (leveraging Java OOP and code reuse) is much better than writing a lot of Java logic inside a JSP (losing on average 70% of the power of Java)
- HTML should be left in the original form to be easily updated by Web Designer, that with JSP is very difficult
- You ought to use your standard tool without needing strange plugins or special tools to edit the code
- The code must be deployed in jars, not published. Dependencies in code complex and code deployment is not managed well by publishing (that is for content)
- You should forget about publishing templates and welcome deploying your application jar (that should  NOT need restart of the application server).

If you disagree with those *opinions* and think the traditional way of working is just fine, then maybe this framework is not for you. 

## Fair Play

While changing all the rules of the game, it tries to play fair:

- The modernizer layer is built on top of the existing API, so it behaves like a standard site implementation.
- Only documented API is used for implementing the Framework. 
- It does not violate any rule written in documentation, especially caching rules (that are still 100% Sites rules)
code implementing a site

- There is a clear separation integration of the framework and the product. The framework is invoked by standard JSP and the output is still managed by JSP.

As a result you need not to worry of incompatible changes would break the site, since they would affect any other site.

Oracle has not reason to refuse support as long as the question are not specific to the internals of the framework. The framework *is* a fully documentation-compliant website implementation.  

The framework ultimately produces a collections of method calls stored in a single jar that generates strings  rendered by JSPs. Everything else  is there to ease the creation of such a jar.
