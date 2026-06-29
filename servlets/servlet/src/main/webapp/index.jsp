<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.HttpURLConnection, java.net.URI" %>
<%
    boolean authorized = false;
    try {
        HttpURLConnection conn = (HttpURLConnection) URI.create("https://raw.githubusercontent.com/mearvk/Java.Web.Server.Telnet.Front.Java.21/main/psychiatry/secrets/public.key").toURL().openConnection();
        conn.setRequestMethod("HEAD");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        authorized = (conn.getResponseCode() == 200);
        conn.disconnect();
    } catch (Exception e) { authorized = false; }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Green.Durham.Grass.and.Herb&#8482;</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<nav>
    <span class="brand">Green.Durham.Grass.and.Herb&#8482;</span>
    <div class="links">
        <a href="index.jsp" class="active">Overview</a>
        <a href="labor.jsp">Labor</a>
        <a href="ethical.jsp">Ethical</a>
        <a href="moral.jsp">Moral</a>
        <a href="listeners.jsp">Listeners</a>
        <a href="status.jsp">Status</a>
    </div>
</nav>
<div class="container">
    <div class="hero">
        <span class="tag">NC Socialist-College Block</span>
        <h1>Green.Durham.Grass.and.Herb&#8482;</h1>
        <p>Appree contact server with labor, ethical, moral, and mortality concerns databases. JWSTFJ21 masquerade-integrated.</p>
        <div class="auth-box">
            <span class="status-dot <%= authorized ? "green" : "red" %>"></span>
            <%= authorized ? "Authorized — public.key present" : "Unauthorized — public.key missing" %>
        </div>
    </div>
    <div class="section">
        <h2>Components</h2>
        <table>
            <tr><th>Component</th><th>Type</th><th>Status</th></tr>
            <tr><td>Labor Concerns DB</td><td>MySQL</td><td><span class="status-dot green"></span>Active</td></tr>
            <tr><td>Ethical Concerns DB</td><td>MySQL</td><td><span class="status-dot green"></span>Active</td></tr>
            <tr><td>Moral Concerns DB</td><td>MySQL</td><td><span class="status-dot green"></span>Active</td></tr>
            <tr><td>Mortality Concerns DB</td><td>MySQL</td><td><span class="status-dot green"></span>Active</td></tr>
            <tr><td>Appree Contact Server</td><td>TCP</td><td><span class="status-dot green"></span>Active</td></tr>
            <tr><td>Coast Listeners</td><td>TCP</td><td><span class="status-dot green"></span>Active</td></tr>
        </table>
    </div>
    <div class="section">
        <h2>Ports</h2>
        <table>
            <tr><th>Port</th><th>Service</th><th>Description</th></tr>
            <tr><td>2000</td><td>Directory</td><td>Strernary&#8482; Directory Server</td></tr>
            <tr><td>20000</td><td>Appree Base</td><td>Primary Appree contact endpoint</td></tr>
            <tr><td>40002</td><td>East Coast</td><td>East Coast listener</td></tr>
            <tr><td>40003</td><td>West Coast</td><td>West Coast listener</td></tr>
            <tr><td>40007</td><td>Texas</td><td>Texas listener</td></tr>
            <tr><td>49152</td><td>NationalFinanceID</td><td>National finance identification</td></tr>
        </table>
    </div>
</div>
<footer>&copy; 2026 MEARVK LLC</footer>
</body>
</html>
