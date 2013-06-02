---
layout: page
title: Prerequisites
---

##### Prev:  [Tutorial Index](/tutorial.html)

In this section we will see what is needed in order to use this framework.

## Prerequisites 

The framework **requires** you develop in your local machine with a local installation of Sites.  You are expected to share code and content model through a version control system. 

However this is not a limitation, since you can easily install them on your system..

The JumpStartKit (a version of Sites easy to install, with its own app server and database) is available from Oracle Support to customers. 

It is convenient because it does not requires too many resources and can be installed in a developer machine. It includes everything can be needed for development.

**NOTE** It is possible to [install the full Sites in your computer](http://www.sciabarra.com/fatwire/2012/04/09/download-and-install-a-development-fatwire-instance-also-on-mac/) for development purposes. You can install it with free Oracle XE or  the free Microsoft SQLServer. It is even possible to install it with the simple, in-memory database Hypersonic SQL  (note, only using **version 1.8.0**) 

Once you installed Sites, follows the [installation procedure](Install.html) to install AgileSites. You do not require the installation of the demo site, although it is convenient to have it installed during development.

## A local instance is required

We stress here that the framework assumes you are going (and willing) to use a local installation of Sites that is unique to each developer.

It is opinion of the author that working on a shared instance is against good development practices, so the framework is assuming that you may not want to do this.

If your habit is developing directly on the server, on a shared development instance, using Sites Explorer and you want to keep working that way, then AgileSites is probably not fit for your way of working. 

##### Next:  [Configuration](Configuration.html)
