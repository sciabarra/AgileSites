<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>JNDI Lookup</title></head>
<body>
Checking csDataSource:
<%
    try
    {
        javax.naming.Context ctx 
           = new javax.naming.InitialContext();
        javax.naming.Context envContext  
           = (javax.naming.Context)ctx.lookup("java:/comp/env");
        javax.sql.DataSource ds 
           = (javax.sql.DataSource)envContext
            .lookup("jdbc/csDataSource");
        out.println(ds.toString());
    }
    catch (Exception e)
    {
        out.println(e);
    }
%>
</body>
</html>