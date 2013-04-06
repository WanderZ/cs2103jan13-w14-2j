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
		panRecords.update();
		this.validate();
	}

	@Override
	public void addUndoAction(AbstractAction action, String name) {
		undoMgr.add(action, name);
	}
}
