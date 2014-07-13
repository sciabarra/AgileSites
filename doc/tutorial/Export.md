##### Prev: [Navigation](Navigation.md) - Next:  [Tutorial Index](/tutorial.html)

Once you have defined your content model, you can export it and save an xml representation on the file system.

The export format is good for being placed in a revision control system, from where it can be imported back.

In general if you want to export your site, you will do better first to create a new workspace, for example:

```
wcs-dt mkws MySite-11.8
```

Then you can export everything with 

```
wcs-dt export #MySite-11.8 !MySite @ALL
```

Where `#MySite-11.8` is the workspace name (using the '#' prefix) and `MySite` is the site name (using `!` prefix to specify it). 

The site is by default the first site in your `wcsSites` variable, so you can omit the site if you only want to export the site where you are working.

As a shortcut, the `#` search for a workspace with the specified string as a substring.

You do not have to specify all the assets for export. For example it is pretty useful to specify only the sites of a give type. For example exporting 'Page' of the current site you can do just:

```
wcs-dt export #My Page
```

This will export all the pages of the current work site in the workspace containing the string 'My' in the name. 

# Importing

After export, everything will be stored in the folder `export/envision/<workspace>` and can then be put under revision control.

You can then reimport it in your local environment or on a live site with the command:

```
wcs-dt import #MySite !MySite @ALL
```
