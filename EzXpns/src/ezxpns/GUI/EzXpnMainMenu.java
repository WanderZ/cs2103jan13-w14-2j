package ezxpns.GUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class EzXpnMainMenu extends JMenuBar {
	
	private JMenu mRecord, mEdit, mReport;
	private UIControl guiCtrl;
	
	public EzXpnMainMenu(UIControl guiControlRef) {
		
		guiCtrl = guiControlRef;
		
		mRecord = new MenuRecord("Record", guiCtrl);
		mEdit = new MenuEdit("Edit", guiCtrl);
		mReport = new MenuReport("Report", guiCtrl);
		
		this.add(mRecord);
		this.add(mEdit);
		this.add(mReport);
	}
}

@SuppressWarnings("serial")
class MenuRecord extends JMenu {
	
	private JMenuItem itmExpense, itmIncome, itmCatMgr, itmPayMgr, itmSearch;
	private UIControl guiCtrl;
	
	public MenuRecord(String name, UIControl guiCtrlRef) {
		super(name);
		
		this.guiCtrl = guiCtrlRef;
		
		itmExpense = new JMenuItem("New Expense");
		itmIncome = new JMenuItem("New Income");
		
		this.add(itmExpense);
		this.add(itmIncome);
		
		this.add(new JSeparator());
		
		itmCatMgr = new JMenuItem("Manage Category");
		itmPayMgr = new JMenuItem("Manage Payment");
		itmSearch = new JMenuItem("Search");
		
		this.add(itmCatMgr);
		this.add(itmPayMgr);
		this.add(itmSearch);
		
		itmExpense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent mEvent) {
				guiCtrl.showRecWin(RecordFrame.TAB_EXPENSE);
			}
		});
		
		itmIncome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent mEvent) {
				guiCtrl.showRecWin(RecordFrame.TAB_INCOME);
			}
			
		});
		
		itmCatMgr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				guiCtrl.showCatWin();
			}
		});
		
		itmPayMgr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				guiCtrl.showPayWin();
			}
		});
		
		itmSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guiCtrl.showSearchWin();
			}
		});
	}
}

@SuppressWarnings("serial")
class MenuEdit extends JMenu {
	
	private JMenuItem itmUndo, itmConfig;
	private UIControl guiCtrl;
	private UndoManager undoMgr;
	
	public MenuEdit(String name, UIControl guiCtrlRef) { 
		super(name);
		
		this.guiCtrl = guiCtrlRef;
		this.undoMgr = guiCtrl.getUndoMgr();
		
		itmUndo = new JMenuItem();
		itmUndo.setAction(undoMgr.getAction());
		itmConfig = new JMenuItem("Settings");
		
		this.add(this.itmUndo);
		this.add(this.itmConfig);
	}
	
	public void updateUndoText(String msg) {
		this.itmUndo.setText(msg);
	}
}

@SuppressWarnings("serial")
class MenuReport extends JMenu {
	
	private JMenuItem itmReport;
	private UIControl guiCtrl;
	
	public MenuReport(String name, UIControl guiCtrlRef) {
		super(name);
		
		guiCtrl = guiCtrlRef;
		
		itmReport = new JMenuItem("View Report");
		this.add(this.itmReport);
		
		itmReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guiCtrl.showReportWin();
			}
			
		});
	}
}

@SuppressWarnings("serial")
class MenuTarget extends JMenu {

	private JMenuItem itmViewGoals, itmNewGoal, itmEditGoal, itmRmGoal;
	
	public MenuTarget(String name) {
		super(name);
		
		itmViewGoals = new JMenuItem("View All Target(s)");
		itmNewGoal = new JMenuItem("Set New Target");
		itmEditGoal = new JMenuItem("Edit Target");
		itmRmGoal = new JMenuItem("Remove Target");
		
		this.add(this.itmViewGoals);
		this.add(this.itmNewGoal);
		this.add(this.itmEditGoal);
		this.add(this.itmRmGoal);
	}
}
