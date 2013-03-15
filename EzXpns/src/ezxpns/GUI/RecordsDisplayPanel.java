package ezxpns.GUI;

import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel Class containing the inner workings of the Records' bit on the main screen
 */
@SuppressWarnings("serial")
public class RecordsDisplayPanel extends JPanel implements ActionListener {
	
	public static final int DEFAULT_MAX_ONSCREEN = 12;
	public static final Font btnFont = new Font("Segoe UI", 0, 30); // Font name, Font Style, Font Size
	
	public final String TAB_EX = "Expense";
	public final String TAB_IN = "Income";
	
	private RecordsListerPanel panListIncome, panListExpense; 
	private JButton btnEx, btnIn;
	private JPanel panContent;
	private CardLayout loCard;
	
	RecordHandlerInterface recHandler;
	CategoryHandlerInterface inCatHandler;
	CategoryHandlerInterface exCatHandler;
	
	public RecordsDisplayPanel(RecordHandlerInterface recHandlerRef) {
		super();
		
		recHandler = recHandlerRef; // Receive Handler
		
		
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());

		JPanel panTabs = new JPanel(new GridLayout(1, 0, 0 ,0));
		panTabs.add(getBtnEx());
		panTabs.add(getBtnIn());
		
		this.add(panTabs, BorderLayout.NORTH);
		
		panContent = new JPanel();
		
		loCard = new CardLayout();
		panContent.setLayout(loCard);
		
		panContent.add(this.getPanExpenseList(), TAB_EX);
		panContent.add(this.getPanIncomeList(), TAB_IN);
		
		loCard.show(panContent, TAB_EX);
		
		this.add(panContent, BorderLayout.CENTER);
	}
	
	private JButton getBtnEx() {
		if(btnEx == null) {
			btnEx = new JButton("Expenses");
			btnEx.setFont(btnFont);
			btnEx.setBorderPainted(false);
			btnEx.setContentAreaFilled(false);
			btnEx.setFocusPainted(false);
			
			btnEx.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent mEvent) { // Hover start
					JButton btn = (JButton) mEvent.getSource();
					btn.setForeground(Color.BLUE);
					// btn.setBackground(Color.LIGHT_GRAY); // Currently useless as the content area is set to be transparent 
					
					/* Underlining the word for "hover*/
					Font btnFont = btn.getFont();
/*					Map attribute = btnFont.getAttributes();
					attribute.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					btn.setFont(btnFont.deriveFont(attribute));*/
				}
				
				public void mousePressed(MouseEvent mEvent) {
					loCard.show(panContent, TAB_EX);
				}
				
				public void mouseExited(MouseEvent mEvent) { // Hover end
					JButton btn = (JButton) mEvent.getSource();
					btn.setFont(btnFont); // return to original font (without the underline - workaround)
					btn.setForeground(Color.DARK_GRAY);
					// Current Issue: unable to remove the underlining from after hovering over it.
				}
			});
		}
		return btnEx;
	}
	
	private JButton getBtnIn() {
		if(btnIn == null) {
			btnIn = new JButton("Income");
			btnIn.setFont(btnFont);
			btnIn.setBorderPainted(false);
			btnIn.setContentAreaFilled(false);
			
			btnIn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent mEvent) { // Hover start
					JButton btn = (JButton) mEvent.getSource();
					btn.setForeground(Color.BLUE);
					// btn.setBackground(Color.LIGHT_GRAY); // Currently useless as the content area is set to be transparent 
					
					/* Underlining the word for "hover*/
					Font btnFont = btn.getFont();
/*					Map attribute = btnFont.getAttributes();
					attribute.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					btn.setFont(btnFont.deriveFont(attribute));*/
				}
				
				public void mousePressed(MouseEvent mEvent) {
					loCard.show(panContent, TAB_IN);
				}
				
				public void mouseExited(MouseEvent mEvent) { // Hover end
					JButton btn = (JButton) mEvent.getSource();
					btn.setFont(btnFont); // return to original font (without the underline - workaround)
					btn.setForeground(Color.DARK_GRAY);
					// Current Issue: unable to remove the underlining from after hovering over it.
				}
			});
		}
		return btnIn;
	}
	
	/**
	 * Method to retrieve the panel listing all the income records
	 * @return
	 */
	private JPanel getPanIncomeList() {
		if(panListIncome == null) {
			panListIncome = new RecordsListerPanel(RecordsListerPanel.TAB_INCOME, recHandler);
		}
		return panListIncome;
	}
	
	/**
	 * Method to retrieve the panel listing all the expense records
	 * @return
	 */
	private JPanel getPanExpenseList() {
		if(panListExpense == null) {
			panListExpense = new RecordsListerPanel(RecordsListerPanel.TAB_EXPENSE, recHandler);
		}
		return panListExpense;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		// Handling opening new windows and such (like right click to have something like edit/remove and stuff.
	}
	
}

