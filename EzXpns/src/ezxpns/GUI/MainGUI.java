package ezxpns.GUI;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import ezxpns.data.*;
import ezxpns.data.records.*;

/**
 * The MainGUI Window for EzXpns
 * <br />Implements UpdateNotifyee to be able to be updated when things are changed 
 * @author A0097973
 */
@SuppressWarnings("serial")
public class MainGUI extends JFrame implements UpdateNotifyee {
	
//	private UIControl uiCtrl;
	
	/**
	 * The Undo Manager for EzXpns
	 */
	private UndoManager undoMgr;
	
	/**
	 * Main Navigator - the side bar
	 */
	private EzNavigator navi;
	
	/**
	 * Content Placeholder - the content display that gets switched
	 */
	private JLayeredPane panContent;
	
	/**
	 * Reference to the SavingsOverviewPanel on the Dash board
	 */
	private SavingsOverviewPanel panSavings;
	
	/**
	 * Reference to the OverviewPanel on the Dash board
	 */
	private OverviewPanel panOverview;
	
	/**
	 * Reference to the TargetOverviewPanel on the Dash board
	 */
	private TargetOverviewPanel panTarget;
	
	/**
	 * Reference to the SearchPanel 
	 */
	private SearchPanel panSearch;
	
	/**
	 * Reference to the CategoryManagerPanel - also the TargetManager
	 */
	private CategoryTargetPanel panCategory;
	
	/**
	 * Reference to the RecordsDisplayPanel on the Dash board
	 */
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
		
//		this.uiCtrl = uiCtrl;
		
		CardLayout contentMgr = new CardLayout();
		panContent = new JLayeredPane();
		panContent.setLayout(contentMgr);
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		navi = new EzNavigator(uiCtrl, contentMgr, panContent);
		navi.setPreferredSize(new Dimension(180, 400));
		
		this.add(navi, "1, 2, fill, fill");
		
		JPanel homeContent = new JPanel();
		homeContent.setBorder(new EmptyBorder(5, 0, 10, 10));
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
		
		panContent.add(homeContent, NormalMenuOpt.DASHBD.toString());
		
		this.add(panContent, "2, 2, fill, fill");
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
		this.setTitle("EzXpnz - the revolutionary next generation game changer-ish luxury classy high-end expense manager");
	}
	
	/**
	 * Loads the Category Panel
	 * @param expenseHandler CategoryHandler reference for ExpenseRecord
	 * @param incomeHandler CategoryHandler reference for IncomeRecord
	 * @param targetMgr TargetManager object reference to manage targets
	 */
	public void loadCategoryPanel(
			CategoryHandler<ExpenseRecord> expenseHandler, 
			CategoryHandler<IncomeRecord> incomeHandler, 
			TargetManager targetMgr) {
		if(panCategory==null) {
			panCategory = new CategoryTargetPanel(expenseHandler, incomeHandler, targetMgr, this);
			panContent.add(panCategory, NormalMenuOpt.CATMGR.toString());
		}
	}
	
	/**
	 * Loads the Search Panel
	 * @param searchHandler SearchHandler reference for searching Records
	 * @param display RecordListView reference for the display
	 * @param incomeHandler CategoryHandler reference for IncomeRecord
	 * @param expenseHandler CategoryHandler reference for ExpenseRecord
	 */
	public void loadSearchPanel(
			SearchHandler searchHandler, 
			RecordListView display, 
			CategoryHandler<IncomeRecord> incomeHandler, 
			CategoryHandler<ExpenseRecord> expenseHandler){
		if(panSearch == null) {
			panSearch = new SearchPanel(searchHandler, display, incomeHandler, expenseHandler);
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
		panRecords.update();
		navi.updateUndoBtn();
		this.validate();
	}

	@Override
	public void addUndoAction(AbstractAction action, String name) {
		navi.updateUndoBtn();
		undoMgr.add(action, name);
	}
}
