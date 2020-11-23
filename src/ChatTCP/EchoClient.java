/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package ChatTCP;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;



public class EchoClient extends JFrame {

    static String clientName = "pseudo";
    public static String getClientName(){
        return clientName;
    }
    static JTextArea Name;
    static JTextArea TextOut;
    static JTextField TextIn;
    static JPanel Panel;
    static JButton Send;
    static JButton ChangeName;
    static JScrollPane Scroll;

    static Socket echoSocket;
    static PrintStream socOut;
    static BufferedReader stdIn;
    static BufferedReader socIn;

    static String line;
 
  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/

    public static void main(String[] args) throws IOException {
        EchoClient aClient = new EchoClient();
    	
        echoSocket = null;
        socOut = null;
        stdIn = null;
        socIn = null;

        if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
          System.exit(1);
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
            Send.addActionListener(new MyMonitor(aClient));

            ChangeName = new JButton("Change Name");
            ChangeName.addActionListener(new MyMonitor(aClient));

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

      	    // creation socket ==> connexion
      	    echoSocket = new Socket(args[0], Integer.parseInt(args[1]));
	        socIn = new BufferedReader(
	    		          new InputStreamReader(echoSocket.getInputStream()));    
	        socOut= new PrintStream(echoSocket.getOutputStream());
	        stdIn = new BufferedReader(new InputStreamReader(System.in));
	        System.out.println("Connected to host " + args[0] + " at port " + args[1]);

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }

        System.out.println("please enter your name");
        clientName = stdIn.readLine();

        while (true) {

        	if (stdIn.ready()){
	        	line=stdIn.readLine();
        		socOut.println(line);
                if (line.equals(".")) {
                    break;
                }
            }

        	if (socIn.ready()){
        		System.out.println("echo: "+socIn.readLine());
        	}
        	
        	try {
        		Thread.sleep(300);
        	} catch (Exception e){
        		System.err.println("Sleep exception");
        	}
        }
      socOut.close();
      socIn.close();
      stdIn.close();
      echoSocket.close();
    }
}

class MyMonitor implements ActionListener {
    EchoClient mf = null;
    public MyMonitor(EchoClient ttmf) {
        this.mf = ttmf;
    }
    public void actionPerformed(ActionEvent e) {
        String buttonName = e.getActionCommand();
        if(buttonName.equals("Change Name")){
            String oldName = mf.clientName;
            mf.clientName = mf.Name.getText();
            //mf.TextOut .append("You've changed your name to:"+mf.clientName+'\n');
            //String msgString = "user: "+oldName+" changed his name to: "+mf.clientName+'\n';
            //mf.msg = new DatagramPacket(msgString.getBytes(), msgString.length(), mf.groupAddr, mf.groupPort);
            //try {
               // mf.socket.send(mf.msg);
            //} catch (IOException ioException) {
                //ioException.printStackTrace();
            //}
            mf.line = "You've changed your name to:"+mf.clientName+'\n';
            try {
                if (mf.stdIn.ready()){
                    mf.line=mf.stdIn.readLine();
                    mf.socOut.println(mf.line);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }else {
            if (buttonName.equals("send")) {
                //mf.line = mf.TextIn.getText();
                //mf.TextIn.setText("");
                //if (mf.line.equals(".exit")){
                    //String msgString = "User: "+mf.clientName + " has left the chat\n";
                    //mf.msg = new DatagramPacket(msgString.getBytes(), msgString.length(), mf.groupAddr, mf.groupPort);
                    //try {
                        //mf.socket.send(mf.msg);
                    //} catch (IOException ioException) {
                        //ioException.printStackTrace();
                    //}
                    //System.exit(0);
                //}else {
                    //String msgString = mf.clientName + " : " + mf.line + '\n';
                    //mf.msg = new DatagramPacket(msgString.getBytes(), msgString.length(), mf.groupAddr, mf.groupPort);
                    //mf.TextOut.append(mf.clientName + " : " + mf.line+'\n');
                    //try {
                       // mf.socket.send(mf.msg);
                    //} catch (IOException ioException) {
                        //ioException.printStackTrace();
                    //}

                //}
            } else {
                JOptionPane.showMessageDialog(mf, "Unknown event" );
            }
        }
    }
}




