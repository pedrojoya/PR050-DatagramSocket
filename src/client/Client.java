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
            datagramSocket.setSoTimeout(5000);
            for (int i = 0; !Thread.currentThread().isInterrupted(); i++) {
                // Build datagramPacket with same message
                String message = String.format("Quillo que - %d", i);
                byte[] messageData = message.getBytes();
                int messageLength = messageData.length;
                InetAddress serverAddress = InetAddress.getByName(serverName);
                DatagramPacket messageDatagramPacket =
                        new DatagramPacket(messageData, messageLength, serverAddress, serverPort);
                // Send message.
                datagramSocket.send(messageDatagramPacket);
                System.out.printf("Client: \"%s\" sent to host %s (port %d)\n",
                        message, serverAddress, serverPort);


                // DatagramPacket to be filled.
                DatagramPacket responseDatagramPacket = new DatagramPacket(buffer, buffer.length);
                // Receive datagram and fill de receivedDatagramPacket with it.
                datagramSocket.receive(responseDatagramPacket);
                // Extract data received.
                InetAddress senderAddress = responseDatagramPacket.getAddress();
                int senderPort = responseDatagramPacket.getPort();
                byte[] responseData = responseDatagramPacket.getData();
                String responseMessage = (new String(responseData)).trim();
                // Inform data received.
                System.out.printf("Client: \"%s\" echo received from host %s (port %d)\n",
                        responseMessage, senderAddress.getHostAddress(), senderPort);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Client: Finished");
                    return;
                }
            }
        } catch (SocketException e) {
            // Socket could not be opened, or the socket could not bind to the specified local port.
            System.out.printf("Client: Socket can't be opened on port %d\n", serverPort);
        } catch (SocketTimeoutException e) {
            // Socket timeout
            System.out.println("Client: socket timeout");
        } catch (IOException e) {
            // IO error on datagramSocket.receive() or datagramSocket.send() method
            System.out.println("Client: Input/Output error in server socket");
        }
    }

}
