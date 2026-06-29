<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.Properties, java.io.*" %>
<%
    Properties dbProps = new Properties();
    boolean propsLoaded = false;
    try (InputStream is = application.getResourceAsStream("/WEB-INF/db.properties")) {
        if (is != null) { dbProps.load(is); propsLoaded = true; }
    } catch (Exception e) { /* fallback below */ }
    if (!propsLoaded) {
        try (FileInputStream fis = new FileInputStream("/opt/tomcat/webapps/gdgh/WEB-INF/db.properties")) {
            dbProps.load(fis); propsLoaded = true;
        } catch (Exception e) { /* handled in page */ }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Listeners — Green.Durham.Grass.and.Herb&#8482;</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<nav>
    <span class="brand">Green.Durham.Grass.and.Herb&#8482;</span>
    <div class="links">
        <a href="index.jsp">Overview</a>
        <a href="labor.jsp">Labor</a>
        <a href="ethical.jsp">Ethical</a>
        <a href="moral.jsp">Moral</a>
        <a href="listeners.jsp" class="active">Listeners</a>
        <a href="status.jsp">Status</a>
    </div>
</nav>
<div class="container">
    <div class="section">
        <h2>Listener Status</h2>
<%
    if (!propsLoaded) {
%>
        <div class="error">Unable to load database properties.</div>
<%
    } else {
        try {
            Class.forName(dbProps.getProperty("db.driver"));
            try (Connection conn = DriverManager.getConnection(dbProps.getProperty("db.url"), dbProps.getProperty("db.user"), dbProps.getProperty("db.password"))) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT id, name, port, region, status, last_seen FROM listeners ORDER BY port"); ResultSet rs = ps.executeQuery()) {
%>
        <table>
            <tr><th>ID</th><th>Name</th><th>Port</th><th>Region</th><th>Status</th><th>Last Seen</th></tr>
<%
                    while (rs.next()) {
                        String st = rs.getString("status");
%>
            <tr>
                <td><%= rs.getInt("id") %></td>
                <td><%= rs.getString("name") %></td>
                <td><%= rs.getInt("port") %></td>
                <td><%= rs.getString("region") %></td>
                <td><span class="status-dot <%= "active".equalsIgnoreCase(st) ? "green" : "red" %>"></span><%= st %></td>
                <td><%= rs.getTimestamp("last_seen") %></td>
            </tr>
<%
                    }
%>
        </table>
<%
                }
            }
        } catch (Exception e) {
%>
        <div class="error"><%= e.getMessage() %></div>
<%
        }
    }
%>
    </div>
</div>
<footer>&copy; 2026 MEARVK LLC</footer>
</body>
</html>
