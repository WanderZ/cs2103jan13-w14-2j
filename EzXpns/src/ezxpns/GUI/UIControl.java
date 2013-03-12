package ezxpns.GUI;

/**
 * To assist EzXpns in managing all the GUI Windows
 * @param <T>
 */
public class UIControl<T extends RecordHandlerInterface & CategoryHandlerInterface & SearchHandlerInterface> {
	private T master;
	private HomeScreen homeScreen;
	private RecordFrame recWin;
	private SearchFrame searchWin;	
	private ReportFrame reportWin;
	
	public UIControl(T masterRef) {
		master = masterRef;
		homeScreen = new HomeScreen(master);
		
		// Faking a pop up :)
		/*JWindow jWin = new JWindow();
		jWin.getContentPane().add(new JLabel("helloworld!"));
		jWin.setSize(800,600);
		jWin.setLocationRelativeTo(null);
		jWin.setVisible(true);*/
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
	
	/** Displays the new record handler window */
	public void showRecWin() {
		if(recWin == null) {
			recWin = new RecordFrame(master);
		}
		recWin.setVisible(true);
	}
	
	/**
	 * Displays a new record handler window with the chosen tab
	 * <br />Use RecordFrame.TAB_INCOME or TAB_EXPENSE to choose
	 * @param recordType the type of new record Expense/Income 
	 */
	public void showRecWin(int recordType) {
		if(recWin == null) {
			recWin = new RecordFrame(master, recordType);
		}
		recWin.setVisible(true);
	}
	
	/** Displays the search handler window */
	public void showSearchWin() {
		if(searchWin == null) {
			searchWin = new SearchFrame(master);
		}
		searchWin.setVisible(true);
	}
	
	/** Displays the report handler window */
	public void showReportWin() {
		if(reportWin == null) {
			reportWin = new ReportFrame();
		}
		reportWin.setVisible(true);
	}
}
