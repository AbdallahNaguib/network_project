import java.io.*;
import java.util.Vector;

class FileInterface {
    private static String fileName =
            "E:\\My work\\Java\\network_project\\src\\Emails.txt";
    static Vector<EmailData> readData(String name) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fileReader);
        Vector<EmailData> res = new Vector<>();
        while(true){
            String from = br.readLine();
            if(from == null)break;
            String to = br.readLine();
            String sub = br.readLine();
            String msg = br.readLine();
            if(to.equals(name)){
                EmailData emailData=new EmailData(from , to , sub , msg);
                res.add(emailData);
            }
        }
        br.close();
        return res;
    }
    static void addRecord(EmailData ed) throws IOException {
        FileWriter fw = new FileWriter(fileName , true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.append(ed.from + '\n');
        bw.append(ed.to + '\n');
        bw.append(ed.sub + '\n');
        bw.append(ed.msg + '\n');
        bw.close();
    }
}
