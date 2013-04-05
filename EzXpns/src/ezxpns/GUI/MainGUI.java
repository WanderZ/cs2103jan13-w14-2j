package ezxpns.GUI;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import ezxpns.data.*;
import ezxpns.data.records.*;

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
		
		JPanel homeContent = new JPanel();
		homeContent.setBorder(new EmptyBorder(15, 15, 15, 15));
		homeContent.setLayout(new GridLayout(2, 2, 15, 15));
		panOverview = new OverviewPanel(sumGen);
		homeContent.add(panOverview);
		
		
		// Targets Panel
		panTarget = new TargetOverviewPanel(targetMgr);
		homeContent.add(panTarget);
		
		// Savings Panel
		panSavings = new SavingsOverviewPanel(nwsGen);
		homeContent.add(panSavings);
		
		// Records Display Panel
		panRecords = new RecordsDisplayPanel(recHandler, uiCtrl, this);
		homeContent.add(panRecords);
		
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
		this.setMinimumSize(new Dimension(Config.MIN_UI_WIDTH, Config.MIN_UI_HEIGHT));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("EzXpnz - the revolutionary next genration game changer-ish luxury classy high-end expense manager");
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
