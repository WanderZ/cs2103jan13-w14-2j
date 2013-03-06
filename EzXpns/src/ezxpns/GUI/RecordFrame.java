package ezxpns.GUI;

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

@SuppressWarnings("serial")
public class RecordFrame extends JFrame implements ActionListener {
	
	private JPanel panMain, panOpt;
	
	public RecordFrame() {
		super();
		this.setTitle("EzXpns - New Record");
		this.setLayout(new BorderLayout());
		this.setBounds(0, 0, 600, 400);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.initialize();
	}
	
	private void initialize() {
		this.panMain = new PanelMain();
		this.add(panMain, BorderLayout.CENTER);
		this.panOpt = new PanelOption(this);
		this.add(panOpt, BorderLayout.SOUTH);
		
	}
	
	public void showScreen() {this.setVisible(true);}
	public void hideScreen() {this.setVisible(false);}
	
	public void disposeFrame() { this.dispose(); }
	
	/**
	 * To update the preview panel when the user does changes...
	 */
	public void updatePreview() {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(((PanelOption) this.panOpt).getSaveBtn() == e.getSource()) {
			// Save button has been invoked. 
			// Invoke validation
		}
		if(((PanelOption) this.panOpt).getCancelBtn() == e.getSource()) {
			// Cancel button has been invoked. 
			// Invoke confirmation? - will it be retarded to invoke confirmation?
		}
	}

}

@SuppressWarnings("serial")
class PanelRecur extends JPanel implements ActionListener{
	
	private JCheckBox chkRecur;
	private JComboBox cboxFrequency;
	private JTextField txtStart, txtEnd;
	
	public PanelRecur () {
		super();
		this.setLayout(new java.awt.FlowLayout());
		initPan();
	}
	
	private void initPan() {
		this.chkRecur = new JCheckBox("Repeating Record");
		this.chkRecur.addActionListener(this);
		this.add(chkRecur);
		this.cboxFrequency = new JComboBox();
		this.cboxFrequency.setEditable(false);
		
		this.txtStart = new JTextField("Commence Date");
		this.txtStart.setEnabled(false);
		this.txtEnd = new JTextField("Terminate Date");
		this.txtEnd.setEnabled(false);
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
	
	public void getStart() {}
	public void getEnd(){}
	public void getFrequency() {}
	public boolean isToRecur() { return this.chkRecur.isSelected(); }

	@Override
	public void actionPerformed(ActionEvent e) { this.toggle(); }
}

@SuppressWarnings("serial")
class PanelMain extends JPanel {
	
	private JPanel panExpense, panIncome, panRecurOpt;
	private JTabbedPane tabs;
	
	public PanelMain() {
		super();
		this.setLayout(new BorderLayout());
		this.tabs = new JTabbedPane();
		
		this.panExpense = new PanelExpense();
		this.panIncome = new PanelIncome();
		
		this.tabs.addTab("Expense", null, this.panExpense, "Expenses");
		this.tabs.addTab("Income", null, this.panIncome, "Income");
		// this.tabs.setMnemonicAt(); // set keyboard shortcut
		
		this.add(tabs, BorderLayout.CENTER);
		
		// Create Recurring options panel
		this.panRecurOpt = new PanelRecur();
		this.add(panRecurOpt, BorderLayout.SOUTH);
	}
	
	// Access and Mutate methods (for internal internal components)
	// Auto-complete - requires action listener
	// Auto-calculate - requires action listener
}

/* Panel is missing payment modes field */
@SuppressWarnings("serial")
class PanelExpense extends JPanel {
	
	public final int TOP_PAD = 27;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 120;
	private ButtonGroup bgType;
	private JRadioButton rbtnNeed, rbtnWant;
	private JLabel lblAmt, lblName, lblType, lblCat, lblPayment, lblDate, lblDesc;
	private JTextField 	txtAmt, txtName, txtDate;
	private TextArea taDesc;
	private JComboBox cboxCategory, cboxPayment;
	
	public PanelExpense() {
		super();
		
		/* The Layout governing the positions */
		SpringLayout loForm = new SpringLayout();
		this.setLayout(loForm);
		
		// Initialise Radio Buttons
		this.lblType = new JLabel("Type");
		this.bgType = new ButtonGroup();
		this.rbtnNeed = new JRadioButton("Need");
		this.rbtnWant = new JRadioButton("Want");
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
		this.txtName = new JTextField("", 20);
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
		this.txtAmt = new JTextField("", 20);
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, rbtnNeed);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, rbtnWant);
		// This will need a listener to calculate and display the information on the label

		this.lblDate = new JLabel("Date");
		this.txtDate = new JTextField("", 20);
		this.add(lblDate);
		this.add(txtDate);
		loForm.putConstraint(SpringLayout.WEST, lblDate, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDate, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDate, TOP_PAD, SpringLayout.NORTH, lblAmt);
		loForm.putConstraint(SpringLayout.NORTH, txtDate, TOP_PAD, SpringLayout.NORTH, txtAmt);
		// Insert Calendar View? Dropdown box here

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
	
	// Validation method (mainly for calculation
	// Save method - to send for saving into "database" or GUI internal memory.
}

/**
 * Form panel for Income records
 */
@SuppressWarnings("serial")
class PanelIncome extends JPanel {
	
	public static final int TEXTFIELD_SIZE = 20;
	private JLabel lblAmt, lblName;
	private JTextField 	txtAmt, txtName;
	// Combobox for category (guess there is no need for payment mode
	// Description text area
	// Date of adding
	
	public PanelIncome() {
		super();
		
		// Set the layout to govern each element's location
		
		this.lblName = new JLabel("Name");
		this.txtName = new JTextField("", TEXTFIELD_SIZE);
		
		this.lblAmt = new JLabel("Amount");
		this.txtAmt = new JTextField("", TEXTFIELD_SIZE);
		
		
				
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
		this.btnSave = new JButton("Save");
		this.btnCancel = new JButton("Or discard changes");
		this.btnCancel.setBorderPainted(false);
		this.btnCancel.setContentAreaFilled(false);
		this.btnCancel.setForeground(Color.DARK_GRAY);
		
		this.btnSave.addActionListener(frame);
		this.btnCancel.addActionListener(frame);
		
		this.btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				btn.setForeground(Color.BLUE);
				Font btnFont = btn.getFont();
				Map attribute = btnFont.getAttributes();
				attribute.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				btn.setFont(btnFont.deriveFont(attribute));
			}
			
			public void mouseExited(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				btn.setForeground(Color.DARK_GRAY);
			}
		});
		
		this.add(btnSave);
		this.add(btnCancel);
	}	
	
	public JButton getSaveBtn() {return this.btnSave;}
	public JButton getCancelBtn() {return this.btnCancel;}
}

/**
 * Idea - to make it damn easy for the user to press the save button.
 * Preview to let the user know that hey - its mostly handled. (may not be accepted)
 * "help text to aid user in adding records?"
 * 
 */
@SuppressWarnings("serial")
class PanelPreview extends JPanel {
	private JLabel lblName;
	private JLabel lblAmt;
	private JLabel lblType;
	private JLabel lblCat;
	private JLabel lblDesciption;
	
	public PanelPreview() {
		super();
	}
}