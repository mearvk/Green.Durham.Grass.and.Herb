package presidential.Green.Durham.Grass.and.Herb.source.listeners;

import java.net.ServerSocket;
import java.net.Socket;

public class CoastListeners {

    private static final Thread.UncaughtExceptionHandler HANDLER = (thread, ex) -> {
        System.err.println("[EXCEPTION] Thread: " + thread.getName() + " | " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        ex.printStackTrace(System.err);
    };

    public static void main(String... args) {
        Thread.setDefaultUncaughtExceptionHandler(HANDLER);

        startListener(40002, "East Coast");
        startListener(40003, "West Coast");
        startListener(40005, "Wyoming");
        startListener(40007, "Texas");
        startListener(40009, "Indiana");
    }

    private static void startListener(int port, String label) {
        Thread t = new Thread(() -> listen(port, label), label + "-" + port);
        t.setUncaughtExceptionHandler(HANDLER);
        t.start();
    }

    private static void listen(int port, String label) {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println(label + " listening on port " + port);
            while (true) {
                Socket client = server.accept();
                System.out.println(label + " connection from " + client.getRemoteSocketAddress());
                client.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(label + " on port " + port + " failed", e);
        }
    }
}
