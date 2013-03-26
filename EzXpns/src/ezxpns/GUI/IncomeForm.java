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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

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
//	private JFormattedTextField txtDate;
	private JDateChooser txtDateChooser;
	private JComboBox cboxCat;
	private JTextField txtDesc;
	
	// #Logic Components
	private RecordHandler recHandler; 
	private CategoryHandler<IncomeRecord> catHandler;
	
	/**
	 * The Existing record, if available
	 */
	private IncomeRecord record;
	private UpdateNotifyee notifyee;
	private boolean isEdit;
	
	// #Data Components
	private List<Category> categories;
	
	/**
	 * Create a form for a new income record
	 * @param recHandlerRef 
	 * @param catHandlerRef
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
	 * @param recHandlerRef 
	 * @param catHandlerRef
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
		System.out.println("Entered IncomeForm populateFields()");
		// Name
		txtName.setText(record.getName());
		
		// Amount
		this.setAmt(record.getAmount());
		
		// Category
		cboxCat.setSelectedIndex(categories.indexOf(record.getCategory()));
		
		// Date
		txtDateChooser.setDate(isEdit ? record.getDate() : new Date());
		System.out.println("incomeform - isEdit? " + isEdit);
		
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
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				txtName.selectAll(); 
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// Auto Complete - one use only.
				IncomeRecord oldRecord = recHandler.lastIncomeRecord(txtName.getText()); 
				if(record==null & oldRecord!=null) {
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
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, cboxCat);
		// TODO: Calculator
		
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
		txtDesc = new JTextField("");
		txtDesc.setPreferredSize(new Dimension(200, 25));
		this.add(lblDesc);
		this.add(txtDesc);
		loForm.putConstraint(SpringLayout.WEST, lblDesc, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDesc, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDesc, TOP_PAD, SpringLayout.NORTH, lblDate);
		loForm.putConstraint(SpringLayout.NORTH, txtDesc, TOP_PAD, SpringLayout.NORTH, txtDateChooser);
	}
	
	/**
	 * to validate the fields entered into the system.
	 * @return true is there is no problem with inputs, else false;
	 */
	public boolean validateFields() {
		boolean validateSuccess = true;
		
		if(!validateAmt()) {
			System.out.println("failed amt");
			this.markErr(txtAmt);
			validateSuccess = false;
		}
		
		if(!validateName()) {
			System.out.println("failed name");
			this.markErr(txtName);
			validateSuccess = false;
		}
		
		if(!validateDate()) {
			this.markErr(txtDateChooser);
			System.out.println("failed date");
			validateSuccess = false;
		}
		
		// TODO: Validate Income Category
		
		// TODO: Validate Description?
		
		return validateSuccess;
	}
	
	/**
	 * to validate the amount field, checking type and value (>0)
	 * @return true if input is valid, else false
	 */
	private boolean validateAmt() {
		// Data type check
		double result;
		try {
			result = Double.parseDouble(getAmt());
		}
		catch(Exception err) { 
			return false; 
		}
		
		// Value check		
		return result >= 0.01; // Minimum value
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
		if(getDate().after(new Date())) return false; // #Constraint disallow users to add future records
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
	 * Method to mark fields with a red border to indicate to user that it has error
	 * @param myTxtDateChooser JDateChooser to be marked for error
	 */
	private void markErr(JDateChooser myTxtDateChooser) {
		myTxtDateChooser.setBorder(BorderFactory.createLineBorder(Color.RED));
	}
	
	/**
	 * Method to mark fields with a red border to indicate to user that it has error
	 * @param myTxtDateChooser JTextField to be marked for error
	 */
	private void markErr(JTextField txtAmt) {
		txtAmt.setBorder(BorderFactory.createLineBorder(Color.RED));
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
			System.out.println(
					"Modify: " +
					this.recHandler.modifyRecord(record.getId(), iRecord, isNewCategory())
			);
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
		label.setFont(new Font("Segoe UI", 0, 18)); // #Font
		return label;
	}
}