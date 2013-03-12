package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomeScreen extends JFrame {
	
	public final int DEFAULT_HEIGHT = 860;
	public final int DEFAULT_WEIGHT = 680;
	private JMenuBar menu;
	private JPanel panOverview, panSavings, panTargets, panRecords, panTips;
	private RecordHandlerInterface recHandler;
	
	public HomeScreen(RecordHandlerInterface recHandler){
		super("EzXpns - Main Menu"); // Setting the title
		this.setBounds(0, 0, DEFAULT_HEIGHT, DEFAULT_WEIGHT); /*x coordinate, y coordinate, width, height*/
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To change to dispose if doing daemon 
		// this.setVisible(true);
		this.setJMenuBar(getMenu());
		
		this.getContentPane().setBackground(Color.WHITE);
		
		this.recHandler = recHandler;
				
		// To set Layout - grid or maybe grid bag layout 
		// Temporary set to grid layout for even distribution
		this.setLayout(new BorderLayout());
		JPanel contentDivider = new JPanel();
		contentDivider.setLayout(new java.awt.GridLayout(2, 2, 0, 0));
		contentDivider.add(getOverviewPanel());
		contentDivider.add(getSavingsPanel());
		contentDivider.add(getTargetsPanel());
		contentDivider.add(getRecordsPanel());
		
		this.add(contentDivider, BorderLayout.NORTH);
		this.add(getTipsPanel(), BorderLayout.SOUTH);
	}
	
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
 * <br />[To Be Moved to a file on its on in the next iteration]
 */
@SuppressWarnings("serial")
class PanelTip extends JPanel {
	
	public PanelTip() {
		super();
		this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
		addLabel("Tips Panel - Under Construction", this);
		addLabel("Quote of the day: 'Be the change you wish to see in this world'",this);
		this.setOpaque(false); // Transparent background
	}
	
	private void addLabel(String txt, java.awt.Container container) {
		javax.swing.JLabel lbl = new javax.swing.JLabel(txt);
		lbl.setAlignmentY(CENTER_ALIGNMENT);
		container.add(lbl);
	}
}