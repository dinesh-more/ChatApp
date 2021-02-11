import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending Request to Server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection Done...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        // Thread1 - Reading
        Runnable r1 = () -> {
            System.out.println("Reading Started...");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server Terminated the Chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection Closed");
            }
        };

        new Thread(r1).start();
    }

    public void startWriting() {
        // Thread2 -Wrinting
        Runnable r2 = () -> {
            System.out.println("Writer Started...");
            try {
                while (true && !socket.isClosed()) {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
                    String content = bReader.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
                System.out.println("Connection Closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Client");
        new Client();
    }
}