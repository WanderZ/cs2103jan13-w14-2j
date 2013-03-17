package ezxpns.GUI;
import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.ExpenseType;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.PayMethodHandler;
import ezxpns.data.records.PaymentMethod;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * This is a JFrame object (Window) that allows users to enter a new record (Expense/Income) into the EzXpns
 */
@SuppressWarnings("serial")
public class RecordFrame extends JFrame implements ActionListener {
	
	public static final int DEFAULT_WIDTH = 760;
	public static final int DEFAULT_HEIGHT = 550; 
	
	public static final int TAB_INCOME = 0011;
	public static final int TAB_EXPENSE = 1100;
	
	private RecordHandler recHandler;
	private CategoryHandler incomeHandler, expenseHandler;
	private PayMethodHandler payHandler;
	
	private PanelMain panMain;
	private PanelOption panOpt;
	
	/** 
	 * Normal constructor for RecordFrame - Starts the window with the expenses view
	 * @param handlerRef Reference to the handler that will handle all the data management  
	 */
	public RecordFrame(
			RecordHandler recHandlerRef, 
			CategoryHandler incomeHandlerRef, 
			CategoryHandler expenseHandlerRef,
			PayMethodHandler payHandlerRef
			) {
		
		super();
		
		recHandler = recHandlerRef;
		incomeHandler = incomeHandlerRef;
		expenseHandler = expenseHandlerRef;
		payHandler = payHandlerRef;
		
		this.init();
	}
	
	/**
	 * Constructor to specify the initial tab to be displayed
	 * @param initTab use either TAB_INCOME or TAB_EXPENSE to indicate which tab to choose
	 * @param handlerRef Reference to the handler that will handle all the data management
	 */
	public RecordFrame(
			RecordHandler recHandlerRef, 
			CategoryHandler incomeHandlerRef, 
			CategoryHandler expenseHandlerRef,
			PayMethodHandler payHandlerRef,
			int initTab) {
		
		this(recHandlerRef, incomeHandlerRef, expenseHandlerRef, payHandlerRef);
		
		/* This part may need refactoring to enums */
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
	 * Initialize this frame with its components and properties
	 */
	private void init() {
		this.setTitle("EzXpns - New Record");
		this.setLayout(new BorderLayout());
		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		panMain = new PanelMain(recHandler, incomeHandler, expenseHandler, payHandler);
		this.add(panMain, BorderLayout.CENTER);
		
		panOpt = new PanelOption(this);
		this.add(panOpt, BorderLayout.SOUTH);
		
		panMain.toggleExpenseTab(); // Default
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(this.panOpt.getSaveBtn() == e.getSource()) { // Save button has been invoked.
			if(panMain.validateForm()) { // Invoke validation
				panMain.save();
				// all is good. save as new Record.
				// Check if it is a recurring record
				// do the necessary to ensure that EzXpns knows it. 
			}
		}
		if(this.panOpt.getCancelBtn() == e.getSource()) {
			this.dispose();
		}
	}
}


/** Panel Containing the Form for user to fill up - to create new record */
@SuppressWarnings("serial")
class PanelMain extends JPanel {
	
	public final String CARD_EXPENSE = "Expenses";
	public final String CARD_INCOME = "Income";
	
	private PanelExpense panExpense;
	private PanelIncome panIncome;
	private PanelRecur panRecurOpt;
	private CardLayout loCard;
	private JPanel metroTabs, metroTabBtns, metroTabContent;
	private JButton mtabExpense, mtabIncome;
		
	/**
	 * Constructor for the main content panel for new record
	 * @param recHandlerRef reference to the record handler
	 * @param incomeHandlerRef reference to the income category handler
	 * @param expenseHandlerRef reference to the expense category handler
	 * @param payHandlerRef reference to the payment method handler
	 */
	public PanelMain(
			RecordHandler recHandlerRef,
			CategoryHandler incomeHandlerRef,
			CategoryHandler expenseHandlerRef,
			PayMethodHandler payHandlerRef
			) {
		super();
		this.setLayout(new BorderLayout());
		
		metroTabs = new JPanel(); // the main panel for keeping everything together
		metroTabs.setLayout(new BorderLayout(15, 15));
		metroTabs.setBackground(Color.WHITE);
		
		// Buttons to trigger
		metroTabBtns = new JPanel();
		metroTabBtns.setLayout(new GridLayout(1, 0, 15, 15));
		metroTabBtns.setOpaque(false);
		
		metroTabBtns.add(getExpenseTab());
		metroTabBtns.add(getIncomeTab());
		
		metroTabs.add(metroTabBtns, BorderLayout.NORTH);
		
		panExpense = new PanelExpense(recHandlerRef, expenseHandlerRef, payHandlerRef);
		panIncome = new PanelIncome(recHandlerRef, incomeHandlerRef);

		metroTabContent = new JPanel();
		
		loCard = new CardLayout(15, 15);
		metroTabContent.setLayout(loCard);
		
		metroTabContent.add(panExpense, this.CARD_EXPENSE);
		metroTabContent.add(panIncome, this.CARD_INCOME);
		metroTabs.add(metroTabContent, BorderLayout.CENTER);
		
		loCard.show(metroTabContent, CARD_EXPENSE);
		
		// this.tabs.setMnemonicAt(); // setting keyboard shortcut
		this.add(metroTabs, BorderLayout.CENTER);
		
		// Create Recurring options panel
		panRecurOpt = new PanelRecur();
		this.add(panRecurOpt, BorderLayout.SOUTH);
	}
	
