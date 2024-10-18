package zad1.LanguageServer;


import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;


public class LanguageServer{
    private ServerSocket ss;
    private volatile boolean serverRunning = true;
    public static HashMap<String, String> map = new HashMap<>();
    public LanguageServer(ServerSocket ss){
        this.ss = ss;
        System.out.println("listening at port: " + ss.getLocalPort());
        System.out.println("bind address: " + ss.getInetAddress());
        run();
    }
    public void run() {
        while (serverRunning) {
            try {
                Socket conn = ss.accept();

                System.out.println("Connection established by " + ss.getLocalPort() + " Lan");

                RequestHandlerLan requestHandlerLan = new RequestHandlerLan(conn, map);
                requestHandlerLan.start();

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        try {
            ss.close();
        } catch (Exception exc) {
        }
    }


    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            String host = args[1];
            int port = Integer.parseInt(args[2]);
            InetSocketAddress isa = new InetSocketAddress(host, port);
            ss =  new ServerSocket();
            ss.bind(isa);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            String line;
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] tab = line.split(" ");
                map.put(tab[0],tab[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new LanguageServer(ss);

    }
}
