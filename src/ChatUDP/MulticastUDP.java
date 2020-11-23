package ChatUDP;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedList;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class MulticastUDP extends JFrame{

    private static final LinkedList<String> chatHistory = new LinkedList<>();

    static JTextArea Name;
    static JTextArea TextOut;
    static JTextField TextIn;
    static JPanel Panel;
    static JButton Send;
    static JButton ChangeName;
    static JScrollPane Scroll;

    String clientName = "pseudo";
    String line;
    InetAddress groupAddr;
    int groupPort = 50000;
    MulticastSocket socket = null;
    DatagramPacket msg;

    public static void main(String[] args) throws IOException {
        MulticastUDP aUDP = new MulticastUDP();

        BufferedReader stdIn = null;
        aUDP.groupAddr = InetAddress.getByName("224.0.0.233");
        //int groupPort = 50000;

        //DatagramPacket msg;
        ClientThreadUDP clientThreadUDP = null;

        if (args.length == 2) {
            aUDP.groupAddr = InetAddress.getByName(args[0]);
            aUDP.groupPort = new Integer(args[1]).intValue();
        }

        try {

            JFrame f =new JFrame();
            f.setSize(550,440);
            Panel = new JPanel();
            f.add(Panel);


            TextOut = new JTextArea(20,45);
            TextOut.setBorder(BorderFactory.createLineBorder(Color.gray,5));
            TextOut.setText("welcome to the chat system\n");
            TextOut.setEditable(false);
            JScrollPane Scroll = new JScrollPane(TextOut);
            Scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            Send = new JButton("send");
            Send.addActionListener(new MyMonitor(aUDP));

            ChangeName = new JButton("Change Name");
            ChangeName.addActionListener(new MyMonitor(aUDP));
        /*ChangeName.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        JTextArea nta = (JTextArea)e.getSource();
                    }
                });
        */

            TextIn = new JTextField(39);
            Name = new JTextArea(1,15);
            Name.setText("pseudo");

            //Panel.add(TextOut);
            Panel.add(Scroll);
            Panel.add(TextIn);
            Panel.add(Send);
            Panel.add(Name);
            Panel.add(ChangeName);

            f.setVisible(true);
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            TextOut.append("enter your user name\n");
            //System.out.print( "Please enter your name : \n" );

            aUDP.socket = new MulticastSocket(aUDP.groupPort);
            aUDP.socket.joinGroup(aUDP.groupAddr);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            clientThreadUDP = new ClientThreadUDP(aUDP.socket);
            clientThreadUDP.start();

            System.out.println(chatHistory);
            for (String m: chatHistory) {
                System.out.println(m);
            }

            String connexionMsg = aUDP.clientName + " joins the chat\n";
            aUDP.msg = new DatagramPacket(connexionMsg.getBytes(),connexionMsg.length(),aUDP.groupAddr,aUDP.groupPort);
            aUDP.socket.send(aUDP.msg);


        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        while (true) {

            //aUDP.line =stdIn.readLine();
            if (aUDP.line.equals(".")) {
                String deconnexionMsg = aUDP.clientName + " leaves the chat";
                chatHistory.add(deconnexionMsg);
                aUDP.msg = new DatagramPacket(deconnexionMsg.getBytes(),deconnexionMsg.length(),aUDP.groupAddr,aUDP.groupPort);
                aUDP.socket.send(aUDP.msg);
                aUDP.socket.leaveGroup(aUDP.groupAddr);
                clientThreadUDP.stop();
                System.out.println("before break");
                break;
            }

            //String msgString = aUDP.clientName + " : " + aUDP.line;
            //aUDP.msg = new DatagramPacket(msgString.getBytes(), msgString.length(), aUDP.groupAddr, aUDP.groupPort);
            //TextOut.append(clientName + " : " + line+'\n');
            //aUDP.socket.send(aUDP.msg);

        }

        stdIn.close();
        System.exit(0);
    }

    private static class ClientThreadUDP extends Thread{
        private final MulticastSocket socket;
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
                    TextOut.append(message);
                }
            } catch (Exception e) {
                System.err.println("Error in UDPClientReception:" + e);
            }
        }

    }

}

class MyMonitor implements ActionListener {
    MulticastUDP mf = null;
    public MyMonitor(MulticastUDP ttmf) {
        this.mf = ttmf;
    }
    public void actionPerformed(ActionEvent e) {
        String buttonName = e.getActionCommand();
        //System.out.println(buttonName);
        if(buttonName.equals("Change Name")){
            String oldName = mf.clientName;
            mf.clientName = mf.Name.getText();
            //mf.TextOut .append("You've changed your name to:"+mf.clientName+'\n');
            String msgString = "user: "+oldName+" changed his name to: "+mf.clientName+'\n';
            mf.msg = new DatagramPacket(msgString.getBytes(), msgString.length(), mf.groupAddr, mf.groupPort);
            try {
                mf.socket.send(mf.msg);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else {
            if (buttonName.equals("send")) {
                mf.line = mf.TextIn.getText();
                mf.TextIn.setText("");
                if (mf.line.equals(".exit")){
                    String msgString = "User: "+mf.clientName + " has left the chat\n";
                    mf.msg = new DatagramPacket(msgString.getBytes(), msgString.length(), mf.groupAddr, mf.groupPort);
                    try {
                        mf.socket.send(mf.msg);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    System.exit(0);
                }else {
                    String msgString = mf.clientName + " : " + mf.line + '\n';
                    mf.msg = new DatagramPacket(msgString.getBytes(), msgString.length(), mf.groupAddr, mf.groupPort);
                    //mf.TextOut.append(mf.clientName + " : " + mf.line+'\n');
                    try {
                        mf.socket.send(mf.msg);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                }
            } else {
                JOptionPane.showMessageDialog(mf, "Unknown event" );
            }
        }
    }
}



