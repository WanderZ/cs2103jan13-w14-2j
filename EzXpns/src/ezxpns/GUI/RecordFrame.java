package ezxpns.GUI;
import ezxpns.data.records.Record;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * This is a JFrame object (Window) that allows users to enter a new record (Expense/Income) into the EzXpns
 * @param <T>An object that implements RecordHandlerInterface and CategoryHandlerInterface.
 */
@SuppressWarnings("serial")
public class RecordFrame<T extends RecordHandlerInterface & CategoryHandlerInterface> extends JFrame implements ActionListener {
	
	private PanelMain panMain;
	private PanelOption panOpt;
	
	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 400; 
	
	public static final int TAB_INCOME = 0011;
	public static final int TAB_EXPENSE = 1100;
	
	/**
	 * The master class that handles all the data
	 */
	private T handler;
	
	/** Normal constructor for RecordFrame - Starts the window with the expenses view */
	public RecordFrame() {
		super();
		this.init();
	}
	
	/**
	 * Constructor to specify the initial tab to be displayed
	 * @param initTab use either TAB_INCOME or TAB_EXPENSE to indicate which tab to choose
	 */
	public RecordFrame(int initTab) {
		this();
		switch(initTab) {
			case TAB_INCOME: 
				panMain.toggleIncomeTab();
				break;
			case TAB_EXPENSE:
				panMain.toggleExpenseTab();
			default:break;
		}
	}
	
	/**
	 * This methods contains all the starting initialisation codes for this window.
	 */
	private void init() {
		this.setTitle("EzXpns - New Record");
		this.setLayout(new BorderLayout());
		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		panMain = new PanelMain();
		this.add(panMain, BorderLayout.CENTER);
		
		panOpt = new PanelOption(this);
		this.add(panOpt, BorderLayout.SOUTH);	
	}
	
	/** To update the preview panel when the user does changes... */
	public void updatePreview() {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.panOpt.getSaveBtn() == e.getSource()) { // Save button has been invoked.
			if(panMain.validateForm()) { // Invoke validation
				// all is good. save as new Record.
				Record newRecord = panMain.save(); 
				handler.createRecord(newRecord); // Probably need to know which type (Ex/In) this record is
				
				// Check if the user subtlety created a new category
				// handler.createCategory(newCat);
				
				// Check if it is a recurring record
				// do the necessary to ensure that EzXpns knows it. 
				
				// Display confirmation?
			}
		}
		if(this.panOpt.getCancelBtn() == e.getSource()) {
			// Cancel button has been invoked. 
			// Invoke confirmation? - will it be retarded to invoke confirmation?
			this.dispose();
		}
	}
	
	/**
	 * Set the reference for the handler that will be doing all 
	 * the data management
	 * @param handlerRef reference for the handler
	 */
	public void setHandler(T handlerRef) {
		this.handler = handlerRef;
	}
}

@SuppressWarnings("serial")
class PanelRecur extends JPanel implements ActionListener{
	
	private JCheckBox chkRecur;
	private JComboBox cboxFrequency;
	private JTextField txtStart, txtEnd;
	
	public PanelRecur() {
		super();
		this.setBackground(Color.WHITE);
		this.setLayout(new java.awt.FlowLayout());
		init();
	}
	
	private void init() {
		chkRecur = new JCheckBox("Repeating Record");
		chkRecur.setBackground(Color.WHITE);
		chkRecur.addActionListener(this);
		this.add(chkRecur);
		cboxFrequency = new JComboBox();
		cboxFrequency.setEditable(false);
		
		txtStart = new JTextField("Commence Date");
		txtStart.setEnabled(false);
		txtEnd = new JTextField("Terminate Date");
		txtEnd.setEnabled(false);
		this.add(txtStart);
		this.add(txtEnd);
	}
	
	/**
	 * To toggle the enabled-ability of the recurring options
	 */
	public void toggle() {
		if(this.chkRecur.isSelected()) {
			this.cboxFrequency.setEnabled(true);
			this.txtStart.setEnabled(true);
			this.txtEnd.setEnabled(true);
		}
		else {
			this.cboxFrequency.setEnabled(false);
			this.txtStart.setEnabled(false);
			this.txtEnd.setEnabled(false);
		}
	}
	
	/**
	 * To toggle the visibility of the recurring options
	 */
	public void toggleVisiblity() {
		if(this.chkRecur.isSelected()) {
			this.cboxFrequency.setVisible(true);
			this.txtStart.setVisible(true);
			this.txtEnd.setVisible(true);
		}
		else {
			this.cboxFrequency.setVisible(false);
			this.txtStart.setVisible(false);
			this.txtEnd.setVisible(false);
		}
	}
	
