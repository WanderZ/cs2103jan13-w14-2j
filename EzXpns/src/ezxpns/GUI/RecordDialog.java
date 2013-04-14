package ezxpns.GUI;

import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This is a JFrame object (Window) that allows users to enter a new record (Expense/Income) into the EzXpns
 */
@SuppressWarnings("serial")
public class RecordDialog extends JDialog implements ActionListener {
	
	public static final int TAB_INCOME = 0011;
	public static final int TAB_EXPENSE = 1100;
	
	private RecordHandler recHandler;
	private CategoryHandler<IncomeRecord> incomeHandler;
	private CategoryHandler<ExpenseRecord> expenseHandler;
	private UpdateNotifyee notifyee;
	private boolean isEditing;
	
	private PanelMain panMain;
	private PanelOption panOpt;
	
	/**
	 * Generalized constructor for RecordFrame
	 * @param recHandlerRef RecordHandler reference to manage Records
	 * @param incomeHandlerRef CategoryHandler reference to manage Income Categories
	 * @param expenseHandlerRef CategoryHandler reference to manage Expense Categories
	 * @param payHandlerRef PayHandler reference to manage Payment Methods
	 * @param undoMgrRef UndoManager reference for managing undo actions
	 */
	public RecordDialog(
			JFrame homeRef,
			String title,
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> incomeHandlerRef, 
			CategoryHandler<ExpenseRecord> expenseHandlerRef,
			UpdateNotifyee notifyeeRef) {
		super(homeRef, title, true); /* Owner, Title, Modularity */
		recHandler = recHandlerRef;
		incomeHandler = incomeHandlerRef;
		expenseHandler = expenseHandlerRef;
		notifyee = notifyeeRef;
		isEditing = false;
		this.initFrame();
		
	}
	
