package ezxpns.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.ExpenseType;
import ezxpns.data.records.PayMethodHandler;
import ezxpns.data.records.PaymentMethod;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

/** Panel to contain and maintain the form for a new expense record */
@SuppressWarnings("serial")
public class ExpenseForm extends JPanel {
	
	// #Constants
	public final int TOP_PAD = 27;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 200;
	
	public final String EXPENSE_TYPE_NEED = "Need";
	public final String EXPENSE_TYPE_WANT = "Want";
	
	// #Swing Components
	private ButtonGroup bgType;
	private JRadioButton rbtnNeed, rbtnWant;
	private JLabel lblAmt, lblName, lblType, lblCat, lblPayment, lblDate, lblDesc;
	private JTextField 	txtAmt, txtName;
	private JFormattedTextField txtDate;
	private JTextField txtDesc;
	private JComboBox cboxCat, cboxPay;
	
	// #Logic Components
	private RecordHandler recHandler; 
	private CategoryHandler catHandler;
	private PayMethodHandler payHandler;
	
	// #Data Components
	private List<Category> categories;
	private List<PaymentMethod> methods;
	private ExpenseRecord record;
	
	/**
	 * Create a Form for new expense records
	 * @param recHandlerRef RecordHandler
	 * @param catHandlerRef CategoryHandler
	 * @param payHandlerRef PaymentMethodHandler
	 */
	public ExpenseForm(RecordHandler recHandlerRef, CategoryHandler catHandlerRef, PayMethodHandler payHandlerRef) {
		recHandler = recHandlerRef; 
		catHandler = catHandlerRef;
		payHandler = payHandlerRef;
		categories = catHandler.getAllCategories();
		methods = payHandler.getAllPaymentMethod();
		this.initFields();
	}
	
	/**
	 * Create a Form for existing expense records - for display
	 * @param record ExpenseRecord to be modified
	 * @param catHandlerRef
	 * @param payHandlerRef
	 */
	public ExpenseForm(RecordHandler recHandlerRef, CategoryHandler catHandlerRef, PayMethodHandler payHandlerRef, ExpenseRecord record) {
		this(recHandlerRef, catHandlerRef, payHandlerRef);
		this.record = record;
		this.populateFields();
	}
	
	/**
	 * To populate all the fields with the given record's data
	 */
	private void populateFields() {
		
		// Name
		txtName.setText(record.getName());
		
		// Amount
		this.setAmt(record.getAmount());
		
		// Category
		cboxCat.setSelectedIndex(categories.indexOf(record.getCategory()));
		
		// Payment Method
		cboxPay.setSelectedIndex(methods.indexOf(record.getPaymentMethod()));
		
		// Date
		txtDate.setValue(record.getDate());
		
		// Description
		txtDesc.setText(record.getRemark());
		
		// Recurrence stuff...
		// Not handled at the moment
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
	
	/** Populates the Categories Drop down field */
	private void initCatComboBox() {
		for(Category cat: categories) {
			this.cboxCat.addItem(cat.getName());
		}
	}
	
	/** Populates the Payment Methods Drop down field */
	private void initPayComboBox() {
		for(PaymentMethod method: methods) {
			this.cboxPay.addItem(method.getName());
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
		cboxCat = new JComboBox();
		this.initCatComboBox();
		cboxCat.setEditable(true);		
		
		// Initialize Combo Box - To be a dynamic updating list.
		lblPayment = this.createLabel("Payment Method");
		cboxPay = new JComboBox();
		this.initPayComboBox();
		cboxPay .setEditable(true);
		
		lblName = this.createLabel("Name");
		txtName = new JTextField("");
		txtName.setPreferredSize(new Dimension(200, 25));
		txtName.setBorder(BorderFactory.createLoweredBevelBorder());
		txtName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				txtName.selectAll();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO To update all the other fields.
				ExpenseRecord oldRecord = recHandler.lastExpenseRecord(txtName.getText()); // TODO: Validation
				if(record==null) {
					record = oldRecord;
					populateFields();
				}
			}
		});
		
