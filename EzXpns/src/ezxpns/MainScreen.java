import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainScreen extends JFrame {
	
	private JMenuBar menu;
	private JPanel panMain, panNotify, panTips, panProfile;
	
	public MainScreen(){
		this.setBounds(0, 0, 800, 600); /*x coordinate, y coordinate, width, height*/
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		// this.setVisible(true);
		this.setJMenuBar(getMenu());
		this.setTitle("EzXpns - Main Menu");
	}
	
	public void showScreen() {super.setVisible(true);}
	public void hideScreen() {super.setVisible(false);}
	
	private JMenuBar getMenu() {
		if(this.menu==null) {
			this.menu = new EzXpnMainMenu();
		}
		return this.menu;
	}
	
}

/**
 * This panel contains the elements required to display notification
 * Will be hidden and displayed at will (when there is the need to display)
 * 
 */
@SuppressWarnings("serial")
class PanelAlert extends JPanel {
	
	public PanelAlert() {
		super();
	}
	
}

/**
 * This panel contains the main content for the main screen
 * Mainly Records (Top) then the brief Summary (Bottom left)
 *
 */
@SuppressWarnings("serial")
class PanelContent extends JPanel {
	
	public PanelContent() {
		super();
	}
	
}

/** 
 * This panel contains the educational tips displays at the bottom of the screen 
 *
 */
@SuppressWarnings("serial")
class PanelTip extends JPanel {
	
}

/**
 * This panel contains the profile information for the gamification of the program
 * Contains mainly the Level, Experience(Exp), Exp required, etc...
 *
 */
@SuppressWarnings("serial")
class PanelProfile extends JPanel {
	
}