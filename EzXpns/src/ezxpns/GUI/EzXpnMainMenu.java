package ezxpns.GUI;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class EzXpnMainMenu extends JMenuBar {
	
	private JMenu mRecord, mEdit;
	
	public EzXpnMainMenu() {
		mRecord = new MenuRecord("Record");
		mEdit = new MenuEdit("Edit");
		// mReport = new MenuReport("Report");
		// mAlert = new MenuAlert("Alert");
		
		this.add(mRecord);
		this.add(mEdit);
		// this.add(mReport);
		// this.add(mAlert);
	}
}

@SuppressWarnings("serial")
class MenuRecord extends JMenu {
	
	private JMenuItem itmExpense, itmIncome, itmCatMgr, itmSearch;
	
	public MenuRecord(String name) {
		super(name);
		
		itmExpense = new JMenuItem("New Expense");
		itmIncome = new JMenuItem("New Income");
		
		this.add(itmExpense);
		this.add(itmIncome);
		
		this.add(new JSeparator());
		
		itmCatMgr = new JMenuItem("Category Manager");
		itmSearch = new JMenuItem("Search");
		
		this.add(itmCatMgr);
		this.add(itmSearch);
	}
}

@SuppressWarnings("serial")
class MenuEdit extends JMenu {
	
	private JMenuItem itmUndo, itmConfig;
	
	public MenuEdit(String name) { 
		super(name);
		
		itmUndo = new JMenuItem("Undo <something here>");
		itmConfig = new JMenuItem("Settings");
		
		this.add(this.itmUndo);
		this.add(this.itmConfig);
	}
}

@SuppressWarnings("serial")
class MenuReport extends JMenu {
	
	private JMenuItem itmReport;
	
	public MenuReport(String name) {
		super(name);
		
		itmReport = new JMenuItem("View Report");
		this.add(this.itmReport);
	}
}

@SuppressWarnings("serial")
class MenuAlert extends JMenu {
	
	private JMenuItem itmNewAlert, itmEditAlert, itmRmAlert;
	
	public MenuAlert(String name) {
		super(name);
		
		itmNewAlert = new JMenuItem("Create New Reminder");
		itmEditAlert = new JMenuItem("Edit Reminder");
		itmRmAlert = new JMenuItem("Remove Reminder");
		
		this.add(this.itmNewAlert);
		this.add(this.itmEditAlert);
		this.add(this.itmRmAlert);
	}
}

/*
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
*/