<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="user" uri="futuretense_cs/user.tld"
%><%@ page import="org.apache.log4j.*,org.apache.log4j.net.*,java.util.*"
%><cs:ftcs>
<user:login 
 username='<%= ics.GetVar("username") %>' 
 password='<%= ics.GetVar("password") %>'/><% 

String name = "";
String op = (String)ics.GetVar("op");
Level level = Level.INFO;

Map<String, String> map = (Map<String,String>)application.getAttribute("logmap");
if(map==null) map = new HashMap<String,String>();

if(ics.UserIsMember("xceladmin,xceleditor") && op!=null) {
	
	Logger logger;
	
	if(ics.GetVar("logger")==null) {
		ics.SetVar("logger", "/");
	}
	
	if(ics.GetVar("logger").equals("/")) {
		 logger=Logger.getRootLogger();
		 name= "/";
	} else {
		logger = Logger.getLogger(ics.GetVar("logger"));
		name = ics.GetVar("logger");
	}
	
	if(ics.GetVar("level")!=null) {
		level = Level.toLevel(ics.GetVar("level"), Level.DEBUG);
	}
			
	String host = (String)ics.GetVar("host");
	if(host==null) host="127.0.0.1";
	int port = 4445;
	try { port = Integer.parseInt((String)ics.GetVar("port")); }
	catch(Exception ex) { }
		
	if(op.equals("start")) {
		Appender appender = new SocketAppender(host, port);
		appender.setName(name);
		logger.removeAppender(name);		
		logger.addAppender(appender);
		logger.setLevel(level);
		map.put(name, level+"@"+host+":"+port);
	    %><%= op+" "+name + " at "+level+"@"+host+":"+port %><%
	}
	
	if(op.equals("stop")) {
		logger.removeAppender(name);
		map.remove(name);
		%><%= op+" "+name %><%
	}
	
	if(op.equals("list")) {
	  if(!map.isEmpty())
		for (Map.Entry<String,String> entry: map.entrySet()) {
			%><%= entry.getKey() +" "+ entry.getValue()+"\n" %><%
		}
	  else { %>No logger enabled<% }
	}
	application.setAttribute("logmap", map);
} else { %>login error<% } %></cs:ftcs>