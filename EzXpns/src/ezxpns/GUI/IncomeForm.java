package ezxpns.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

/** 
 * GUI Form for Income records
 */
@SuppressWarnings("serial")
public class IncomeForm extends JPanel {
	
	// #Constants
	public final int TOP_PAD = 30;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 150;
	
	// #Swing Components
	private JLabel lblAmt, lblName, lblCat, lblDesc, lblDate;
	private JTextField 	txtAmt, txtName;
//	private JFormattedTextField txtDate;
	private JDateChooser txtDateChooser;
	private JComboBox cboxCat;
	private JTextArea txtDesc;
	
	// #Logic Components
	private RecordHandler recHandler; 
	private CategoryHandler<IncomeRecord> catHandler;
	
	/**
	 * The Existing record, if available
	 */
	private IncomeRecord record;
	private UpdateNotifyee notifyee;
	private boolean isEdit;
	private boolean blockAutoFill = false;
	
	// #Data Components
	private List<Category> categories;
	
	/**
	 * Create a form for a new income record
	 * @param recHandlerRef RecordHandler reference to manage records
	 * @param catHandlerRef CategoryHandler reference to manage categories
	 */
	public IncomeForm(
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> catHandlerRef,
			UpdateNotifyee notifyeeRef) {
		
		recHandler = recHandlerRef;
		catHandler = catHandlerRef;
		notifyee = notifyeeRef;
		
		categories = catHandler.getAllCategories();
		this.initFields();
		isEdit = false;
	}
	
	/**
	 * Create a form of the existing record
	 * @param recHandlerRef RecordHandler reference to manage records
	 * @param catHandlerRef CategoryHandler reference to manage categories
	 * @param record IncomeRecord object to be edit
	 */
	public IncomeForm(
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> catHandlerRef,
			UpdateNotifyee notifyeeRef,
			IncomeRecord record) {
		
		this(recHandlerRef, catHandlerRef, notifyeeRef);
		this.record = record;
		isEdit = true;
		this.populateFields();
	}
	
	/**
	 * To populate all the fields with the given record's data
	 */
	private void populateFields() {
		blockAutoFill = true;
		// Name
		if(isEdit && !txtName.getText().equals(record.getName())){
			txtName.setText(record.getName());
		}
		
		// Amount
		this.setAmt(record.getAmount());
		
		// Category
		cboxCat.setSelectedIndex(categories.indexOf(record.getCategory()));
		
		// Date
		txtDateChooser.setDate(isEdit ? record.getDate() : new Date());
		
		// Description
		txtDesc.setText(record.getRemark());
		
		// Recurrence stuff...
		// Not handled at the moment
		
		blockAutoFill = false;
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
		txtName.setToolTipText("Short name to name this record");
		txtName.setPreferredSize(new Dimension(200, 25));
		txtName.setBorder(BorderFactory.createEmptyBorder());
		txtName.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				fill();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				fill();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				fill();
			}
			
