/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientThread
	extends Thread {
	
  private static ArrayList<Socket> socketList = new ArrayList<Socket>();
	private Socket clientSocket;
	
	ClientThread(Socket s) {
		this.clientSocket = s;
		socketList.add(s);
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		// PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		PrintStream socOut;
    		while (true) {

			    String line = socIn.readLine();
			    for(Socket socket: socketList){
			    	if (socket != this.clientSocket) {
				    	socOut = new PrintStream(socket.getOutputStream());
				    	socOut.println("port: " + socket.getPort() + 
				    			", LocalAddress:" + socket.getLocalAddress() + 
				    			line);
			    	}
			    }
	        	
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  
  }

  
