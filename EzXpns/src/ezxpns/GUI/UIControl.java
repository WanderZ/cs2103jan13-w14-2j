package ezxpns.GUI;

public class UIControl<T extends RecordHandlerInterface & CategoryHandlerInterface & SearchHandlerInterface> {
	private T master;
	private HomeScreen homeScreen;
	private RecordFrame recWin;
	private SearchFrame searchWin;	
	private ReportFrame reportWin;
	
	public UIControl(T masterRef) {
		master = masterRef;
		homeScreen = new HomeScreen(master);
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
			recWin = new RecordFrame();
		}
		recWin.setVisible(true);
	}
	
	/** Displays the search handler window */
	public void showSearchWin() {
		if(searchWin == null) {
			searchWin = new SearchFrame();
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
