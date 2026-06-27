package presidential.Green.Durham.Grass.and.Herb.source.listeners;

import java.io.*;
import java.net.*;
import java.sql.*;

public class BaseListener {

    private static final int BASE_PORT = 20000;
    private static final int REG_PORT = 49152;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/green_durham_grass_and_herb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void main(String... args) {
        try (ServerSocket server = new ServerSocket(BASE_PORT)) {
            System.out.println("Base server listening on port " + BASE_PORT);
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handle(client)).start();
            }
        } catch (Exception e) {
            throw new RuntimeException("Base server failed on port " + BASE_PORT, e);
        }
    }

    private static void handle(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String ip = client.getInetAddress().getHostAddress();
            String geo = resolveGeo(ip);
            String nationalId = null;

            String request = in.readLine();
            if ("IDENTIFY".equalsIgnoreCase(request)) {
                out.println("Provide NationalID or register at port " + REG_PORT);
                String response = in.readLine();
                if (response != null && response.startsWith("NationalID:")) {
                    nationalId = response.substring("NationalID:".length());
                    out.println("Acknowledged NationalID=" + nationalId);
                } else {
                    out.println("Unrecognized. Apply at registration server port " + REG_PORT);
                }
            } else {
                out.println("Send IDENTIFY to authenticate, or apply at port " + REG_PORT);
            }

            storeConnection(request, ip, geo, nationalId);
        } catch (Exception e) {
            System.err.println("Client handling error: " + e.getMessage());
        }
    }

    private static String resolveGeo(String ip) {
        // Try local jar (GeoLite2) first
        try {
            Class<?> readerClass = Class.forName("com.maxmind.geoip2.DatabaseReader");
            Object reader = readerClass.getMethod("open", File.class)
                    .invoke(null, new File("data/GeoLite2-City.mmdb"));
            Object response = readerClass.getMethod("city", InetAddress.class)
                    .invoke(reader, InetAddress.getByName(ip));
            Object city = response.getClass().getMethod("getCity").invoke(response);
            Object country = response.getClass().getMethod("getCountry").invoke(response);
            String cityName = (String) city.getClass().getMethod("getName").invoke(city);
            String countryName = (String) country.getClass().getMethod("getName").invoke(country);
            return cityName + ", " + countryName;
        } catch (Exception ignored) {}

        // Fallback to remote server
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

    private static void storeConnection(String request, String ip, String geo, String nationalId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO `2000_iq` (question, remote_address, ip, geo, national_id) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, request);
            ps.setString(2, ip);
            ps.setString(3, ip);
            ps.setString(4, geo);
            ps.setString(5, nationalId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[2000_iq] Store failed: " + e.getMessage());
        }
    }
}
