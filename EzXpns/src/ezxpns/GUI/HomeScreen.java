package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import ezxpns.data.TargetManager;
import ezxpns.data.SummaryGenerator;
import ezxpns.data.records.RecordHandler;

@SuppressWarnings("serial")
public class HomeScreen extends JFrame implements UpdateNotifyee {
	
	public final int DEFAULT_HEIGHT = 860;
	public final int DEFAULT_WEIGHT = 680;
	
	private JMenuBar menu;
	// private JPanel panTips;
	private OverviewPanel panOverview;
	private SavingsOverviewPanel panSavings;
	private RecordsDisplayPanel panRecords;
	private TargetOverviewPanel panTargets;
	
	private RecordHandler recHandler;
	private TargetManager targetMgr;
	private SummaryGenerator sumGen;
	
	private UndoManager undoManager;
	
	private UIControl guiCtrl;
	
	public HomeScreen(
			UIControl guiControlRef,
			RecordHandler recHandlerRef,
			TargetManager targetMgrRef,
			UndoManager undomng,
			SummaryGenerator sumGenRef){
		super("EzXpns - Main Menu"); // Setting the title
		this.setBackground(Color.WHITE);
		undoManager = undomng;
		undoManager.setPostUndo(new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				updateAll();
				
			}
			
		});
		this.setBounds(0, 0, DEFAULT_HEIGHT, DEFAULT_WEIGHT); /*x coordinate, y coordinate, width, height*/
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To change to dispose if doing daemon 
		// this.setVisible(true);
		
		this.guiCtrl = guiControlRef;
		this.setJMenuBar(getMenu());
		
		this.getContentPane().setBackground(Color.WHITE);
		
		this.recHandler = recHandlerRef;
		this.targetMgr = targetMgrRef;
		this.sumGen = sumGenRef;
				
		// To set Layout - grid or maybe grid bag layout 
		// Temporary set to grid layout for even distribution
		this.setLayout(new BorderLayout());
		JPanel contentDivider = new JPanel();
		contentDivider.setLayout(new java.awt.GridLayout(2, 2, 10, 10));
		contentDivider.add(getOverviewPanel());
		contentDivider.add(getSavingsPanel());
		contentDivider.add(getTargetsPanel());
		contentDivider.add(getRecordsPanel());
		
		this.add(contentDivider, BorderLayout.CENTER);		
	}
	
	/**
	 * Retrieve the main menu
	 * @return an Initialized EzXpnsMainMenu Object
	 */
	private JMenuBar getMenu() {
		if(this.menu==null) {
			this.menu = new EzXpnMainMenu(guiCtrl);
		}
		return this.menu;
	}
	
	/**
	 * Retrieve the Savings Panel
	 * @return an Initialized SavingsOverviewPanel Object
	 */
	private JPanel getSavingsPanel() {
		if(panSavings == null) {
			panSavings = new SavingsOverviewPanel();
			panSavings.setBackground(Color.WHITE);
		}
		return panSavings;
	}
	
	/**
	 * Retrieve the Targets Panel
	 * @return an Initialized TargetOverviewPanel Object
	 */
	private JPanel getTargetsPanel() {
		if(panTargets==null) {
			panTargets = new TargetOverviewPanel(targetMgr);
			panTargets.setBackground(Color.WHITE);
		}
		return panTargets;
	}
	
	/**
	 * Retrieve the Overview Panel
	 * @return an Initialized OverviewPanel Object
	 */
	private JPanel getOverviewPanel() {
		if(panOverview == null) {
			panOverview = new OverviewPanel(sumGen);
			panOverview.setBackground(Color.WHITE);
		}
		return panOverview;
	}
	
	/**
	 * Retrieves the Records Panel
	 * @return an Initialized RecordsDisplayPanel Object
	 */
	private JPanel getRecordsPanel() {
		if(panRecords == null) {
			panRecords = new RecordsDisplayPanel(recHandler, guiCtrl, this);
			panRecords.setBackground(Color.WHITE);
		}
		return panRecords;
	}
	
	/**
	 * Updates all the Panels in this Window
	 */
	public void updateAll() {
		panRecords.update();
		
		panTargets.update();
		
		panSavings.update();
		
		panOverview.updateOverview();
		panOverview.validate();
		
		this.validate();
	}

	@Override
	public void addUndoAction(AbstractAction action, String name) {
		undoManager.add(action, name);
	}
}
