package zad1.LanguageServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.rmi.UnknownHostException;
import java.util.HashMap;

public class RequestHandlerLan extends Thread {
    private Socket connection;
    private BufferedReader in = null;
    private PrintWriter out = null;

    public HashMap<String, String> words;

    public RequestHandlerLan(Socket connection, HashMap<String, String> words) {
        this.connection = connection;
        this.words= words;
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
            String line = in.readLine();
            String modifiedString = Character.toUpperCase(line.toLowerCase().charAt(0)) + line.toLowerCase().substring(1);
            String[] tab = modifiedString.split(" ");
            try (
                    Socket socket = new Socket(tab[1],Integer.parseInt(tab[2]));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                if(words.get(tab[0])!=null) {
                    System.out.println("Connected to the server " + tab[1] + "(Client) on the port " + tab[2]);
                    out.println("Answer: " + words.get(tab[0]));
                }else{
                    out.println("We don`t have translation for this word");
                }
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + tab[2]);
            } catch (IOException e) {
                System.err.println("Error connecting to the main server: " + e.getMessage());
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
