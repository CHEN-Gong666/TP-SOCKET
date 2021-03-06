/*
 EchoServer
 Example of a TCP server
 Date: 10/01/04
 Authors:
 */

package ChatTCP;

import java.io.*;
import java.net.*;

public class EchoServer  {

 	/**
  	* receives a request from client then sends an echo to the client
  	**/
	static void doService(Socket clientSocket) {
    	  try {
    		BufferedReader socIn;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		while (true) {
    		  String line = socIn.readLine();
          System.out.println("Message received from " 
            + clientSocket.getInetAddress()+ ": " + line);
    		  socOut.println(line);
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  
 	/**
  	* main method
	* @param args EchoServer port
  	* 
  	**/
       public static void main(String[] args){
        ServerSocket listenSocket;
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
  	try {
  		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
  		while (true) {
        System.out.println("Waiting for clients...");
  			Socket clientSocket = listenSocket.accept();
  			System.out.println("connexion from:" + clientSocket.getInetAddress());
        doService(clientSocket);
  		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
  }

  
