package presidential.Green.Durham.Grass.and.Herb.source-code.listeners;

import java.io.*;
import java.net.*;
import java.sql.*;

public class SocialismListener {

    private static final int PORT = 20003;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/green_durham_grass_and_herb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void main(String... args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("[SOCIALISM] Listening on port " + PORT);
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handle(client)).start();
            }
        } catch (Exception e) {
            throw new RuntimeException("Socialism listener failed on port " + PORT, e);
        }
    }

    private static void handle(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String ip = client.getInetAddress().getHostAddress();

            out.println("SOCIALISM MODULE — Provide your NationalID:");
            String nationalId = in.readLine();
            if (nationalId == null || nationalId.isBlank()) {
                out.println("No NationalID provided. Connection closed.");
                return;
            }

            int score = evaluateScore(nationalId);
            String explanation;
            if (score == 1) {
                explanation = "Too new to know its name (score=1)";
            } else if (score == 8) {
                explanation = "Otherwise able (score=8)";
            } else {
                explanation = "Socialist score: " + score + "/330";
            }

            out.println("NationalID acknowledged: " + nationalId);
            out.println("Assessment: " + explanation);

            store(nationalId, ip, score);
        } catch (Exception e) {
            System.err.println("[SOCIALISM] Client error: " + e.getMessage());
        }
    }

    private static int evaluateScore(String nationalId) {
        if (nationalId.length() < 4) return 1;   // too new to know its name
        int hash = Math.abs(nationalId.hashCode());
        int base = hash % 340;
        if (base > 330) return 8;                 // otherwise able
        return base;                              // 0-330
    }

    private static void store(String nationalId, String ip, int score) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO socialism (national_id, ip, score, created_at) VALUES (?, ?, ?, NOW())")) {
            ps.setString(1, nationalId);
            ps.setString(2, ip);
            ps.setInt(3, score);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[SOCIALISM] Store failed: " + e.getMessage());
        }
    }
}
