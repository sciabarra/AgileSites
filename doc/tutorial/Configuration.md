##### Prev: [Prerequisites](Prerequisites.md)

In this section we introduce the configuration steps required in order to start development.

## Choose a name and a prefix

The first step is naming your site. Here I am assume your choice was **MySite**. Of course you need to change examples accordingly to your decision.

Also you need to choose a prefix that you will use consistently to name specific assets of your sites (most notably  types, content definitions, attributes, typese). 

The prefix is the common conventions used to avoid name clashes. Here I will use the prefix *My*.

**NOTE** We experienced problems when Content Definitions or Attributes have the same name in 2 different sites. So we recommend to use always a prefix when naming Attributes, Content and Parent definitions, in order to guarantee the uniqueness of the names. Templates are also automatically named with a prefix when created by the deployer (see later in this tutorial).

## Configure a new site and a virtual host

Edit the `build.sbt` and put your site name in the `wcsSites` variable. For example:

``
wcsSites in ThisBuild := "MySite"
``

If you have a virtual host configured (with satellite and apache in place) for your new site then you should also configure virtual hosts mapping.

For example if you have a front-end url `http://www.mysite.com` for the site `MySite` you should add a line mapping the site to the 

``
wcsVirtualHosts in ThisBuild += ("MySite" -> "http://www.mysite.com")
``

You can remove others sites but if they were already installed the installer won't remove configurations for them so they still be available.

## Reinstall to update configurations

 After changing the name  of the current site, you **need** to reinstall. Installation is needed in order to create  the appropriate configurations. 

Reinstallation steps:

On Content Server (Sites):

- shut down the application server running Sites,
- execute again the ``wcs-setup-offline`` command
- restart it  
- execute again the ``wcs-setup-online`` command.

On Satellite Server:

- shut down the application server running Sites,
- execute again the ``wcs-setup-offline satellite`` command
- restart it  

Check the [Installation Guide](http://www.agilesites.org/install.html) for more details.


##### Next: [New Site](NewSite.md)