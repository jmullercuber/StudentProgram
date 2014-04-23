package studentprogram;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

/**
*
* @author Sam
*/
public class MainClass {

    //assistButton: the button for the student to request help
    //gradeButton: the button for the student to request grading
    static RequestButton assistButton, gradeButton;
    //socket: the network socket that the application uses to connect to the server
    static Socket socket;
    static JLabel label;
    static ImageIcon downImage;
    static ImageIcon upImage;
    static BufferedReader in;

    /**
* @param args the command line arguments
*/
    public static void main(String[] args) {
        try {
            //attempt to connect to the server
            //server will be at 127.0.0.1 for testing or IST-RM101-TS for deployed version
            socket = new Socket("127.0.0.1", 42421);
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //Declare the PrintWriter for sending commands to the server
        PrintWriter out = null;
        try {
            //try to initialize the PrintWriter with the socket
            out = new PrintWriter(socket.getOutputStream(), true);
            //Send the server your username, gotten by the computer
            out.println("USERNAME:" + System.getProperty("user.name"));
        } catch (IOException ex) {
        }

        //initialize button
        assistButton = new RequestButton("A", "Hand is DOWN", "Hand is UP", out);
        //initialize button2
        gradeButton = new RequestButton("G", "No Current Grading Request", "Grading Request Sent", out);

        
        //Declare and initialize a new JFrame
        JFrame f = new JFrame();
        //When the frame closes
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //call terminate
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    if (assistButton.isOn()) {
                        //If hand is up, put it down
						assistButton.doClick();
                    }
                    if (gradeButton.isOn()) {
                        //If grading request is present, close it
						gradeButton.doClick();
                    }
                    //Tell the server to sever tie
                    out.println("QUIT");
                } catch (IOException ex) {
                }
                System.exit(1);
            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                while(true)
                {
                    String read = null;
                    try {
                        read = in.readLine();
                    } catch (IOException ex) {
                    }
                    if(read != null)
                    {
                        if(read.equals("ASSISTDOWN"))
                        {
                            assistButton.doClick();
                        }
                        if(read.equals("GRADEDOWN"))
                        {
                            gradeButton.doClick();
                        }
                    }
                    //System.out.println(read);
                }
            }
        };
        thread.start();
        //Graphics stuff:

        assistButton.setSize(400, 50);
        downImage = new ImageIcon(new MainClass().getClass().getResource("/downImage.jpg"));
        upImage = new ImageIcon(new MainClass().getClass().getResource("/upImage.jpg"));
        label = new JLabel();
        label.setIcon(downImage);
        //Initialize JPanel
        JPanel panel = new JPanel();
        //Things added top to bottom
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        //Add button to panel
        panel.add(assistButton);
        //Add button2 to panel
        panel.add(gradeButton);
        panel.add(label);
        //Add labels
        panel.add(new JLabel("CREATED BY SAM GOLDMAN"));
        panel.add(new JLabel("TO LEARN MORE, VISIT:"));
        panel.add(new JLabel("https://github.com/samg2014/StudentProgram.git"));
        panel.add(new JLabel("https://github.com/samg2014/TeacherRMH.git"));

        //Add panel to frame
        f.add(panel);
        //Set frame size
        //300, 170
        f.setSize(300, 170);
        f.setResizable(false);
        //Show frame
        f.setVisible(true);
    }
	
	public static void setIcon(boolean b) {
		label.setIcon(b?upImage:downImage);
	}
// // a method that puts both of the hand states to false and changes the buttons.
// public void putHandDown() {
// stateAssist = false;
// stateGrade = false;
// assistButton.setText("Hand is DOWN");
// gradeButton.setText("No Current Grading Request");
// }
}

