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
    // Read in the commands sent from Master
    while (true) {
      sender.Read();
      // Reply to the commands
      sender.Reply();
      // Sleep to avoid spam
      int sleepTime = 1000; //milliseconds
      Thread.sleep(sleepTime);
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
    String command = in.readLine();
    System.out.println("Command: "+ command);
    // use method action();
    action(command);
  }

  void action(String command) {

  }

  void Reply() throws IOException {
    out.println("<reply>");
    out.flush();
  }

}
