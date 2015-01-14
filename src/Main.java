/**
 * Created by Admin on 01.01.2015.
 */
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(2301);
        server.start();

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        server.interrupt();
//        sysInfo();
    }

    public static void sysInfo() {
        String info = System.getProperty("os.name");
        System.out.println(info);
        info = System.getProperty("os.version");
        System.out.println(info);
        info = System.getProperty("file.encoding");
        System.out.println(info);
        info = System.getProperty("file.separator");
        System.out.println(info);
        info = System.getProperty("java.io.tmpdir");
        System.out.println(info);
        info = System.getProperty("line.separator");
        System.out.println(info);
        info = System.getProperty("user.country");
        System.out.println(info);
        info = System.getProperty("user.name");
        System.out.println(info);
    }
}

class Server extends Thread {
    private AtomicInteger requestNumber;
    private int port;


    public Server(int port) {
        requestNumber = new AtomicInteger();
        this.port = port;
    }

    public String getSystemInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("REQUEST #");
        sb.append(requestNumber.incrementAndGet() + "===>");
        sb.append("OS NAME: ");
        sb.append(System.getProperty("os.name") + "===>");
        sb.append("OS VERSION: ");
        sb.append(System.getProperty("os.version") + "===>");
        sb.append("ENCODING: ");
        sb.append(System.getProperty("file.encoding") + "===>");
        sb.append("COUNTRY: ");
        sb.append(System.getProperty("user.country") + "===>");
        sb.append("SERVER TIME: " + currentDateTime());

        return sb.toString();
    }

    public String currentDateTime() {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return myFormat.format(new Date());
    }

    @Override
    public void run() {
//        super.run();
        ServerSocket server = null;
        Socket client;
        try {
            server = new ServerSocket(port);
            System.out.println("Server started at " + port + " port at " + currentDateTime());
            while(!isInterrupted()) {
                client = server.accept();
                if (client.isConnected()) {
                    System.out.println("Connected client at " + client.getInetAddress().toString() + " TIME: " + currentDateTime());
                    OutputStream os = client.getOutputStream();
                    os.write(getSystemInfo().getBytes());
                    os.flush();
                    os.close();

                    try {
                        client.close();
                    } catch (IOException e) {
                        System.err.println("Client IOException: " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            }

        } catch (IOException e) {
            System.err.println("Server IOException: " + e.getMessage());
            e.printStackTrace();
        } finally {

            if(server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    System.err.println("Server-closing IOException: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}

