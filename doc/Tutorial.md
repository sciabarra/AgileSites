# Tutorial

This tutorial teach you how to use AgileSites as a step-by-step guide to create a new site.

## Prerequisites

The framework **requires** you develop in your local machine with a local installation of Sites and share code and content model through a version control system. 

However this is not a limitation. The JumpStartKit (a locally installable version of Sites) is available from Oracle support to customers. It is convenient because it is very easy to install and includes all you need for development.

Furthermore it is possible to [install Sites in your computer](http://www.sciabarra.com/fatwire/2012/04/09/download-and-install-a-development-fatwire-instance-also-on-mac/) for development purposes. You can install it without Oracle or you may also use the free Oracle XE.

If your habit is to develop on a shared instance and you want to keep working that way, then AgileSites is probably not fit for your way of working.

Once done, follows the [installation](Install.md) steps. You do not require the demo site, although it is convenient to have it installed during development.

## Configure your site

The first step is naming your site. Here I am assume your choice was *MySite*. Of course change accordingly to your decision.

Also you need to choose a prefix that you will use consistently to name specific assets of your sites (most notably  types, content definitions, attributes, typese). 

The prefix is the common conventions used to avoid name clashes. Here I will use the prefix *My*.

**Important** We experienced problems when Content Definitions or Attributes have the same name in 2 different sites. Although this should be not a problem, in practice it is so we recommend to use always a prefix when naming Attributes, Content and Parent definitions. Templates are also automatically named with a prefix.

Now edit the `build.sbt` and put your site name in the `wcsSites` variable. For example:

``
wcsSites in ThisBuild := "MySite"
``

**IMPORTANT** After changing the name of the current site, you **must** reinstall. This means:

- shut down the application server running Sites,
- execute again the ``wcs-setup-offline`` command
-  restart it  
-  execute the ``wcs-setup-online`` command.


If you have a virtual host configured (with satellite and apache in place) for your new site then you should also configure virtual hosts mapping, but for now you can leave out this step.


**NOTE** This variable is actually a comma separated list of sites name, however the list of sites instead of a single site should be used only for tightly couples sites sharing assets.


## Create a site in Sites

Access to the Sites admin and create a new site with the choosen name (in this case *MySite*) , enabling the following assets:

- Template
- CSElement
- SiteEntry
- AttrTypes
- Page
- PageAttribute
- PageDefinitions

After creating the site, you need also to create an user and assign to that user at the minimum the following roles for your site *MySite*

- AdvancedUser
- SitesUser
- GeneralAdmin

This way you can log out and then login again and select your site as the active site.

## Create a site

``wcs-generate site``

Then deploy it on sites

``wcs-deploy``

Accessing the site with 

> http://localhost:8080/cs/Satellite/mysite

You should see "Error, Home Page not found"

This is normal since you do not have yet any page.

## Run Tests

>http://localhost:8080/cs/ContentServer?pagename=MySite/MyTester

Run All tests.

##  Create the home page

In the Admin, create a content definition, named Content

In the Contributor, create a Page, named Home, with description Home Page

Create a template for the home page, and deploy it

``wcs-generate template
wcs-deploy``


