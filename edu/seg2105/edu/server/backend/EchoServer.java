package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;
import edu.seg2105.client.ui.ServerConsole;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }

  /** 
   * this method prints a message when a new client connects.
   */
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("Client connected: " + client);
  }
  /** 
   * this method prints a message when a client disconnects.
   */
  protected void clientDisconnected(ConnectionToClient client) {
    super.clientDisconnected(client);
    System.out.println("Client disconnected: " + client);
  }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  protected void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println("Client " + client.toString() + " disconnected with exception: " + exception);
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  /**
   * This method handles messages from the server console.
   *
   * @param message The message from the console.
   */
  public void handleMessageFromAdmin(String message)
  {
    if(message.charAt(0)=='#'){
      handleCommand(message);
      return;
    }
    else{
      System.out.println("SERVER MSG> " + message);
      this.sendToAllClients("SERVER MSG> " + message);
      return;
    }
  }

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    ServerConsole server = new ServerConsole(sv);

    try 
    {
      sv.listen(); //Start listening for connections
      server.accept();  //Wait for console data
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  /**
   * This method handles any commands received from the client.
   *
   * @param msg The command received from the client.
   */
  public void handleCommand(String msg){
    String[] parts = msg.split(" ");
    String command= parts[0].substring(1);
    if(command.equals("quit")){
        try{
          close();
          System.exit(0);
        }
        catch(IOException e){
          System.out.println(">Error closing the server: " + e.getMessage());
          return;
        }
        System.out.println(">Server has quit.");
    }
    if(command.equals("stop")){
      try {
        if(!this.isListening()){
          System.out.println(">Server is not currently listening for connections.");
          return;
        }
        } catch (Exception e) {
          System.out.println(">Error checking server status: " + e.getMessage());
          return;
        }
        stopListening();
      }

    if(command.equals("close")){
      try {
        if(!this.isListening()){
          System.out.println(">Server is not currently listening for connections.");
          return;
        }
        } catch (Exception e) {
          System.out.println(">Error checking server status: " + e.getMessage());
          return;
        }
        try{
          close();
        }
        catch(IOException e){
          System.out.println(">Error closing the server: " + e.getMessage());
          return;
        }
        System.out.println(">Server has stopped listening for connections and disconnected every client.");
      }
      
    if(command.equals("setport")){
      if(this.isListening()){
        System.out.println(">Cannot set port while server is listening for connections.");
        return;
      }
      if(getNumberOfClients()>0){
        System.out.println(">Cannot set port while clients are connected.");
        return;
      }
      else if(getNumberOfClients()==0){
      int newPort = Integer.parseInt(parts[1]);
      setPort(newPort);
      System.out.println(">Port has been set to: " + newPort);
      }

    }
    if(command.equals("start")){
      try {
        if(this.isListening()){
          System.out.println(">Server is already listening for connections.");
          return;

        }
        else{
          listen();
          System.out.println(">Server has started listening for connections.");
        } 
      }catch (Exception e) {
          System.out.println(">Error checking server status: " + e.getMessage());
          return;
        }

    }
    if(command.equals("getport")){
      System.out.println(">Current port: " + this.getPort());
    }

  }
}
//End of EchoServer class
