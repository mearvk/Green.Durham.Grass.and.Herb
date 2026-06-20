import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.File;
import java.util.*;

/**
 * Green.Durham.Grass.and.Herb — Main entry point.
 *
 * <p>Appree is a careful and concernful program all the way down to the Mayor
 * of Chapel Hill and her study of students of the County.</p>
 *
 * <p>This is subtly an Appree project riding on NC Labor Laws &amp; Organization.</p>
 */
public class Main {

    private static final String[] CONFIG_PATHS = {
        "configuration/appree-config.xml",
        "configuration/github-polling-config.xml",
        "configuration/ai-interpreter-config.xml",
        "configuration/ethical-db-config.xml",
        "configuration/labor-db-config.xml",
        "configuration/moral-db-config.xml",
        "configuration/mortality-db-config.xml"
    };

    public static void main(String... args) throws Exception {
        XmlReader reader = new XmlReader();
        Map<String, Document> configs = reader.loadAll(CONFIG_PATHS);

        DecisionMaker dm = new DecisionMaker(configs);
        dm.evaluate();
    }

    static class XmlReader {
        Map<String, Document> loadAll(String[] paths) {
            Map<String, Document> docs = new LinkedHashMap<>();
            for (String path : paths) {
                try {
                    Document doc = DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder().parse(new File(path));
                    doc.getDocumentElement().normalize();
                    docs.put(path, doc);
                    System.out.println("[XML] Loaded: " + path);
                } catch (Exception e) {
                    System.err.println("[XML] Failed: " + path + " - " + e.getMessage());
                }
            }
            return docs;
        }
    }

    static class DecisionMaker {
        private final Map<String, Document> configs;

        DecisionMaker(Map<String, Document> configs) {
            this.configs = configs;
        }

        void evaluate() {
            System.out.println("\n[DECISION] Evaluating " + configs.size() + " config(s)...");

            for (Map.Entry<String, Document> entry : configs.entrySet()) {
                String path = entry.getKey();
                Document doc = entry.getValue();

                if (path.contains("appree") && !path.contains("github")) {
                    evaluateListeners(doc);
                } else if (path.contains("github-polling")) {
                    evaluateGithubPolling(doc);
                } else if (path.contains("ai-interpreter")) {
                    evaluateInterpreters(doc);
                } else if (path.contains("db-config")) {
                    evaluateDbConfig(path, doc);
                }
            }
            System.out.println("[DECISION] Evaluation complete.");
        }

        private void evaluateListeners(Document doc) {
            NodeList nodes = doc.getElementsByTagName("listener");
            int enabled = 0;
            for (int i = 0; i < nodes.getLength(); i++) {
                if ("true".equals(((Element) nodes.item(i)).getAttribute("enabled"))) enabled++;
            }
            System.out.println("[DECISION] Listeners: " + enabled + "/" + nodes.getLength() + " enabled");
        }

        private void evaluateInterpreters(Document doc) {
            NodeList nodes = doc.getElementsByTagName("interpreter");
            System.out.println("[DECISION] AI Interpreters configured: " + nodes.getLength());
        }

        private void evaluateDbConfig(String path, Document doc) {
            String module = path.replaceAll(".*/", "").replace("-db-config.xml", "");
            NodeList urlNodes = doc.getElementsByTagName("url");
            String url = urlNodes.getLength() > 0 ? urlNodes.item(0).getTextContent() : "unknown";
            System.out.println("[DECISION] DB for " + module + ": " + url);
        }

        private void evaluateGithubPolling(Document doc) {
            NodeList sites = doc.getElementsByTagName("site");
            int enabled = 0;
            for (int i = 0; i < sites.getLength(); i++) {
                if ("true".equals(((Element) sites.item(i)).getAttribute("enabled"))) enabled++;
            }
            System.out.println("[DECISION] GitHub polling: " + enabled + "/" + sites.getLength() + " sites enabled");
        }
    }
}
