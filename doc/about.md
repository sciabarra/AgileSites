AgileSites is a framework for Oracle WebCenter Sites that changes the rules of the game in Fatwire/Sites Development. 

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

While changing all the rules of the game, it tries to play fair with Oracle Support rule.

- The whole framework is, from the point of view of Oracle WebCenter Sites, just a standard site. It is a collection of JSP templates that invokes some java code deployed in a jar.
- The modernizer layer is built on top of the existing API, so it behaves like a standard site implementation.
- Only documented API is used for implementing the Framework. 
- It does not violate any rule written in documentation, especially caching rules (that are still 100% Sites rules)
code implementing a site
- There is a clear separation integration of the framework and the product. The framework is invoked by standard JSP and the output is still managed by JSP.

As a result you need not to worry of incompatible changes would break the site, since they would affect any other site.

The framework *is* a fully documentation-compliant website implementation.  

# What it does then?

AgileSites is a framework for Oracle WebCenter Sites designed with the following goals in mind:

- The framework is easy to use for development
- The provided tools enable agile development practices
- Development with distributed (offshore) team is manageable
- It is compatible with standard Java coding practices
- It only uses documented API to keep Oracle Support

In short AgileSites scale and speed up development with Oracle WebCenter Sites.

Here is a list of what it has to offer.

# <a name="API"> </a>Simpler API

## WebCenter Sites has a large, complex API

WebCenter Sites api is pretty old, sometimes ugly and inconsistent.
 
Furthermore it is offering too many options, resulting in a confusing api,
The documentation and the API is not guiding the developer to follow best practices.

## AgileSites offers a simpler API 

A new, simplified, consistent API is offered. This API hide many of the complexities of the Fawire API, like the different way to access basic and flex attributes.
 
The goal is to provide 80% of the functionality with  20% of the complexity.
However, all the existing WebCenter Sites is kept accessible and available, and can be used if required.

# <a name="Deploy"> </a>Deployment Hell 
 
## WebCenter Sites deployment is a manual process

A WebCenter Sites website is typically very complex to deploy, since it is comprised of many different components: library jars, customization jars, css and javascript files, CSElements, Templates, database tables and some content that must be always present.

## AgileSites deployment is fully automated

The system is provided from the ground up with an automated build system that performs all the step for the installation and update. Everything is kept in source form and can be deployed in an unchanged blank system.

A whole site can be install a new system with 2 commands:

- `wcs-setup` before starting the application server, 
- `wcs-deploy` after starting the application server

Then a site can be updated with a single command (wcs-deploy) 
You do not need to restart the application server except once, the first time, when installing the core.

# <a name="Compatible"> </a>Compatible with your work

## WebCenter Sites impementations need compatibile extensions

Rarely a project exists in a vacuum, so if you decide to give a try to this framework, you want to be able to keep your existing work and not having to rewrite everything.

## AgileSites is pluggable in existing WCS sites

The framework is totally compatible with any existing project. It implements template logic in Java, but templates are called by standard JSP, so you can freely mix framework managed templates with existing templates. 

You can even write templates partially in JSP and partially in Java using the entire hot deploy infrastructure of the framework.

# <a name="CI"> </a>Continuous Integration

## WebCenter Sites does not allow for testing and continuous integration

Continuous Integration a key practices in Agile development.

Every developer should work in isolation in his own machine but when many developers are working at the same time, they should commit to a version control system that will rebuild and run the full test suite continuosly.

Because of the limitations of the CMS, usually developers share a common development environment and rely on this for working as a team. This practice has a lot of shortcomings, including periods of unavailabilty of the shared environment because of some mistakes, conflicts in accessing  the same template or definition , inability to work without a working internet connection and so on.

## AgileSites provides out-of-the-box test runner and continuos integration 

The entire framework is built in order to enable Agile development practices.

Since building the site is pretty easy from a single source, every developer can get a development environment working in his local machine quickly and easily, so there is no need to share a development environment.

Since there is a build and writing tests is easy, and a version control system can be used, every developer have just to commit his changes to the version control system

Using a standard Continuous Integration server like Jenkins is just a matter of:

- installing a Jump Start Kit on the integration server and perform the first install
- configure Jenkins it to checkout the code and deploy it on the server
- run the tests (both unit test coming from the developers and integration test coming from tester) using the provided build tool and distribuite the results to developers through a mailing list.

# <a name="HotDeploy"> </a>No restart of application server for Java coding

## Coding in Java with WebCenter Sites requires restarting the applicaton server for each change of the code

Coding in JSP with WebCenter Sites has the advantage that you can see immediately the result without restarting the application server.

