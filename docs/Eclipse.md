# Eclipse #

You need eclipse with Scala Support. So please get an eclipse and install the 
[[Scala IDE|http://www.scala-ide.org]].

Using eclipse for development is pretty easy:

- fist, package core and api (this is required to give eclipse all the required dependencies): 
From sbt:
<br><pre>
core/package
api/package
</pre>

- second, enter in sbt and type:<br>``eclipse``

- finally, open eclipse and import in eclipse the projects in scalawcs. You should finde 3 projects:
	
 - scalawcs-api
 - scalawcs-core
 - scalawcs-app
   

