import java.net.*;
import java.io.*;

public class TCPserver {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter outbound;
    private BufferedReader inbound;
    private char bufferIn[] = new char[512];

    public void establishSocket(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        outbound = new PrintWriter(clientSocket.getOutputStream(), true);
        inbound = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void receiveData() throws Exception {
        if(inbound.ready()) {
            inbound.read(bufferIn, 0, bufferIn.length);
            System.out.println("Data received from client: " + bufferIn);
            handleRecvData();
        }
    }

    public void handleRecvData() {
        if (bufferIn[0] == 0x02) {

        }
    }

    public void sendData() throws Exception {
    }

    public void terminateConnection() throws Exception {
        serverSocket.close();
        clientSocket.close();
        outbound.close();
        inbound.close();
    }

    public static void main(String[] args) {
        try {
            TCPserver server = new TCPserver();

            server.establishSocket(6666);
            server.sendData();
            server.receiveData();
            server.terminateConnection();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}
