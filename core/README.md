To build the core you need a local install of Sites. JumpStartKit is just fine for this purpose.

Please build it using the appropriate script, passing to it the path of the cs webapp within the JSK

For example:

- build118 <JSK_FOLDER>\App_Server\apache-tomcat-7.0.42\Sites\webapps\cs for 11.1.1.8.0
- build116 <JSK_FOLDER>\App_Server\apache-tomcat-6.0.32\webapps\cs for 11.1.1.6.1
- build762 <JSK_FOLDER>\App_Server\apache-tomcat-6.0.32\webapps\cs for 7.6.2

Core is then built and stored in the local maven repository.

