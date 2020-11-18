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
import java.util.LinkedList;

public class ClientThread extends Thread {
	private static LinkedList<String> chatHistory = EchoServerMultiThreaded.getChatHistory();
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
			  System.out.println("thread is running for port: " + clientSocket.getPort());
			  BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		// PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		PrintStream socOut;
    		while (true) {

			    String line = socIn.readLine();
				System.out.println("message from port " + clientSocket.getPort() + ": " + line);
				System.out.println(socketList);
				if (line.equals(".")) {
					socketList.remove(this.clientSocket);
					System.out.println(socketList);
					this.stop();
					break;
				}
				for (String m: chatHistory) {
					socOut = new PrintStream(clientSocket.getOutputStream());
					socOut.println("port: " + clientSocket.getPort() + ": " + line);
				}

			    for(Socket socket: socketList){
			    	if (socket != this.clientSocket) {
						socOut = new PrintStream(socket.getOutputStream());
						socOut.println("port: " + socket.getPort() + ": " + line);
			    	}
			    }
    		}

		  } catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e);
        }
	}

  }

  
