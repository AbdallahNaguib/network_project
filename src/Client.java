import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static BufferedReader br;
    private static JTextArea jLiveChat , jInbox , JOpened;
    static String username;
    static JFrame liveFrame , inboxFrame , openedFrame;
    public static void main(String[] args){
        System.out.print("Enter your name : ");
        Scanner scan = new Scanner(System.in);
        username = scan.nextLine();
        buildGUI();
        try {
            Socket socket = new Socket("localhost", 55555);
            br = new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println(username);
            Receive r = new Receive();
            r.start();
            while(true){
                System.out.println("1- send msg");
                System.out.println("2- show inbox");
                System.out.println("3- exit");
                int res = Integer.parseInt(scan.nextLine());
                if(res == 1){
                    pw.println(1);
                    System.out.print("Enter the subject : ");
                    String sub = scan.nextLine();
                    pw.println(sub);

                    System.out.print("Enter the message : ");
                    String msg = scan.nextLine();
                    pw.println(msg);

                    System.out.print("Enter the number of people you " +
                            " send to : ");
                    int num = Integer.parseInt(scan.nextLine());
                    pw.println(num);
                    for (int i=0 ; i<num ; i++){
                        System.out.print("Enter the name : ");
                        String name = scan.nextLine();
                        pw.println(name);
                    }
                }else if(res == 2){
                    pw.println(2);
                }else{
                    pw.println("end");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    static class Receive extends Thread{
        public void run(){
            try {
                while(true) {
                    String query=br.readLine();
                    if(query.equals("3")){
                        JOpened.append(br.readLine() + '\n');
                        continue;
                    }else if(query.equals("4")){
                        JOpened.setText("");
                        continue;
                    }
                    String from = br.readLine();
                    String sub = br.readLine();
                    String msg = br.readLine();
                    if(query.equals("1")) {
                        jLiveChat.append("you have a " +
                                "message from " + from + '\n' +
                                "subject : " + sub + '\n' +
                                "content : " + msg + '\n' + '\n' + '\n');
                    }else{
                        jInbox.append("you have a " +
                                "message from " + from + '\n' +
                                "subject : " + sub + '\n' +
                                "content : " + msg + '\n' + '\n' + '\n');
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    static void buildGUI(){
        jLiveChat = new JTextArea();
        jInbox = new JTextArea();
        JOpened = new JTextArea();
        liveFrame = new JFrame();
        inboxFrame = new JFrame();
        openedFrame = new JFrame();
        liveFrame.add(jLiveChat);
        liveFrame.setTitle(username + " live chat");
        liveFrame.setSize(500,500);
        liveFrame.setVisible(true);

        openedFrame.add(JOpened);
        openedFrame.setTitle("الناس اللي فاتحين");
        openedFrame.setSize(500  , 500);
        openedFrame.setVisible(true);

        inboxFrame.add(jInbox);
        inboxFrame.setTitle(username + " inbox");
        inboxFrame.setSize(500,500);
        inboxFrame.setVisible(true);
    }
}
