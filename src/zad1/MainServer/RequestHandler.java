package zad1.MainServer;

import java.net.*;
import java.io.*;
import java.rmi.UnknownHostException;
import java.util.HashMap;

public class RequestHandler extends Thread {
    private Socket connection;
    private BufferedReader in = null;
    private PrintWriter out = null;

    public  HashMap<String, Integer> languages;
    public RequestHandler(Socket connection, HashMap<String, Integer> languages) {
        this.connection = connection;
        this.languages = languages;
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            out = new PrintWriter(
                    connection.getOutputStream(), true);
        } catch (Exception exc) {
            exc.printStackTrace();
            try { connection.close(); } catch(Exception e) {}
        }
    }

    public void run() {
        try {
            String serverAddress = "localhost";
            String[] tab = in.readLine().split(" ");
            int serverPort;
            if (languages.get(tab[1].toUpperCase())!=null) {
                serverPort = languages.get(tab[1].toUpperCase());
                try (
                        Socket socket = new Socket(serverAddress, serverPort);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    System.out.println("Connected to the server " + serverAddress + "(Lan) on the port " + serverPort);
                    out.println(tab[0]+" "+"localhost"+" "+ tab[2] );
                } catch (UnknownHostException e) {
                    System.err.println("Unknown host: " + serverAddress);
                } catch (IOException e) {
                    System.err.println("Error connecting to the server");
                    Socket socket = new Socket("localhost",Integer.parseInt(tab[2]));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("Server with this languages is not found");
                }
            }else {
                out.println("Server not found");
            }
        } catch (IOException exc) {
            out.println("Not correct format of data");
        } finally {
            try {
                connection.close();
                connection = null;
            } catch (Exception exc) { }
        }
    }

}

