package studentprogram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import javax.swing.JButton;

/**
 *
 * @author jmuller
 */
public class RequestButton extends JButton {
	// class variables
	private static int buttonCount;
	private static boolean anyOtherRequests = false;
	
	// instance variables
	//on: true = help request; false = no help request
	private boolean on;
	private int buttonID;
	private String screenID;
	private String textOn, textOff;
	private PrintWriter communications;
	
	// Constructor
	public RequestButton(String sid, String sOff, String sOn, PrintWriter comm) {
		// Call to the parent constructor
		super(sOff);
		
		// Instantiate variables
		this.on = false;
		this.screenID = sid;
		this.textOn = sOn;
		this.textOff = sOff;
		this.communications = comm;
		
		this.buttonID = RequestButton.buttonCount;
		RequestButton.buttonCount++;
		
		
		//Add action for when button is pressed
        addActionListener(new ActionListener() {
            @Override
            //When the button is pressed, do this
            public void actionPerformed(ActionEvent e) {
                /*//Declare PrintWriter for this thread
                PrintWriter out = null;
                try {
                    //Attempt to initialize the PrintWriter for this thread
                    out = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException ex) {
                }*/
                if (changeState() == true) {
                    communications.println(screenID + (isOn()?"T":"F")); // "UP", "DOWN" => (if screenID is "G"): "GT", "GF"
					System.out.println(screenID + (isOn()?"T":"F"));
                    MainClass.setIcon(isOn());
                }
            }
        });
	}
	
	public boolean isOn() {
		return on;
	}
	
	public boolean canChange() {
		return RequestButton.anyOtherRequests == on;
	}
	
	private boolean changeState() {
		// add a request
		if (!RequestButton.anyOtherRequests && !isOn()) {
			on = true;
			setText(textOn);
			RequestButton.anyOtherRequests = true;
			return true;
		}
		// remove a request
		if (RequestButton.anyOtherRequests && isOn()) {
			on = false;
			setText(textOff);
			RequestButton.anyOtherRequests = false;
			return true;
		}
		return false;
	}
}