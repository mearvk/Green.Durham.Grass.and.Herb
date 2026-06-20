package listeners;

import java.net.ServerSocket;
import java.net.Socket;

public class CoastListeners {

    public static void main(String... args) throws Exception {
        new Thread(() -> listen(40002, "East Coast")).start();
        new Thread(() -> listen(40003, "West Coast")).start();
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
            e.printStackTrace();
        }
    }
}