Traditionally, when coding in Java instead  a major hassle is the requirement to restart the application server to see the code change. 

## AgileSites allows coding in Java without restarting thanks to hot-deploying of changed jars

The framework features hot deploy as a key feature. After any code change, a jar is automatically built and copied in place, and the code is picked up immediately. No need to restart anything.

So you have the advantage of being able to use full java without losing the immediate gratification of seeing the result without any delay that usually breaks the development flow and is a productivity killer.

# <a name="IDE"> </a>Use your preferred IDE and VCS

## WebCenter Sites requires Eclipse with the CSDT plugin

The choice of development tool usable with WebCenter Sites has been traditionally very limited. Traditionally you have to use the windows-only and limited Content Server Explorer. More recently a few plugins has been made available, all of them Eclipse based, including the CSDT tool.

But if you want to use another IDE, or just Eclipse without a plugin (that may be unavailable or incompatibile with your eclipse flavor) you are out of luck.

## AgileSites allows for coding with any Java IDE writing standard Java

The application built with AgileSites is a completely standard Java application not requiring any plugin to be edited: just add the relevant jars in the classpath, and a tool to generate the correct classpath and download them from repositories is included.

As a result, nothing else but any java IDE is required, and this can be Eclipse without plugins, NetBeans, Idea or even nothing at all. Compilation, packaging and deploying is included by a command line tool that is part of the framework.

# <a name="NoJSP"> </a>Move Away from JSP

## WebCenter Sites requires you code everything in JSP

Sites/WebCenter Sites require developers implement templates either using JSP or its proprietary XML based language. 

JSP are great for implementing simple rendering logic, and they dynamic, since they are recompiled on the fly. Developers enjoy instant gratifications seeing immediately the results of their efforts.

With those advantages comes many disadvantages. The problem with JSP is that they are really meant to implement **simple** rendering logic *ONLY*. A normal Java Web Applications uses also servlet as controllers, and very often MVC frameworks like Struts or Spring MVC to better organize the code. 

In Sites/WebCenter Sites unfortunately JSP is the only mean to develop code, unless you write an extensions jar to add to the webapp, then you deploy it and restart the application server.

Since restarting the application server is slow, most developers  end up writing complex logic directly in the JSP, writing JSP that are almost all comprised of Java code. This  practice has many shortcomings.

- First, JSP cannot share code. You cannot define for example an utility method in a JSP and call it in another jsp.

You can use a JSP in WebCenter Sites as a CSElement and call it from another JSP, but they are very cumbersome to use.

Alternatively, you can write your code in a Java class, deploy it in a separate jar, then restart the application server before using it. This is very cumbersome in the development process.

- Second, the syntax of JSP is frankly ugly and unreadable. Indentation is very difficult, and often you cannot indent at all to avoid generating tons unindented HTML (that must required to be readable too). 

- Third, WebCenter Sites is complicating things because the API can often be invoked only through the tag, so you end up switching from java to html and back only to call an API through a tag.

Many projects end up with a lot of Java code inside a JSP, with a lot of code copied and pasted between JSP, getting a large, unmanageable amount of spaghetti code with lot of repetitions. 

This is not entirely a WebCenter Sites fault but definitely the limitations of JSP encourage those bad practices.

## AgileSites allows to code everything in full Java

The fundamental feature of this framework is allowing developers to write templates directly in Java, and providing APIs to render the view  from an unchanged HTML (the typical HTML that you receive from web designers). 

Since accessing to the content model in WebCenter Sites is usually done using JSP tags, a specific API has been developed to provide easy invocation of  JSP tags directly in Java.

To keep the Agility of development using JSP, all your code is stored in a jar that is dinamically redeployed when you change it, so there is no need to restart the application server. You keep the instant gratification of JSP, but you can write ordered, clean Java code, splitting it in multiple classes and reusing code without limite call-elements.

You have all the Java power at your finger tips. However, templates are still templates, and they are invoked by a JSP. The stub code to generate the JSP is managed automatically by the framework so you don't have to worry of it.

# <a name="MVC"> </a>MVC Pattern

## WebCenter Sites uses a composite view rendering pattern without a controller

A common problem in WebCenter Sites development is that templates do not implement a Model-View-Controller pattern.

Instead, a template is just a JSP where you mix  the rendering logic (view), the content reading logic (model) and some specific business logic (control)

## AgileSites integrates an MVC model for each template and cselement

The AgileSites framework enables you writing your code in Java, not in JSP.

Each template now has a corresponding class that acts as a controller. 

A specific API to define the content (model) and retrieve it, is provided, separating the model from the controller.

