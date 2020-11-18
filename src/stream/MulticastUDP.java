package stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedList;

public class MulticastUDP {
    private static LinkedList<String> chatHistory = new LinkedList<>();

    public static void main(String[] args) throws IOException {

        MulticastSocket socket = null;
        BufferedReader stdIn = null;
        InetAddress groupAddr = InetAddress.getByName("224.0.0.233");
        int groupPort = 50000;
        String clientName = "peuso";
        String line;
        DatagramPacket msg;
        ClientThreadUDP clientThreadUDP = null;

        if (args.length == 2) {
            groupAddr = InetAddress.getByName(args[0]);
            groupPort = new Integer(args[1]).intValue();
        }

        try {
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print( "Please enter your name : " );
            clientName = stdIn.readLine();

            socket = new MulticastSocket(groupPort);
            socket.joinGroup(groupAddr);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            clientThreadUDP = new ClientThreadUDP(socket);
            clientThreadUDP.start();

            System.out.println(chatHistory);
            for (String m: chatHistory) {
                System.out.println(m);
            }

            String connexionMsg = clientName + " joins the chat";
            msg = new DatagramPacket(connexionMsg.getBytes(),connexionMsg.length(),groupAddr,groupPort);
            socket.send(msg);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        while (true) {
            line=stdIn.readLine();
            if (line.equals(".")) {
                String deconnexionMsg = clientName + " leaves the chat";
                chatHistory.add(deconnexionMsg);
                msg = new DatagramPacket(deconnexionMsg.getBytes(),deconnexionMsg.length(),groupAddr,groupPort);
                socket.send(msg);
                socket.leaveGroup(groupAddr);
                clientThreadUDP.stop();
                System.out.println("before break");
                break;
            }
            String msgString = clientName + " : " + line;
            msg = new DatagramPacket(msgString.getBytes(),msgString.length(),groupAddr,groupPort);
            socket.send(msg);
        }

        stdIn.close();
        System.exit(0);
    }

    private static class ClientThreadUDP extends Thread{
        private MulticastSocket socket;
        ClientThreadUDP(MulticastSocket socket) {
            super();
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    byte[] buf = new byte[256];
                    DatagramPacket recv = new DatagramPacket(buf,buf.length);
                    socket.receive(recv);
                    String message = new String(recv.getData(),0,recv.getLength());
                    System.out.println(message);
                }
            } catch (Exception e) {
                System.err.println("Error in UDPClientReception:" + e);
            }
        }

    }

}