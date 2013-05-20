##### Prev:  [Configuration](Configuration.md)

## Create a site in Sites

After the configuration and the installation of the configuration, you can now create a new site.

Access to the Sites admin (see the image below).

![Admin Site Creation](../img/snap8769.png)

Create a new site with the chosen name (in this case *MySite*), enabling at least the following assets:

- Template
- CSElement
- SiteEntry
- AttrTypes
- Page
- PageAttribute
- PageDefinitions

![Asset Type enabling](../img/snap4206.png)

After creating the site, you need also to create an user and assign to that user at the minimum the following roles for your site *MySite*

- AdvancedUser
- SitesUser
- GeneralAdmin

![Roles enabling for the new site](../img/snap5044.png)

Once done you need to log out and then log in again to select your new site as the active site.

## Create the site code with the generator

Once created the site in Sites, you can use generate the site code from the shell with the command:

``wcs-generate site``

A popup will appear asking for the site name (use `MySite` or your own) and the site prefix (user `My` or your own).

Then you can deploy 

``wcs-deploy``

# Access the site and the tests

Accessing the site with 

> http://localhost:8080/cs/Satellite/mysite

You should see "Error, Home Page not found"

This is normal since you do not have yet any page.

## Run Tests

>http://localhost:8080/cs/ContentServer?pagename=MySite/MyTester

Run All tests.

#####  Next:  [New Template](NewTemplate.md)

