# <a name="API"></a>Simpler API

## Problem

Fatwire/WCS api is pretty old, sometimes ugly and inconsistent. Furthermore is it offering too many options that are confusing and are not guiding to follow best practices.

## Solution

A new, simplified, consistent API is offered. This API hide many of the complexities of the Fawire API, like the different way to access basic and flex attributes. The goal is to provide 80% of the functionalities with  20% of the complexity.

Furthermore, all the existing Fatwire is  kept accessible and available, and can be used if required.

# <a name="Deploy"></a>Deployment Hell 
 
## Problem

A Fatwire website is tipically  very complex to deploy, since it is comprised of many different components: library jars, customization jars, css and javascript files, CSElements, Templates, database tables and some content mandatory

## Solution

The system is provided from the ground up with an automated build system that performs all the step for the installation and update. Everything is kept in source form and can be deployed in an unchanged blank system.

A whole site can be install a new system with 2 commands:

- wcs-setup-offlinet before starting the application server, 
- wcs-setup-online after starting the application server

An existing install system can be updated with a single command (wcs-deploy) without having to restart the application server.

# Deployment Hell {Compatible} 

## Problem

Rarely a project exists in a vacuum, so if you decide to give a try to this framework, you want to be able to keep your existing work and not having to rewrite everything.

## Solution

The framework is totally compatible with any existing project. It implements template logic in Java, but templates are called by standard JSP, so you can freely mix framework managed templates with existing templates. 

You can even write templates partially in JSP and partially in Java using the entire hot deploy infrastructure of the framework.

# Continuous Integation {CI} 


## Problem


Continuos Integration is one the key practices in Agile development.

Every developer should work in isolation in his own machine but when many developers are working at the same time, the full test suite must be run against the development server.

Because of the limitations of the CMS, usually developers share a common development environment and rely on this for working as a team. This practice has a lot of shortcomings, including down of the shared environment because of some mistakes, conflict of accessing to the same template or definition , inability to work without a working internet connection and so on.

## Solution

The entire framework is built in order to enable Agile development practices.

Since building the site is pretty easy from a single source, every developer can get a development environment working in his local machine quickly and easily, so there is no need to share a development environment.

Since there is a build and writing tests is easy, and a version control system can be used, every developer have just to commit his changes to the version control system

Using a standard Continuous Integration server like Jenkins is just a matter of:
- installing a Jump Start Kit on the integration server and perform the first install
- configure Jenkins it to checkout the code and deploy it on the server
- run the tests (both unit test coming from the developers and integration test coming from tester) using the provided build tool and distribuite the results to developers through a mailing list.



# Do not restart the Application Server {HotDeploy} 

## Problem

Coding in JSP with Fatwire/WCS has the advantage that you can see immediately the result without restarting the application server. Traditionally, when coding in Java instead  a major hassle is the requirement to restart the application server to see the code change. 

## Solution

The framework features hot deploy as a key feature. After any code change, a jar is automatically built and copied in place, and the code is picked up immediately. No need to restart anything.

So you have the advantage of being able to use full java without losing the immediate gratification of seeing the result without any delay that usually breaks the development flow and is a productivity killer.

# Pick your preferred IDE {IDE} 

## Problem

The choice of development tool usable with Fatwire has been traditionally very limited. Traditionally you have to use the windows-only and limited Content Server Explorer. More recently a few plugins has been made available, all of them Eclipse based, including the CSDT tool.

But if you want to use another IDE, or just Eclipse without a plugin (that may be unavailable or incompatibile with your eclipse flavor) you are out of luck.

## Solution

The application built with AgileWCS is a completely standard Java application not requiring any plugin to be edited: just add the relevant jars in the classpath, and a tool to generate the correct classpath and download them from repositories is included.

As a result, nothing else but any java IDE is required, and this can be Eclipse without plugins, NetBeans, Idea or even nothing at all. Compilation, packaging and deploying is included by a command line tool that is part of the framework.

# Move Away from JSP {NoJSP} 

## Problem

WCS/Fatwire require developers implement templates either using JSP or its proprietary XML based language. 

JSP are great for implementing simple rendering logic, and they dynamic, since they are recompiled on the fly. Developers enjoy instant gratifications seeing immediately the results of their efforts.

With those advantages comes many disadvantages. The problem with JSP is that they are really meant to implement **simple** rendering logic *ONLY*. A normal Java Web Applications uses also servlet as controllers, and very often MVC frameworks like Struts or Spring MVC to better organize the code. 

In WCS/Fatwire unfortunately JSP is the only mean to develop code, unless you write an extensions jar to add to the webapp, then you deploy it and restart the application server.

Since restarting the application server is slow, most developers  end up writing complex logic directly in the JSP, writing JSP that are almost all comprised of Java code. This  practice has many shortcomings.

First, JSP cannot share code. You cannot define for example an utility method in a JSP and call it in another jsp.

You can use a JSP in Fatwire/WCS as a CSElement and call it from another JSP, but they are very cumbersome to use.

Alternatively, you can write your code in a Java class, deploy it in a separate jar, then restart the application server before using it. This is very cumbersome in the development process.

Second, the syntax of JSP is frankly ugly and unreadable. Indentation is very difficult, and often you cannot indent at all to avoid generating tons unindented HTML (that must required to be readable too). 

Third, Fatwire/WCS is complicating things because the API can often be invoked only through the tag, so you end up switching from java to html and back only to call an API through a tag.

Many projects end up with a lot of Java code inside a JSP, with a lot of code copied and pasted between JSP, getting a large, unmanageable amount of spaghetti code with lot of repetitions. 

