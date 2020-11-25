/*
 ClientThread
 Example of a TCP server
 Date: 14/12/08
 Authors:
 */

package ChatTCP;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;


public class ClientThread extends Thread {
	private static LinkedList<String> chatHistory = EchoServerMultiThreaded.getChatHistory();
  	private static ArrayList<Socket> socketList = new ArrayList<>();
	private Socket clientSocket;

	ClientThread(Socket s) {
		this.clientSocket = s;
		socketList.add(s);
	}
	/**
	 * receives a request from client then sends an echo to the client
	 **/

	public void run() {
    	  try {
			  System.out.println("thread is running for port: " + clientSocket.getPort());
			  BufferedReader socIn;
    			socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    			// PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
			  PrintStream socOut;

			  for (String m: chatHistory) {
				  socOut = new PrintStream(clientSocket.getOutputStream());
				  socOut.println(m);
			  }

    		while (true) {

				String line = socIn.readLine();
				//String outputLine = "Message from port " + clientSocket.getPort() + ": " + line+'\n';
				String outputLine = line;
				if (line.equals(".exit")) {
					socketList.remove(this.clientSocket);
					System.out.println(socketList);
					this.stop();
					break;
				}

				EchoServerMultiThreaded.addHistory(outputLine);

				System.out.println(outputLine);
				System.out.println(socketList);

			    for(Socket socket: socketList){
					socOut = new PrintStream(socket.getOutputStream());
					//System.out.println(outputLine);
					socOut.println(outputLine);
			    }
    		}

		  } catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e);
        }
	}

  }

  