			private void fill(){
				if(blockAutoFill || isEdit)return;
				IncomeRecord oldRecord = recHandler.lastIncomeRecord(txtName.getText()); 
				if(oldRecord!=null) {
					record = oldRecord;
					populateFields();
				}
			}
			
		});
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				txtName.selectAll(); 
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Validate name field
			}
			
		});
		this.add(lblName);
		this.add(txtName);
		loForm.putConstraint(SpringLayout.WEST, lblName, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtName, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD, SpringLayout.NORTH, this);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD, SpringLayout.NORTH, this);
		
		lblCat = this.createLabel("Category");
		cboxCat = new JComboBox();
		cboxCat.setPreferredSize(new Dimension(200, 25));
		cboxCat.setBorder(BorderFactory.createEmptyBorder());
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
		txtAmt.setToolTipText("Enter Amount Here!");
		txtAmt.setBorder(BorderFactory.createEmptyBorder());
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, cboxCat);
		
		lblDate = this.createLabel("Date");
		// JDateChooser stuff starts here (tingzhe)
		txtDateChooser = new JDateChooser(new Date());
		txtDateChooser.getJCalendar().setTodayButtonVisible(true);
		txtDateChooser.setDateFormatString("dd/MM/yyyy");
		txtDateChooser.setMaxSelectableDate(new Date());
		PropertyChangeListener calendarChangeListener  = new PropertyChangeListener() {
	        @Override
	        public void propertyChange(PropertyChangeEvent evt) {
	            Date selectedDate = ((JCalendar)evt.getSource()).getDate();
	            //TODO: selectedDate seems to be redundant?
	        }
	    };
	    txtDateChooser.getJCalendar().addPropertyChangeListener("calendar",calendarChangeListener);
		txtDateChooser.setPreferredSize(new Dimension(200, 25));
		txtDateChooser.setBorder(BorderFactory.createEmptyBorder());
	    // jDateChooser stuff ends here (tingzhe)
		//txtDate = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
		//txtDate.setMargin(new Insets(0, 10, 0, 10));
		//txtDate.setValue(new Date());
		this.add(lblDate);
		this.add(txtDateChooser);
		loForm.putConstraint(SpringLayout.WEST, lblDate, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDateChooser, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDate, TOP_PAD, SpringLayout.NORTH, lblAmt);
		loForm.putConstraint(SpringLayout.NORTH, txtDateChooser, TOP_PAD, SpringLayout.NORTH, txtAmt);
		
		lblDesc = this.createLabel("Remarks");
		txtDesc = new JTextArea("");
		txtDesc.setPreferredSize(new Dimension(200, 100));
		txtDesc.setBorder(BorderFactory.createEmptyBorder());
		this.add(lblDesc);
		this.add(txtDesc);
		loForm.putConstraint(SpringLayout.WEST, lblDesc, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDesc, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDesc, TOP_PAD, SpringLayout.NORTH, lblDate);
		loForm.putConstraint(SpringLayout.NORTH, txtDesc, TOP_PAD, SpringLayout.NORTH, txtDateChooser);
		
		manageFocus();
	}
	
	private void manageFocus() {
		// Request focus in txtName
		txtName.requestFocusInWindow();
		// TODO: Bind Enter to change focus to the next field
		// TODO: Validate on "Enter"
	}
	
	/**
	 * to validate the fields entered into the system.
	 * @return true is there is no problem with inputs, else false;
	 */
	public boolean validateFields() {
		boolean validateSuccess = true;
		StringBuilder errMsg = new StringBuilder();
		
		if(!validateAmt(errMsg)) {
			// TODO: Error message for failed amount
			this.markErr(txtAmt);
			validateSuccess = false;
		}
		else {
			this.unmarkErr(txtAmt);
		}
		
		if(!validateName(errMsg)) {
			// TODO: Error message for failed name
			this.markErr(txtName);
			validateSuccess = false;
		}
		else {
			this.unmarkErr(txtName);
		}
		
		if(!validateDate(errMsg)) {
			// TODO: Error message for failed date
			this.markErr(txtDateChooser);
			validateSuccess = false;
		}
		else {
			this.unmarkErr(txtDateChooser);
		}
		
		if(!validateCategory(errMsg)) {
			this.markErr(cboxCat);
			validateSuccess = false;
		}
		else {
			this.unmarkErr(cboxCat);
		}
	
		if(!validateDescription(errMsg)) {
			this.markErr(txtDesc);
			validateSuccess = false;
		}
		else {
			this.unmarkErr(txtDesc);
		}
		
		UINotify.createErrMsg(this, errMsg.toString());
		return validateSuccess;
	}
	
	/**
	 * Validates the description field
	 * @param errMsg StringBuilder Object to store error message, if any
	 * @return true if validation is successful, otherwise false
	 */
	private boolean validateDescription(StringBuilder errMsg) {
		if(txtDesc.getText().trim().equals("")) {
			// Empty
			return true;
		}
		
		if(Config.isAlphaNumeric(txtDesc.getText().trim())) {
			errMsg.append("Description contains invalid characters \n");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Validates the Category field
	 * @param errMsg StringBuilder object to store the error message, if any
	 * @return true is validation is successful, otherwise false
	 */
	private boolean validateCategory(StringBuilder errMsg) {
		if(this.isNewCategory()) {
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
	 * Validates the amount field, checking type and value (>0)
	 * @return true if input is valid, else false
	 */
	private boolean validateAmt(StringBuilder errMsg) {
		// Data type check
		double result;
		try {
			result = Double.parseDouble(getAmt());
		}
		catch(Exception err) { 
			return false; 
		}
		
		// Value check
		return result >= Config.DEFAULT_MIN_AMT_PER_RECORD && result < Config.DEFAULT_MAX_AMT_PER_RECORD; // Minimum value
	}
	
	/**
	 * validates the name field - if there is any input
	 * @return true if there is input, otherwise false
	 */
	private boolean validateName(StringBuilder errMsg) {
		if(getName().equals("")) {
			errMsg.append("Please enter a name for this record\n");
			return false;
		}
		if(Config.isAlphaNumeric(getName())) {
			errMsg.append("Name field contains non alphanumeric characters\n");
			return false;
		}	
		return true;
	}
	
	/**
	 * validates the date field - if the date entered is a valid date (non future date)
	 * @return true if it is a historical date, otherwise false
	 */
	private boolean validateDate(StringBuilder errMsg) {
		if(getDate().after(new Date())) {
			// #Constraint disallow users to add future records
			errMsg.append("Future records are not supported\n");
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
		return (Date) txtDateChooser.getDate();
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
	 * Method to update the amount field with the given text
	 * @param amt the amount to update the field
	 */
	private void setAmt(double amt) {
		this.txtAmt.setText(new DecimalFormat("##0.00").format(amt));
	}
	
	/**
	 * Mark a JComponent (a form field) with a red border to indicate error
	 * @param component JComponent to mark
	 */
	private void markErr(JComponent component) {
		component.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
	}
	
	/**
	 * Unmark a JComponent (a form field) if it was previously marked
	 * @param component JComponent to unmark
	 */
	private void unmarkErr(JComponent component) {
		component.setBorder(BorderFactory.createEmptyBorder());
	}
	
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
		if(isEdit) {
			this.recHandler.modifyRecord(record.getId(), iRecord, isNewCategory());
		}
		else {
			iRecord = this.recHandler.createRecord(iRecord, isNewCategory());
		}
		notifyee.addUndoAction(createUndoAction(iRecord, isNewCategory()), isEdit ? "Edit Income" : "New Income");
		return iRecord;
	}
	
	/**
	 * Create an action to undo what the user just did
	 * @param nRecord new IncomeRecord that user just created
	 * @param isNewCat true for creation of new category, otherwise false
	 * @return AbstractAction undo action
	 */
	private AbstractAction createUndoAction(final IncomeRecord nRecord, final boolean isNewCat) {
		return new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if(isEdit) {
					recHandler.modifyRecord(record.getId(), record, false); // Undo an edit.
				}
				else { // A new record
					recHandler.removeRecord(nRecord.getId()); // Remove the Record	
				}
				if(isNewCat) { 
					catHandler.removeCategory(nRecord.getCategory().getID());
				}
			}
		};
	}
	
	/** 
	 * Creates a label with the system font.
	 * @param lblTxt the text to apply to the JLabel
	 * @return the JLabel object generated
	 */
	private JLabel createLabel(String lblTxt) {
		JLabel label = new JLabel(lblTxt);
		label.setFont(Config.TEXT_FONT); // #Font
		return label;
	}
}