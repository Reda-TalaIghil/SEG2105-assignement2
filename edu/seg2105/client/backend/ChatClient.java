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

  /**
   * The login ID of the client.
   */
  private String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI,String loginID) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
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
    System.out.println(">" + msg.toString());
    
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
      sendToServer(message);
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
  /**
    * This method handles connection exceptions.
    *
    * @param exception The exception raised.
    */

  protected void connectionException(Exception exception) {
      clientUI.display("The server has shut down.");
      quit();
    }


    /**
   * This method handles the event of a closed connection.
   */
  protected void connectionClosed() {
    clientUI.display("Connection closed by server.");
  }



  /**
   * This method handles the event of a stablished connection.
   */
  protected void connectionEstablished() {
    clientUI.display("Connection established with server.");
    try {
      sendToServer("#login " + this.loginID);
    } catch (IOException e) {
      clientUI.display("Error sending login ID to server: " + e.getMessage());
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


  /**
   * This method returns the login ID of the client.
   *
   * @return The login ID.
   */
  public String getLoginID(){
    return this.loginID;
  }
}
//End of ChatClient class
