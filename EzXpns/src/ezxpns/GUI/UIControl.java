package ezxpns.GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ezxpns.data.NWSGenerator;
import ezxpns.data.ReportGenerator;
import ezxpns.data.SummaryGenerator;
import ezxpns.data.TargetManager;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.PaymentHandler;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;
import ezxpns.data.records.SearchHandler;

/**
 * To assist EzXpns in managing all the GUI Windows
 */
public class UIControl implements RecordListView.RecordEditor {
	
	// JComponents
	// private HomeScreen homeScreen;
	private MainGUI home;
	private RecordDialog recWin;
	private SearchFrame searchWin;	
	private ReportFrame reportWin;
	private CategoryFrame catWin;
	private PaymentMethodFrame payWin;
	
	// Logical Components
	private SearchHandler findHandler;
	private RecordHandler recHandler;
	private CategoryHandler<IncomeRecord> inCatHandler;
	private CategoryHandler<ExpenseRecord> exCatHandler;
	private PaymentHandler payHandler;
	private TargetManager targetMgr;
	private ReportGenerator rptGen;
	private SummaryGenerator sumGen;
	private UndoManager undoMgr;
	
	/**
	 * To create a UI Manager. 
	 * @param searchHandlerRef the reference to the search handling logic component
	 * @param recHandlerRef the reference to the record handling logic component
	 * @param incomeHandlerRef the reference to the income category handling logic component
	 * @param expenseHandlerRef the reference to the expense category handling logic component
	 * @param payHandlerRef the reference to the payment method handling logic component
	 * @param targetMgrRef the reference to the target management logic component
	 * @param rptGenRef the reference to the report generator logic component
	 * @param sumGenRef the reference to the summary generator logic component
	 */
	public UIControl(
			SearchHandler searchHandlerRef, 
			RecordHandler recHandlerRef, 
			CategoryHandler<IncomeRecord> incomeHandlerRef, 
			CategoryHandler<ExpenseRecord> expenseHandlerRef,
			PaymentHandler payHandlerRef,
			TargetManager targetMgrRef,
			ReportGenerator rptGenRef,
			SummaryGenerator sumGenRef,
			NWSGenerator nwsGen) {
		UINotify.createPopUp("Welcome! Please wait while we load your previous records");
		// Handlers for the various places
		findHandler = searchHandlerRef;
		recHandler = recHandlerRef;
		inCatHandler = incomeHandlerRef;
		exCatHandler = expenseHandlerRef;
		targetMgr = targetMgrRef;
		payHandler = payHandlerRef;
		rptGen = rptGenRef;
		sumGen = sumGenRef;
		undoMgr = new UndoManager();
		
		homeScreen = new HomeScreen(this, recHandlerRef, targetMgr, undoMgr, sumGen, nwsGen);
		home = new MainGUI(sumGen, targetMgr, undoMgr);
		home.loadCategoryPanel(expenseHandlerRef, incomeHandlerRef, targetMgrRef);
		home.loadSearchPanel(findHandler, new RecordListView(this, recHandler, home), inCatHandler, exCatHandler, payHandler);
	}
	
	/**
	 * Display the main/home screen of EzXpns
	 */
	public void showHomeScreen() {
		if(!home.isVisible()) {
			home.setVisible(true);
		}
	}
	
	/**
	 * Closes the main/home screen of EzXpns
	 */
	public void closeHomeScreen() {
		if(home.isVisible()) {
			home.setVisible(false);
		}
	}
	
	/**
	 * Displays a new record handler window with the chosen tab
	 * <br />Use RecordFrame.TAB_INCOME or TAB_EXPENSE to choose
	 * @param recordType the type of new record Expense/Income 
	 */
	public void showRecWin(int recordType) {
		recWin = new RecordDialog(home, recHandler, inCatHandler, exCatHandler, payHandler, home, recordType);
		recWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				home.updateAll();
				System.out.println("RecordFrame exiting!");
			}
		});
		recWin.setVisible(true);
	}
	
	/**
	 * Display the record window - in edit mode
	 * @param record Record object to be edited
	 */
	public void showRecWin(Record record) {
		recWin = new RecordDialog(home, recHandler, exCatHandler, payHandler, home, record);
		if(record instanceof ExpenseRecord) {
			ExpenseRecord expense = (ExpenseRecord) record;
			recWin = new RecordFrame(homeScreen, recHandler, exCatHandler, payHandler, homeScreen, expense);
		}
		else {
			IncomeRecord income = (IncomeRecord) record;
			recWin = new RecordFrame(homeScreen, recHandler, inCatHandler, homeScreen, income);
		recWin = new RecordDialog(home, recHandler, inCatHandler, undoMgr, home, record);
		}		
	}
	
	/**
	 * Displays the search handler window
	 */
	public void showSearchWin() {
		if(searchWin == null) {
			searchWin = new SearchFrame(findHandler, new RecordListView(this, recHandler, home),inCatHandler,exCatHandler, payHandler);
		}
		searchWin.setVisible(true);
	}

	@Override
	public void edit(Record record, RecordListView display) {
		System.out.println("Attempting to edit");
		final RecordListView displayer = display; 
		showRecWin(record);
		recWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				if(wEvent instanceof SuccessfulSaveEvent) {
					//TODO: call the callback method in display
					SuccessfulSaveEvent success = (SuccessfulSaveEvent) wEvent;
					displayer.itemEdited(success.getRecord()); 
					homeScreen.updateAll();
					System.out.println("I was here");
				}
			}
		});
		recWin.setVisible(true);
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
					home.updateAll();
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
		/*
		catWin = new CategoryFrame(exCatHandler, inCatHandler, targetMgr, homeScreen);
		catWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				homeScreen.updateAll();
				catWin.dispose();
			}
		});
		catWin.setVisible(true); 
		*/
	}
	
	/**
	 * Displays the Payment Method Manager Window
	 */
	public void showPayWin() {
		payWin = new PaymentMethodFrame(payHandler);
		payWin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wEvent) {
				home.updateAll();
				payWin.dispose();
			}
		});
		payWin.setVisible(true);
	}
	
	public UndoManager getUndoMgr() {
		return undoMgr;
	}
}
