---
layout: page
title: NewSite
---
##### Prev:  [Configuration](Configuration.html)

## Create a site in Sites

After the configuration and the installation of the configuration, you can now create a new site.

Access to the Sites admin (see the image below).

![Admin Site Creation](/img/snap8769.png)

Create a new site with the chosen name (in this case *MySite*), enabling at least the following assets:

- Template
- CSElement
- SiteEntry
- AttrTypes
- Page
- PageAttribute
- PageDefinitions

![Asset Type enabling](/img/snap4206.png)

After creating the site, you need also to create an user and assign to that user at the minimum the following roles for your site *MySite*

- AdvancedUser
- SitesUser
- GeneralAdmin

![Roles enabling for the new site](/img/snap5044.png)

Once done you need to log out and then log in again to select your new site as the active site.

## Create the basic code of the site with the generator

Once created the site in Sites, you can use generate the site code from the shell with the command:

``wcs-generate site``

A popup will appear asking for the site name (use `MySite` or your own) and the site prefix (user `My` or your own).

Then you can deploy 

``wcs-deploy``

# Access the site and the tests

Accessing the site with 

> http://localhost:8080/cs/Satellite/mysite

You should see the following screenshot:

![Default Error Page](/img/snap3695.png)

This is normal since you do not have yet any page, so the Error Page is shown declaring it cannot find the home pages

You can then invoker the tester with

>http://localhost:8080/cs/ContentServer?pagename=MyTester

You should see this screenshot:

![Tester](/img/snap2246.png)

Clicking on "Run All Tests" you should expect no errors:

![Run All Tests](/img/snap2543.png)

You can also select a few tests and run only the selected tests clicking on "Run Some Tests". Try it.

## Import in eclipse

**Note**: you need the eclipse plugin [http://www.scala-ide.org](http://www.scala-ide.org "Scala IDE") to use the default export. Projects are currently Java only, but planned extensions of the framework  will allow also Scala coding, so this requirement is not going to be removed. 
 
Once the new site and the new project has been created you can generate configuration files for your IDE. Execute the command:

`eclipse`

In the shell. Using eclipse you can select the following sequence of options to import the projects in Eclipse:

> File | Import | General | Existing Projects 

and select the main directory of AgileSites. You should see the following screenshot:

![Import AgileSites projects](/img/snap4673.png)

You will actually work only on the `agilesites-app` project. 

#####  Next:  [New Template](NewTemplate.html)