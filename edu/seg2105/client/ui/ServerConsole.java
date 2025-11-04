package edu.seg2105.client.ui;
import java.util.Scanner;
import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 */

public class ServerConsole implements ChatIF {
    //Class variables *************************************************
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 
  EchoServer server;
    /**
     * The instance of the server that created this ConsoleChat.
     */

    //Constructors ****************************************************

    /**
     * Constructs an instance of the server console.
     *
     * @param client The chat client for this console.
     */
    public ServerConsole(EchoServer server) {
        this.server = server;
        fromConsole = new Scanner(System.in);
    }

    //Instance methods ************************************************
    
    /**
     * This method waits for input from the console.  Once it is 
     * received, it sends it to the client's message handler.
     */
    public void accept() 
    {
        try
        {

        String message;

        while (true) 
        {
            message = fromConsole.nextLine();
            server.handleMessageFromAdmin(message);
        }
        } 
        catch (Exception ex) 
        {
        display("Unexpected error while reading from console!");
        }
    }
        /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message){
        System.out.println("SERVER MSG> " + message);
    }
}
