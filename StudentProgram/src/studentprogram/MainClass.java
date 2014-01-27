package studentprogram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Sam
 */
public class MainClass {

    //button: the button for the student to request help
    //button2: the button for the student to request grading
    static JButton button, button2;
    //socket: the network socket that the application uses to connect to the server
    static Socket socket;
    //state: true = help request; false = no help request
    //state2: true = grade request; false = no grade request
    static boolean state, state2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //no requests on startup
        state = false;
        state2 = false;
        try {
            //attempt to connect to the server
            //server will be at 127.0.0.1 for testing or IST-RM101-TS for deployed version
            socket = new Socket("127.0.0.1", 42421);
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

        //Thread to attempt to read input from the server, dormant for now
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                BufferedReader input = null;
//                try {
//                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                } catch (IOException ex) {
//                }
//                while (true) {
//                    try {
//                        String read = input.readLine();
//                        if (read.equals("DOWN")) {
//                            if (state == true) {
//                                try {
//                                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                                    out.println("DOWN");
//                                    out.println("QUIT");
//                                    button.setText("Hand is DOWN");
//                                    state = false;
//                                } catch (Exception ex) {
//                                }
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };

        //initialize button
        button = new JButton("Hand is DOWN");
        //initialize button2
        button2 = new JButton("No Current Grading Request");

        //Add action for when button is pressed
        button.addActionListener(new ActionListener() {
            @Override
            //When the button is pressed, do this
            public void actionPerformed(ActionEvent e) {
                //Declare PrintWriter for this thread
                PrintWriter out = null;
                try {
                    //Attempt to initialize the PrintWriter for this thread
                    out = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException ex) {
                }
                if (state == false) {
                    //If there is no help request:
                    //Send command to the server to put hand up
                    out.println("UP");
                    //Update the button
                    button.setText("Hand is UP");
                    //Update the variable
                    state = true;
                } else {
                    //If there is a help request:
                    //Send command to the server to put hand down
                    out.println("DOWN");
                    //Update the button
                    button.setText("Hand is DOWN");
                    //Update the variable
                    state = false;
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Declare the PrintWriter
                PrintWriter out = null;
                try {
                    //Attempt to initialize the PrintWriter
                    out = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException ex) {
                }
                if (state2 == false) {
                    //If there is no grading request:
                    //Send the command to the server to place a grading request
                    out.println("GRADEME");
                    //Update the button
                    button2.setText("Grading Request Sent");
                    //update the variable
                    state2 = true;
                } else {
                    //If there is a grading request:
                    //Send the command to the server to remove the grading request
                    out.println("NOGRADE");
                    //Update the button
                    button2.setText("No Current Grading Request");
                    //update the variable
                    state2 = false;
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
                    if (state) {
                        //If hand is up, put it down
                        out.println("DOWN");
                    }
                    if (state2) {
                        //If grading request is present, close it
                        out.println("NOGRADE");
                    }
                    //Tell the server to sever tie
                    out.println("QUIT");
                } catch (IOException ex) {
                }
            }
        });
        //Graphics stuff:
        
        button.setSize(400, 50);
        //Initialize JPanel
        JPanel panel = new JPanel();
        //Things added top to bottom
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        //Add button to panel
        panel.add(button);
        //Add button2 to panel
        panel.add(button2);
        //Add labels
        panel.add(new JLabel("CREATED BY SAM GOLDMAN"));
        panel.add(new JLabel("TO LEARN MORE, VISIT:"));
        panel.add(new JLabel("https://github.com/samg2014/StudentProgram.git"));
        panel.add(new JLabel("https://github.com/samg2014/TeacherRMH.git"));
        
        //Add panel to frame
        f.add(panel);
        //Set frame size
        f.setSize(300, 170);
        //Show frame
        f.setVisible(true);
    }
}
