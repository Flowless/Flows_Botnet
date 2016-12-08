import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Zombie
{
    Socket sock;
    String ip_addr;
    int port;
    PrintWriter out;
    BufferedReader in;

    Zombie(String ipaddress, int targetPort)
    {
        ip_addr = ipaddress;
        port = targetPort;
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        // Default target is localhost
        String ip = "127.0.0.1";
        int port = 8888;
        // If anything else given, assign that IP address instead
        if (args.length > 0)
            ip = args[0];
        // If two arguments are given, port will be second one
        if (args.length > 1)
            port = Integer.parseInt(args[1]);
        // Instantiate the sender
        Zombie sender = new Zombie(ip, port);
        if (!sender.Connect())
            return;
        int sleepTime = 1000;
        System.out.println("Writing every "+sleepTime+" ms");
        while (true)
        {
            // Data that is to be sent to Server
            // This part is what will be modified with commands or w/e
            sender.Write("www.google.com");
            // Read what Server replied
            sender.Read();

            Thread.sleep(sleepTime);
            if (sender.sock.isClosed() || !sender.sock.isConnected())
            {
                System.out.println("Socket closed");
                return;
            }
        }
    }
    boolean Connect() throws IOException
    {
        try {
            System.out.println("Connecting to "+ip_addr+":"+port);
            sock = new Socket(ip_addr, port);
        }
        catch (java.net.ConnectException e)
        {
            System.out.println(e.toString());
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        System.out.println("Connected!");
        // Instantiates send and receive stream
        out = new PrintWriter(sock.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        return true;
    }
    void Write(String text) throws IOException
    {
        //Prints out text and flushes
        out.println(text);
        out.flush();
    }
    void Read() throws IOException
    {
        if (!in.ready())
            return;
        // Reads one line at a time and prints out to Zombie-console
        String line = in.readLine();
        System.out.println("Reply: "+line);
    }

}
