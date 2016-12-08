import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Master {

    // Port that will be opened
    int port = 8888;
    int millisecondsTimeout = 50;
    // List that will fill upp with connected clients
    List<Socket> sockets = new ArrayList<Socket>();

    ServerSocket servSock;
    public static void main(String[] args) throws IOException
    {
        // Instantiate the server
        Master serv = new Master();
        // Open the given port
        serv.Open();
        // Wait for incoming connections
        while (true)
        {
            // Constantly looks for new connections
            serv.AcceptClients();
            // When someone connected, read the data
            if (serv.sockets.size() > 0)
                serv.ReadIncomingData();
        }
    }
    void Open() throws IOException
    {
        servSock = new ServerSocket(port);
        servSock.setSoTimeout(millisecondsTimeout);
        System.out.println("Launching TCP listener server on port: "+port);
    }
    static int count = 0;
    void AcceptClients() throws IOException
    {
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
    void ReadIncomingData() throws IOException
    {
        boolean incData = false;
        for (int i = 0; i < sockets.size(); ++i)
        {
            Socket sock = sockets.get(i);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            if (!in.ready())
                continue;
            String line = in.readLine();
            if (line.length() <= 1)
                incData = true;
            System.out.println("Message: "+line);
            // Reply to sender
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            out.println("Confirmed");
            out.flush();
        }
    }
}
