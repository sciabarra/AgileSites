

**NOTE** For version 1.0, not yet updated to 1.8

 `wcsSites in ThisBuild` 

Names of the  sites you are currently working on with AgileSites. It  is a comma-separated list of site names.

`wcsVersion in ThisBuild` 

to the version of your Fatwire/WCS. Currently it can 7.6 or 11g.

`wcsHome in ThisBuild`

Points to the home WebCenter Sites application directory

`wcsShared in ThisBuild`

Points to the WebCenter Sites  shared folder 

`wcsWebapp in ThisBuild`
 
Points to the web application folder of the Content Server  application (usually named cs)

`wcsCsdtJar in ThisBuild`

Points to the location of your CSDT jar. 
Position of this file may vary, in JSK it is located in 

wcsHome folder, in the full installations it is usually stored in a subdirectory.

It is recommended to download the latest patch from the support site where the latest jar can be found.

`wcsUrl in ThisBuild` 

Points to the url to access content server.

Please DO NOT include a slash at the end.
Include the protocol (http or https), the port and the  path to ContentServer (for example /cs ).

This url must  expose Content Server servlets (ContentServer, CatalogManager), so it is generally available only in development (on live site, the /cs/Satellite/site prefix is added by an Apache Proxy so the '/cs/' is inaccessible)


TODO document other assets

