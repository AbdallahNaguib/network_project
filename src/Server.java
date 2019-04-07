import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    public static void main(String[] args){
        try {
            int port = 55555;
            ServerSocket doorbellSocket = new ServerSocket(port);
            System.out.println("waiting for connections");
            while(true){
                Socket client = doorbellSocket.accept();
                ClientManager clientManager = new ClientManager(client);
                ClientManager.clients.add(clientManager);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
