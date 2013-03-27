package ezxpns.GUI;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JLayeredPane;

/**
 * Navigator for the MainGUI
 */
@SuppressWarnings("serial")
public class EzNavigator extends JLayeredPane {

	// TODO: Navigator Menus (A list of functions/links)
	// TODO: Incorporate Simple Search 
	// TODO: Manipulate the main gui panels
	
	private CardLayout loCtrl;
	private JLayeredPane content;
	
	private EzNavigator() {
		this.setBackground(Color.BLACK);
		// TODO: Create a Accordion Menu :) if possible
		// TODO: Link the menu buttons to the appropriate link
		// TODO: Setup the layout properly for the menu buttons
		// TODO: SUBMENU Hiding - Categorizing Menu options, etc 
	}
	
	public EzNavigator(CardLayout loCtrl, JLayeredPane contentPane) {
		this();
		this.loCtrl = loCtrl;
		this.content = contentPane;
	}
	
	public void navigate(MenuCard card) {
		loCtrl.show(content, card.toString());
	}
}

/**
 * Menu Names for labeling and operating cards
 */
enum MenuCard {
	
	NEWRCD 	("New Record"),
	NEWINC 	("New Income Record"),
	NEWEXP 	("New Expense Record"),
	SEARCH 	("Search"),
	CATMGR 	("Manage Category"),
	PAYMGR 	("Manage Payments"),
	TARGET 	("Manage Targets"),
	REPORT 	("Generate Report"),
	DASHBD 	("Dashboard"),
	UNDO	("Undo");
	
	public final String name;
	private MenuCard(String name) {this.name = name;}
	
	public String toString() {
		return this.name;
	}
}