package studentprogram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Sam
 */
public class MainClass {

    static JButton button;
    static Socket socket;
    static boolean state;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        state = false;
        try {
            socket = new Socket("192.168.0.22", 42420);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("USERNAME:" + System.getProperty("user.name"));
        } catch (IOException ex) {
        }
        
        button = new JButton("Hand is DOWN");
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
                }
                else{
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
        JFrame f = new JFrame();
        f.add(button);
        f.setSize(400, 100);
        f.setVisible(true);
    }
}