import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPclient {
    private Socket clientSocket;
    private PrintWriter outbound;
    private BufferedReader inbound;
    private char bufferOut[] = new char[512];
    private char bufferIn[] = new char[512];
    private String serverIP;
    private int port;

    private void userInput(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter server address: ");
        serverIP = reader.next();

        System.out.println("Please enter port number: ");
        port = reader.nextInt();
    }

    private void establishSocket() throws Exception{
        InetAddress serverAddress = InetAddress.getByName(serverIP);
        clientSocket = new Socket(serverAddress, port);
        outbound = new PrintWriter(clientSocket.getOutputStream(), true);
        inbound = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    private void sendData() throws Exception{
        if(bufferIn[2] == 0x02) {
            bufferOut[0] = 0x00;
            bufferOut[1] = 0x09;
            bufferOut[2] = 0x01;
            bufferOut[3] = '1';
            bufferOut[4] = '4';
            bufferOut[5] = '5';
            bufferOut[6] = '1';
            bufferOut[7] = '2';
            bufferOut[8] = '5';
            outbound.println(bufferOut);
        }
        else {
            bufferOut[0] = 0x00;
            bufferOut[1] = 0x03;
            bufferOut[2] = "Pong";

        }
    }

    private void receiveData() throws Exception {
        if(inbound.ready()) {                                       // Ready VERY important to ensure server is ready to send.
            inbound.read(bufferIn, 0, bufferIn.length);
            System.out.print(bufferIn);
            handleRecvData();
        }
    }

    private void handleRecvData() throws Exception {
        if(bufferIn[2] == 0x02) {
            port = bufferIn[3];
            terminateConnection();
            establishSocket();
        }

        else if(bufferIn[2] == 0x03) {                              // If the data received is a Ping...
            while(bufferIn[2] != 0x05) {                            // Keep sending a Pong until a Quit is received.
                sendData();
                inbound.read(bufferIn, 0, bufferIn.length);
                System.out.println("From server: " + bufferIn);
            }
        }

        else if (bufferIn[2] == 0x05) {
            terminateConnection();
        }
    }

    private void terminateConnection() throws Exception {
        clientSocket.close();
        outbound.close();
        inbound.close();
    }

    public static void main(String[] args) {
        try {
            TCPclient client = new TCPclient();

            client.userInput();
            client.establishSocket();
            client.receiveData();
            client.handleRecvData();

            if (clientSocket.isConnected()) {
                client.terminateConnection();
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}
