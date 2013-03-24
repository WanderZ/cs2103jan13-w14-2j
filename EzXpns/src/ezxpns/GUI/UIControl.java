package ezxpns.GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ezxpns.data.ReportGenerator;
import ezxpns.data.SummaryGenerator;
import ezxpns.data.TargetManager;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.PayMethodHandler;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;
import ezxpns.data.records.SearchHandler;

/**
 * To assist EzXpns in managing all the GUI Windows
 */
public class UIControl implements RecordListView.RecordEditor {
	
	// JComponents
	private HomeScreen homeScreen;
	private RecordFrame recWin;
	private SearchFrame searchWin;	
	private ReportFrame reportWin;
	private CategoryFrame catWin;
	private PaymentMethodFrame payWin;
	
	// Logical Components
	private SearchHandler findHandler;
	private RecordHandler recHandler;
	private CategoryHandler inCatHandler, exCatHandler;
	private PayMethodHandler payHandler;
	private TargetManager targetMgr;
	private ReportGenerator rptGen;
	private SummaryGenerator sumGen;
	private UndoManager undoMgr;
	
	/**
	 * To create a UI Manager. 
	 * @param searchHandlerRef the reference to the search handling logic component
	 * @param recHandlerRef the reference to the record handling logic component
	 * @param inCatHandlerRef the reference to the income category handling logic component
	 * @param exCatHandlerRef the reference to the expense category handling logic component
	 * @param payHandlerRef the reference to the payment method handling logic component
	 * @param targetMgrRef the reference to the target management logic component
	 * @param rptGenRef the reference to the report generator logic component
	 * @param sumGenRef the reference to the summary generator logic component
	 */
	public UIControl(
			SearchHandler searchHandlerRef, 
			RecordHandler recHandlerRef, 
			CategoryHandler inCatHandlerRef, 
			CategoryHandler exCatHandlerRef,
			PayMethodHandler payHandlerRef,
			TargetManager targetMgrRef,
			ReportGenerator rptGenRef,
			SummaryGenerator sumGenRef) {
		
		// Handlers for the various places
		findHandler = searchHandlerRef;
		recHandler = recHandlerRef;
		inCatHandler = inCatHandlerRef;
		exCatHandler = exCatHandlerRef;
		targetMgr = targetMgrRef;
		payHandler = payHandlerRef;
		rptGen = rptGenRef;
		sumGen = sumGenRef;
		undoMgr = new UndoManager();
		
		homeScreen = new HomeScreen(this, recHandlerRef, targetMgr, undoMgr, sumGen);
		
		homeScreen.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent wEvent) {
				System.exit(0);
			}
		});
	}
	
	/**
	 * Display the main/home screen of EzXpns
	 */
	public void showHomeScreen() {
		if(!homeScreen.isVisible()) {
			homeScreen.setVisible(true);
		}
	}
	
	/**
	 * Closes the main/home screen of EzXpns
	 */
	public void closeHomeScreen() {
		if(homeScreen.isVisible()) {
			homeScreen.setVisible(false);
		}
	}
	
	/**
	 * Displays a new record handler window with the chosen tab
	 * <br />Use RecordFrame.TAB_INCOME or TAB_EXPENSE to choose
	 * @param recordType the type of new record Expense/Income 
	 */
	public void showRecWin(int recordType) {
		recWin = new RecordFrame(recHandler, inCatHandler, exCatHandler, payHandler, undoMgr, recordType);
		recWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				homeScreen.updateAll();
				System.out.println("RecordFrame exiting!");
			}
		});
		recWin.setVisible(true);
	}
	
	/**
	 * Display the record window - edit an ExpenseRecord record
	 * @param record ExpenseRecord to be edited
	 */
	public void showRecWin(ExpenseRecord record) {
		recWin = new RecordFrame(recHandler, exCatHandler, payHandler, undoMgr, record);
		recWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				homeScreen.updateAll();
			}
		});
		recWin.setVisible(true);
	}
	
	/**
	 * Display the record window - edit an IncomeRecord record
	 * @param record IncomeRecord to be be edited
	 */
	public void showRecWin(IncomeRecord record) {
		recWin = new RecordFrame(recHandler, inCatHandler, undoMgr, record);
		recWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				homeScreen.updateAll();
			}
		});
		recWin.setVisible(true);
	}
	
	/**
	 * Displays the search handler window
	 */
	public void showSearchWin() {
		if(searchWin == null) {
			searchWin = new SearchFrame(findHandler, new RecordListView(this, recHandler));
		}
		searchWin.setVisible(true);
	}

	@Override
	public void edit(Record record, RecordListView display) {
		// TODO Auto-generated method stub
		
		RecordListView displayer = display; 
		
		//TODO: Check if this can be integrated better - need further testing
		if(record instanceof ExpenseRecord) {
			ExpenseRecord expense = (ExpenseRecord) record;
			this.showRecWin(expense);
		}
		else {
			IncomeRecord income = (IncomeRecord) record;
			this.showRecWin(income);
		}
		
		//TODO: call the callback method in display
		displayer.itemEdited(null); // null for no changes
		
	}
	
	/** 
	 * Displays the Report handler Window 
	 */
	public void showReportWin() {
		if(reportWin == null) {
			reportWin = new ReportFrame(rptGen);
			reportWin.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent wEvent) {
					homeScreen.updateAll();
					reportWin.dispose();
				}
			});
		}
		reportWin.setVisible(true);
	}
	
	/**
	 * Displays the Category Manager window
	 */
	public void showCatWin() {
		catWin = new CategoryFrame(exCatHandler, inCatHandler, targetMgr, homeScreen);
		catWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				homeScreen.updateAll();
				catWin.dispose();
			}
		});
		catWin.setVisible(true); 
	}
	
	/**
	 * Displays the Payment Method Manager Window
	 */
	public void showPayWin() {
		payWin = new PaymentMethodFrame(payHandler);
		payWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				homeScreen.updateAll();
				payWin.dispose();
			}
		});
		payWin.setVisible(true);
	}
	
	public UndoManager getUndoMgr() {
		return undoMgr;
	}
}