Furthermore, also the view is kept separated, and in its original form: the view is indeed the original HTML as provided by web designers.

Developers write all the template code in plain Java, implementing a controller that collect data from the model and place it in the view as needed.

# <a name="HTML"> </a>Keep HTML in the original form

## WebCenter Site requires you cut the origina mockup in pieces and add code to it.

In any large web development project, web designer are different people from web developers, since different skills are required.

Usually Web Designer deliever their work as a set of html/css/javascript files that must then used by web developers to implement the web site in the CMS. 

Major problems arises when Web Designers update the web design, since at every update developer have to track down html changes and update code following the changes in the design.

## AgileSites allows keeping the HTML in the original form

The framework allows developer to use unchanged original HTML files. A specific API, using jquery-like CSS based selectors allow to pick specific pieces of html, replace with content and render them without having to mix code and HTML. This way web designer can freely update the html and the CSS and, as long as id and classes are kept unchanged, there is no need to change the business logic code.

# <a name="Jar"> </a>Single Jar Deploy

## WebCenter Sites deploys as a list of separate JSP to be published singularly

Deploying code though publishing templates is error prone, does not capture real dependencies in code and is very hard to track.

## AgileSites deploys as a single jar with everything inside

The deployment of an AgileSites project is a single jar containing both source code and html resources. The jar is automatically packaged and deployed just copying it on his final destinations. Changes are picked when the jar is deployed.

Since all the code is a single jar, deploying a site is only a matter of copying a jar in place, and it is possible to compare the complete code deployed for a site just comparing the deployed jar.

# <a name="Support"> </a>Keep Oracle Support

## WebCenter Sites is supported by Oracle

If you pick the framework, you may want to keep getting support from the vendor, and using this framework should not be a reason to lose support. 

Furthermore you want to use all the available feature of WebCenter Sites  without being limited in any way.

## AgileSites follows Oracle standards - everything is just a JSP template.

AgileSites does not change WebCenter Sites way of operate in any way. Templates are still invoked using JSP, and JSP code is standard Java code, so nothing is different from standard practices. 

The entire hot deploy api is actually a standard feature of Java that exists since Java 1.0: it is implemented using the standard class loader used for applets. Everything else is provided either with java classes that extends existing api (but don't change it) and a standard external build tool that packges your code and deploy it. 

Finally, the asset model update is implemented using standard WebCenter Sites Elements and SiteEntries, invoked through the Catalog Manager.

In conclusion, all the framework is a layer built on top of standard WebCenter Sites and nothing is "invalidating your guarantee", except you may not ask support for the specific framework to the Vendor. Support for the framework however is available from us.

# <a name="UnitTest"> </a>Unit Test Templates and CSElement 

## WebCenter Sites does not offer tools for unit testing

Agile programming mandates you write tests for every features you implement.

In addition to unit test, is is important to be able to perform integration test in an automated way.

Unfortunately, with WebCenter Sites templates live inside the CMS and are almost impossible to test:  they can be called only through HTTP and often not directly.  

Templates depends on the current content model so you cannot reproduce their behaviour unless you initialise the whole content in an expected way, that can be a lot time consuming.

## AgileSites offers everything needed to unit test easily.

Since all the templates are implemented in a Java class, they can now be tested outside of the CMS. Furthermore, you can simulate the behaviour of the CMS outside of it using a set of provided mocks.

This makes very easy to write unit test. Furthermore, the framework integrates content impor and the tool selenium. This make easy to write also integration test to be used in [[continuous integration|Feature-Continuous-Integration]].

# <a name="VersionControl"> </a> Version Control your Code 

## WebCenter Sites version controls a CSDT export only

Developers cannot really use a version control system with standard WebCenter Sites since all the templates are stored inside the database and it may required to version control database records, that is not easy (if possible at all).

A limited solution is using internal WebCenter Sites revision tracking, but that is not a real version control system. It is more suitable for revision tracking content, not code. It lacks many of the features of typical VCS, like merging changes from different developers and allowing concurrent development.

Furthermore you do not have a real source code of your templates, since they are actually stored inside the database. Comparing versions is pretty difficult (you have to manually export templates, compare then then compare manually database records storing metadata about the templates).

## AgileSites version control everything, content model as site code and static templates

The framework keeps everything in source form in Java code, including metadata for templates. Developers work entirely outside of the CMS, then they update metadata and deploy code with a single command, without having to restart the application server. The development lifecycle is pretty fast. 

However, since everything is still in source format and not kept outside the CMS, it can be revision controlled, shared, merged and tracked using any standard version control system, either subversion, git, mercurial or proprietary systems.

