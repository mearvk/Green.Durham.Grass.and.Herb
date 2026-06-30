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
    boolean dbOk = false;
    int laborCount = 0, ethicalCount = 0, moralCount = 0;
    String dbError = null;
    if (propsLoaded) {
        try {
            Class.forName(dbProps.getProperty("db.driver"));
            try (Connection conn = DriverManager.getConnection(dbProps.getProperty("db.url"), dbProps.getProperty("db.user"), dbProps.getProperty("db.password"))) {
                dbOk = true;
                try (Statement st = conn.createStatement()) {
                    try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM labor_concerns")) { if (rs.next()) laborCount = rs.getInt(1); }
                    try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM ethical_concerns")) { if (rs.next()) ethicalCount = rs.getInt(1); }
                    try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM moral_concerns")) { if (rs.next()) moralCount = rs.getInt(1); }
                }
            }
        } catch (Exception e) { dbError = e.getMessage(); }
    }
    Runtime rt = Runtime.getRuntime();
    long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1048576;
    long maxMB = rt.maxMemory() / 1048576;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Status — Green.Durham.Grass.and.Herb&#8482;</title>
    <link rel="stylesheet" href="css/style.css">
<script src="js/scroll-preserve.js"></script>
</head>
<body>
<nav>
    <span class="brand">Green.Durham.Grass.and.Herb&#8482;</span>
    <div class="links">
        <a href="index.jsp">Overview</a>
        <a href="labor.jsp">Labor</a>
        <a href="ethical.jsp">Ethical</a>
        <a href="moral.jsp">Moral</a>
        <a href="listeners.jsp">Listeners</a>
        <a href="status.jsp" class="active">Status</a>
    </div>
</nav>
<div class="container">
    <div class="section">
        <h2>Health Check</h2>
        <table>
            <tr><th>Check</th><th>Status</th></tr>
            <tr><td>Database Connectivity</td><td><span class="status-dot <%= dbOk ? "green" : "red" %>"></span><%= dbOk ? "Connected" : "Failed" %></td></tr>
            <tr><td>JVM Memory</td><td><%= usedMB %> MB / <%= maxMB %> MB</td></tr>
            <tr><td>Java Version</td><td><%= System.getProperty("java.version") %></td></tr>
            <tr><td>Servlet Container</td><td><%= application.getServerInfo() %></td></tr>
        </table>
<% if (dbError != null) { %>
        <div class="error"><%= dbError %></div>
<% } %>
    </div>
    <div class="section">
        <h2>Record Counts</h2>
        <div class="stat-grid">
            <div class="stat-card"><div class="value"><%= laborCount %></div><div class="label">Labor Concerns</div></div>
            <div class="stat-card"><div class="value"><%= ethicalCount %></div><div class="label">Ethical Concerns</div></div>
            <div class="stat-card"><div class="value"><%= moralCount %></div><div class="label">Moral Concerns</div></div>
        </div>
    </div>
</div>
<footer>&copy; 2026 MEARVK LLC</footer>
</body>
</html>
