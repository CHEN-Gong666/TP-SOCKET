/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class EchoServerMultiThreaded {
	private static LinkedList<String> chatHistory = new LinkedList<>();
	// private static final String filePath = "stream/ChatHistory.txt";
	private static final String filePath = "C:\\Users\\jeffc\\IdeaProjects\\TP-SOCKET\\src\\stream\\ChatHistory.txt" ;
	private static final File file = new File(filePath);

	public static LinkedList<String> getChatHistory() {
		return chatHistory;
	}

	public static int addHistory(String msg) {
		chatHistory.add(msg);
		// serializeMessage(msg);
		return 1;
	}

	/**
	 * main method
	 *
	 * @param EchoServer port
	 **/
	public static void main(String args[]) {
		ServerSocket listenSocket;
		if (args.length != 1) {
			System.out.println("Usage: java EchoServer <EchoServer port>");
			System.exit(1);
		}
		try {
			listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
			// loadChatHistory();
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

	private static int loadChatHistory() throws IOException {
		System.out.println("loading message");
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine();
			while (line != null) {
				line = br.readLine();
				chatHistory.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	private static int serializeMessage(String msg) {
		System.out.println("serializing message");

		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(file));
			printWriter.println(msg);
			// out.flush();
			printWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 1;
	}
}

