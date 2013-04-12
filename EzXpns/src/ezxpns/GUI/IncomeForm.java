package ezxpns.GUI;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import ezxpns.GUI.Calculator.EvaluationException;
import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

/** 
 * GUI Form for Income records
 */
@SuppressWarnings("serial")
public class IncomeForm extends RecordForm {
	
	private CategoryHandler<IncomeRecord> catHandler; 
	
	private IncomeRecord record;
	
	/**
	 * Create a form for a new income record
	 * @param recHandlerRef RecordHandler reference to manage records
	 * @param catHandlerRef CategoryHandler reference to manage categories
	 */
	public IncomeForm(
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> catHandlerRef,
			UpdateNotifyee notifyeeRef) {
//		cal = Calculator.getInstance();
		super();
		
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
		
		// TODO: Recurring Records
		
		blockAutoFill = false;
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
		defaultTFBorder = txtName.getBorder();
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
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD>>2, SpringLayout.NORTH, this);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD>>2, SpringLayout.NORTH, this);
		
		lblCat = this.createLabel("Category");
		cboxCat = new JComboBox();
		cboxCat.setPreferredSize(new Dimension(200, 25));
		defaultCBBorder = cboxCat.getBorder();
		this.populateCategories();
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
		
		this.add(lblAmt);
		this.add(txtAmt);
		loForm.putConstraint(SpringLayout.WEST, lblAmt, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtAmt, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblAmt, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.NORTH, txtAmt, TOP_PAD, SpringLayout.NORTH, cboxCat);
		
		/* Calculator begins here */
		final JLabel lblResult = this.createLabel("");
		this.add(lblResult);
		loForm.putConstraint(SpringLayout.WEST, lblResult, COL1_PAD, SpringLayout.EAST, txtAmt);
		loForm.putConstraint(SpringLayout.NORTH, lblResult, TOP_PAD, SpringLayout.NORTH, cboxCat);
		txtAmt.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				evaluate(lblResult);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				evaluate(lblResult);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				evaluate(lblResult);
			}			
		});
		
		txtAmt.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				evaluate(lblResult);
				txtAmt.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				evaluate(lblResult);
			}
			
		});
		
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
	            // TODO: selectedDate seems to be redundant?
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
		txtDesc = new JTextArea("");
		txtDesc.setPreferredSize(new Dimension(200, 100));
		txtDesc.setBorder(defaultTFBorder);
		txtDesc.setWrapStyleWord(true);
		txtDesc.setLineWrap(true);
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
		
		if(!validateName(errMsg)) {
			this.markErr(txtName);
			validateSuccess = false;
		}
		else {
			this.unmarkErr(txtName);
		}
		
		if(!validateAmt(errMsg)) {
			this.markErr(txtAmt);
			validateSuccess = false;
		}
		else {
			this.unmarkErr(txtAmt);
		}
		
		if(!validateDate(errMsg)) {
			validateSuccess = false;
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
		
		if(!validateSuccess) displayErr(errMsg.toString());
		return validateSuccess;
	}
	
	/** 
	 * Save the entered field as a new record
	 * @return Record object containing the user input
	 */
	public Record save() {
		IncomeRecord iRecord = null;
		try {
			iRecord = new IncomeRecord(
					evaluate(), 
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
		catch (EvaluationException e) {
			
		}
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
}