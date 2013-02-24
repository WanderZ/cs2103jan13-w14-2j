import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class FrameRecord extends JFrame {
	
	JPanel panPreview, panRecur, panMain, panOpt;
	
	public FrameRecord() {
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
		this.panOpt = new PanelOption();
		this.add(panOpt, BorderLayout.SOUTH);
	}
	
	public void showScreen() {this.setVisible(true);}
	public void hideScreen() {this.setVisible(false);}
	
	/**
	 * To update the preview panel when the user does changes...
	 */
	public void updatePreview() {}

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

@SuppressWarnings("serial")
class PanelRecur extends JPanel {
	
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
		this.add(chkRecur);
		this.cboxFrequency = new JComboBox();
		this.cboxFrequency.setEditable(false);
		
		this.txtStart = new JTextField("Commence Date");
		this.txtEnd = new JTextField("Terminate Date");
		this.add(txtStart);
		this.add(txtEnd);
	}
}

@SuppressWarnings("serial")
class PanelMain extends JPanel {
	
	private JPanel panExpense, panIncome;
	private JTabbedPane tabs;
	
	public PanelMain() {
		super();
		this.setLayout(new BorderLayout());
		this.tabs = new JTabbedPane();
		
		this.panExpense = new PanelExpense();
		this.panIncome = new PanelIncome();
		
		this.tabs.addTab("Expense", null, this.panExpense, "Expenses");
		this.tabs.addTab("Income", null, this.panIncome, "Income");
		// this.tabs.setMnemonicAt();
		
		this.add(tabs, BorderLayout.CENTER);
	}
}

@SuppressWarnings("serial")
class PanelExpense extends JPanel {
	
	public final int TOP_PAD = 25;
	public final int COL1_PAD = 20;
	public final int COL2_PAD = 90;
	private ButtonGroup bgType;
	private JRadioButton rbtnNeed, rbtnWant;
	private JLabel lblAmt, lblName, lblType, lblCat, lblDate, lblDesc;
	private JTextField 	txtAmt, txtName, txtDate;
	private TextArea taDesc;
	private JComboBox cboxCat;
	
	public PanelExpense() {
		super();
		SpringLayout loForm = new SpringLayout();
		this.setLayout(loForm);
		
		// Initialising the components
		this.lblCat = new JLabel("Category");
		this.lblName = new JLabel("Name");
		this.lblAmt = new JLabel("Amount");
		this.lblType = new JLabel("Type");
		this.lblDate = new JLabel("Date");
		this.lblDesc = new JLabel("Remarks");
		
		this.txtAmt = new JTextField("", 15);
		this.txtName = new JTextField("", 15);
		this.txtDate = new JTextField("", 15);
		this.taDesc = new TextArea("", 5, 30, TextArea.SCROLLBARS_NONE); /* inital text, height, width, scrollbars options*/
		
		// Initialise Radio Buttons
		bgType = new ButtonGroup();
		rbtnNeed = new JRadioButton("Need");
		rbtnWant = new JRadioButton("Want");
		rbtnNeed.setSelected(true);
		bgType.add(rbtnNeed);
		bgType.add(rbtnWant);
		// Action Listener to update the label text in preview?
		
		// Initialise Combo Box - To be a dynamic updating list.
		cboxCat = new JComboBox(getCategories());
		cboxCat.setEditable(true);
		
		this.add(lblName);
		this.add(txtName);
		loForm.putConstraint(SpringLayout.WEST, lblName, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtName, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD, SpringLayout.NORTH, this);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD, SpringLayout.NORTH, this);
		// AutoComplete (for the rest of the fields - when completed?)
		
		this.add(lblCat);
		this.add(cboxCat);
		loForm.putConstraint(SpringLayout.WEST, lblCat, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxCat, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblCat, TOP_PAD, SpringLayout.NORTH, lblName);
		loForm.putConstraint(SpringLayout.NORTH, cboxCat, TOP_PAD, SpringLayout.NORTH, txtName);
		
		this.add(lblType);
		this.add(rbtnNeed);
		this.add(rbtnWant);
		loForm.putConstraint(SpringLayout.WEST, lblType, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblType, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.WEST, rbtnNeed, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, rbtnWant, COL2_PAD, SpringLayout.WEST, rbtnNeed);
		loForm.putConstraint(SpringLayout.NORTH, rbtnNeed, TOP_PAD, SpringLayout.NORTH, cboxCat);
		loForm.putConstraint(SpringLayout.NORTH, rbtnWant, TOP_PAD, SpringLayout.NORTH, cboxCat);
		
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, rbtnNeed);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, rbtnWant);
		// This will need a listener to calculate and display the information on the label
		
		this.add(lblDate);
		this.add(txtDate);
		loForm.putConstraint(SpringLayout.WEST, lblDate, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDate, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDate, TOP_PAD, SpringLayout.NORTH, lblAmt);
		loForm.putConstraint(SpringLayout.NORTH, txtDate, TOP_PAD, SpringLayout.NORTH, txtAmt);
		// Insert Calendar View? Dropdown box here
		
		this.add(lblDesc);
		this.add(taDesc);
		loForm.putConstraint(SpringLayout.WEST, lblDesc, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, taDesc, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDesc, TOP_PAD, SpringLayout.NORTH, lblDate);
		loForm.putConstraint(SpringLayout.NORTH, taDesc, TOP_PAD, SpringLayout.NORTH, txtDate);
		
	}
	
	private String[] getCategories() {
		return new String [] {"Food", "Transport", "Misc.", "Entertainment", "Studies"};
	}
	
}

@SuppressWarnings("serial")
class PanelIncome extends JPanel {
	
	private JTextField 	txtAmt, txtName;
	
	public PanelIncome() {
		super();
		// set layout
		txtAmt = new JTextField();
		txtName = new JTextField();
		
	}
}

@SuppressWarnings("serial")
class PanelOption extends JPanel {
	JButton btnSave, btnCancel;
	
	public PanelOption() {
		super();
		this.btnSave = new JButton("Save");
		this.btnCancel = new JButton("Discard");
		/* Maybe Action Listeners here*/
		this.add(btnSave);
		this.add(btnCancel);
	}
	
}