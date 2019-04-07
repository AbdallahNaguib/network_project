
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class ClientManager extends Thread{
    static Vector<ClientManager> clients = new Vector<>();
    private String userName;
    private BufferedReader input;
    private PrintWriter output;
    ClientManager(Socket client) throws IOException {
        input = new BufferedReader(new
                InputStreamReader(client.getInputStream()));
        output = new PrintWriter(client.getOutputStream(), true);
        userName = input.readLine();
        start();
    }
    private void sendMsg(String from, String sub, String chatMsg){
        output.println(1); // tell the client that this is live
        output.println(from);
        output.println(sub);
        output.println(chatMsg);
    }
    public void whoIsOpen(){
        for(ClientManager cl:clients) {
            if(cl.userName.equals(this.userName))continue;
            output.println(3);
            output.println(cl.userName);
        }
        for(ClientManager cl:clients){
            if(cl.userName.equals(this.userName))continue;
            cl.output.println(3);
            cl.output.println(userName);
        }
    }
    public void run(){
        System.out.println(userName + " connected!");
        whoIsOpen();
        String line;
        try{
            while(true){
                line = input.readLine();
                if(line.equals("end")){
                    System.out.println(userName + " disconnected!");
                    clients.remove(this);
                    for(ClientManager c1:clients){
                        // tell the client to clear the list of available people
                        c1.output.println(4);
                        for(ClientManager c2:clients){
                            if(c1.userName.equals(c2.userName))continue;
                            c1.output.println(3);
                            c1.output.println(c2.userName);
                        }
                    }
                    break;
                }
                if(line.equals("1")){
                    String sub = input.readLine();
                    String msg = input.readLine();
                    int numOfClientsToSend = Integer.parseInt(
                            input.readLine());
                    for(int i=0 ; i<numOfClientsToSend ; i++){
                        String to=input.readLine();
                        FileInterface.addRecord(new EmailData(userName,
                                to , sub , msg));
                        for (ClientManager client : clients) {
                            if (client.userName.equals(to)) {
                                client.sendMsg(userName ,sub , msg);
                            }
                        }
                    }
                }else{
                    Vector<EmailData> vec = FileInterface.readData(userName);
                    for (EmailData aVec : vec) {
                        output.println(2);//tell the client that this is from inbox
                        output.println(aVec.from);
                        output.println(aVec.sub);
                        output.println(aVec.msg);
                    }
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