	/**
	 * Constructor to specify the initial tab to be displayed
	 * @param recHandlerRef RecordHandler reference to manage Records
	 * @param incomeHandlerRef CategoryHandler reference to manage Income Categories
	 * @param expenseHandlerRef CategoryHandler reference to manage Expense Categories
	 * @param payHandlerRef PayHandler reference to manage Payment Methods
	 * @param undoMgrRef UndoManager reference for managing undo actions
	 * @param initTab use either TAB_INCOME or TAB_EXPENSE to indicate which tab to choose
	 */
	public RecordDialog(
			JFrame homeRef,
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> incomeHandlerRef, 
			CategoryHandler<ExpenseRecord> expenseHandlerRef,
			UpdateNotifyee notifyeeRef,
			int initTab) {
		
		this(homeRef, initTab == TAB_INCOME ? "EzXpns - New Income Record": "EzXpns - New Expense Record", recHandlerRef, incomeHandlerRef, expenseHandlerRef, notifyeeRef);
		this.initComponent();
		
		panMain.toggleIncomeTab(); // Fix
		panMain.toggleExpenseTab(); // Default
		
		// TODO: Refactor this bit
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
	 * Constructor for editing an existing ExpenseRecord
	 * @param recHandlerRef RecordHandler reference for managing records
	 * @param expenseHandlerRef CategoryHandler reference for managing categories
	 * @param payHandlerRef PayHandler reference for managing payment methods
	 * @param undoMgrRef UndoManager reference for managing undo actions
	 * @param record existing ExpenseRecord to be edited
	 */
	public RecordDialog(
			JFrame homeRef,
			String title,
			RecordHandler recHandlerRef,
			CategoryHandler<ExpenseRecord> expenseHandlerRef,
			UpdateNotifyee notifyeeRef,
			ExpenseRecord record) {
		this(homeRef, title, recHandlerRef, null, expenseHandlerRef, notifyeeRef);
		isEditing = true;
		this.initComponent(record);
		
	}
	
	/**
	 * Constructor for editing an existing IncomeRecord
	 * @param recHandlerRef RecordHandler reference for managing records
	 * @param incomeHandlerRef CategoryHandler reference for managing categories
	 * @param undoMgrRef UndoManager reference for managing undo actions
	 * @param record existing IncomeRecord to be edited
	 */
	public RecordDialog(
			JFrame homeRef,
			String title,
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> incomeHandlerRef,
			UpdateNotifyee notifyeeRef,
			IncomeRecord record) {
		this(homeRef, title, recHandlerRef, incomeHandlerRef, null, notifyeeRef);
		isEditing = true;
		this.initComponent(record);
		
	}
	
	/**
	 * Initialize this frame with its properties
	 */
	private void initFrame() {
		this.getContentPane().setLayout(new BorderLayout(5, 5));
		
		this.setBounds(0, 0, Config.DEFAULT_DIALOG_WIDTH, Config.DEFAULT_DIALOG_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Initialize this frame with its components
	 */
	private void initComponent() {
		panMain = new PanelMain(recHandler, incomeHandler, expenseHandler, notifyee);
		this.getContentPane().add(panMain, BorderLayout.CENTER);
		
		panOpt = new PanelOption(this);
		this.getContentPane().add(panOpt, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(panOpt.getSaveBtn());
	}
	
	/**
	 * Initialize this frame with its components with the given ExpenseRecord
	 * @param record ExpenseRecord to be modified
	 */
	private void initComponent(ExpenseRecord record) {
		panMain = new PanelMain(recHandler, incomeHandler, expenseHandler, notifyee, record);
		this.getContentPane().add(panMain, BorderLayout.CENTER);
		
		panOpt = new PanelOption(this);
		this.getContentPane().add(panOpt, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(panOpt.getSaveBtn());
	}
	
	/**
	 * Initialize this frame with its components with the given IncomeRecord
	 * @param record IncomeRecord to be modified
	 */
	private void initComponent(IncomeRecord record) {
		panMain = new PanelMain(recHandler, incomeHandler, expenseHandler, notifyee, record);
		this.getContentPane().add(panMain, BorderLayout.CENTER);
		
		panOpt = new PanelOption(this);
		this.getContentPane().add(panOpt, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(panOpt.getSaveBtn());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.panOpt.getSaveBtn() == e.getSource()) { // Save button has been invoked.
			System.out.println("Saved invoked!");
			if(panMain.validateForm()) { // Invoke validation
				this.closeWin(panMain.save());
				return;
			}
			System.out.println("Validate Fail!");
			// TODO: Display why the validation failed back to user
		}
		
		if(this.panOpt.getCancelBtn() == e.getSource()) {
			this.closeWin();
		}
	}
	
	/**
	 * Closing the window - without editing
	 */
	public void closeWin() {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        this.dispatchEvent(wev); // "Throw" Event
        this.dispose();
	}
	
	/**
	 * To close this window safely - in edit mode
	 */
	public void closeWin(Record record) {
		SuccessfulSaveEvent success = new SuccessfulSaveEvent(this, WindowEvent.WINDOW_CLOSING, record);
        // WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        this.dispatchEvent(success); // "Throw" Event
        this.dispose();
        // java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev); // Don't seem to work
	}
}

/**
 * Modified WindowEvent to return the edited record
 *
 */
@SuppressWarnings("serial")
class SuccessfulSaveEvent extends WindowEvent {

	private Record saved;
	
	public SuccessfulSaveEvent(Window source, int id, Record savedRecord) {
		super(source, id);
		saved = savedRecord;
	}
	
	public Record getRecord() {
		return saved;
	}
}


/** Panel Containing the Form for user to fill up - to create new record */
@SuppressWarnings("serial")
class PanelMain extends JPanel {
	
	public final String CARD_EXPENSE = "Expenses";
	public final String CARD_INCOME = "Income";
	
	private ExpenseForm panExpense;
	private IncomeForm panIncome;
	// private PanelRecur panRecurOpt;
	private CardLayout loCard;
	private JPanel metroTabs, metroTabBtns, metroTabContent;
	private JButton mtabExpense, mtabIncome;
	
	/** 
	 * true for ExpenseRecord, otherwise false for IncomeRecord
	 */
	private boolean isExpense;
		
	/**
	 * Constructor for the main content panel for new record
	 * @param recHandlerRef reference to the record handler
	 * @param incomeHandlerRef reference to the income category handler
	 * @param expenseHandlerRef reference to the expense category handler
	 * @param payHandlerRef reference to the payment method handler
	 * @param undoMgrRef
	 */
	public PanelMain(
			RecordHandler recHandlerRef,
			CategoryHandler<IncomeRecord> incomeHandlerRef,
			CategoryHandler<ExpenseRecord> expenseHandlerRef,
			UpdateNotifyee notifyeeRef) {
		this.setLayout(new BorderLayout());
		this.isExpense = true;
		
		panExpense = new ExpenseForm(recHandlerRef, expenseHandlerRef, notifyeeRef);
		panIncome = new IncomeForm(recHandlerRef, incomeHandlerRef, notifyeeRef);
		this.initTabs();
		
		// Create Recurring options panel
		// panRecurOpt = new PanelRecur();
		// this.add(panRecurOpt, BorderLayout.SOUTH);
	}
	
	/**
	 * Constructor for the main content panel for editing of an existing ExpenseRecord
	 * @param recHandlerRef
	 * @param incomeHandlerRef
	 * @param expenseHandlerRef
	 * @param payHandlerRef
	 * @param undoMgrRef
	 * @param record ExpenseRecord to be edited
	 */
	public PanelMain(
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> incomeHandlerRef, 
			CategoryHandler<ExpenseRecord> expenseHandlerRef, 
			UpdateNotifyee notifyeeRef,
			ExpenseRecord record) {
		
		this.setLayout(new BorderLayout());
		this.isExpense = true;
		panExpense = new ExpenseForm(recHandlerRef, expenseHandlerRef, notifyeeRef, record);
		this.add(panExpense, BorderLayout.CENTER);
	}
	
	/**
	 * Constructor for the main content panel for editing of an existing IncomeRecord
	 * @param recHandlerRef
	 * @param incomeHandlerRef
	 * @param expenseHandlerRef
	 * @param payHandlerRef
	 * @param undoMgrRef
	 * @param record IncomeRecord to be edited
	 */
	public PanelMain(
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> incomeHandlerRef, 
			CategoryHandler<ExpenseRecord> expenseHandlerRef, 
			UpdateNotifyee notifyeeRef,
			IncomeRecord record) {
		this.setLayout(new BorderLayout());
		this.isExpense = false;
		panIncome = new IncomeForm(recHandlerRef, incomeHandlerRef, notifyeeRef, record);
		this.add(panIncome, BorderLayout.CENTER);
	}
	
	private void initTabs() {
		metroTabs = new JPanel(); // the main panel for keeping everything together
		metroTabs.setLayout(new BorderLayout());
		
		// Buttons to trigger
		metroTabBtns = new JPanel();
		metroTabBtns.setLayout(new GridLayout(1, 0, 15, 15));
		metroTabBtns.setOpaque(false);
		
		metroTabBtns.add(getExpenseTab());
		metroTabBtns.add(getIncomeTab());
		
//		metroTabs.add(metroTabBtns, BorderLayout.NORTH); // no need to show the tabs now
		
		metroTabContent = new JPanel();
		
		loCard = new CardLayout(15, 15);
		metroTabContent.setLayout(loCard);
		
		metroTabContent.add(panExpense, this.CARD_EXPENSE);
		metroTabContent.add(panIncome, this.CARD_INCOME);
		metroTabs.add(metroTabContent, BorderLayout.CENTER);
		
		loCard.show(metroTabContent, CARD_EXPENSE);
		
		// this.tabs.setMnemonicAt(); // setting keyboard shortcut
		this.add(metroTabs, BorderLayout.CENTER);
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
		return isExpense ? panExpense.save() : panIncome.save();
	}
	
	/**
	 * To check if the current tab is the Expense Tab
	 * @return true if it is, otherwise false
	 */
	public boolean isExpense() {return isExpense;}
	

	/**
	 * Method to toggle to the tab for a new income record
	 */
	public void toggleIncomeTab() {
		if(isExpense) {
			loCard.show(metroTabContent, CARD_INCOME);
			// Indicate some difference to let user know that this tab is selected
			changeFocus(this.mtabIncome, this.mtabExpense);
			isExpense = false;
			return;
		}
	}
	
	/**
	 * Method to toggle to the tab for a new expense record
	 */
	public void toggleExpenseTab() {
		if(!isExpense) {
			loCard.show(metroTabContent, CARD_EXPENSE);
			// Indicate some difference to let user know that this tab is selected
			changeFocus(this.mtabExpense, this.mtabIncome);
			isExpense = true;
			return;
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
}

/** Panel containing the options available to this frame */
@SuppressWarnings("serial")
class PanelOption extends JPanel {
	
	private JButton btnSave, btnCancel;
	
	public PanelOption(ActionListener listener) {
		// Automated layout - new FlowLayout()
		btnSave = new JButton("Save");
		
		btnCancel = new JButton("Discard");
		
		btnSave.addActionListener(listener);
		btnCancel.addActionListener(listener);
		
		this.add(btnSave);
		this.add(btnCancel);
	}
	
	public JButton getSaveBtn() {return this.btnSave;}
	public JButton getCancelBtn() {return this.btnCancel;}
}


/** Panel to store all the options pertaining to recurrence of a record */
@SuppressWarnings("serial")
class PanelRecur extends JPanel implements ActionListener {
	
	private JCheckBox chkRecur;
	private JComboBox cboxFrequency;
	private JTextField txtStart, txtEnd;
	
	/** To create a new recurring panel */
	public PanelRecur() {
		init();
	}
	
	/** Initiate the fields within */
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
	
	/** Check if user indicate this to be a recurring record */
	public boolean isToRecur() { return this.chkRecur.isSelected(); }

	@Override
	public void actionPerformed(ActionEvent e) {this.toggle();}
}