	/**
	 * Retrieve the tab for the expenses
	 * @return JButton object of the initialized JButton for the expense tab
	 */
	private JButton getExpenseTab() {
		if(mtabExpense == null) {
			mtabExpense = new JButton(CARD_EXPENSE);
			mtabExpense.setFont(new Font("Segoe UI", 0, 24)); // #Font
			// mtabExpense.setBorderPainted(false);
			mtabExpense.setFocusPainted(false);
			mtabExpense.setContentAreaFilled(false);
			mtabExpense.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent mEvent) { // Hover start
					JButton btn = (JButton) mEvent.getSource();
					btn.setForeground(Color.CYAN);
				}
				
				@Override
				public void mousePressed(MouseEvent mEvent) {
					toggleExpenseTab();
				}
				
				@Override
				public void mouseExited(MouseEvent mEvent) { // Hover end
					JButton btn = (JButton) mEvent.getSource();
					btn.setForeground(
							btn.isEnabled() ? Color.BLACK : Color.WHITE
						);
				}
			});
		}
		return mtabExpense;
	}
	
	/** 
	 * Retrieve the tab for the income
	 * @return JButton object of the initialized JButton for the income tab
	 */
	private JButton getIncomeTab() {
		if(mtabIncome == null) {
			mtabIncome = new JButton(CARD_INCOME);
			mtabIncome.setFont(new Font("Segoe UI", 0, 24)); // #Font
			// mtabIncome.setBorderPainted(false);
			mtabIncome.setFocusPainted(false);
			mtabIncome.setContentAreaFilled(false);
			mtabIncome.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent mEvent) { // Hover start
					JButton btn = (JButton) mEvent.getSource();
					btn.setForeground(Color.CYAN);
				}
				
				public void mousePressed(MouseEvent mEvent) {
					toggleIncomeTab();
				}
				
				public void mouseExited(MouseEvent mEvent) { // Hover end
					JButton btn = (JButton) mEvent.getSource();
					btn.setForeground(
							btn.isEnabled() ? Color.BLACK : Color.WHITE
						);
				}
			});
		}
		return mtabIncome;
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
	public boolean isExpense() {return panExpense.isVisible();}
	
	/**
	 * To check if the current tab is the Expense Tab
	 * @return true if it is, otherwise false
	 */
	public boolean isIncome() {return panIncome.isVisible();}

	/**
	 * Method to toggle to the tab for a new income record
	 */
	public void toggleIncomeTab() {
		if(isExpense()) {
			loCard.show(metroTabContent, CARD_INCOME);
			// Indicate some difference to let user know that this tab is selected
			changeFocus(this.mtabIncome, this.mtabExpense);
		}
	}
	
	/**
	 * Method to toggle to the tab for a new expense record
	 */
	public void toggleExpenseTab() {
		if(isIncome()) {
			loCard.show(metroTabContent, CARD_EXPENSE);
			// Indicate some difference to let user know that this tab is selected
			changeFocus(this.mtabExpense, this.mtabIncome);
		}		
	}
	
	/**
	 * Method to manage the look and feel of the tabs on focus and off focus
	 * @param toFocus the JButton to create focus
	 * @param rmFocus the JButton to remove focus
	 */
	private void changeFocus(JButton toFocus, JButton rmFocus) {
		toFocus.setBackground(Color.WHITE);
		toFocus.setContentAreaFilled(true);
		toFocus.setEnabled(false);
		toFocus.setBorder(BorderFactory.createLoweredBevelBorder());
		
		rmFocus.setContentAreaFilled(false);
		rmFocus.setForeground(Color.BLACK);
		rmFocus.setEnabled(true);
		rmFocus.setBorder(BorderFactory.createRaisedBevelBorder());
	}
	
	// Access and Mutate methods (for internal internal components)
	// Auto-complete - requires action listener
	// Auto-calculate - requires action listener
}

