import client.Client;
import server.EchoServer;

import java.util.concurrent.TimeUnit;

class Main {

    private static final int SERVER_PORT = 60000;

    public static void main(String[] args) throws InterruptedException {
        String serverAddress = "localhost";
        Thread server = new Thread(new EchoServer(SERVER_PORT));
        server.start();
        Thread client = new Thread(new Client(serverAddress, SERVER_PORT));
        client.start();
        TimeUnit.SECONDS.sleep(3);
        client.interrupt();
        server.interrupt();
        server.join();
        client.join();
        System.out.println("Main: Finished");
    }

}
