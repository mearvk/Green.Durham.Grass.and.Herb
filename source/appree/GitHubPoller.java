package presidential.Green.Durham.Grass.and.Herb.source.appree;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.*;
import java.util.*;

public class GitHubPoller {

    private final List<SiteEntry> sites = new ArrayList<>();
    private final Map<String, String> responses = new HashMap<>();
    private int intervalSeconds = 300;
    private int timeoutMs = 5000;

    private static class SiteEntry {
        String id, url;
        boolean enabled;
        int lastStatus = -1;
    }

    public GitHubPoller(String configPath) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new File(configPath));
        doc.getDocumentElement().normalize();

        NodeList siteNodes = doc.getElementsByTagName("site");
        for (int i = 0; i < siteNodes.getLength(); i++) {
            Element el = (Element) siteNodes.item(i);
            SiteEntry s = new SiteEntry();
            s.id = el.getAttribute("id");
            s.url = el.getAttribute("url");
            s.enabled = "true".equals(el.getAttribute("enabled"));
            sites.add(s);
        }

        NodeList respNodes = doc.getElementsByTagName("response");
        for (int i = 0; i < respNodes.getLength(); i++) {
            Element el = (Element) respNodes.item(i);
            responses.put(el.getAttribute("keyword"), el.getTextContent());
        }

        NodeList interval = doc.getElementsByTagName("interval-seconds");
        if (interval.getLength() > 0) intervalSeconds = Integer.parseInt(interval.item(0).getTextContent());
        NodeList timeout = doc.getElementsByTagName("timeout-ms");
        if (timeout.getLength() > 0) timeoutMs = Integer.parseInt(timeout.item(0).getTextContent());
    }

    public String interpret(String input) {
        String lower = input.toLowerCase().trim();
        if (lower.contains("status")) {
            StringBuilder sb = new StringBuilder("GitHub Poll Status:\n");
            for (SiteEntry s : sites) {
                sb.append("  ").append(s.id).append(" (").append(s.url).append(") -> ")
                  .append(s.lastStatus == -1 ? "not yet polled" : "HTTP " + s.lastStatus).append("\n");
            }
            return sb.toString();
        }
        if (lower.contains("check")) {
            pollAll();
            return interpret("status");
        }
        return responses.getOrDefault("default", "GitHub polling service active.");
    }

    public void pollAll() {
        for (SiteEntry s : sites) {
            if (!s.enabled) continue;
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(s.url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(timeoutMs);
                conn.setReadTimeout(timeoutMs);
                s.lastStatus = conn.getResponseCode();
                conn.disconnect();
                System.out.println("[GITHUB-POLL] " + s.id + " -> HTTP " + s.lastStatus);
            } catch (Exception e) {
                s.lastStatus = -1;
                System.err.println("[GITHUB-POLL] " + s.id + " failed: " + e.getMessage());
            }
        }
    }

    public void sendMessage(Socket client, String msg) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println(msg);
    }

    public void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                String reply = interpret(line);
                sendMessage(client, reply);
            }
        } catch (IOException e) {
            System.err.println("[GITHUB-POLL] Client error: " + e.getMessage());
        }
    }

    public int getIntervalSeconds() { return intervalSeconds; }
}
