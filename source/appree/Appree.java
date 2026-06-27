package presidential.Green.Durham.Grass.and.Herb.source-code.appree;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Appree {

    private static final int PUBLIC_PORT = 20000;
    private static final List<ListenerEntry> activeListeners = new ArrayList<>();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/green_durham_grass_and_herb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void main(String... args) throws Exception {
        loadConfig("source-code/appree/appree-config.xml");

        for (ListenerEntry entry : activeListeners) {
            startListener(entry);
        }

        System.out.println("Appree contact server listening on port " + PUBLIC_PORT);
        try (ServerSocket publicSocket = new ServerSocket(PUBLIC_PORT)) {
            while (true) {
                Socket client = publicSocket.accept();
                new Thread(() -> route(client)).start();
            }
        }
    }

    private static void loadConfig(String path) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
        NodeList nodes = doc.getElementsByTagName("listener");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            if ("true".equals(el.getAttribute("enabled"))) {
                activeListeners.add(new ListenerEntry(
                    Integer.parseInt(el.getAttribute("port")),
                    el.getAttribute("label")
                ));
            }
        }
        System.out.println("Loaded " + activeListeners.size() + " active listener(s) from config.");
    }

    private static void startListener(ListenerEntry entry) {
        Thread t = new Thread(() -> listen(entry), entry.label + "-" + entry.port);
        t.setUncaughtExceptionHandler((thread, ex) ->
            System.err.println("[EXCEPTION] " + thread.getName() + ": " + ex.getMessage()));
        t.start();
    }

    private static void listen(ListenerEntry entry) {
        try (ServerSocket server = new ServerSocket(entry.port)) {
            System.out.println("  " + entry.label + " listening on port " + entry.port);
            while (true) {
                Socket client = server.accept();
                System.out.println("  " + entry.label + " connection from " + client.getRemoteSocketAddress());
                client.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(entry.label + " on port " + entry.port + " failed", e);
        }
    }

    private static void route(Socket client) {
        try {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();

            // Read the incoming question/request
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String question = reader.readLine();
            String nationalId = null;

            // Check if client provides NationalID on second line
            if (question != null && question.startsWith("NationalID:")) {
                nationalId = question.substring("NationalID:".length());
                question = reader.readLine();
            }

            // Store in 2000_iq table
            if (question != null && !question.isEmpty()) {
                storeQuestion(question, client.getRemoteSocketAddress().toString(), nationalId);
            }

            // Send available listeners to connecting client
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < activeListeners.size(); i++) {
                ListenerEntry e = activeListeners.get(i);
                sb.append(i).append(": ").append(e.label).append(" (port ").append(e.port).append(")\n");
            }
            out.write(sb.toString().getBytes());
            out.flush();

            // Read selection
            int selection = reader.read() - '0';
            if (selection >= 0 && selection < activeListeners.size()) {
                ListenerEntry target = activeListeners.get(selection);
                try (Socket forward = new Socket("127.0.0.1", target.port)) {
                    out.write(("Connected to " + target.label + "\n").getBytes());
                }
            }
            client.close();
        } catch (Exception e) {
            System.err.println("Route error: " + e.getMessage());
        }
    }

    private static void storeQuestion(String question, String remoteAddress, String nationalId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO `2000_iq` (question, remote_address, ip, dns, name, geo, national_id) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            String ip = remoteAddress.contains(":") ? remoteAddress.substring(1, remoteAddress.lastIndexOf(':')) : remoteAddress;
            String dns = "";
            try { dns = InetAddress.getByName(ip).getCanonicalHostName(); } catch (Exception ignored) {}
            String geo = resolveGeo(ip);
            ps.setString(1, question);
            ps.setString(2, remoteAddress);
            ps.setString(3, ip);
            ps.setString(4, dns);
            ps.setString(5, "");
            ps.setString(6, geo);
            ps.setString(7, nationalId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[2000_iq] Store failed: " + e.getMessage());
        }
    }

    private static String resolveGeo(String ip) {
        try {
            URL url = new URL("http://ip-api.com/line/" + ip + "?fields=country,regionName,city");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String country = r.readLine();
            String region = r.readLine();
            String city = r.readLine();
            r.close();
            return city + ", " + region + ", " + country;
        } catch (Exception e) {
            return "unknown";
        }
    }

    private static class ListenerEntry {
        final int port;
        final String label;
        ListenerEntry(int port, String label) { this.port = port; this.label = label; }
    }
}
