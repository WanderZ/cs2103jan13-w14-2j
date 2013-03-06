package ezxpns.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainScreen extends JFrame {
	
	private JMenuBar menu;
	private JPanel panOverview, panSavings, panTargets, panRecords, panTips;
	
	public MainScreen(){
		super("EzXpns - Main Menu"); // Setting the title
		this.setBounds(0, 0, 800, 600); /*x coordinate, y coordinate, width, height*/
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To change to dispose if doing daemon 
		// this.setVisible(true);
		this.setJMenuBar(getMenu());
		
		
		// To set Layout - grid or maybe grid bag layout
		this.setLayout(new BorderLayout());
		JPanel contentDivider = new JPanel();
		contentDivider.setLayout(new java.awt.GridLayout(2,2));
		contentDivider.add(getOverviewPanel());
		contentDivider.add(getSavingsPanel());
		contentDivider.add(getTargetsPanel());
		contentDivider.add(getRecordsPanel());
		
		this.add(contentDivider, BorderLayout.NORTH);
		this.add(getTipsPanel(), BorderLayout.SOUTH);
	}
	
	public void showScreen() {super.setVisible(true);}
	public void hideScreen() {super.setVisible(false);}
	
	private JMenuBar getMenu() {
		if(this.menu==null) {
			this.menu = new EzXpnMainMenu();
		}
		return this.menu;
	}
	
	private JPanel getSavingsPanel() {
		if(panSavings == null) {
			panSavings = new SavingsOverviewPanel();
		}
		return panSavings;
	}
	
	private JPanel getTargetsPanel() {
		if(panTargets==null) {
			panTargets = new TargetOverviewPanel();
		}
		return panTargets;
	}
	
	private JPanel getOverviewPanel() {
		if(panOverview == null) {
			panOverview = new OverviewPanel();
		}
		return panOverview;
	}
	
	private JPanel getRecordsPanel() {
		if(panRecords == null) {
			panRecords = new RecordsDisplayPanel();
		}
		return panRecords;
	}
	
	private JPanel getTipsPanel() {
		if(panTips == null) {
			panTips = new PanelTip();
		}
		return panTips;
	}
}

/** 
 * This panel contains the educational tips displays at the bottom of the screen 
 * [To Be Moved to a file on its on in the next inter
 */
@SuppressWarnings("serial")
class PanelTip extends JPanel {
	
	public PanelTip() {
		super();
		this.add(new javax.swing.JLabel("Tips Panel - Under Construction"));
	}
}