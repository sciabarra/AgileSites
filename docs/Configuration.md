# Configuration

Edit ``build.sbt`` and set

- ``wcsHome in ThisBuild`` to the home WebCenter Sites application directory<br>
- ``wcsWebapp in ThisBuild`` to the web application folder
- ``wcsVersion in ThisBuild`` is the version of your Fatwire/WCS (currently ONLY `7.6` or `11g`)
- ``wcsCsdtJar in ThisBuild``  to the location of your CSDT jar
<br>Position may vary, in JSK it is located in wcsHome folder, in other installations it may be in a subdirectory.
- ``wcsSite in ThisBuild`` to the site you are currently working on
- ``wcsUrl in ThisBuild`` to the url to access content server, slash terminated.
<br>Include the protocol (``http`` or ``https``) the port and the path
- ``wcsUser in ThisBuild`` - username of an user that can read/write your site
- ``wcsPassword in ThisBuild`` password of an user that can read/write your site

**NOTE** Separate settings with a blank line and not add a ';' to the end.
Comments here starts with `//` not with `#`.

Example

<pre>
// home directory of WCS
wcsHome in ThisBuild := "C:/Users/user/JSK/ContentServer/11.1.1.6.0/"

// webapp directory of WCS
wcsWebapp in ThisBuild := "C:/Users/user/JSK/App_Server/apache-tomcat-6.0.32/webapps/cs/"

// location of the csdt-client jar
wcsCsdtJar in ThisBuild := "C:/Users/user/JSK/ContentServer/11.1.1.6.0/csdt-client-1.2.jar"

// site to import/export
wcsSite in ThisBuild := "App"

//url to content server
wcsUrl in ThisBuild := "http://localhost:8080/cs/"

// user to import/export
wcsUser in ThisBuild := "fwadmin"

// password to import/export
wcsPassword in ThisBuild := "xceladmin"
</pre>

Now [[setup|Setup]] ScalaWCS in your JSK WCS (in development mode) or in your live site (in production mode)
