package presidential.Green.Durham.Grass.and.Herb.source.integration;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.File;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

/**
 * Reflective Masquerade for Java.Web.Server.Telnet.Front.Java.21 (JWSTFJ21) integration.
 *
 * <p>This class allows Green.Durham.Grass.and.Herb to present itself as a module
 * within a JWSTFJ21 instance that may house more than 65536 ports (virtual port
 * addressing via multiplexed channels over physical sockets).</p>
 *
 * <p>The masquerade reflectively discovers JWSTFJ21 classes at runtime and registers
 * this program's listeners under virtual port assignments that exceed the TCP 16-bit
 * port limit, using JWSTFJ21's extended port-space protocol.</p>
 */
public class JWSTFJ21Masquerade {

    private static final String CONFIG_PATH = "configuration/jwstfj21-integration.xml";
    private static final String JWSTFJ21_REGISTRY_CLASS = "com.mearvk.jwstfj21.PortRegistry";
    private static final String JWSTFJ21_MODULE_CLASS = "com.mearvk.jwstfj21.Module";
    private static final String JWSTFJ21_CHANNEL_CLASS = "com.mearvk.jwstfj21.VirtualChannel";

    private String jwstfj21Host;
    private int jwstfj21ControlPort;
    private long[] virtualPorts;  // long — exceeds 65536
    private String moduleId;
    private Class<?> registryClass;
    private Class<?> moduleClass;
    private Class<?> channelClass;
    private Object registryInstance;

    public JWSTFJ21Masquerade() throws Exception {
        loadConfig();
        reflectJWSTFJ21();
    }

    private void loadConfig() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new File(CONFIG_PATH));
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        jwstfj21Host = getTagValue(root, "host");
        jwstfj21ControlPort = Integer.parseInt(getTagValue(root, "control-port"));
        moduleId = getTagValue(root, "module-id");

        NodeList vpNodes = root.getElementsByTagName("virtual-port");
        virtualPorts = new long[vpNodes.getLength()];
        for (int i = 0; i < vpNodes.getLength(); i++) {
            virtualPorts[i] = Long.parseLong(vpNodes.item(i).getTextContent().trim());
        }

        System.out.println("[JWSTFJ21] Config loaded: host=" + jwstfj21Host +
                " control-port=" + jwstfj21ControlPort +
                " virtual-ports=" + virtualPorts.length);
    }

    /**
     * Reflectively load JWSTFJ21 classes. If not on classpath, degrade gracefully
     * and operate in standalone mode until the parent server is available.
     */
    private void reflectJWSTFJ21() {
        try {
            registryClass = Class.forName(JWSTFJ21_REGISTRY_CLASS);
            moduleClass = Class.forName(JWSTFJ21_MODULE_CLASS);
            channelClass = Class.forName(JWSTFJ21_CHANNEL_CLASS);

            // Obtain singleton registry via PortRegistry.getInstance(host, controlPort)
            Method getInstance = registryClass.getMethod("getInstance", String.class, int.class);
            registryInstance = getInstance.invoke(null, jwstfj21Host, jwstfj21ControlPort);

            System.out.println("[JWSTFJ21] Reflective binding successful — registry connected.");
        } catch (ClassNotFoundException e) {
            System.out.println("[JWSTFJ21] JWSTFJ21 not on classpath — standalone mode. " +
                    "Will retry on next integration cycle.");
            registryClass = null;
        } catch (Exception e) {
            System.err.println("[JWSTFJ21] Reflection error: " + e.getMessage());
            registryClass = null;
        }
    }

    /**
     * Register this program's listeners with the JWSTFJ21 extended port space.
     * Virtual ports above 65535 are multiplexed over JWSTFJ21's channel protocol.
     */
    public boolean register() {
        if (registryClass == null || registryInstance == null) {
            System.out.println("[JWSTFJ21] Cannot register — no registry connection.");
            return false;
        }

        try {
            // Module.create(moduleId, description)
            Method createModule = moduleClass.getMethod("create", String.class, String.class);
            Object module = createModule.invoke(null, moduleId, "Green.Durham.Grass.and.Herb");

            // Register each virtual port: registry.registerVirtualPort(module, long port, handler)
            Method registerVP = registryClass.getMethod("registerVirtualPort", moduleClass, long.class, Object.class);
            for (long vp : virtualPorts) {
                registerVP.invoke(registryInstance, module, vp, (Runnable) () -> handleVirtualConnection(vp));
                System.out.println("[JWSTFJ21] Registered virtual port " + vp);
            }

            // Announce masquerade identity to parent
            Method announce = registryClass.getMethod("announceModule", moduleClass);
            announce.invoke(registryInstance, module);

            System.out.println("[JWSTFJ21] Masquerade registration complete. Module: " + moduleId);
            return true;
        } catch (Exception e) {
            System.err.println("[JWSTFJ21] Registration failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deregister cleanly from JWSTFJ21 on shutdown.
     */
    public void deregister() {
        if (registryClass == null || registryInstance == null) return;
        try {
            Method deregister = registryClass.getMethod("deregisterModule", String.class);
            deregister.invoke(registryInstance, moduleId);
            System.out.println("[JWSTFJ21] Deregistered module: " + moduleId);
        } catch (Exception e) {
            System.err.println("[JWSTFJ21] Deregistration error: " + e.getMessage());
        }
    }

    /**
     * Handle an incoming connection on a virtual port — dispatches to the appropriate
     * local listener based on the virtual-to-physical port mapping in config.
     */
    private void handleVirtualConnection(long virtualPort) {
        System.out.println("[JWSTFJ21] Incoming on virtual port " + virtualPort);
        // Dispatch is handled by the parent JWSTFJ21 channel multiplexer;
        // this callback signals readiness to the local listener thread pool.
    }

    /**
     * Query whether JWSTFJ21 integration is active.
     */
    public boolean isIntegrated() {
        return registryClass != null && registryInstance != null;
    }

    /**
     * Get the assigned virtual ports for external reference.
     */
    public long[] getVirtualPorts() {
        return virtualPorts;
    }

    private static String getTagValue(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : "";
    }
}
