package presidential.Green.Durham.Grass.and.Herb.source-code.labor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class LaborAiInterpreter {

    private final String coast;
    private final int port;
    private final long maxMemoryBytes;
    private final Map<String, String> responses = new LinkedHashMap<>();

    public LaborAiInterpreter(String coast, int port, int maxMemoryMb, Map<String, String> responses) {
        this.coast = coast;
        this.port = port;
        this.maxMemoryBytes = maxMemoryMb * 1024L * 1024L;
        this.responses.putAll(responses);
    }

    public void start() {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println(coast + " AI interpreter listening on port " + port
                        + " (max " + (maxMemoryBytes / 1024 / 1024) + " MB)");
                while (true) {
                    Socket client = server.accept();
                    handleClient(client);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            String input = in.readLine();
            if (input != null) {
                checkMemory();
                String reply = interpret(input);
                out.println(reply);
                System.out.println("[" + coast + "] Q: " + input + " -> A: " + reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String interpret(String input) {
        String lower = input.toLowerCase();
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            if (!entry.getKey().equals("default") && lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return responses.getOrDefault("default", "Concern noted.");
    }

    private void checkMemory() {
        long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        if (used > maxMemoryBytes) {
            System.gc();
        }
    }

    public static void main(String... args) throws Exception {
        String configPath = args.length > 0 ? args[0] : "source-code/labor/ai-interpreter-config.xml";
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new File(configPath));
        doc.getDocumentElement().normalize();

        NodeList interpreters = doc.getElementsByTagName("interpreter");
        for (int i = 0; i < interpreters.getLength(); i++) {
            Element el = (Element) interpreters.item(i);
            String coast = el.getElementsByTagName("coast").item(0).getTextContent();
            int port = Integer.parseInt(el.getElementsByTagName("port").item(0).getTextContent());
            int maxMem = Integer.parseInt(el.getElementsByTagName("max-memory-mb").item(0).getTextContent());

            Map<String, String> responseMap = new LinkedHashMap<>();
            NodeList respNodes = el.getElementsByTagName("response");
            for (int j = 0; j < respNodes.getLength(); j++) {
                Element r = (Element) respNodes.item(j);
                responseMap.put(r.getAttribute("keyword"), r.getTextContent());
            }

            new LaborAiInterpreter(coast, port, maxMem, responseMap).start();
        }
    }
}
