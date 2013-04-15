package ezxpns.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import ezxpns.data.records.*;

/** 
 * Panel to contain and maintain the form for a new expense record
 * @author A0097973
 *  
 * @author Yan Ting Zhe
 * @author Yao Yujian
 */
@SuppressWarnings("serial")
public class ExpenseForm extends RecordForm {
		
	// #Swing Components
	private ButtonGroup bgType;
	private JRadioButton rbtnNeed, rbtnWant;

	/**
	 * The Record reference to the provided record
	 */
	private ExpenseRecord record;
	
	/**
	 * Create a Form for new expense records
	 * @param recHandlerRef RecordHandler reference to manage ExpenseRecords
	 * @param catHandlerRef CategoryHandler reference to manage Expense Categories
	 * @param payHandlerRef PaymentMethodHandler reference to manage payment methods
	 * @param undoMgrRef UndoManager reference to manage possible undo actions
	 */
	public ExpenseForm(
			RecordHandler recHandlerRef, 
			CategoryHandler<ExpenseRecord> catHandlerRef, 
			UpdateNotifyee notifyeeRef) {
//		cal = Calculator.getInstance();
		super();
		recHandler = recHandlerRef; 
		catHandler = catHandlerRef;
		notifyee = notifyeeRef;
		
		categories = catHandler.getAllCategories();
//		methods = payHandler.getAllPaymentMethod();
		this.initFields();
		isEdit = false;
	}
	
	/**
	 * Create a Form for existing expense records
	 * @param recHandlerRef RecordHandler reference to manage ExpenseRecords
	 * @param catHandlerRef CategoryHandler reference to manage Expense Categories
	 * @param payHandlerRef PaymentMethodHandler reference to manage payment methods
	 * @param undoMgrRef UndoManager reference to manage possible undo actions
	 * @param record ExpenseRecord reference - existing record indicated by user to be modified
	 */
	public ExpenseForm(
			RecordHandler recHandlerRef, 
			CategoryHandler<ExpenseRecord> catHandlerRef, 
			UpdateNotifyee notifyeeRef,
			ExpenseRecord record) {
		
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
		if(isEdit){
			if(!txtName.getText().equals(record.getName())) txtName.setText(record.getName());
		}
		
		// Amount
		this.setAmt(record.getAmount());
		
		// Category
		cboxCat.setSelectedIndex(categories.indexOf(record.getCategory()));
		
		// Payment Method
//		cboxPay.setSelectedIndex(methods.indexOf(record.getPaymentMethod()));
		
		// Date - populated only if editing
		txtDateChooser.setDate(isEdit ? record.getDate() : new Date()); 
		
		// Need or Want
		selectExpenseType(record.getExpenseType());
		
		// Description
		txtDesc.setText(record.getRemark());
		
		// Recurrence stuff...
		// Not handled at the moment
		
		blockAutoFill = false;
	}
	
	/** 
	 * Selects the ExpenseType 
	 * @param type the ExpenseType enum of the record
	 */
	private void selectExpenseType(ExpenseType type) {
		if(type == ExpenseType.NEED) {
			rbtnNeed.setSelected(true);
		}
		if(type == ExpenseType.WANT) {
			rbtnWant.setSelected(true);
		}
	}
	
//	/** Populates the Payment Methods Drop down field */
//	private void initPayComboBox() {
//		for(PaymentMethod method: methods) {
//			this.cboxPay.addItem(method.getName());
//		}
//	}
	
	/** 
	 * Initiates all the fields of this panel. 
	 */
	private void initFields() {
		
		/* The Layout governing the positions */
		SpringLayout loForm = new SpringLayout();
		this.setLayout(loForm);
		
		// Initialize Radio Buttons
		lblType = this.createLabel("Type");
		bgType = new ButtonGroup();
		
		rbtnNeed = new JRadioButton(ExpenseType.NEED.name);
		rbtnNeed.setContentAreaFilled(false);
		rbtnNeed.setSelected(true);
		
		rbtnWant = new JRadioButton(ExpenseType.WANT.name);
		rbtnWant.setContentAreaFilled(false);
		
		bgType.add(rbtnNeed);
		bgType.add(rbtnWant);
		
		// Initialize Combo Box - To be a dynamic updating list.
		lblCat = this.createLabel("Category");
		cboxCat = new JComboBox();
		this.populateCategories();
		cboxCat.setEditable(true);
		cboxCat.setPreferredSize(new Dimension(200, 25));
		defaultCBBorder = cboxCat.getBorder();
		
		// Initialize Combo Box - To be a dynamic updating list.
//		lblPayment = this.createLabel("Payment Method");
//		cboxPay = new JComboBox();
//		this.initPayComboBox();
//		cboxPay .setEditable(true);
//		cboxPay.setPreferredSize(new Dimension(200, 25));
		
		lblName = this.createLabel("Name");
		txtName = new JTextField("");
		txtName.setPreferredSize(new Dimension(200, 25));
		defaultTFBorder = txtName.getBorder();
		txtName.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				fill();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				fill();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				fill();
			}
			