This is not entirely a Fatwire/WCS fault but definitely the limitations of JSP encourage those bad practices.

## Solution

The fundamental feature of this framework is allowing developers to write templates directly in Java, and providing APIs to render the view  from an unchanged HTML (the typical HTML that you receive from web designers). 

Since accessing to the content model in Fatwire/WCS is usually done using JSP tags, a specific API has been developed to provide easy invocation of  JSP tags directly in Java.

To keep the Agility of development using JSP, all your code is stored in a jar that is dinamically redeployed when you change it, so there is no need to restart the application server. You keep the instant gratification of JSP, but you can write ordered, clean Java code, splitting it in multiple classes and reusing code without limite call-elements.

You have all the Java power at your finger tips. However, templates are still templates, and they are invoked by a JSP. The stub code to generate the JSP is managed automatically by the framework so you don't have to worry of it.


# MVC Pattern {MVC} 

## Problem

A common problem in Fatwire/WCS development is that templates do not implement a Model-View-Controller pattern.

Instead, a template is just a JSP where you mix  the rendering logic (view), the content reading logic (model) and some specific business logic (control)

## Solution

The AgileWCS framework enables you writing your code in Java, not in JSP.

Each template now has a corresponding class that acts as a controller. 

A specific API to define the content (model) and retrieve it, is provided, separating the model from the controller.

Furthermore, also the view is kept separated, and [[in its original form|Feature-Original-HTML]]: the view is indeed the original HTML as provided by web designers.

Developers write all the template code in plain Java, implementing a controller that collect data from the model and place it in the view as needed.

See this [[sample in Java|Feature-Sample-Java]] or [[this sample in Scala|Feature-Sample-Scala]].
## Problem

In any large web development project, web designer are different people from web developers, since different skills are required.

Usually Web Designer deliever their work as a set of html/css/javascript files that must then used by web developers to implement the web site in the CMS. 

Major problems arises when Web Designers update the web design, since at every update developer have to track down html changes and update code following the changes in the design.

## Solution

The framework allows developer to use unchanged original HTML files. A specific API, using jquery-like CSS based selectors allow to pick specific pieces of html, replace with content and render them without having to mix code and HTML. This way web designer can freely update the html and the CSS and, as long as id and classes are kept unchanged, there is no need to change the business logic code.


# Single Jar Deploy {Jar}

## Problem

Deploying code though publishing templates is error prone, does not capture real dependencies in code and is very hard to track.

## Solution

The deployment of a web site is a single jar containing both source code and html resources. The jar is automatically packaged and deployed just copying it on his final destinations. Changes are picked when the jar is deployed.

Since all the code is a single jar, deploying a site is only a matter of copying a jar in place, and it is possible to compare the complete code deployed for a site just comparing the deployed jar.

# Keep Oracle Support {Support}

## Problem

If you pick the framework, you may want to keep getting support from the vendor, and using this framework should not be a reason to lose support. Furthermore you want to use all the available feature of Fatwire/WCS  without being limited in any way.

## Solution

AgileWCS does not change Fatwire/WCS in any way. Templates are still invoked using JSP, and JSP code is standard Java code, so nothing is different from standard practices. 

The entire hot deploy api is actually a standard feature of Java that exists since Java 1.0: it is implemented using the standard class loader used for applets. Everything else is provided either with java classes that extends existing api (but don't change it) and a standard external build tool that packges your code and deploy it. 

Finally, the asset model update is implemented using standard Fatwire/WCS Elements and SiteEntries, invoked through the Catalog Manager.

In conclusion, all the framework is a layer built on top of standard Fatwire/WCS and nothing is "invalidating your guarantee", except you may not ask support for the specific framework to the Vendor. Support for the framework however is available from us.

# Unit Test Templates and CSElement {UnitTest}

## Problem

Agile programming mandates you write tests for every features you implement.

In addition to unit test, is is important to be able to perform integration test in an automated way.

Unfortunately, with Fatwire/WCS templates live inside the CMS and are almost impossible to test:  they can be called only through HTTP and often not directly.  

Templates depends on the current content model so you cannot reproduce their behaviour unless you initialise the whole content in an expected way, that can be a lot time consuming.

## Solution

Since all the templates are implemented in a Java class, they can now be tested outside of the CMS. Furthermore, you can simulate the behaviour of the CMS outside of it using a set of provided mocks.

This makes very easy to write unit test. Furthermore, the framework integrates content impor and the tool selenium. This make easy to write also integration test to be used in [[continuous integration|Feature-Continuous-Integration]].

# Version Control your Code {VersionControl}

## Problem

Developers cannot really use a version control system with standard Fatwire/WCS since all the templates are stored inside the database and it may required to version control database records, that is not easy (if possible at all).

A limited solution is using internal Fatwire/WCS revision tracking, but that is not a real version control system. It is more suitable for revision tracking content, not code. It lacks many of the features of typical VCS, like merging changes from different developers and allowing concurrent development.

Furthermore you do not have a real source code of your templates, since they are actually stored inside the database. Comparing versions is pretty difficult (you have to manually export templates, compare then then compare manually database records storing metadata about the templates).

## Solution

The framework keeps everything in source form in Java code, including metadata for templates. Developers work entirely outside of the CMS, then they update metadata and deploy code with a single command, without having to restart the application server. The development lifecycle is pretty fast. 

However, since everything is still in source format and not kept outside the CMS, it can be revision controlled, shared, merged and tracked using any standard version control system, either subversion, git, mercurial or proprietary systems.

