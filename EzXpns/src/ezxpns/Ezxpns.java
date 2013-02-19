package ezxpns;

import ezxpns.data.*;
import ezxpns.util.Pair;
import ezxpns.GUI.*;
import java.awt.EventQueue;
import java.util.*;

import ezxpns.GUI.MainWindow;


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
					MainWindow window = new MainWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		reportGenerator = new ReportGenerator(this);
		targetManager = data.targetManager();
		targetManager.setDataProvider(this);
	}

	@Override
	public Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> getDataInDateRange(
			Date start, Date end) {
		// TODO Auto-generated method stub
		return null;
	}
}
