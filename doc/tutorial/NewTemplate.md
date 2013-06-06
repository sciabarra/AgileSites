##### Prev:  [New Site](NewSite.md).

We have now the skeleton of the site.

We are ready to render the home page.

## Continuos compilations

``~wcs-package-jar``


## Create the content Model for the home page

![New Page Definition](../img/snap1821.png)

Create `MyHome` page definition: 

![My Home](../img/snap6106.png)

## Generate and deploy layout

`wcs-generate layout`

- subtype: MyHome
- site: MySite
- type: Page
- prefix: (leave empty)

**NOTE: if your subtype has a prefix (``MyHome``) then it is recommended to leave  the prefix empty to avoid ugly names like 'MyMyHomeLayout`.

`wcs-deploy`


##  Create the home page

New Page

Give `Home` as name and select the (only) available template `MyHomeLayout`

![New Layout](../img/snap6677.png)

Verify that in meta data the defintion is `MyHome`

![Page Definition is MyHome](../img/snap5365.png)

##### Next: [Java Idiom](JavaIdiom.md)






