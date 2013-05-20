Tutorial Prev: [Prerequisites](Prerequisites.md)

----

# Choose a name and a prefix


The first step is naming your site. Here I am assume your choice was **MySite**. Of course you need to change examples accordingly to your decision.

Also you need to choose a prefix that you will use consistently to name specific assets of your sites (most notably  types, content definitions, attributes, typese). 

The prefix is the common conventions used to avoid name clashes. Here I will use the prefix *My*.


**NOTE** We experienced problems when Content Definitions or Attributes have the same name in 2 different sites.

Although this should be not a problem, in practice it is so we recommend to use always a prefix when naming Attributes, Content and Parent definitions. Templates are also automatically named with a prefix but the deployer.

# Configure a new site

Edit the `build.sbt` and put your site name in the `wcsSites` variable. For example:

``
wcsSites in ThisBuild := "MySite"
``

**NOTE** After changing the name of the current site, you **must** reinstall. This means:

- shut down the application server running Sites,
- execute again the ``wcs-setup-offline`` command
- restart it  
- execute the ``wcs-setup-online`` command.

If you have a virtual host configured (with satellite and apache in place) for your new site then you should also configure virtual hosts mapping, but for now you can leave out this step.


**NOTE** The variable `wcsSites` variable is actually a comma separated list of sites name. However you should use more than one site only when you are using tightly coupled sites sharing assets. They are exported all together by CSDT.

----

Tutorial Next: [New Site](NewSite.md)
