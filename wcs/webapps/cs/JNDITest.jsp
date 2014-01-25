<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>JNDI Lookup</title></head>
<body>
<%
String jndiName = request.getParameter("jndiName");
String prefill = "";
if (jndiName == null)
{
    jndiName = "";
}
else
{
    prefill = jndiName;
}
%>
<p>Enter a JNDI name to lookup:</p>
<form method="post" action="<%= request.getRequestURI()%>">
    <p>
    JNDI Name: <input type="text" name="jndiName" value="<%= jndiName %>" size="40"/>
    <input type="submit" value="Submit"/>
    </p>
</form>
<%
if (jndiName.trim().length() != 0)
{
    try
    {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        out.println(ctx.lookup(jndiName));
    }
    catch (Exception e)
    {
        out.println(e);
    }
}
%>
</body>
</html>