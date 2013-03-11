package ezxpns;

import ezxpns.data.*;
import ezxpns.data.records.Category;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordManager.RecordUpdateException;
import ezxpns.util.Pair;
import ezxpns.GUI.*;
import java.awt.EventQueue;
import java.util.*;

import ezxpns.GUI.CategoryHandlerInterface;
import ezxpns.GUI.MainWindow;
import ezxpns.GUI.MainScreen;
import ezxpns.GUI.RecordHandlerInterface;
import ezxpns.GUI.SearchHandlerInterface;
import ezxpns.GUI.SearchRequest;


public class Ezxpns implements
		ReportGenerator.DataProvider,
		TargetManager.DataProvider,
		CategoryHandlerInterface,
		RecordHandlerInterface,
		SearchHandlerInterface{
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
	
	public void addRecord(ExpenseRecord r) throws RecordUpdateException{
		data.expenses().addNewRecord(r);
	}
	
	public void addRecord(IncomeRecord r) throws RecordUpdateException{
		data.incomes().addNewRecord(r);
	}

	@Override
	public Vector<Record> search(SearchRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> getAllRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Record getRecord(int identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createRecord(Record newRecord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRecord(int identifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRecord(Record selectedRecord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyRecord(Record selectedRecord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Record> searchRecord(SearchRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createCategory(Category newCat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeCategory(int identifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeCategory(Category selectedCat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyCategory(Category selectedCat) {
		// TODO Auto-generated method stub
		return false;
	}
}