/** Panel to contain and maintain the form for a new expense record */
@SuppressWarnings("serial")
class PanelExpense extends JPanel {
	
	// #Constants
	public final int TOP_PAD = 27;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 200;
	public final int TEXTFIELD_SIZE = 20;
	
	public final String EXPENSE_TYPE_NEED = "Need";
	public final String EXPENSE_TYPE_WANT = "Want";
	
	// #Swing Components
	private ButtonGroup bgType;
	private JRadioButton rbtnNeed, rbtnWant;
	private JLabel lblAmt, lblName, lblType, lblCat, lblPayment, lblDate, lblDesc;
	private JTextField 	txtAmt, txtName, txtDate;
	private TextArea taDesc;
	private JComboBox cboxCategory, cboxPayment;
	
	// #Logic Components
	private RecordHandler recHandler; 
	private CategoryHandler catHandler;
	private PayMethodHandler payHandler;
	
	// #Data Components
	private List<Category> categories;
	private List<PaymentMethod> methods;
	
	/**
	 * Create a Form for expense records
	 * @param recHandlerRef RecordHandler
	 * @param catHandlerRef CategoryHandler
	 * @param payHandlerRef PaymentMethodHandler
	 */
	public PanelExpense(
			RecordHandler recHandlerRef, 
			CategoryHandler catHandlerRef, 
			PayMethodHandler payHandlerRef
		) {
		
		super();
		
		recHandler = recHandlerRef; 
		catHandler = catHandlerRef;
		payHandler = payHandlerRef;
		categories = catHandler.getAllCategories();
		methods = payHandler.getAllPaymentMethod();
		this.initFields();
		this.setBackground(Color.WHITE);
	}
	
	/** 
	 * Creates a label with the system font.
	 * @param lblTxt the text to apply to the JLabel
	 * @return the JLabel object generated
	 */
	private JLabel createLabel(String lblTxt) {
		JLabel lbl = new JLabel(lblTxt);
		lbl.setFont(new Font("Segoe UI", 0, 18)); // #Font
		return lbl;
	}
	
	/** Initializes the Categories Drop down field */
	private void initCatComboBox() {
		for(Category cat: categories) {
			this.cboxCategory.addItem(cat.getName());
		}
	}
	
	/** Initializes the Payment Methods Drop down field */
	private void initPayComboBox() {
		for(PaymentMethod method: methods) {
			this.cboxPayment.addItem(method.getName());
		}
	}
	
