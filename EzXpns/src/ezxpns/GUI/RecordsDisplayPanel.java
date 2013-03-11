package ezxpns.GUI;

import ezxpns.data.records.Record;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel Class containing the inner workings of the Records' bit on the main screen
 */
@SuppressWarnings("serial")
public class RecordsDisplayPanel extends JPanel implements ActionListener {
	
	public static final int DEFAULT_MAX_ONSCREEN = 12;
	private RecordsListerPanel panListIncome, panListExpense; 
	private JButton btnEx, btnIn;
	
	List<Record> records;
	
	public RecordsDisplayPanel() {
		super();
		
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		// CardLayout - for toggling between expenses and income...?
		CardLayout tabs = new CardLayout();
		
		tabs.addLayoutComponent(getPanExpenseList(), "Component.CENTER_ALIGNMENT");
		tabs.addLayoutComponent(getPanIncomeList(), "Component.CENTER_ALIGNMENT");
		
		
		// Mouse Adapter (hover click...)		
	}
	
	// Load records (expenses, income)
	// Display
	
	private JButton getBtnEx() {
		if(btnEx == null) {
			btnEx = new JButton();
		}
		return btnEx;
	}
	
	private JButton getBtnIn() {
		if(btnIn == null) {
			btnIn = new JButton();
		}
		return btnIn;
	}
	
	private JPanel getPanIncomeList() {
		if(panListIncome == null) {
			panListIncome = new RecordsListerPanel(RecordsListerPanel.TAB_INCOME);
		}
		return panListIncome;
	}
	
	private JPanel getPanExpenseList() {
		if(panListExpense == null) {
			panListExpense = new RecordsListerPanel(RecordsListerPanel.TAB_EXPENSE);
		}
		return panListExpense;
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	
}

@SuppressWarnings("serial")
// Panel to display all expenses
class RecordsListerPanel extends JPanel {
	
	public static final int TAB_INCOME = 0011;
	public static final int TAB_EXPENSE = 1100;
	
	public RecordsListerPanel(int tabType) {
		super();
		
	}
	
}
// Panel to display all income records

/**
 * Panel Object containing each of the records, with the functionality
 * <br />Will display in the format &#60;Amt&#62; on &#60;Name&#62;&nbsp;&#60;Category&#62;&nbsp;&#60;Day&#62;&nbsp;&#60;Time&#62;
 */
@SuppressWarnings("serial")
class RecordDisplayPanel extends JPanel {
	
	private Record record;
	private JLabel lblAmtName, lblCat, lblDate, lblTime;
	private final DecimalFormat MONEY_FORMAT = new DecimalFormat("$###,###,##0.00");
	
	public RecordDisplayPanel(Record record) {
		super();
		
		this.record = record;
		this.add(getAmtName());
		this.add(getCat());
		this.add(getDate());
	}
	
	public void setRecord(Record record) {this.record = record;}
	public Record getRecord() {return this.record;}
	
	public JLabel getAmtName() {
		if(lblAmtName == null) {
			String colAmtName = MONEY_FORMAT.format(record.getAmount()) + " on " + record.getName();
			lblAmtName = new JLabel(colAmtName);
		}
		return lblAmtName;
	}
	
	public JLabel getCat() {
		if(lblCat == null) {
			lblCat = new JLabel(record.getCategory().getName());
		}
		return lblCat;
	}
	
	public JLabel getDate() {
		if(lblDate == null) {
			lblDate = new JLabel("yesterday");
		}
		return lblDate;
	}
	
	public JLabel getTime() {
		if(lblTime == null) {
			lblTime = new JLabel("16:20");
		}
		return lblTime;
	}
	
	// init. frame
	
	
	
}