package ezxpns.GUI;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.toedter.calendar.JDateChooser;

import ezxpns.GUI.Calculator.EvaluationException;
import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.PaymentHandler;
import ezxpns.data.records.PaymentMethod;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

@SuppressWarnings("serial")
public abstract class RecordForm extends JPanel {

	// #Constants
	public final int TOP_PAD = 30;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 150;
	
	// #Swing Components
	protected JLabel lblAmt, lblName, lblType, lblCat, lblDate, lblDesc;
	protected JTextField 	txtAmt, txtName;
	protected JComboBox cboxCat;
	
	/**
	 * The JDateChooser reference to the date field
	 */
	protected JDateChooser txtDateChooser;
	
	/**
	 * The JTextArea reference to the description field
	 */
	protected JTextArea txtDesc;
		
	/**
	 * The default border for the text field
	 */
	protected Border defaultTFBorder;
	
	/**
	 * The default border for the combo box field
	 */
	protected Border defaultCBBorder;
	
	// #Logic Components
	protected RecordHandler recHandler; 
	protected CategoryHandler catHandler;
	protected PaymentHandler payHandler;
	protected UpdateNotifyee notifyee;
	
	/**
	 * The Calculator reference for the calculator object
	 */
	protected final Calculator cal;
	
	/**
	 * Edit flag - true for editing existng record, otherwise false
	 */
	protected boolean isEdit;
	protected boolean blockAutoFill = false;
	
	/**
	 * The List containing the Categories displayed
	 */
	protected List<Category> categories;
	
	/**
	 * The Record reference to the provided record
	 */
	protected Record record;
	
	protected RecordForm() {
		cal = Calculator.getInstance();
	}
	
	/** 
	 * Creates a label with the system font.
	 * @param lblTxt the text to apply to the JLabel
	 * @return the JLabel object generated
	 */
	protected JLabel createLabel(String lblTxt) {
		JLabel lbl = new JLabel(lblTxt);
		lbl.setFont(Config.TEXT_FONT); // #Font
		return lbl;
	}
	
	/** 
	 * Populates the Categories Drop down field 
	 */
	protected void populateCategories() {
		for(Category cat: categories) {
			this.cboxCat.addItem(cat.getName());
		}
	}

	/**
	 * Retrieves the user entered name for this record
	 * @return the string input user entered the name
	 */
	public String getName() {return this.txtName.getText().trim();}
	
	/**
	 * Retrieves the user entered amount for this record
	 * @return the string input user entered for the amount
	 */
	public String getAmt() {return this.txtAmt.getText().trim();}
	
	/**
	 * Retrieves the user specified category (name)
	 * @return the Category chosen by the user
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
	 * Checks if this record is tagged to a new Category
	 * @return true if it is, otherwise false
	 */
	protected boolean isNewCategory() {
		return this.cboxCat.getSelectedIndex() < 0;
	}
	
	/**
	 * Retrieve the user entered date for this record
	 * @return the Date object reference for the specified date
	 */
	public Date getDate() {
		return (Date) txtDateChooser.getDate();
	}
	
	/**
	 * Retrieve the user specified payment methods
	 * @return the PaymentMethod chosen by the user
	 */
	public PaymentMethod getMode() {
		return PaymentMethod.undefined;
//		if(isNewMethod()) {
//			// User defined new payment
//			String userInput = this.cboxPay.getSelectedItem().toString().trim();
//			return new PaymentMethod(userInput);
//		}
//		return methods.get(cboxPay.getSelectedIndex());
	}
	
	/**
	 * Check if this record is via a new payment method
	 * @return true if it is new, otherwise false;
	 */
	protected boolean isNewMethod() {
//		return this.cboxPay.getSelectedIndex() < 0;
		return false;
	}
	
	/**
	 * Retrieves the user entered description
	 * @return a String containing the description/remarks
	 */
	public String getDesc() {return txtDesc.getText().trim();}
	
	/**
	 * Method to update the amount field with the given text
	 * @param amt the amount to update the field
	 */
	protected void setAmt(double amt) {
		this.txtAmt.setText(new DecimalFormat("##0.00").format(amt));
	}
	
	/**
	 * Sets the calculated amount next to the amount field
	 * @param lblResult the label to display calculated amount
	 * @param amt the calculated amount to be displayed
	 */
	protected void setAmt(JLabel lblResult, double amt) {
		lblResult.setText("=" + Config.MONEY_FORMAT.format(amt));
	}
	
	public abstract Record save();
	
