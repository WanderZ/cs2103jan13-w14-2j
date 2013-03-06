package ezxpns.GUI;

import javax.swing.JFrame;


/**
 * The window to handle all the extensive record analysis and report
 * <br />Should contain a form for the user to enter the starting and the ending date of the records analysis
 */
@SuppressWarnings("serial")
public class ReportFrame extends JFrame {
	
	public ReportFrame() {
		super(); // new JFrame();
	}

	// Probably should have a form here for the start and end month (a stand-alone panel)
	// Probably a method here to access the data handler interface in main or something like that
	// Fitting the received report into a different panel 
	// Probably an update method to update the on screen (visuals?) display upon report request
}
