package ezxpns;

import ezxpns.data.*;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.util.Pair;
import ezxpns.GUI.*;
import java.awt.EventQueue;
import java.util.*;

import ezxpns.GUI.MainWindow;
import ezxpns.GUI.MainScreen;


public class Ezxpns implements
		ReportGenerator.DataProvider,
		TargetManager.DataProvider{
	private StorageManager store;
	private DataManager data;
	private ReportGenerator reportGenerator;
	private TargetManager targetManager;
	
	public Ezxpns(){
		try{
			store = new StorageManager("data.json");
			store.read();
			data = store.getDataManager();
		}catch(Exception e){
			System.out.println(e.toString());
			System.exit(1);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					MainScreen main = new MainScreen();
					main.showScreen();
					
					RecordFrame recordMgrI = new RecordFrame(RecordFrame.TAB_INCOME);
					recordMgrI.showScreen();
					
					RecordFrame recordMgrE = new RecordFrame();
					recordMgrE.showScreen();
					
					/*
					SearchFrame searchMgr = new SearchFrame();
					searchMgr.setVisible(true);
					*/
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		reportGenerator = new ReportGenerator(this);
		targetManager = data.targetManager();
		// targetManager.setDataProvider(this);
	}

	@Override
	public Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> getDataInDateRange(
			Date start, Date end) {
		return new Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>>(
				data.expenses().getRecordsBy(start, end, -1, false),
				data.incomes().getRecordsBy(start, end, -1, false));
	}
	
	public void addTarget(){
		
	}
	
	public void applicationQuitting(){
		store.save();
	}
	
	public void addRecord(ExpenseRecord r){
		data.expenses().addNewRecord(r);
	}
	
	public void addRecord(IncomeRecord r){
		data.incomes().addNewRecord(r);
	}
}