	/**
	 * Method to retrieve the entered starting date of the recurrence
	 */
	public void getStart() {}
	
	/**
	 * Method to retrieve the entered ending date of the recurrence
	 */
	public void getEnd() {}
	
	/**
	 * Method to retrieve the frequency of the recurrence
	 */
	public void getFrequency() {}
	
	public boolean isToRecur() { return this.chkRecur.isSelected(); }

	@Override
	public void actionPerformed(ActionEvent e) { this.toggle(); }
}

@SuppressWarnings("serial")
class PanelMain extends JPanel {
	
	private PanelExpense panExpense;
	private PanelIncome panIncome;
	private PanelRecur panRecurOpt;
	private JTabbedPane tabs;
	
	public PanelMain() {
		super();
		this.setLayout(new BorderLayout());
		
		tabs = new JTabbedPane();
		tabs.setBackground(Color.WHITE);
		
		panExpense = new PanelExpense();
		panIncome = new PanelIncome();
		
		tabs.addTab("Expense", null, this.panExpense, "Expenses");
		tabs.addTab("Income", null, this.panIncome, "Income");
		// this.tabs.setMnemonicAt(); // setting keyboard shortcut
		
		this.add(tabs, BorderLayout.CENTER);
		
		// Create Recurring options panel
		this.panRecurOpt = new PanelRecur();
		this.add(panRecurOpt, BorderLayout.SOUTH);
	}
	
	/**
	 * Invoke the validation methods for either live screen
	 * @return true if successful, otherwise false
	 */
	public boolean validateForm() {
		return isExpense() ? panExpense.validateFields(): panIncome.validateFields();
	}
	
	/**
	 * Invoke the save method on either live screen
	 * @return the new Record object containing the user inputs.
	 */
	public Record save() {
		return isExpense() ? panExpense.save() : panIncome.save();
	}
	
	/**
	 * To check if the current tab is the Expense Tab
	 * @return true if it is, otherwise false
	 */
	private boolean isExpense() {return tabs.getSelectedIndex()==0;}
	
	/**
	 * To check if the current tab is the Expense Tab
	 * @return true if it is, otherwise false
	 */
	private boolean isIncome() {return tabs.getSelectedIndex()==1;}
	
	/**
	 * Method to toggle to the tab for a new income record
	 */
	public void toggleIncomeTab() {
		tabs.setSelectedIndex(1);
	}
	
	/**
	 * Method to toggle to the tab for a new expense record
	 */
	public void toggleExpenseTab() {
		tabs.setSelectedIndex(0);		
	}
	
	// Access and Mutate methods (for internal internal components)
	// Auto-complete - requires action listener
	// Auto-calculate - requires action listener
}

@SuppressWarnings("serial")
class PanelExpense extends JPanel {
	
	public final int TOP_PAD = 27;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 120;
	public final int TEXTFIELD_SIZE = 20;
	
	private ButtonGroup bgType;
	private JRadioButton rbtnNeed, rbtnWant;
	private JLabel lblAmt, lblName, lblType, lblCat, lblPayment, lblDate, lblDesc;
	private JTextField 	txtAmt, txtName, txtDate;
	private TextArea taDesc;
	private JComboBox cboxCategory, cboxPayment;
	
	public PanelExpense() {
		super();
		this.setBackground(Color.WHITE);
		/* The Layout governing the positions */
		SpringLayout loForm = new SpringLayout();
		this.setLayout(loForm);
		
		// Initialise Radio Buttons
		this.lblType = new JLabel("Type");
		this.bgType = new ButtonGroup();
		this.rbtnNeed = new JRadioButton("Need");
		rbtnNeed.setBackground(Color.WHITE);
		this.rbtnWant = new JRadioButton("Want");
		rbtnWant.setBackground(Color.WHITE);
		this.rbtnNeed.setSelected(true);
		this.bgType.add(rbtnNeed);
		this.bgType.add(rbtnWant);
		// Action Listener to update the label text in preview?
		
		// Initialise Combo Box - To be a dynamic updating list.
		this.lblCat = new JLabel("Category");
		this.cboxCategory = new JComboBox(getCategories());
		this.cboxCategory.setEditable(true);
		
		// Initialise Combo Box - To be a dynamic updating list.
		this.lblPayment = new JLabel("Payment Mode");
		this.cboxPayment = new JComboBox(getPayModes());
		this.cboxPayment .setEditable(true);
		
		this.lblName = new JLabel("Name");
		this.txtName = new JTextField("", TEXTFIELD_SIZE);
		this.add(lblName);
		this.add(txtName);
		loForm.putConstraint(SpringLayout.WEST, lblName, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtName, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD, SpringLayout.NORTH, this);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD, SpringLayout.NORTH, this);
		// AutoComplete (for the rest of the fields - when completed?)
		
