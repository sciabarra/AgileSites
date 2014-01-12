# Agile Shell

Reference of the commands available the shell.

## setup commands

### wcs-setup 

Perform the offline setup

### wcs-setup-satellite

Perform the setup on satellite

### wcs-deploy

Perform the online setup and deployment

## compilation

Main compilation method:

### wcs-package-jar

to be used in continuos mode (`~ wcs-package-jar`)

### wcs-copy-static

copy files from the `app/src/main/static` in the application folder

### wcs-update-model

update the content model - currently it deploys Templates, CSElemements and SiteEntries.

### wcs-deploy

This command perform in a single shot `wcs-package-jar`, `wcs-copy-static` and `wcs-update-model`. 

Also it upload the special elements required by AgileSites. It does only once.

## log

### wcs-log view

Show a new windows for logging. You must create a new client window for receiving the log. Select on the menu "client", using localhost and 4445 (the default values). You can create multiple windows but you need to specify different ports. You can then direct outputs to different ports, see below.

### wcs-log list

This command list the currently active log appenders (corresponding to the packages you are tracing)

### wcs-log trace mysite

This command enable tracing for the package `mysite`. Tracing will go in the client running in localhost listening in port `4445`

### wcs-log trace wcs 4444

This command enable tracing for the package `wcs`, and send the output in localhost to a client listening in port `4444`.

### wcg-log `<level>` `<appender>` [`<port>`]

This is the general form. `<level>` is the trace leve (one of trace, debug, info, warn, error), the `<appender>` is a class or package name and the `<port>` is a number.

## csdt

The shell integrates CSDT

The general format is:

```
wcs-dt  [<cmd>]  [<selector> ...] [#<workspace>] [!<sites>]```

You can then execute one of the available csdt commands, which are:

- `listcs`: list assets using ids to identify them
- `listds`: list assets using uids to identify them
- `import`: import assets 
- `export`: export assets

Each command will act on a site and on a workspace, and will select a set of assets using a selector

The `<workspace>` can be omitted and it is by default `cs_workspace`, but you can specify a different workspace with `#<workspace>`. Note that the syntax will search for a workspace that contains `<workspace>` as a substring (so you do not have to specify a long workspace name).

The site can be omitted and defaults to the first site specified in the `wcsSites` variable. You can specify a different site with the syntax `!<site>`.

The shell has also an additional command  `mkws`  that create a new workspace.

The `<selector>` is specified in the developer tools documentation. As a reminder they have `<AssetType>[:<id>]` or a special form starting with `@`. Recognized special forms are @SITE @ASSET_TYPE @ALL_ASSETS @STARTMENU @TREETAB.

There is also the additional @ALL as a shortcurt for all of them.