	/** Initiates all the fields of this panel. */
	private void initFields() {
		
		/* The Layout governing the positions */
		SpringLayout loForm = new SpringLayout();
		this.setLayout(loForm);
		
		// Initialize Radio Buttons
		lblType = this.createLabel("Type");
		
		bgType = new ButtonGroup();
		
		rbtnNeed = new JRadioButton(EXPENSE_TYPE_NEED);
		rbtnNeed.setContentAreaFilled(false);
		rbtnNeed.setSelected(true);
		
		rbtnWant = new JRadioButton(EXPENSE_TYPE_WANT);
		rbtnWant.setContentAreaFilled(false);
		
		bgType.add(rbtnNeed);
		bgType.add(rbtnWant);
		// Action Listener to update the label text in preview?
		
		// Initialize Combo Box - To be a dynamic updating list.
		lblCat = this.createLabel("Category");
		cboxCategory = new JComboBox();
		this.initCatComboBox();
		cboxCategory.setEditable(true);		
		
		// Initialize Combo Box - To be a dynamic updating list.
		lblPayment = this.createLabel("Payment Method");
		cboxPayment = new JComboBox();
		this.initPayComboBox();
		cboxPayment .setEditable(true);
		
		lblName = this.createLabel("Name");
		txtName = new JTextField("", TEXTFIELD_SIZE);
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
		
		lblAmt = this.createLabel("Amount");
		txtAmt = new JTextField("", TEXTFIELD_SIZE);
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, rbtnNeed);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, rbtnWant);
		// This will need a listener to calculate and display the information on the label

		lblDate = this.createLabel("Date");
		txtDate = new JTextField("", TEXTFIELD_SIZE);
		this.add(lblDate);
		this.add(txtDate);
		loForm.putConstraint(SpringLayout.WEST, lblDate, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDate, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDate, TOP_PAD, SpringLayout.NORTH, lblAmt);
		loForm.putConstraint(SpringLayout.NORTH, txtDate, TOP_PAD, SpringLayout.NORTH, txtAmt);
		// Insert Calendar View? Drop down box here

		lblDesc = this.createLabel("Remarks");
		taDesc = new TextArea("", 5, 25, TextArea.SCROLLBARS_NONE); /* Initial text, height, width, scroll bar option */
		this.add(lblDesc);
		this.add(taDesc);
		loForm.putConstraint(SpringLayout.WEST, lblDesc, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, taDesc, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDesc, TOP_PAD, SpringLayout.NORTH, lblDate);
		loForm.putConstraint(SpringLayout.NORTH, taDesc, TOP_PAD, SpringLayout.NORTH, txtDate);
	}
	
	/**
	 * Access method to retrieve the user entered name for this record
	 * @return the string input user entered the name
	 */
	public String getName() {return this.txtName.getText().trim();}
	
	/**
	 * Access method to retrieve the user entered amount for this record
	 * @return the string input user entered for the amount
	 */
	public String getAmt() {return this.txtAmt.getText().trim();}
	
	/**
	 * Access method to retrieve the user specified category (name)
	 * @return
	 */
	public Category getCat() {
		if(this.cboxCategory.getSelectedIndex() < 0) {
			// User defined new category
			String userInput = this.cboxCategory.getSelectedItem().toString().trim();
			return new Category(userInput);
		}
		// Else find the selected Category
		return categories.get(cboxCategory.getSelectedIndex());
	}
	
	/**
	 * Access method to retrieve the user entered date for this record
	 * @return the Date object reference for the specified date
	 */
	public Date getDate() {
		return new Date(); // This is today
		//return this.txtDate.getText();
	}
	
	/**
	 * Access method to retrieve the user specified payment methods
	 * @return
	 */
	public PaymentMethod getMode() {
		
		if(this.cboxPayment.getSelectedIndex() < 0) {
			// User defined new payment
			String userInput = this.cboxPayment.getSelectedItem().toString().trim();
			return new PaymentMethod(userInput);
		}
		// Else find the selected Payment Method
		// NOTE: MAY HAVE TO STORE A THE LIST TO RETRIEVE IN VIA INDEX.
		return methods.get(cboxPayment.getSelectedIndex());
	}
	
	public String getDesc() {
		return taDesc.getText().trim();
	}
	
	/**
	 * Retrieve the type field for the form
	 * @return ExpenseType.Need if it is a need, otherwise ExpenseType.Want 
	 */
	public ExpenseType getType() {
		return isNeed() ? ExpenseType.NEED : ExpenseType.WANT ;
	}
	
	/**
	 * Retrieve if the type field is a need
	 * @return true if user indicated record as need, otherwise false
	 */
	public boolean isNeed() {
		return this.rbtnNeed.isSelected();
	}
	
	/**
	 * to validate the fields entered into the system.
	 * @return true is there is no problem with inputs, else false;
	 */
	public boolean validateFields() {
		if(!validateAmt()) return false; 
		System.out.println(getName());
		System.out.println(getAmt());
		System.out.println(getDate());
		System.out.println(getDesc());
		System.out.println(getCat());
		System.out.println(getMode());
		System.out.println(getType());
		return true;
		// Validation method (mainly for calculation)
	}
	
	/**
	 * Method to validate the amount field
	 * @return true if no problems parsing, otherwise false
	 */
	private boolean validateAmt() {
		try {
			double result = Double.parseDouble(getAmt()); // To be updated to the inbuilt calculator
			this.setAmt(result);
			return true;
		}
		catch(Exception err) {
			return false;
		}
	}
	
	/**
	 * Method to update the amount field with the given text
	 * @param amt the amount to update the field
	 */
	private void setAmt(double amt) {
		this.txtAmt.setText(amt + "" ); // May want to decimal format this
	}
	
	/** 
	 * Save the entered field as a new expense record
	 * @return Record object containing the user input
	 */
	public Record save() {
		ExpenseRecord eRecord = new ExpenseRecord(
				Double.parseDouble(this.getAmt()), 					// the amount - double might not suffice
				this.getName(),										// the name reference of the record
				this.getDesc(),										// the description/remarks for this record, if any
				this.getDate(),										// Date of this record (in user's context, not system time)
				this.getCat(),										// Category of this record
				this.getType(), 									// The ExpenseType of the record (need/want)
				this.getMode()										// Payment method/mode of this record
			);
		this.recHandler.createRecord(eRecord);
		return eRecord;
	}
}

/** GUI Form for Income records */
@SuppressWarnings("serial")
class PanelIncome extends JPanel {
	
	// #Constants
	public final int TOP_PAD = 27;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 120;
	public final int TEXTFIELD_SIZE = 20;
	