	/**
	 * Evaluates the amount field
	 * @param label JLabel to populate result
	 */
	protected void evaluate(JLabel label) {
		try {
			if(txtAmt.getText().trim().equals("")) {
				label.setText("<< try using + - * /");
				return;
			}
			Double result = evaluate();
			if(result > Config.DEFAULT_MAX_AMT_PER_RECORD) {
				// Expression is too big!
				label.setText("<< Value is too huge!");
				return;
			}
			
			if(result < Config.DEFAULT_MIN_AMT_PER_RECORD) {
				label.setText("<< Value is too small");
				return;
			}
			if(result!=null) setAmt(label, result);
		}
		catch(EvaluationException evalErr) {
			System.out.println(evalErr.getMessage());
			label.setText("<< Invalid");
		}
		catch(Exception err) {
			System.out.println(err.getMessage());
			label.setText("<< Invalid");
		}
	}
	
	/**
	 * Evaluates the given expression
	 * @return the result of the
	 * @throws EvaluationException
	 */
	protected double evaluate() throws EvaluationException {
		return cal.evaluate(getAmt());
	}
	
	/**
	 * Validates the description field
	 * @param errMsg StringBuilder Object to store error message, if any
	 * @return true if validation is successful, otherwise false
	 */
	protected boolean validateDescription(StringBuilder errMsg) {
		if(txtDesc.getText().trim().equals("")) {
			return true; // Empty description
		}
		
		if(this.getDesc().length() >= Config.DEFAULT_MAX_LENGTH_DESC) {
			errMsg.append("Description is too long!\n");
			return false;
		}
		
//		if(Config.isAlphaNumeric(txtDesc.getText().trim())) {
//			errMsg.append("Description contains invalid characters \n");
//			return false;
//		}
		return true;
	}

	/**
	 * Validates the Category field
	 * @param errMsg StringBuilder object to store the error message, if any
	 * @return true is validation is successful, otherwise false
	 */
	protected boolean validateCategory(StringBuilder errMsg) {
		if(this.isNewCategory()) {
			if(cboxCat.getSelectedItem() == null) {
				errMsg.append("Please choose a category\n");
				return false;
			}
			String err = catHandler.validateCategoryName(cboxCat.getSelectedItem().toString().trim());
			if(err!=null) { // null is error free
				errMsg.append(err);
				errMsg.append("\n");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * validates the date field - if the date entered is a valid date (non future date)
	 * @return true if it is a historical date, otherwise false
	 */
	protected boolean validateDate(StringBuilder errMsg) {
		if(getDate().after(new Date())) {
			// #Constraint disallow users to add future records
			errMsg.append("Future records are not supported\n");
			return false;
		}
		return true;
	}
	
	/**
	 * Validates the amount field
	 * @return true if no problems parsing, otherwise false
	 */
	protected boolean validateAmt(StringBuilder errMsg) {
		double result;
		try {
			result = evaluate();
			if(result > Config.DEFAULT_MAX_AMT_PER_RECORD) {
				// Thats some big ticket item
				errMsg.append("That amount is too big\n");
				return false;
			}
			if(result < Config.DEFAULT_MIN_AMT_PER_RECORD) { // Minimum value
				errMsg.append("That amount is too small\n");
				return false;
			}
			return true;
		}
		catch(Exception err) {
			errMsg.append("Invalid amount\n");
			return false;
		}
	}
	
	/**
	 * Validates the name field - if there is any input
	 * @return true if there is input, otherwise false
	 */
	protected boolean validateName(StringBuilder errMsg) {
		if(getName().equals("")) {
			errMsg.append("Please enter a name for this record\n");
			return false;
		}
		if(getName().length() > Config.DEFAULT_MAX_LENGTH_NAME) {
			errMsg.append("Name is too long!\n");
			return false;
		}
		if(Config.isAlphaNumeric(getName())) {
			errMsg.append("Name field contains non alphanumeric characters\n");
			return false;
		}	
		return true;
	}
	
	/**
	 * Displays an error dialog
	 * @param msg Message to be displayed
	 */
	protected void displayErr(String msg) {
		UINotify.createErrMsg(this, msg);
	}
	
	/**
	 * Marks fields with a red border to indicate to user that it has error
	 * @param component JTextField to be marked for error
	 */
	protected void markErr(JComponent component) {
		component.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
	}
	
	/**
	 * Unmarks fields without error with their default border
	 * @param component
	 */
	protected void unmarkErr(JComponent component) {
		component.setBorder(component instanceof JTextField ? defaultTFBorder : defaultCBBorder);
	}
}
