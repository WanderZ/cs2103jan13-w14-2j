package ezxpns.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainScreen extends JFrame {
	
	private JMenuBar menu;
	private JPanel panTips;
	
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

@SuppressWarnings("serial")
class PanelRecords extends JPanel implements ActionListener {
	
	/**
	 * A list of JButtons, 20 most recent ones will be generated. More will be generated upon user's request
	 */
	public PanelRecords() {
		
	}
	
	/**
	 * Generate the list of most recently added expenses
	 */
	public List generateRecords() {
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		// Mouse Event...
	}
	
}

/** 
 * This panel contains the educational tips displays at the bottom of the screen 
 *
 */
@SuppressWarnings("serial")
class PanelTip extends JPanel {
	
	public PanelTip() {
		super();
	}
}