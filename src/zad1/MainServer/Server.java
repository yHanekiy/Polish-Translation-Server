package zad1.MainServer;

import java.net.*;
import java.util.HashMap;

public class Server extends Thread {

    private ServerSocket ss;

    private volatile boolean serverRunning = true;

    private String serverTID;

    public static HashMap<String, Integer> languages = new HashMap(){
        {
            put("EN",100);
            put("IT",101);
            put("DE",102);
        }
    };
    public Server(String serverTID,
                  ServerSocket ss) {
        this.serverTID = serverTID;
        this.ss = ss;
        System.out.println("Server " + serverTID + " started");
        System.out.println("listening at port: " + ss.getLocalPort());
        System.out.println("bind address: " + ss.getInetAddress());

        start();
    }


    public void run() {
        while (serverRunning) {
            try {
                Socket conn = ss.accept();

                System.out.println("Connection established by " + serverTID);

                serviceRequests(conn);

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        try { ss.close(); } catch (Exception exc) {}
    }

    private void serviceRequests(Socket connection) {
        RequestHandler requestHandler = new RequestHandler(connection, languages);
        requestHandler.start();
    }

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            InetSocketAddress isa = new InetSocketAddress(host, port);
            ss =  new ServerSocket();
            ss.bind(isa);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        for (int i=0; i<5;i++){
            new Server("Serv thread "+(i+1),ss);
        }
    }

}
