# Tutorial

(work in progress - still an incomplete page)

You need to start

## Configure your site

Before anything, decide for your site name. Here I am assume MySite

Then

- edit build.sbt and put your site in the comma separated list of sites in wcsBuild 

## Create a site in Sites

Access to the admin, create a new site, enable those assets:

- Template
- CSElement
- Page
- PageAttribute
- PageDefinitions

## Create a site

``wcs-generate site``

Then deploy it on sites

``wcs-deploy``

Accessing the site with 

http://localhost:8080/cs/Satellite/mysite

You should see "Error, Home Page not found"

This is normal since you do not have yet any pages

## Run Tests

http://localhost:8080/cs/ContentServer?pagename=MySite/MyTester

Run All tests.

##  Create the home page

In the Admin, create a content definition, named Content

In the Contributor, create a Page, named Home, with description Home Page

Create a template for the home page, and deploy it

``wcs-generate template
wcs-deploy``


