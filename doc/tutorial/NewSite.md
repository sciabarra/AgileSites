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