		this.add(lblName);
		this.add(txtName);
		loForm.putConstraint(SpringLayout.WEST, lblName, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtName, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD, SpringLayout.NORTH, this);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD, SpringLayout.NORTH, this);
		
		this.add(lblPayment);
		this.add(cboxPay);
		loForm.putConstraint(SpringLayout.WEST, lblPayment, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxPay, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblPayment, TOP_PAD, SpringLayout.NORTH, lblName);
		loForm.putConstraint(SpringLayout.NORTH, cboxPay, TOP_PAD, SpringLayout.NORTH, txtName);
		
		this.add(lblCat);
		this.add(cboxCat);
		loForm.putConstraint(SpringLayout.WEST, lblCat, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxCat, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblCat, TOP_PAD, SpringLayout.NORTH, lblPayment);
		loForm.putConstraint(SpringLayout.NORTH, cboxCat, TOP_PAD, SpringLayout.NORTH, cboxPay);
				
		this.add(lblType);
		this.add(rbtnNeed);
		this.add(rbtnWant);
		loForm.putConstraint(SpringLayout.WEST, lblType, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblType, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.WEST, rbtnNeed, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, rbtnWant, COL2_PAD/2, SpringLayout.WEST, rbtnNeed);
		loForm.putConstraint(SpringLayout.NORTH, rbtnNeed, TOP_PAD, SpringLayout.NORTH, cboxCat);
		loForm.putConstraint(SpringLayout.NORTH, rbtnWant, TOP_PAD, SpringLayout.NORTH, cboxCat);
		
		lblAmt = this.createLabel("Amount");
		txtAmt = new JTextField("");
		txtAmt.setPreferredSize(new Dimension(200, 25));
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, rbtnNeed);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, rbtnWant);
		// This will need a listener to calculate and display the information on the label

		lblDate = this.createLabel("Date");
		txtDate = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
		txtDate.setPreferredSize(new Dimension(200, 25));
		txtDate.setValue(new Date());
		txtDate.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(lblDate);
		this.add(txtDate);
		loForm.putConstraint(SpringLayout.WEST, lblDate, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDate, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDate, TOP_PAD, SpringLayout.NORTH, lblAmt);
		loForm.putConstraint(SpringLayout.NORTH, txtDate, TOP_PAD, SpringLayout.NORTH, txtAmt);
		// Insert Calendar View? Drop down box here

		lblDesc = this.createLabel("Remarks");
		txtDesc = new JTextField("");
		txtDesc.setPreferredSize(new Dimension(200, 25));
		this.add(lblDesc);
		this.add(txtDesc);
		loForm.putConstraint(SpringLayout.WEST, lblDesc, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDesc, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDesc, TOP_PAD, SpringLayout.NORTH, lblDate);
		loForm.putConstraint(SpringLayout.NORTH, txtDesc, TOP_PAD, SpringLayout.NORTH, txtDate);
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
	 * @return the Category chosen by the user
	 */
	public Category getCat() {
		if(isNewCategory()) {
			// User defined new category
			String userInput = this.cboxCat.getSelectedItem().toString().trim();
			return new Category(userInput); // To be refactored in the future implementation
		}
		// Else find the selected Category
		return categories.get(cboxCat.getSelectedIndex());
	}
	
	/**
	 * Check if this record is tagged to a new Category
	 * @return true if it is a tagged to a new Category, otherwise false
	 */
	private boolean isNewCategory() {
		return this.cboxCat.getSelectedIndex() < 0;
	}
	
	/**
	 * Access method to retrieve the user entered date for this record
	 * @return the Date object reference for the specified date
	 */
	public Date getDate() {
		return (Date) txtDate.getValue();
	}
	
	/**
	 * Access method to retrieve the user specified payment methods
	 * @return the PaymentMethod chosen by the user
	 */
	public PaymentMethod getMode() {
		if(isNewMethod()) {
			// User defined new payment
			String userInput = this.cboxPay.getSelectedItem().toString().trim();
			return new PaymentMethod(userInput);
		}
		// Else find the selected Payment Method
		// NOTE: MAY HAVE TO STORE A THE LIST TO RETRIEVE IN VIA INDEX.
		return methods.get(cboxPay.getSelectedIndex());
	}
	
	/**
	 * Check if this record is via a new payment method
	 * @return true if it is new, otherwise false;
	 */
	private boolean isNewMethod() {
		return this.cboxPay.getSelectedIndex() < 0;
	}
	
	/**
	 * Retrieves the user entered description
	 * @return a String containing the description/remarks
	 */
	public String getDesc() {return txtDesc.getText().trim();}
	
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
		boolean validateSuccess = true;
		if(!validateAmt()) {
			this.markErr(txtAmt);
			validateSuccess = false;
		}
		
		if(!validateName()) {
			this.markErr(txtName);
			validateSuccess = false;
		}
		
		if(!validateDate()) {
			this.markErr(txtDate);
			validateSuccess = false;
		}
		
		System.out.println("Name: " + getName());
		System.out.println("Amt: " + getAmt());
		System.out.println("Date: " + getDate());
		System.out.println("Desc: " + getDesc());
		System.out.println("Cat: " + getCat());
		System.out.println("Mode: " + getMode());
		System.out.println("Type: " + getType());
		
		return validateSuccess;
		
		// Validation method (mainly for calculation)
	}
	
	/**
	 * Method to validate the date field
	 * @return true if the date is before today's date, otherwise false
	 */
	private boolean validateDate() {
		//TODO: validate the date
		return true;
	}
	
	/**
	 * Method to validate the amount field
	 * @return true if no problems parsing, otherwise false
	 */
	private boolean validateAmt() {
		double result;
		try {
			result = Double.parseDouble(getAmt()); // To be updated to the inbuilt calculator
			
			this.setAmt(result);
			return result >= 0.01;
		}
		catch(Exception err) {
			return false;
		}
	}
	
	/**
	 * Method to validate the name field - ensure its not empty
	 * @return true if name is not empty, otherwise false
	 */
	private boolean validateName() {
		return !getName().equals("");
	}
	
	/**
	 * Method to mark fields with a red border to indicate to user that it has error
	 * @param txtField JTextField to be marked for error
	 */
	private void markErr(JTextField txtField) {
		txtField.setBorder(BorderFactory.createLineBorder(Color.RED));
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
		this.recHandler.createRecord(eRecord, isNewCategory(), isNewMethod());
		return eRecord;
	}
}