		this.add(lblPayment);
		this.add(cboxPayment);
		loForm.putConstraint(SpringLayout.WEST, lblPayment, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxPayment, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblPayment, TOP_PAD, SpringLayout.NORTH, lblName);
		loForm.putConstraint(SpringLayout.NORTH, cboxPayment, TOP_PAD, SpringLayout.NORTH, txtName);
		
		this.add(lblCat);
		this.add(cboxCategory);
		loForm.putConstraint(SpringLayout.WEST, lblCat, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxCategory, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblCat, TOP_PAD, SpringLayout.NORTH, lblPayment);
		loForm.putConstraint(SpringLayout.NORTH, cboxCategory, TOP_PAD, SpringLayout.NORTH, cboxPayment);
				
		this.add(lblType);
		this.add(rbtnNeed);
		this.add(rbtnWant);
		loForm.putConstraint(SpringLayout.WEST, lblType, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblType, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.WEST, rbtnNeed, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, rbtnWant, COL2_PAD, SpringLayout.WEST, rbtnNeed);
		loForm.putConstraint(SpringLayout.NORTH, rbtnNeed, TOP_PAD, SpringLayout.NORTH, cboxCategory);
		loForm.putConstraint(SpringLayout.NORTH, rbtnWant, TOP_PAD, SpringLayout.NORTH, cboxCategory);
		
		this.lblAmt = new JLabel("Amount");
		this.txtAmt = new JTextField("", TEXTFIELD_SIZE);
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, rbtnNeed);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, rbtnWant);
		// This will need a listener to calculate and display the information on the label

		this.lblDate = new JLabel("Date");
		this.txtDate = new JTextField("", TEXTFIELD_SIZE);
		this.add(lblDate);
		this.add(txtDate);
		loForm.putConstraint(SpringLayout.WEST, lblDate, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDate, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDate, TOP_PAD, SpringLayout.NORTH, lblAmt);
		loForm.putConstraint(SpringLayout.NORTH, txtDate, TOP_PAD, SpringLayout.NORTH, txtAmt);
		// Insert Calendar View? Drop down box here

		this.lblDesc = new JLabel("Remarks");
		this.taDesc = new TextArea("", 5, 25, TextArea.SCROLLBARS_NONE); /* Initial text, height, width, scroll bar option */
		this.add(lblDesc);
		this.add(taDesc);
		loForm.putConstraint(SpringLayout.WEST, lblDesc, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, taDesc, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDesc, TOP_PAD, SpringLayout.NORTH, lblDate);
		loForm.putConstraint(SpringLayout.NORTH, taDesc, TOP_PAD, SpringLayout.NORTH, txtDate);
	}
	
	/**
	 * To retrieve the list of user-defined categories
	 * @return User-defined categories
	 */
	private String[] getCategories() {
		return new String [] {" Food", " Transport", " Misc.", " Entertainment", " Studies"};
	}
	
	/**
	 * To retrieve the list of user-defined & pre-defined payment modes
	 * @return Stored payment modes 
	 */
	private String[] getPayModes() {
		return new String [] {" Cash", " PayPal", " Cheque"}; 
	}
	
	public String getName() {return this.txtName.getText();}
	public String getAmt() {return this.txtAmt.getText();}
	public String getCat() {
		if(this.cboxCategory.getSelectedIndex() < 0) {
			// User defined new category
			String userInput = this.cboxCategory.getSelectedItem().toString();
			return userInput;
		}
		// NOTE: MAY HAVE TO STORE A THE LIST TO RETRIEVE IN VIA INDEX.
		return this.cboxCategory.getSelectedItem().toString();
	}
	public String getDate() {return null;}
	public String getMode() {
		// return selected payment mode
		return null;
	}
	
	/**
	 * to validate the fields entered into the system.
	 * @return true is there is no problem with inputs, else false;
	 */
	public boolean validateFields() {
		System.out.println(txtAmt.getText());
		System.out.println(txtName.getText());
		System.out.println(txtDate.getText());
		System.out.println(taDesc.getText());
		System.out.println(getCat());
		return true;
		// Validation method (mainly for calculation)
	}
	
	/** 
	 * Save the entered field as a new record
	 * @return Record object containing the user input
	 */
	public Record save() {
		return null;
	}
}

/**
 * Form for Income records
 */
@SuppressWarnings("serial")
class PanelIncome extends JPanel {
	
	public final int TOP_PAD = 27;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 120;
	public final int TEXTFIELD_SIZE = 20;
	private JLabel lblAmt, lblName, lblCat, lblDesc, lblDate;
	private JTextField 	txtAmt, txtName, txtDate;
	private JComboBox cboxCat;
	private TextArea taDesc;
	
