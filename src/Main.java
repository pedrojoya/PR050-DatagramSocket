import client.Client;
import server.EchoServer;

class Main {

    private static final int SERVER_PORT = 60000;
    private static final int NUMBER_OF_CLIENTS = 10;

    public static void main(String[] args) {
        String serverAddress = "localhost";
        Thread server = new Thread(new EchoServer(SERVER_PORT));
        server.start();
        Thread client = new Thread(new Client(serverAddress, SERVER_PORT));
        client.start();
    }

}
