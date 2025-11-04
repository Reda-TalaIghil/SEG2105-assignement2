// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    System.out.println(msg.toString());
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    if(message.charAt(0)=='#'){
      handleCommand(message);
      return;
    }
    else{
      sendToServer("> " + message);
    }
  }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
protected void connectionException(Exception exception) {
    clientUI.display("Connection to server lost. Terminating client.");
    quit();
  }



  protected void connectionClosed() {
    clientUI.display("Connection closed by server.");
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
        this.quit();
        clientUI.display("Client "+ this.getInetAddress() + " has quit.");
    }
    if(command.equals("logoff")){
      try {
        this.closeConnection();
        clientUI.display("You have been logged off from the server.");
      } catch (IOException e) {
        clientUI.display("Error logging off: " + e.getMessage());
      }

    }
    if(command.equals("sethost")){
      String newHost = parts[1];
      this.setHost(newHost);
      clientUI.display("Host has been set to: " + newHost);
    }
    if(command.equals("setport")){
      int newPort = Integer.parseInt(parts[1]);
      this.setPort(newPort);
      clientUI.display("Port has been set to: " + newPort);
    }
    if(command.equals("login")){
      try {
        if(this.isConnected()){
          clientUI.display("You are already connected to the server.");
          return;
        }
        this.openConnection();
        clientUI.display("You are now logged in to the server.");
      } catch (IOException e) {
        clientUI.display("Error logging in: " + e.getMessage());
      }

    }
    if(command.equals("gethost")){
      String currentHost = this.getHost();
      clientUI.display("Current host: " + currentHost);
    }
    if(command.equals("getport")){
      int currentPort = this.getPort();
      clientUI.display("Current port: " + currentPort);
    }
  }
}
//End of ChatClient class
