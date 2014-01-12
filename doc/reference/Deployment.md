AgileSites supports front end with an Apache reverse proxy. It works also with other reverse proxy.

This document describe this setup: 

You have a frontend site `www.mysite.com` and you want to have friendly urls in the form `http://www.mysite.com/friendlyurl`.

Note that AgileSites assembler uses `http://localhost:8080/cs/Satellite`, so you need a reverse proxy that will replace the real frontend site with the internal pathname

For this purpose you need to configure apache for doing the forwarding, then deploy the AgileSites assember in the Satellite configuring it with this forwardings.

Basically you have to map the external url to the internal site name, with this configuration:

```
wcsVirtualHosts += ('MySite' -> 'http://www.mysite.com')
```

Read on for some more details.

# Front End: Apache 

This configuration is a sample only.

```
<VirtualHost *:80>
  ServerName www.mysite.com
  ProxyPass / http://satellite:8080/ss/Satellite/mysite
  ProxyPassReverse /
 </VirtualHost>
```

# Front End: Satellite

Download the agilesites in your installation host.

Copy the development `build.sbt` or create one with the configurator.

The only special configuration needed is the following:

```
wcsVirtualHosts += ('MySite' -> 'http://www.mysite.com')
```

Ensure your site is listed in the `wcsSites`

```
wcsSites in ThisBuild := "MySite,OtherSites"
```

Ensure that the wcsWebapp points to the satellite webapp:

```
wcsWebapp in ThisBuild := "/path/to/satellite/ss"
```

Shut down satellite, then install in each satellite with the command 

```
wcs-satellite-setup
```

Restart satellite.