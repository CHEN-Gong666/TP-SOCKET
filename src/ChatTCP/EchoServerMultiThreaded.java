/*
 EchoServerMultithread
 Server side main program
 Date: 24/11/2020
 Authors: Muye HE, Gong CHEN
 */

package ChatTCP;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class EchoServerMultiThreaded {
	private static LinkedList<String> chatHistory = new LinkedList<>();
	private static final String filePath = "C:\\Users\\hmy82\\TP-SOCKET\\TP-SOCKET\\src\\ChatTCP\\ChatHistory.txt";
	// private static final String filePath = "C:\\Users\\jeffc\\IdeaProjects\\TP-SOCKET\\src\\stream\\ChatHistory.txt" ;
	private static final File file = new File(filePath);

	public static LinkedList<String> getChatHistory() {
		return chatHistory;
	}

	public static void addHistory(String msg) {

		System.out.println(chatHistory);
		chatHistory.add(msg);
		serializeMessage(msg);
		//return 1;
	}

	/**
	 * main method
	 * @param args the server port number
	 **/
	public static void main(String[] args) {
		ServerSocket listenSocket;
		if (args.length != 1) {
			System.out.println("Usage: java EchoServer <EchoServer port>");
			System.exit(1);
		}
		try {
			listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
			loadChatHistory();
			System.out.println("Server ready...");

			while (true) {
				Socket clientSocket = listenSocket.accept();
				System.out.println("Connexion from:" + clientSocket.getInetAddress() + "on port " + clientSocket.getPort());
				ClientThread ct = new ClientThread(clientSocket);
				ct.start();
			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}



	/**
	 * load chat history method
	 **/
	private static void loadChatHistory() throws IOException {
		System.out.println("loading message");
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(reader);
			// br.reset();
			String line = br.readLine();
			while (line != null) {
				chatHistory.add(line);
				//EchoClient.TextOut.append(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 1;
	}
	/**
	 *make the message in a good format
	 **/
	private static void serializeMessage(String msg) {
		msg = "message from:" +msg;
		try {
			FileOutputStream fos = new FileOutputStream(filePath, true);
			fos.write(msg.getBytes());
			fos.write("\r\n".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		//return 1;
	}
}

