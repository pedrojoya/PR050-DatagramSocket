package client;

import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;

public class Client implements Runnable {

    private final String serverName;
    private final int serverPort;

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            while (!Thread.currentThread().isInterrupted()) {
                // Build datagramPacket with same message
                String message = "Quillo que";
                byte[] messageData = message.getBytes();
                int messageLength = messageData.length;
                InetAddress serverAddress = InetAddress.getByName(serverName);
                DatagramPacket messageDatagramPacket =
                        new DatagramPacket(messageData, messageLength, serverAddress, serverPort);
                // Send message.
                datagramSocket.send(messageDatagramPacket);

                // DatagramPacket to be filled.
                DatagramPacket responseDatagramPacket = new DatagramPacket(buffer, buffer.length);
                // Receive datagram and fill de receivedDatagramPacket with it.
                datagramSocket.receive(responseDatagramPacket);
                // Extract data received.
                InetAddress senderAddress = responseDatagramPacket.getAddress();
                int senderPort = responseDatagramPacket.getPort();
                byte[] responseData = responseDatagramPacket.getData();
                String responseMessage = new String(responseData);
                // Inform data received.
                System.out.printf("Client: %s received from host %s (port %d)\n",
                        responseMessage, senderAddress.getHostAddress(), senderPort);

            }
        } catch (SocketException e) {
            // Socket could not be opened, or the socket could not bind to the specified local port.
            System.out.printf("Client: Socket can't be opened on port %d\n", serverPort);
        } catch (IOException e) {
            // IO error on datagramSocket.receive() or datagramSocket.send() method
            System.out.println("Client: Input/Output error in server socket");
        }
    }

//    private void sendMessage(ObjectOutputStream output) throws InterruptedException, IOException {
//        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 10) * 1000);
//        output.writeObject(new Message("Client #" + clientNumber, "Hello from client " + clientNumber));
//    }
//
//    private void receiveMessage(ObjectInputStream input) throws IOException, ClassNotFoundException {
//        Message message = (Message) input.readObject();
//        System.out.printf("Client #%d - Message from %s: %s\n", clientNumber, message.getAuthor(), message.getContent());
//    }
//
//    private void closeConnection() throws InterruptedException {
//        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 10) * 1000);
//    }
//
//    private void showConnectionError() {
//        System.out.printf("Client #%d: Can't connect with server in %s:%d\n", clientNumber, serverName, serverPort);
//    }
//
//    private void showMessageFormatError() {
//        System.out.printf("Client #%d: Incorrect message format\n", clientNumber);
//    }

}