			private void fill(){
				if(blockAutoFill || isEdit) return; 
				ExpenseRecord oldRecord = recHandler.lastExpenseRecord(txtName.getText());
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
			public void focusLost(FocusEvent arg0) {
				// TODO: Validate name
			}
		});
		
		this.add(lblName);
		this.add(txtName);
		loForm.putConstraint(SpringLayout.WEST, lblName, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtName, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD>>2, SpringLayout.NORTH, this);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD>>2, SpringLayout.NORTH, this);
		
//		this.add(lblPayment);
//		this.add(cboxPay);
//		loForm.putConstraint(SpringLayout.WEST, lblPayment, COL1_PAD, SpringLayout.WEST, this);
//		loForm.putConstraint(SpringLayout.WEST, cboxPay, COL2_PAD, SpringLayout.WEST, this);
//		loForm.putConstraint(SpringLayout.NORTH, lblPayment, TOP_PAD, SpringLayout.NORTH, lblName);
//		loForm.putConstraint(SpringLayout.NORTH, cboxPay, TOP_PAD, SpringLayout.NORTH, txtName);
		
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
		
		/* Calculator is inbuilt here */
		final JLabel lblResult = this.createLabel("");
		this.add(lblResult);
		loForm.putConstraint(SpringLayout.WEST, lblResult, COL1_PAD, SpringLayout.EAST, txtAmt);
		loForm.putConstraint(SpringLayout.NORTH, lblResult, TOP_PAD, SpringLayout.NORTH, rbtnWant);
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
	        }
	    };
		txtDateChooser.setPreferredSize(new Dimension(200, 25));
		txtDateChooser.setBorder(BorderFactory.createEmptyBorder());
	    // JDateChooser stuff ends here (tingzhe)
		this.add(lblDate);
		this.add(txtDateChooser);
		loForm.putConstraint(SpringLayout.WEST, lblDate, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDateChooser, COL2_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblDate, TOP_PAD, SpringLayout.NORTH, lblAmt);
		loForm.putConstraint(SpringLayout.NORTH, txtDateChooser, TOP_PAD, SpringLayout.NORTH, txtAmt);

		lblDesc = this.createLabel("Remarks");
		txtDesc = new JTextArea();
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
				
		// Request focus in txtName
		txtName.requestFocusInWindow();
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
			return new Category(userInput); 
		}
		// Else find the selected Category
		return categories.get(cboxCat.getSelectedIndex());
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
		
		if(!validateSuccess) displayErr(errMsg.toString()); // Failed
		return validateSuccess;
	}
	
	@Override
	public Record save() {
		try {
		ExpenseRecord eRecord = new ExpenseRecord(
				evaluate(), 					// the amount - double might not suffice
				this.getName(),										// the name reference of the record
				this.getDesc(),										// the description/remarks for this record, if any
				this.getDate(),										// Date of this record (in user's context, not system time)
				this.getCat(),										// Category of this record
				this.getType() 									// The ExpenseType of the record (need/want)
			);
			if(isEdit) {
				this.recHandler.modifyRecord(record.getId(), eRecord, isNewCategory(), isNewMethod());
			}
			else {
				eRecord = this.recHandler.createRecord(eRecord, isNewCategory());
			}
			notifyee.addUndoAction(createUndoAction(eRecord, isNewCategory(), isNewMethod()), isEdit ? "Edit Expense" : "New Expense");
			return eRecord;
		}
		catch(Exception e) {
			// Something went wrong
			System.out.println("error in creating record");
		}
		return null;
	}
	
	/**
	 * create an action to undo what was just done by the user
	 * @param nExpense new ExpenseRecord that was just created by user 
	 * @param isNewCat true for new category, otherwise false
	 * @param isNewPay true for new payment method, otherwise false
	 * @return AbstractAction undo action
	 */
	private AbstractAction createUndoAction(final ExpenseRecord nExpense, final boolean isNewCat, final boolean isNewPay) {
		final ExpenseRecord record = this.record; // Making sure that the record will not get edited
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(isEdit) {
					recHandler.modifyRecord(record.getId(), record, isNewCat, isNewPay);
				}
				else {
					recHandler.removeRecord(nExpense.getId());
				}
				if(isNewCat) {
					catHandler.removeCategory(nExpense.getCategory().getID());
				}
			}
		};
	}
}