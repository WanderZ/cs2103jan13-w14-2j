package ezxpns;

import ezxpns.data.*;
import ezxpns.data.records.Category;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.PaymentMethod;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordManager.RecordUpdateException;
import ezxpns.util.Pair;
import ezxpns.GUI.*;
import java.awt.EventQueue;
import java.util.*;

import ezxpns.GUI.CategoryHandlerInterface;
import ezxpns.GUI.RecordHandlerInterface;
import ezxpns.GUI.SearchHandlerInterface;
import ezxpns.GUI.SearchRequest;


public class Ezxpns implements
		CategoryHandlerInterface,
		RecordHandlerInterface,
		SearchHandlerInterface,
		PaymentMethodHandlerInterface {
	private StorageManager store;
	private DataManager data;
	private ReportGenerator reportGenerator;
	private TargetManager targetManager;
	private SummaryGenerator summaryGenerator;
	
	public Ezxpns(){
		final UIControl<Ezxpns> main  = new UIControl(this);
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
					
					main.showHomeScreen();
					main.showRecWin();
										
					/* Test Report Frame*/
					// ReportFrame tzTestFrame = new ReportFrame();
					// tzTestFrame.showScreen();
					
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		reportGenerator = new ReportGenerator(data);
		summaryGenerator = new SummaryGenerator(data);
		targetManager = data.targetManager();
	}
	public void addTarget(){
		
	}
	
	public void applicationQuitting() {
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
	public boolean createRecord(Record newRecord) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean removeRecord(Record selectedRecord) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		List cat = Arrays.asList(new String[] {"Food", "Transport", "Entertainment", "School", "Books"});
		return cat;
	}

	@Override
	public boolean addNewCategory(Category newCat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeCategory(Category selectedCat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateCategory(Category selectedCat) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public List<Record> getRecords(int n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Record getRecord(long identifier) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean removeRecord(long identifier) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean modifyRecord(ExpenseRecord selectedRecord) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean modifyRecord(IncomeRecord selectedRecord) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public ExpenseRecord lastExpenseRecord(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IncomeRecord lastIncomeRecord(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean removeCategory(long identifier) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public List<PaymentMethod> getAllPaymentMethod() {
		// TODO Auto-generated method stub
		List pay = Arrays.asList(new String[]{"Cash", "iBank", "Cheque"});
		return pay;
	}
	@Override
	public boolean addNewPaymentMethod(PaymentMethod paymentRef) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean removePaymentMethod(PaymentMethod paymentRef) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean updatePaymentMethod(PaymentMethod paymentRef) {
		// TODO Auto-generated method stub
		return false;
	}
}
