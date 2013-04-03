package ezxpns.GUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ezxpns.data.NWSGenerator;
import ezxpns.data.SummaryGenerator;
import ezxpns.data.TargetManager;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.PaymentHandler;
import ezxpns.data.records.RecordHandler;
import ezxpns.data.records.SearchHandler;

@SuppressWarnings("serial")
public class MainGUI extends JFrame implements UpdateNotifyee {
	
	private UIControl uiCtrl;
	private UndoManager undoMgr;
	
	/**
	 * Main Navigator for UI
	 */
	private EzNavigator navi;
	private JLayeredPane panContent;
	
	/**
	 * 
	 */
	private SavingsOverviewPanel panSavings;
	private OverviewPanel panOverview;
	private TargetOverviewPanel panTarget;
	
	private SearchFrame panSearch;
	private CategoryFrame panCategory;
	private RecordsDisplayPanel panRecords;
	
	public MainGUI(
			NWSGenerator nwsGen,
			RecordHandler recHandler,
			SummaryGenerator sumGen, 
			TargetManager targetMgr, 
			UIControl uiCtrl,
			UndoManager undoMgr) {
		super();
		this.instantiateUI();
		this.undoMgr = undoMgr;
		
		this.uiCtrl = uiCtrl;
		// TODO: InstantiateNavi
		// TODO: CardLayout
		// TODO: InstantiateContent
		// TODO: Iterate through the list of JLayeredPane/JPanel to be displayed
		
		CardLayout contentMgr = new CardLayout();
		panContent = new JLayeredPane();
		panContent.setLayout(contentMgr);
		navi = new EzNavigator(uiCtrl, contentMgr, panContent);
		
		GridBagConstraints gbc = new GridBagConstraints();
		// Setup for Navigator
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.1;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(navi, gbc);
		
		JPanel homeContent = new JPanel(new GridBagLayout());
		GridBagConstraints gbContent = new GridBagConstraints();
		gbContent.fill = GridBagConstraints.BOTH;
		// Overview Panel
		gbContent.gridx = 0;
		gbContent.gridy = 0; // 0, 0
//		gbContent.weightx = 0.3;
//		gbContent.weighty = 0.3;
		gbContent.gridheight = 1;
		panOverview = new OverviewPanel(sumGen);
		homeContent.add(panOverview, gbContent);
		
		// Records Display Panel
		gbContent.gridx = 1;
		panRecords = new RecordsDisplayPanel(recHandler, uiCtrl, this);
		homeContent.add(panRecords, gbContent);
		
		// Targets Panel
		
		gbContent.gridheight = 2;
		gbContent.weightx = 0.1;
		gbContent.weighty = 0.1;
		gbContent.gridy = 2;
		gbContent.gridx = 0;
		panTarget = new TargetOverviewPanel(targetMgr);
		homeContent.add(panTarget, gbContent);
		
		// Savings Panel
//		gbContent.gridy = 1;
		gbContent.gridx = 1;
		panSavings = new SavingsOverviewPanel(nwsGen);
		homeContent.add(panSavings, gbContent);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.9;
		gbc.weighty = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		panContent.add(homeContent, NormalMenuOpt.DASHBD.toString());
		
		this.add(panContent, gbc);
		this.setVisible(true);
	}
	
	/**
	 * Instantiates the User Interface
	 */
	private void instantiateUI() {
		this.setBounds(0, 0, Config.DEFAULT_UI_WIDTH, Config.DEFAULT_UI_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridBagLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// this.setUndecorated(true); // Remove Title Bar 
	}
	
	/**
	 * Loads the Category Panel
	 * @param expenseHandler
	 * @param incomeHandler
	 * @param targetMgr
	 */
	public void loadCategoryPanel(
			CategoryHandler<ExpenseRecord> expenseHandler, 
			CategoryHandler<IncomeRecord> incomeHandler, 
			TargetManager targetMgr) {
		if(panCategory==null) {
			panCategory = new CategoryFrame(expenseHandler, incomeHandler, targetMgr, this);
			panContent.add(panCategory, NormalMenuOpt.CATMGR.toString());
		}
	}
	
	/**
	 * Loads the Search Panel
	 * @param searchHandler
	 * @param display
	 * @param incomeHandler
	 * @param expenseHandler
	 * @param payHandler
	 */
	public void loadSearchPanel(
			SearchHandler searchHandler, 
			RecordListView display, 
			CategoryHandler<IncomeRecord> incomeHandler, 
			CategoryHandler<ExpenseRecord> expenseHandler, 
			PaymentHandler payHandler) {
		if(panSearch == null) {
			panSearch = new SearchFrame(searchHandler, display, incomeHandler, expenseHandler, payHandler);
			panContent.add(panSearch, NormalMenuOpt.SEARCH.toString());
		}
	}

	@Override
	public void updateAll() {
		panTarget.update();
		panSavings.update();
		panOverview.updateOverview();
		panOverview.validate();
		panCategory.reload();
		panSearch.reload();
		this.validate();
	}

	@Override
	public void addUndoAction(AbstractAction action, String name) {
		undoMgr.add(action, name);
	}
}
