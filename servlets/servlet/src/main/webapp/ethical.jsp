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
    String filterPrinciple = request.getParameter("principle");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ethical Concerns — Green.Durham.Grass.and.Herb&#8482;</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<nav>
    <span class="brand">Green.Durham.Grass.and.Herb&#8482;</span>
    <div class="links">
        <a href="index.jsp">Overview</a>
        <a href="labor.jsp">Labor</a>
        <a href="ethical.jsp" class="active">Ethical</a>
        <a href="moral.jsp">Moral</a>
        <a href="listeners.jsp">Listeners</a>
        <a href="status.jsp">Status</a>
    </div>
</nav>
<div class="container">
    <div class="section">
        <h2>Ethical Concerns</h2>
<%
    if (!propsLoaded) {
%>
        <div class="error">Unable to load database properties.</div>
<%
    } else {
        try {
            Class.forName(dbProps.getProperty("db.driver"));
            try (Connection conn = DriverManager.getConnection(dbProps.getProperty("db.url"), dbProps.getProperty("db.user"), dbProps.getProperty("db.password"))) {
                try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT DISTINCT principle FROM ethical_concerns ORDER BY principle")) {
%>
        <div class="filter-bar">
            <a href="ethical.jsp" class="<%= filterPrinciple == null ? "active" : "" %>">All</a>
<%
                    while (rs.next()) {
                        String p = rs.getString("principle");
%>
            <a href="ethical.jsp?principle=<%= java.net.URLEncoder.encode(p, "UTF-8") %>" class="<%= p.equals(filterPrinciple) ? "active" : "" %>"><%= p %></a>
<%
                    }
%>
        </div>
<%
                }
                String sql = filterPrinciple != null ? "SELECT id, concern, principle, source, submitted_at FROM ethical_concerns WHERE principle = ? ORDER BY submitted_at DESC" : "SELECT id, concern, principle, source, submitted_at FROM ethical_concerns ORDER BY submitted_at DESC";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    if (filterPrinciple != null) ps.setString(1, filterPrinciple);
                    try (ResultSet rs = ps.executeQuery()) {
%>
        <table>
            <tr><th>ID</th><th>Concern</th><th>Principle</th><th>Source</th><th>Submitted</th></tr>
<%
                        while (rs.next()) {
%>
            <tr>
                <td><%= rs.getInt("id") %></td>
                <td><%= rs.getString("concern") %></td>
                <td><%= rs.getString("principle") %></td>
                <td><%= rs.getString("source") %></td>
                <td><%= rs.getTimestamp("submitted_at") %></td>
            </tr>
<%
                        }
%>
        </table>
<%
                    }
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