@SuppressWarnings("serial")
// Panel to display all expenses
class RecordsListerPanel extends JPanel {
	
	public static final int TAB_INCOME = 0011;
	public static final int TAB_EXPENSE = 1100;
	
	public final int MAX_RECORDS_IN_PAGE = 12;
	
	private JLabel lblTitle;
	private JButton btnNew;
	
	private List<Record> records;
	private RecordHandlerInterface recHandler;
	
	public RecordsListerPanel(int tabType, RecordHandlerInterface recHandlerRef) {
		super();
		
		this.recHandler = recHandlerRef;
		
		this.setLayout(new BorderLayout());
		
		JPanel panExtraOpt = new JPanel();
		panExtraOpt.setLayout(new BorderLayout());
		panExtraOpt.add(getTitleLabel(tabType == TAB_EXPENSE ? "Recently spent..." : "Recently received..."), BorderLayout.WEST);
		panExtraOpt.add(getNewButton(), BorderLayout.EAST);
		
		this.add(panExtraOpt, BorderLayout.NORTH);
		
		this.initRecords(tabType);
		
		JPanel panRecords = new JPanel(new GridLayout(0, 1, 0, 0));
		this.populateRecords(panRecords);
		JScrollPane jspRecords = new JScrollPane(panRecords);
		jspRecords.setBorder(BorderFactory.createEmptyBorder());
		
		this.add(jspRecords, BorderLayout.CENTER);
	}
	
	private void populateRecords(JPanel pane) {
		for(Record rec : this.records) {
			pane.add(new RecordDisplayPanel(rec));
		}
	}
	
	private void initRecords(int type) {
		List<Record> records = recHandler.getRecords(MAX_RECORDS_IN_PAGE);
		if(this.records == null) this.records = new Vector<Record>();
		for(Record rec: records) {
			if(type == TAB_EXPENSE) initExp(rec);
			if(type == TAB_INCOME) initInc(rec);
		}
	}
	
	private void initExp(Record rec) {
		if(rec instanceof ExpenseRecord) {
			this.records.add(rec);
		}
	}
	
	private void initInc(Record rec) {
		if(rec instanceof IncomeRecord) {
			this.records.add(rec);
		}
	}
	
	private JLabel getTitleLabel(String txt) {
		if(lblTitle == null) {
			lblTitle = new JLabel(txt);
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
	
	// This panel should be a generic panel that can contain both expenses and income
	// > Done < 1. "Recently..." Label (title label) 
	// > Done < 2. "New..." Button (To create new expense/record)
	// 2.1 Linking new button to a new record window 
	// > Done < 3. List all the records down - dynamically generate all the records out, and paging! 
	// > Done < 4. Scroll pane for the 12 records?
	// 5. the next and previous button (paging) - not really required...
}

/**
 * Panel Object containing each of the records, with the functionality
 * <br />Will display in the format &#60;Amt&#62; on &#60;Name&#62;&nbsp;&#60;Category&#62;&nbsp;&#60;Day&#62;&nbsp;&#60;Time&#62;
 */
@SuppressWarnings("serial")
class RecordDisplayPanel extends JPanel {
	
	private Record record;
	private JLabel lblAmtName, lblCat, lblDate;
	private final DecimalFormat MONEY_FORMAT = new DecimalFormat("$###,###,##0.00");
	
	public RecordDisplayPanel(Record record) {
		super(new GridLayout(1, 0, 5, 5));
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
			lblAmtName.setFont(new Font("", 0, 18));
		}
		return lblAmtName;
	}
	
	public JLabel getCat() {
		if(lblCat == null) {
			lblCat = new JLabel(record.getCategory().getName());
			lblCat.setFont(new Font("", 0, 18));
		}
		return lblCat;
	}
	
	public JLabel getDate() {
		if(lblDate == null) {
			lblDate = new JLabel("yesterday");
			lblDate.setFont(new Font("", 0, 18));
		}
		return lblDate;
	}
}