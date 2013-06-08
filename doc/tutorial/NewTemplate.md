##### Prev:  [New Site](NewSite.md).

In the previous section we created the skeleton of the site. There is no content in it, so if you try to access to any page you will get, of course, page not found.

We are now ready to define the content model then create some content to render a home page.


## Content Model: Page Definitions 

A site is organized as a hierarchy of Pages. Typically you have the Home Page, then some Section Pages, sometimes also subsectiions, and finally content pages.

This means there are different types of Pages, and each type has usually a different collection of data associated to it.

In Sites, we model the concept that Pages can havebe of different type  creating a Page definition.

A Page definition is technically a subtype of the Page asset type, while Page is the asset type. You define an asset subtype of pages creating an instance of another asset type, the Page Definition.

We will see later that each page definition has also an associated set of attributes that defines the content specific for each Page subtype. For now, we will create a new Page defintion, one specific for Home Page.


### Creating the MyHome content definition

Switch to the Administrative interface, clicking on the icon shown below in the application bar:

![Admin](../img/snap2093.png)

Click on the **New** link in the toolbar, and then finally click on the **New Page Definition** link in the list that appears.


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


## Continuos compilations

``~wcs-package-jar``


##### Next: [Java Idiom](JavaIdiom.md)






