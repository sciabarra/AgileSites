(Draft)


 `wcsSites in ThisBuild` 

Names of the  sites you are currently working on with AgileSites. It  is a comma-separated list of site names.

`wcsVersion in ThisBuild` 

to the version of your Fatwire/WCS. Currently it can 7.6 or 11g.

`wcsHome in ThisBuild`

Points to the home WebCenter Sites application directory

`wcsShared in ThisBuild`

Points to the WebCenter Sites  shared folder 

`wcsCsWebapp in ThisBuild`
 
Points to the web application folder of the Content Server  application (usually named cs)

`wcsSsWebapp in ThisBuild`

to the web application folder of the Satellite Server  application (usually named ss)

`wcsCsdtJar in ThisBuild`

to the location of your CSDT jar. 
Position of this file may vary, in JSK it is located in 

wcsHome folder, in the full installations it is usually stored in a subdirectory.

It is recommended to download the latest patch from the support site where the latest jar can be found.

`wcsUrl in ThisBuild` 

to the url to access content server.
Please DO NOT include a slash at the end.
Include the protocol (http or https), the port and the  path to ContentServer (for example /cs ). This url must  expose Content Server servlets (ContentServer, CatalogManager)

9. wcsUser in ThisBuild 
is the username of an user with developers privileges for 
your sites

10.  wcsPassword in ThisBuild 
is the password of the wcsUser.
The following settings requires a reinstallation if changed 
since those settings are stored in the code.

11.  wcsFrontUrls in ThisBuild 
is a sequence of associations site to the entry point url for 
each website. See TODO for a discussion of the possible 
values. 

12.  wcsFlexBlobs in ThisBuild 
Regular expression to match blobs stored in Flex Attributes 
–matching a numeric id is required to locate a flex blob. 
It is important to tweak the extensions matched to include 
those used in the site. See TODO for a discussion of the 
subject.

13.  wcsStaticBlobs in ThisBuild 
Regular expression to match blobs stored as static assets. 
Their name generally matches the filename of the static 
asset itself. It is important to tweak the extensions matched 
to include those used in the site. See TODO for a discussion 
of the subject
