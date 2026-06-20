package listeners;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BaseListener {

    private static final int BASE_PORT = 20000;
    private static final int REG_PORT = 49152;

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

            String request = in.readLine();
            if ("IDENTIFY".equalsIgnoreCase(request)) {
                out.println("Provide NationalID or register at port " + REG_PORT);
                String response = in.readLine();
                if (response != null && response.startsWith("NationalID:")) {
                    String id = response.substring("NationalID:".length());
                    out.println("Acknowledged NationalID=" + id);
                } else {
                    out.println("Unrecognized. Apply at registration server port " + REG_PORT);
                }
            } else {
                out.println("Send IDENTIFY to authenticate, or apply at port " + REG_PORT);
            }
        } catch (Exception e) {
            System.err.println("Client handling error: " + e.getMessage());
        }
    }
}
