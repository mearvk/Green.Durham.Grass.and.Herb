package appree;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Appree {

    private static final int PUBLIC_PORT = 20000;
    private static final List<ListenerEntry> activeListeners = new ArrayList<>();

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

            // Send available listeners to connecting client
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < activeListeners.size(); i++) {
                ListenerEntry e = activeListeners.get(i);
                sb.append(i).append(": ").append(e.label).append(" (port ").append(e.port).append(")\n");
            }
            out.write(sb.toString().getBytes());
            out.flush();

            // Read selection
            int selection = in.read() - '0';
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

    private static class ListenerEntry {
        final int port;
        final String label;
        ListenerEntry(int port, String label) { this.port = port; this.label = label; }
    }
}
