package ezxpns.GUI;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.PayMethodHandler;
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
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This is a JFrame object (Window) that allows users to enter a new record (Expense/Income) into the EzXpns
 */
@SuppressWarnings("serial")
public class RecordFrame extends JFrame implements ActionListener {
	
	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 400; 
	
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
		
		panMain.toggleIncomeTab(); // Fix
		panMain.toggleExpenseTab(); // Default
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(this.panOpt.getSaveBtn() == e.getSource()) { // Save button has been invoked.
			System.out.println("Saved invoked!");
			if(panMain.validateForm()) { // Invoke validation
				System.out.println("Validate Success!");
				panMain.save();
				// all is good. save as new Record.
				// Check if it is a recurring record
				// do the necessary to ensure that EzXpns knows it. 
				this.dispose();
				return;
			}
			System.out.println("Validate Fail!");
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
	
	private ExpenseForm panExpense;
	private IncomeForm panIncome;
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
		metroTabs.setLayout(new BorderLayout());
		
		// Buttons to trigger
		metroTabBtns = new JPanel();
		metroTabBtns.setLayout(new GridLayout(1, 0, 15, 15));
		metroTabBtns.setOpaque(false);
		
		metroTabBtns.add(getExpenseTab());
		metroTabBtns.add(getIncomeTab());
		
		metroTabs.add(metroTabBtns, BorderLayout.NORTH);
		
		panExpense = new ExpenseForm(recHandlerRef, expenseHandlerRef, payHandlerRef);
		panIncome = new IncomeForm(recHandlerRef, incomeHandlerRef);

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
		// panRecurOpt = new PanelRecur();
		// this.add(panRecurOpt, BorderLayout.SOUTH);
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
		System.out.println(isExpense());
		System.out.println(isIncome());
		return isExpense() ? panExpense.save() : panIncome.save();
	}
	
	/**
	 * To check if the current tab is the Expense Tab
	 * @return true if it is, otherwise false
	 */
	public boolean isExpense() {
		System.out.println("isExpense:" + panExpense.isVisible());
		return panExpense.isVisible();
	}
	
	/**
	 * To check if the current tab is the Expense Tab
	 * @return true if it is, otherwise false
	 */
	public boolean isIncome() {
		System.out.println("isIncome:" + panIncome.isVisible());
		return panIncome.isVisible();
	}

	/**
	 * Method to toggle to the tab for a new income record
	 */
	public void toggleIncomeTab() {
		if(isExpense()) {
			loCard.show(metroTabContent, CARD_INCOME);
			// Indicate some difference to let user know that this tab is selected
			changeFocus(this.mtabIncome, this.mtabExpense);
			return;
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
	
	// Access and Mutate methods (for internal internal components)
	// Auto-complete - requires action listener
	// Auto-calculate - requires action listener
}

/** Panel containing the options available to this frame */
@SuppressWarnings("serial")
class PanelOption extends JPanel {
	
	private JButton btnSave, btnCancel;
	
	public PanelOption(ActionListener listener) {
		// Automated layout - new FlowLayout()
		btnSave = new JButton("   Save   ");
		btnSave.setFont(new Font("Segoe UI", 0, 18)); // #Font
		btnSave.setContentAreaFilled(false);
		btnSave.setBorder(BorderFactory.createRaisedBevelBorder());
		
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				btn.setBorder(BorderFactory.createLoweredBevelBorder());
			}
			
			@Override
			public void mouseReleased(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				btn.setBorder(BorderFactory.createEmptyBorder());
				// btn.setEnabled(false);
			}			
		});
		
		btnCancel = new JButton("Discard changes");
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