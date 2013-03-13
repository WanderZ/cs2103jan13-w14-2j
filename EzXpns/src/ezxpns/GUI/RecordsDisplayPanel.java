package ezxpns.GUI;

import ezxpns.data.records.Record;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

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
	private Font btnFont;
	
	public RecordsDisplayPanel() {
		super();
		initFont();
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
	
	private void initFont() {
		btnFont = new Font("Segoe UI", 0, 30); // Font name, Font Style, Font Size
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
					Map attribute = btnFont.getAttributes();
					attribute.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					btn.setFont(btnFont.deriveFont(attribute));
				}
				
				public void mousePressed(MouseEvent mEvent) {
					System.out.println("Clicked btnEx!");
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