	public PanelIncome() {
		super();
		this.setBackground(Color.WHITE);
		/* The Layout governing the positions */
		SpringLayout loForm = new SpringLayout();
		this.setLayout(loForm);
		
		lblName = new JLabel("Name");
		txtName = new JTextField("", TEXTFIELD_SIZE);
		this.add(lblName);
		this.add(txtName);
		loForm.putConstraint(SpringLayout.WEST, lblName, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtName, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD, SpringLayout.NORTH, this);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD, SpringLayout.NORTH, this);
		// AutoComplete (for the rest of the fields - when completed?)
		
		lblCat = new JLabel("Category");
		cboxCat = new JComboBox(this.getCategories());
		cboxCat.setEditable(true);
		this.add(lblCat);
		this.add(cboxCat);
		loForm.putConstraint(SpringLayout.WEST, lblCat, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxCat, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblCat, TOP_PAD, SpringLayout.NORTH, lblName);
		loForm.putConstraint(SpringLayout.NORTH, cboxCat, TOP_PAD, SpringLayout.NORTH, txtName);
		
		lblAmt = new JLabel("Amount");
		txtAmt = new JTextField("", TEXTFIELD_SIZE);
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, cboxCat);
		// This will need a listener to calculate and display the information on the label
		
		lblDate = new JLabel("Date");
		txtDate = new JTextField("", TEXTFIELD_SIZE);
		this.add(lblDate);
		this.add(txtDate);
		loForm.putConstraint(SpringLayout.WEST, lblDate, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDate, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDate, TOP_PAD, SpringLayout.NORTH, lblAmt);
		loForm.putConstraint(SpringLayout.NORTH, txtDate, TOP_PAD, SpringLayout.NORTH, txtAmt);
		// Insert Calendar View? Drop down box here
		
		lblDesc = new JLabel("Remarks");
		taDesc = new TextArea("", 5, 25, TextArea.SCROLLBARS_NONE);
		this.add(lblDesc);
		this.add(taDesc);
		loForm.putConstraint(SpringLayout.WEST, lblDesc, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, taDesc, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDesc, TOP_PAD, SpringLayout.NORTH, lblDate);
		loForm.putConstraint(SpringLayout.NORTH, taDesc, TOP_PAD, SpringLayout.NORTH, txtDate);
	}
	
	/**
	 * To get the list of user-defined categories
	 * @return
	 */
	private String[] getCategories() {
		return new String[] {"Allowance", "Ang Bao"};
	}
	
	/**
	 * to validate the fields entered into the system.
	 * @return true is there is no problem with inputs, else false;
	 */
	public boolean validateFields() {
		return true;
	}
	
	/** 
	 * Save the entered field as a new record
	 * @return Record object containing the user input
	 */
	public Record save() {
		return null;
	}
}

/**
 * Panel containing the options available to this frame
 */
@SuppressWarnings("serial")
class PanelOption extends JPanel {
	
	private JButton btnSave, btnCancel;
	
	public PanelOption(ActionListener frame) {
		super();
		
		this.setBackground(Color.WHITE);
		
		btnSave = new JButton("Save");
		btnCancel = new JButton("Or discard changes");
		btnCancel.setBorderPainted(false);
		btnCancel.setContentAreaFilled(false);
		btnCancel.setForeground(Color.DARK_GRAY);
		
		btnSave.addActionListener(frame);
		btnCancel.addActionListener(frame);
		
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent mEvent) { // Hover start
				JButton btn = (JButton) mEvent.getSource();
				btn.setForeground(Color.BLUE);
				
				/* Underlining the word for "hover*/
				Font btnFont = btn.getFont();
				Map attribute = btnFont.getAttributes();
				attribute.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				btn.setFont(btnFont.deriveFont(attribute));
			}
			
			public void mouseExited(MouseEvent mEvent) { // Hover end
				JButton btn = (JButton) mEvent.getSource();
				btn.setForeground(Color.DARK_GRAY);
				// Current Issue: unable to remove the underlining from after hovering over it.
				
			}
		});
		
		this.add(btnSave);
		this.add(btnCancel);
	}
	
	public JButton getSaveBtn() {return this.btnSave;}
	public JButton getCancelBtn() {return this.btnCancel;}
}

//
///**
// * Idea - to make it damn easy for the user to press the save button.
// * Preview to let the user know that hey - its mostly handled. (may not be accepted)
// * "help text to aid user in adding records?"
// * 
// */
//@SuppressWarnings("serial")
//class PanelPreview extends JPanel {
//	private JLabel lblName;
//	private JLabel lblAmt;
//	private JLabel lblType;
//	private JLabel lblCat;
//	private JLabel lblDesciption;
//	
//	public PanelPreview() {
//		super();
//	}
//}