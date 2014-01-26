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

/**
 *
 * @author Sam
 */
public class MainClass {

    static JButton button, button2;
    static Socket socket;
    static boolean state, state2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        state = false;
        state2 = false;
        try {
            socket = new Socket("127.0.0.1", 42421); //IST-RM101-TS , 102.168.0.22
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("USERNAME:" + System.getProperty("user.name"));
        } catch (IOException ex) {
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                BufferedReader input = null;
                try {
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                }
                while (true) {
                    try {
                        String read = input.readLine();
                        if (read.equals("DOWN")) {
                            if (state == true) {
                                try {
                                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                                    out.println("DOWN");
                                    out.println("QUIT");
                                    button.setText("Hand is DOWN");
                                    state = false;
                                } catch (Exception ex) {
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        button = new JButton("Hand is DOWN");
        button2 = new JButton("No Current Grading Request");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == false) {
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("UP");
                        button.setText("Hand is UP");
                        state = true;
                    } catch (Exception ex) {
                    }
                } else {
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("DOWN");
                        button.setText("Hand is DOWN");
                        state = false;
                    } catch (Exception ex) {
                    }
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state2 == false) {
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("GRADEME");
                        button2.setText("Grading Request Sent");
                        state2 = true;
                    } catch (Exception ex) {
                    }
                } else {
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("NOGRADE");
                        button2.setText("No Current Grading Request");
                        state2 = false;
                    } catch (Exception ex) {
                    }
                }
            }
        });
        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //call terminate
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    if (state == true) {
                        out.println("DOWN");
                        out.println("NOGRADE");
                        out.println("QUIT");
                    }
                } catch (IOException ex) {
                }
            }
        });
        button.setSize(400, 50);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(button);
        panel.add(button2);
        panel.add(new JLabel("CREATED BY SAM GOLDMAN"));
        panel.add(new JLabel("TO LEARN MORE, VISIT:"));
        panel.add(new JLabel("https://github.com/samg2014/StudentProgram.git"));
        panel.add(new JLabel("https://github.com/samg2014/TeacherRMH.git"));
        f.add(panel);
        f.setSize(300, 170);
        f.setVisible(true);
    }
}
