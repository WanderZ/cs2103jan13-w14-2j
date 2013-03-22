package ezxpns.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

/** GUI Form for Income records */
@SuppressWarnings("serial")
public class IncomeForm extends JPanel {
	
	// #Constants
	public final int TOP_PAD = 27;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 200;
	
	// #Swing Components
	private JLabel lblAmt, lblName, lblCat, lblDesc, lblDate;
	private JTextField 	txtAmt, txtName;
	private JFormattedTextField txtDate;
	private JComboBox cboxCat;
	private JTextField txtDesc;
	
	// #Logic Components
	private RecordHandler recHandler; 
	private CategoryHandler catHandler;
	private IncomeRecord record;
	
	// #Data Components
	private List<Category> categories;
	
	/**
	 * Create a form for a new income record
	 * @param recHandlerRef 
	 * @param catHandlerRef
	 */
	public IncomeForm(RecordHandler recHandlerRef, CategoryHandler catHandlerRef) {
		recHandler = recHandlerRef;
		catHandler = catHandlerRef;
		
		categories = catHandler.getAllCategories();
		this.initFields();
	}
	
	/**
	 * Create a form of the existing record
	 * @param recHandlerRef 
	 * @param catHandlerRef
	 * @param record IncomeRecord object to be edit
	 */
	public IncomeForm(RecordHandler recHandlerRef, CategoryHandler catHandlerRef, IncomeRecord record) {
		this(recHandlerRef, catHandlerRef);
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
		txtAmt.setText(record.getAmount() + "");
		
		// Category
		cboxCat.setSelectedIndex(categories.indexOf(record.getCategory()));
		
		// Date
		txtDate.setValue(record.getDate());
		
		// Description
		txtDesc.setText(record.getRemark());
		
		// Recurrence stuff...
		// Not handled at the moment
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
		
		lblName = this.createLabel("Name");
		txtName = new JTextField("");
		txtName.setPreferredSize(new Dimension(200, 25));
		this.add(lblName);
		this.add(txtName);
		loForm.putConstraint(SpringLayout.WEST, lblName, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtName, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD, SpringLayout.NORTH, this);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD, SpringLayout.NORTH, this);
		// AutoComplete (for the rest of the fields - when completed?)
		
		lblCat = this.createLabel("Category");
		cboxCat = new JComboBox();
		cboxCat.setPreferredSize(new Dimension(200, 25));
		this.initCat();
		cboxCat.setEditable(true);
		this.add(lblCat);
		this.add(cboxCat);
		loForm.putConstraint(SpringLayout.WEST, lblCat, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxCat, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblCat, TOP_PAD, SpringLayout.NORTH, lblName);
		loForm.putConstraint(SpringLayout.NORTH, cboxCat, TOP_PAD, SpringLayout.NORTH, txtName);
		
		lblAmt = this.createLabel("Amount");
		txtAmt = new JTextField("");
		txtAmt.setPreferredSize(new Dimension(200, 25));
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, cboxCat);
		// This will need a listener to calculate and display the information on the label
		
		lblDate = this.createLabel("Date");
		txtDate = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
		txtDate.setValue(new Date());
		txtDate.setPreferredSize(new Dimension(200, 25));
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
	 * to validate the fields entered into the system.
	 * @return true is there is no problem with inputs, else false;
	 */
	public boolean validateFields() {
		boolean validateSuccess = true;
		if(!validateAmt()) {
			System.out.println("failed amt");
			validateSuccess = false;
		}
		if(!validateName()) {
			System.out.println("failed name");
			validateSuccess = false;
		}
		if(!validateDate()) {
			System.out.println("failed date");
			validateSuccess = false;
		}
		return true;
	}
	
	/**
	 * to validate the amount field, checking type and value (>0)
	 * @return true if input is valid, else false
	 */
	private boolean validateAmt() {
		// Data type check
		double result;
		try {result = Double.parseDouble(getAmt());}
		catch(Exception err) { return false; }
		
		// Value check		
		return result >= 0.01;
	}
	
	/**
	 * validates the name field - if there is any input
	 * @return true if there is input, otherwise false
	 */
	private boolean validateName() {
		return !getName().equals("");
	}
	
	/**
	 * validates the date field - if the date entered is a valid date (non future date)
	 * @return true if it is a historical date, otherwise false
	 */
	private boolean validateDate() {
		if(!getDate().before(new Date())) return false; // #Constraint disallow users to add future records
		return true;
	}
	
	/** Get the user entered Name */
	public String getName() {return txtName.getText().trim();}
	
	/** Get the user entered Amount */
	public String getAmt() {return txtAmt.getText().trim();}
	
	/** Get the user entered Date */
	public Date getDate() {
		return (Date) txtDate.getValue();
	}
	
	/**
	 * Access method to retrieve the user specified category (name)
	 * @return the chosen Category
	 */
	public Category getCat() {
		if(isNewCategory()) {
			// User defined new category
			String userInput = this.cboxCat.getSelectedItem().toString().trim();
			return new Category(userInput);
		}
		// Else find the selected Category
		return categories.get(cboxCat.getSelectedIndex());
	}
	
	/**
	 * Check if this record is tagged to a new Category
	 * @return true if record is under a new Category, otherwise false
	 */
	private boolean isNewCategory() {
		return this.cboxCat.getSelectedIndex() < 0;
	}
	
	/** 
	 * Get the user entered remarks 
	 * @return String description or remarks entered by the user
	 */
	public String getDesc() {return txtDesc.getText().trim();}
	
	/** 
	 * Save the entered field as a new record
	 * @return Record object containing the user input
	 */
	public Record save() {
		System.out.println("Save button invoked! trying to save... ");
		IncomeRecord iRecord = new IncomeRecord(
				Double.parseDouble(this.getAmt()), 
				this.getName(), 
				this.getDesc(), 
				this.getDate(), 
				this.getCat()
			);
		this.recHandler.createRecord(iRecord, isNewCategory());
		return iRecord;
	}
	
	/** 
	 * Creates a label with the system font.
	 * @param lblTxt the text to apply to the JLabel
	 * @return the JLabel object generated
	 */
	private JLabel createLabel(String lblTxt) {
		JLabel label = new JLabel(lblTxt);
		label.setFont(new Font("Segoe UI", 0, 18)); // #Font
		return label;
	}
}