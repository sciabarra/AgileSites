<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>JNDI Lookup</title></head>
<body>
Checking csDataSource:
<%
    try
    {
        javax.naming.Context ctx 
           = new javax.naming.InitialContext();
        javax.sql.DataSource ds 
           = (javax.sql.DataSource)ctx
            .lookup("java:/comp/env/csDataSource");
        out.println(ds.toString());
        java.sql.Connection con = ds.getConnection();
        out.println(con.toString());
        con.close();
    }
    catch (Exception e)
    {
        out.println(e);
    }
%>
</body>
</html>