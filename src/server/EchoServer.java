package server;

import java.io.IOException;
import java.net.*;

public class EchoServer implements Runnable {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            datagramSocket.setSoTimeout(5000);
            while (!Thread.currentThread().isInterrupted()) {
                // DatagramPacket to be filled.
                DatagramPacket receivedDatagramPacket = new DatagramPacket(buffer, buffer.length);
                // Receive datagram and fill de receivedDatagramPacket with it.
                datagramSocket.receive(receivedDatagramPacket);
                // Extract data received.
                InetAddress senderAddress = receivedDatagramPacket.getAddress();
                int senderPort = receivedDatagramPacket.getPort();
                byte[] receivedData = receivedDatagramPacket.getData();
                int receivedDataLength = receivedDatagramPacket.getLength();
                String receivedMessage = new String(receivedData, 0, receivedDataLength).trim();
                // Inform data received.
                System.out.printf("Server: \"%s\" received from host %s (port %d)\n",
                        receivedMessage, senderAddress.getHostAddress(), senderPort);
                // Build response with same message (echo!!!)
                DatagramPacket responseDatagramPacket =
                        new DatagramPacket(receivedData, receivedDataLength, senderAddress, senderPort);
                // Send response.
                datagramSocket.send(responseDatagramPacket);
                System.out.printf("Server: \"%s\" echo sent to host %s (port %d)\n",
                        receivedMessage, senderAddress.getHostAddress(), senderPort);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Server: Finished");
                    return;
                }
            }
        } catch (SocketException e) {
            // Socket could not be opened, or the socket could not bind to the specified local port.
            System.out.printf("Server: Socket can't be opened on port %d\n", port);
        } catch (SocketTimeoutException e) {
            // Socket timeout
            System.out.println("Server: socket timeout");
        } catch (IOException e) {
            // IO error on datagramSocket.receive() or datagramSocket.send() method
            System.out.println("Server: Input/Output error in server socket");
        }
    }

}
