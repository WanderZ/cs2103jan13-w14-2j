package ezxpns.GUI;

import javax.swing.JPanel;

/**
 * This is the panel in which will be slotted into the MainGUI 
 * (the main interface) as the display for the visual breakdown of the targets 
 * on the user defined categories (Food, Transport, etc...)
 */
@SuppressWarnings("serial")
public class TargetOverviewPanel extends JPanel {

	private javax.swing.JLabel lblMsg;
	public TargetOverviewPanel() {
		super(); // new Panel();
		
		lblMsg = new javax.swing.JLabel("Targets - Under Construction :(");
		this.add(lblMsg);
	}
	
	// Will there be any interactions enabled for this panel? 
	// (i.e. slider interface to allow users to adjust their targets)
	// If there is, you may want to do some action-listening [extra stuff] 

	// Buttons for functionality
	// Responding to events, such as a new category/record added (in/out) - updating the information live
	
	// Should interact with some interface or handler to calculate percentage and stuff as such.
	// Note: It may have to accept something in the constructor such as an object reference to the logic. 
	
	// Question: Will this be linked our user's shortcut to CRUDE categories? (or maybe just some of them?)

	// Resources: Will require the icons to display next to each individual targets (danger, warning, safe, none_set)
}