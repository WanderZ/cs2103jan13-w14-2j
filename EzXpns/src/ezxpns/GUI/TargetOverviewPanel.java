package ezxpns.GUI;

import javax.swing.JPanel;

/**
 * This is the panel in which will be slotted into the MainGUI 
 * (the main interface) as the display for the visual breakdown of the targets 
 * on the user defined categories (Food, Transport, etc...)
 */
@SuppressWarnings("serial")
public class TargetOverviewPanel extends JPanel {

	
	public TargetOverviewPanel() {
		super(); // new Panel();
	}
	
	// Will there be any interactions enabled for this panel? 
	// (i.e. slider interface to allow users to adjust their targets)

	// Buttons for functionality
	// Responding to events, such as a new category/record added (in/out) - updating the information live
	
	// Should interact with some interface or handler to calculate percentage and stuff as such.
	
	// Question: Will this be linked our user's shortcut to CRUDE categories? (or maybe just some of them?)

	// Resources: Will require the icons to display next to each individual targets (danger, warning, safe, none_set)
}
