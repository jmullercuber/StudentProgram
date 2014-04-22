package studentprogram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
    static JButton assistButton, gradeButton;
    //socket: the network socket that the application uses to connect to the server
    static Socket socket;
    
    static BufferedReader input;
    //stateAssist: true = help request; false = no help request
    //stateGrade: true = grade request; false = no grade request
    static boolean stateAssist, stateGrade;
            //server will be at 127.0.0.1 for testing or IST-RM101-TS for deployed version
    static String teacherAddress = "127.0.0.1";
    static boolean running;

    /**
* @param args the command line arguments
*/
    public static void main(String[] args) {
        //no requests on startup
        stateAssist = false;
        stateGrade = false;
        running = true;
        try {
            //attempt to connect to the server
            socket = new Socket(teacherAddress, 42421);
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
        //Declare the PrintWriter for sending commands to the server
        PrintWriter out;
        try {
            //try to initialize the PrintWriter with the socket
            out = new PrintWriter(socket.getOutputStream(), true);
            //Send the server your username, gotten by the computer
            out.println("USERNAME:" + System.getProperty("user.name"));
        } catch (IOException ex) {
        }
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
        }

        // Read input from the server
        Thread thread = new Thread() {
            public void run() {
                while (running) {
                    System.out.println(" Special thread is running ");
                    String read = null;
                    try {
                        System.out.println(" Try creating input ");
                        
                        System.out.println(" Created input. Try reading input ");
                        read = input.readLine();
                        System.out.println(" Input is working ");
                    } catch (IOException ex) {
                    }
                    if (read.equals("Teacher is putting your hand DOWN")) {
                        putHandDown();
                    }
                }
            }
        };
        thread.start();

        //initialize button
        assistButton = new JButton("Hand is DOWN");
        //initialize button2
        gradeButton = new JButton("No Current Grading Request");

        //Add action for when button is pressed
        assistButton.addActionListener(new ActionListener() {
            //When the button is pressed, do this
            public void actionPerformed(ActionEvent e) {
                //Declare PrintWriter for this thread
                PrintWriter out = null;
                try {
                    //Attempt to initialize the PrintWriter for this thread
                    out = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException ex) {
                }
                if (stateAssist == false && stateGrade == false) {
                    //If there is no help request:
                    //Send command to the server to put hand up
                    out.println("UP");
                    //Update the button
                    assistButton.setText("Hand is UP");
                    //Update the variable
                    stateAssist = true;
                } else {
                    //If there is a help request:
                    //Send command to the server to put hand down
                    out.println("DOWN");
                    //Update the button
                    assistButton.setText("Hand is DOWN");
                    //Update the variable
                    stateAssist = false;
                }
            }
        });

        gradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Declare the PrintWriter
                PrintWriter out = null;
                try {
                    //Attempt to initialize the PrintWriter
                    out = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException ex) {
                }
                if (stateAssist == false && stateGrade == false) {
                    //If there is no grading request:
                    //Send the command to the server to place a grading request
                    out.println("GRADEME");
                    //Update the button
                    gradeButton.setText("Grading Request Sent");
                    //update the variable
                    stateGrade = true;
                } else {
                    //If there is a grading request:
                    //Send the command to the server to remove the grading request
                    out.println("NOGRADE");
                    //Update the button
                    gradeButton.setText("No Current Grading Request");
                    //update the variable
                    stateGrade = false;
                }
            }
        });
        //Declare and initialize a new JFrame
        JFrame f = new JFrame();
        //When the frame closes
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //call terminate
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    if (stateAssist) {
                        //If hand is up, put it down
                        out.println("DOWN");
                    }
                    if (stateGrade) {
                        //If grading request is present, close it
                        out.println("NOGRADE");
                    }
                    //Tell the server to sever tie
                    out.println("QUIT");
                    running = false;
                } catch (IOException ex) {
                }
            }
        });
        //Graphics stuff:

        assistButton.setSize(400, 50);
        JLabel label = null;
        ImageIcon i;
        i = new ImageIcon(new MainClass().getClass().getResource("/image.jpg"));
        label = new JLabel(i);
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
        f.setSize(300, 350);
        f.setResizable(false);
        //Show frame
        f.setVisible(true);
    }
    
    // a method that puts both of the hand states to false and changes the buttons.
    public static void putHandDown()
    {
        stateAssist = false;
        stateGrade = false;
        assistButton.setText("Hand is DOWN");
        gradeButton.setText("No Current Grading Request");
    }
}