	// #Swing Components
	private JLabel lblAmt, lblName, lblCat, lblDesc, lblDate;
	private JTextField 	txtAmt, txtName, txtDate;
	private JComboBox cboxCat;
	private TextArea taDesc;
	
	// #Logic Components
	private RecordHandler recHandler; 
	private CategoryHandler catHandler;
	
	// #Data Components
	private List<Category> categories;
	
	/**
	 * Create a form for income record
	 * @param recHandlerRef
	 * @param catHandlerRef
	 */
	public PanelIncome(RecordHandler recHandlerRef, CategoryHandler catHandlerRef) {
		
		super();
		
		recHandler = recHandlerRef;
		catHandler = catHandlerRef;
		
		categories = catHandler.getAllCategories();
		
		this.setBackground(Color.WHITE);
		this.initFields();
	}
	
	/** Initialize the categories drop down field */
	private void initCat() {
		for(Category cat: categories) {
			this.cboxCat.addItem(cat.getName());
		}
	}
	
	/** Initiate all Form fields */
	private void initFields() {
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
		cboxCat = new JComboBox();
		this.initCat();
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
	 * to validate the fields entered into the system.
	 * @return true is there is no problem with inputs, else false;
	 */
	public boolean validateFields() {
		try {
			Double.parseDouble(getAmt());
		}
		catch(Exception err) {
			return false;
		}
		return true;
	}
	
	/** Get the user entered Name */
	public String getName() {return txtName.getText().trim();}
	
	/** Get the user entered Amount */
	public String getAmt() {return txtAmt.getText().trim();}
	
	/** Get the user entered Date */
	public Date getDate() {
		return new Date();
	}
	
	/**
	 * Access method to retrieve the user specified category (name)
	 * @return the chosen Category
	 */
	public Category getCat() {
		
		if(this.cboxCat.getSelectedIndex() < 0) {
			// User defined new category
			String userInput = this.cboxCat.getSelectedItem().toString().trim();
			return new Category(userInput);
		}
		// Else find the selected Category
		// NOTE: MAY HAVE TO STORE A THE LIST TO RETRIEVE IN VIA INDEX.
		
		/*
			if category is new
			cat = new category(name)
			createCategory(cat)
			Record = new (details, cat);
		*/
		return categories.get(cboxCat.getSelectedIndex());
	}
	
	/** Get the user entered remarks */
	public String getDesc() {return taDesc.getText().trim();}
	
	/** 
	 * Save the entered field as a new record
	 * @return Record object containing the user input
	 */
	public Record save() {
		IncomeRecord iRecord = new IncomeRecord(
				Double.parseDouble(this.getAmt()), 
				this.getName(), 
				this.getDesc(), 
				this.getDate(), 
				this.getCat()
			);
		this.recHandler.createRecord(iRecord);
		return iRecord;
	}
}

/** Panel containing the options available to this frame */
@SuppressWarnings("serial")
class PanelOption extends JPanel {
	
	private JButton btnSave, btnCancel;
	
	public PanelOption(ActionListener listener) {
		super();
		
		this.setBackground(Color.WHITE);
		
		btnSave = new JButton("Save");
		btnSave.setFont(new Font("Segoe UI", 0, 18)); // #Font
		btnSave.setBorderPainted(false);
		btnSave.setFocusPainted(false);
		btnSave.setBackground(Color.LIGHT_GRAY);
		btnSave.setForeground(Color.BLACK);
		
		btnCancel = new JButton("Or discard changes");
		btnCancel.setFont(new Font("Segoe UI", 0, 18)); // #Font
		btnCancel.setBorderPainted(false);
		btnCancel.setContentAreaFilled(false);
		btnCancel.setForeground(Color.DARK_GRAY);
		
		btnSave.addActionListener(listener);
		btnCancel.addActionListener(listener);
		
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
				btn.setFont(new Font("Segoe UI", 0, 18)); // #Font
				// Current Issue: unable to remove the underlining from after hovering over it.
				
			}
		});
		
		this.add(btnSave);
		this.add(btnCancel);
	}
	
	public JButton getSaveBtn() {return this.btnSave;}
	public JButton getCancelBtn() {return this.btnCancel;}
}


/** Panel to store all the options pertaining to recurrence of a record */
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
		chkRecur.setFont(new Font("Segoe UI", 0, 18)); // #Font)
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
	public void getFrequency() {} // This would need an enum 
	
	public boolean isToRecur() { return this.chkRecur.isSelected(); }

	@Override
	public void actionPerformed(ActionEvent e) { this.toggle(); }
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