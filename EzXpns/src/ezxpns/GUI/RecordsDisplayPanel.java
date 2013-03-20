package ezxpns.GUI;

import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel Class containing the inner workings of the Records' bit on the main screen
 */
@SuppressWarnings("serial")
public class RecordsDisplayPanel extends JPanel implements ActionListener {
	
	public static final int DEFAULT_MAX_ONSCREEN = 12;
	public static final Font btnFont = new Font("Segoe UI", 0, 30); // Font name, Font Style, Font Size
		
	private RecordsListerPanel panContent;
	
	private RecordHandler recHandler;
	
	/**
	 * Constructor to create a RecordDisplayPanel
	 * @param recHandlerRef The RecordHandler Object reference <br />that will be used to deal with the logic of handling records
	 */
	public RecordsDisplayPanel(RecordHandler recHandlerRef) {
		super();
		
		recHandler = recHandlerRef; // Receive Handler
				
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		
		this.loadRecords();
	}	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		// Handling opening new windows and such (like right click to have something like edit/remove and stuff.
	}
	
	/**
	 * Load Records from data storage
	 */
	private void loadRecords() {
		panContent = new RecordsListerPanel(recHandler);
		this.add(panContent, BorderLayout.CENTER);
	}
	
	public void update(){
		this.remove(panContent);
		this.loadRecords();
		this.validate();
	}
}

@SuppressWarnings("serial")
// Panel to display all expenses
class RecordsListerPanel extends JPanel {
	
	public static final int TAB_INCOME = 0011;
	public static final int TAB_EXPENSE = 1100;
	
	private JLabel lblTitle;
	private JButton btnNew;
	
	private List<Record> records;
	private RecordHandler recHandler;
	
	public RecordsListerPanel(RecordHandler recHandlerRef) {
		this.recHandler = recHandlerRef;
		this.setLayout(new BorderLayout());
		this.add(getTitleLabel("Recently added..."), BorderLayout.NORTH);
		
		this.initRecords();
		
		MultiRecDisplay recDisplay = new MultiRecDisplay(records);
		this.add(recDisplay, BorderLayout.CENTER);
	}
	
	public RecordsListerPanel(int tabType, RecordHandler recHandlerRef) {		
		this.recHandler = recHandlerRef;
		this.setLayout(new BorderLayout());
		
		JPanel panExtraOpt = new JPanel();
		panExtraOpt.setLayout(new BorderLayout());
		panExtraOpt.add(getTitleLabel(tabType == TAB_EXPENSE ? "Recently spent..." : "Recently received..."), BorderLayout.WEST);
		panExtraOpt.add(getNewButton(), BorderLayout.EAST);
		
		this.add(panExtraOpt, BorderLayout.NORTH);
		
		this.initRecords(tabType);
	}
	
	
	/** 
	 * Draw the records from the data store
	 */
	private void initRecords() {
		this.records = recHandler.getRecords(RecordsDisplayPanel.DEFAULT_MAX_ONSCREEN);
	}
	
	/** 
	 * Draw the records from the data store 
	 * @param type the type of records to draw
	 */
	private void initRecords(int type) {
		List<Record> records = recHandler.getRecords(RecordsDisplayPanel.DEFAULT_MAX_ONSCREEN);
		if(this.records == null) this.records = new Vector<Record>();
		for(Record rec: records) {
			if(type == TAB_EXPENSE) initExp(rec);
			if(type == TAB_INCOME) initInc(rec);
		}
	}
	
	/**
	 * Helper method for the initRecords
	 * @param rec Record reference
	 */
	private void initExp(Record rec) {
		if(rec instanceof ExpenseRecord) {
			this.records.add(rec);
		}
	}
	
	/**
	 * Helper method for the initRecords
	 * @param rec Record reference
	 */
	private void initInc(Record rec) {
		if(rec instanceof IncomeRecord) {
			this.records.add(rec);
		}
	}
	
	private JLabel getTitleLabel(String txt) {
		if(lblTitle == null) {
			lblTitle = new JLabel(txt);
			lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		}
		return lblTitle;
	}
	
	private JButton getNewButton() {
		if(btnNew == null) {
			btnNew = new JButton("New");
			btnNew.setContentAreaFilled(false);
			btnNew.setBorderPainted(false);
			btnNew.setFocusPainted(false);
		}
		return btnNew;
	}
	
}