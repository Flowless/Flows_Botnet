import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
  // Port that will be opened
  int port = 8888;
  int millisecondsTimeout = 50;
  // List that will fill upp with connected clients
  List<Socket> sockets = new ArrayList<Socket>();

  ServerSocket servSock;

  public void main(String[] args) throws IOException {
    // Instantiate the server
    Controller serv = new Controller();
    // Open the given port
    serv.Open();
    // Wait for incoming connections
    while (true)
    {
      // Constantly looks for new connections
      serv.AcceptClients();
      // Once client connected, send commands and receive replies
      if (serv.sockets.size() > 0) {
        sendCommand();
        receiveReply();
      }
    }
  }

  static void sendCommand() {
    //Randomize a integer which will choose [i] of command array

    //Set sleeptime for the thread, 3 secs between commands

    //The chosen command will be sent to away

    //Initialize PrintWriter and make it work
  }

  void receiveReply() throws IOException {  //Read what is received and print out to console
    boolean incData = false;
    //Does the process for all connected clients/sockets
    for (int i = 0; i < sockets.size(); ++i) {
      Socket sock = sockets.get(i);
      //Initialize a BufferedReader
      BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      if (!in.ready()) {
        continue;
      }
      // Read in the line
      String line = in.readLine();
      if (line.length() <= 1) {
        incData = true;
      }
      // Print out the received line
      System.out.println("Message: "+line);
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
