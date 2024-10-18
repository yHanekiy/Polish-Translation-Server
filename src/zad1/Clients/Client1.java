package zad1.Clients;


import zad1.LanguageServer.RequestHandlerLan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Scanner;

public class Client1 {

    private ServerSocket ss;
    private BufferedReader in = null;
    private volatile boolean serverRunning = true;
    public Client1(ServerSocket ss) {
        this.ss = ss;
        System.out.println("listening at port: " + ss.getLocalPort());
        System.out.println("bind address: " + ss.getInetAddress());
    }
    public void run() {
        while (serverRunning) {
            try {
                Socket conn = ss.accept();
                System.out.println("Connection established by " + ss.getLocalPort());
                try {
                    in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    System.out.println(in.readLine());
                    try {
                        conn.close();
                    } catch (Exception e) {
                    }
                    break;
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            InetSocketAddress isa = new InetSocketAddress(host, port);
            ss = new ServerSocket();
            ss.bind(isa);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        Client1 c = new Client1(ss);
        while (true) {
            System.out.println("Write the message to the main Server or \"Exit\" to exit");
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if (message.split(" ").length==2) {
                String serverAddress = "localhost";
                int serverPort = 12345;
                Socket socket = null;
                try {
                    socket = new Socket(serverAddress, serverPort);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    System.out.println("Connected to the main server " + serverAddress + " on the port " + serverPort);

                    out.println(message+" "+args[1]);
                    c.run();
                } catch (UnknownHostException e) {
                    System.err.println("Unknown host: " + serverAddress);
                } catch (Exception e) {
                    System.err.println("Error connecting to the main server: " + e.getMessage());
                } finally {
                    try {
                        if (socket!= null) {
                            socket.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }else{
                if (message.toLowerCase().equals("exit")){
                    break;
                }
                System.out.println("Not correct type of data");
            }
        }
    }
}
