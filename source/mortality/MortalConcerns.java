package presidential.Green.Durham.Grass.and.Herb.source-code.mortality;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import java.io.File;

public class MortalConcerns {

    private static final String CONFIG_PATH = "source-code/mortality/db-config.xml";

    public static Connection getConnection() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(CONFIG_PATH));
        doc.getDocumentElement().normalize();

        String url = doc.getElementsByTagName("url").item(0).getTextContent();
        String user = doc.getElementsByTagName("user").item(0).getTextContent();
        String password = doc.getElementsByTagName("password").item(0).getTextContent();

        return DriverManager.getConnection(url, user, password);
    }
}
