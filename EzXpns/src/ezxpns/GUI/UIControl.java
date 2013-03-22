package ezxpns.GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ezxpns.data.ReportGenerator;
import ezxpns.data.SummaryGenerator;
import ezxpns.data.TargetManager;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.PayMethodHandler;
import ezxpns.data.records.RecordHandler;
import ezxpns.data.records.SearchHandler;

/**
 * To assist EzXpns in managing all the GUI Windows
<<<<<<< local
=======
 * Implements ActionListener for the main menu.
>>>>>>> other
 */
public class UIControl {
	
	private HomeScreen homeScreen;
	private RecordFrame recWin;
	private SearchFrame searchWin;	
	private ReportFrame reportWin;
	private CategoryFrame catWin;
	
	private SearchHandler findHandler;
	private RecordHandler recHandler;
	private CategoryHandler inCatHandler, exCatHandler;
	private PayMethodHandler payHandler;
	private TargetManager targetMgr;
	private ReportGenerator rptGen;
	private SummaryGenerator sumGen;
	
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
			SummaryGenerator sumGenRef
		) {
		
		// Handlers for the various places
		findHandler = searchHandlerRef;
		recHandler = recHandlerRef;
		inCatHandler = inCatHandlerRef;
		exCatHandler = exCatHandlerRef;
		targetMgr = targetMgrRef;
		payHandler = payHandlerRef;
		rptGen = rptGenRef;
		sumGen = sumGenRef;
		
		homeScreen = new HomeScreen(this, recHandlerRef, targetMgr, sumGen);
		
		homeScreen.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent wEvent) {
				System.exit(0);
			}
		});
		// Faking a pop up :)
		/*JWindow jWin = new JWindow();
		jWin.getContentPane().add(new JLabel("helloworld!"));
		jWin.setSize(800,600);
		jWin.setLocationRelativeTo(null);
		jWin.setVisible(true);
		*/
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
		if(recWin == null) {
			recWin = new RecordFrame(recHandler, inCatHandler, exCatHandler, payHandler, recordType);
			recWin.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent wEvent) {
					homeScreen.updateAll();
				}
			});
		}
		recWin.setVisible(true);
	}
	
	/** Displays the search handler window */
	public void showSearchWin() {
		if(searchWin == null) {
			searchWin = new SearchFrame(findHandler, new RecordListView(recWin, recHandler));
		}
		searchWin.setVisible(true);
	}
	
	/** Displays the report handler window */
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
	
	/** Displays the category handler window */
	public void showCatWin() { 
		if(catWin == null) {
			catWin = new CategoryFrame(exCatHandler, inCatHandler, targetMgr, homeScreen);
			catWin.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent wEvent) {
					homeScreen.updateAll();
					catWin.dispose();
				}
			});
		}
		catWin.setVisible(true); 
	}
}
