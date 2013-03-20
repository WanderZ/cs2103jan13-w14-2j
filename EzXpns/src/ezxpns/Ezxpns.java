package ezxpns;

import ezxpns.data.*;
import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.PaymentMethod;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordHandler;
import ezxpns.data.records.SearchHandler;
import ezxpns.data.records.SearchRequest;
import ezxpns.data.records.RecordManager.RecordUpdateException;
import ezxpns.data.records.SearchRequest.RecordType;
import ezxpns.util.Pair;
import ezxpns.GUI.*;

import java.awt.EventQueue;
import java.util.*;



/**
 * Main class that links up various components
 * @author yyjhao
 */
public class Ezxpns implements
		RecordHandler,
		CategoryHandler,
		SearchHandler{
	/**
	 * The only storage manager
	 */
	private StorageManager store;
	
	/**
	 * @return the storage manager
	 */
	public StorageManager getStore() {
		return store;
	}

	/**
	 * @return the data manager
	 */
	public DataManager getDataMng() {
		return data;
	}

	/**
	 * @return the target manager
	 */
	public TargetManager getTargetManager() {
		return targetManager;
	}
	/**
	 *  The data manager
	 */
	private DataManager data;
	/**
	 * The report Generator
	 */
	private ReportGenerator reportGenerator;
	/**
	 * The target manager
	 */
	private TargetManager targetManager;
	/**
	 * The summary generator
	 */
	private SummaryGenerator summaryGenerator;
	
	public Ezxpns(){
		try{
			store = new StorageManager("data.json");
			store.read();
			data = store.getDataManager();
		} catch(Exception e){
			System.out.println(e.toString());
			System.exit(1);
		}
		reportGenerator = new ReportGenerator(data);
		summaryGenerator = new SummaryGenerator(data);
		targetManager = data.targetManager();
		final UIControl main  = new UIControl( 	this, 				// SearchHandler
												this, 				// RecordHandler
												data.incomes(), 	// IncomeCategoryHandler
												this, 				// ExpenseCategoryHandler
												data.expenses(),	// PaymentMethodHandler
												targetManager, 		// Target Manager
												reportGenerator, 	// Report Generator
												summaryGenerator); 	// Summary Generator
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main.showHomeScreen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	@Override
	public Vector<Record> search(SearchRequest req) {
		RecordQueryHandler tofind = data.combined();
		switch(req.getType()) {
			case EXPENSE:
				tofind = data.expenses();
			break;
			case INCOME:
				tofind = data.incomes();
			break;
		}
		
		if(req.getName() != null){
			return tofind.getRecordsBy(req.getName(), -1);
		}else if(req.getDateRange() != null){
			Date start = req.getDateRange().getLeft(),
				 end = req.getDateRange().getRight();
			return tofind.getRecordsBy(start, end, -1, false);
		}else if(req.getCategory() != null){
//			return tofind.getRecordsBy(req.getCategory(), -1);
			return tofind.getRecordsByCategory(req.getCategory().getName());
		}else{
			return null;
		}
	}


	@Override
	public boolean createRecord(IncomeRecord newRecord) {
		try {
			data.incomes().addNewRecord(newRecord);
			summaryGenerator.markDataUpdated();
		} catch (RecordUpdateException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean createRecord(ExpenseRecord newRecord) {
		try {
			data.expenses().addNewRecord(newRecord);
			targetManager.markDataUpdated();
			summaryGenerator.markDataUpdated();
		} catch (RecordUpdateException e) {
			return false;
		}
		store.save();
		return true;
	}
	
	@Override
	public List<Record> getRecords(int n) {
		return data.combined().getRecordsBy(new Date(0), new Date(), n, false);
	}
	@Override
	public Record getRecord(long identifier) {
		if(data.expenses().getRecordBy(identifier) == null){
			return data.incomes().getRecordBy(identifier);
		}else{
			return data.expenses().getRecordBy(identifier);
		}
	}
	@Override
	public boolean removeRecord(long identifier) {
		if(data.expenses().getRecordBy(identifier) != null){
			try {
				data.expenses().removeRecord(identifier);
				targetManager.markDataUpdated();
				summaryGenerator.markDataUpdated();
			} catch (RecordUpdateException e) {
				return false;
			}
			return true;
		}else{
			try {
				data.incomes().removeRecord(identifier);
				summaryGenerator.markDataUpdated();
			} catch (RecordUpdateException e) {
				return false;
			}
			return true;
		}
	}
	@Override
	public boolean modifyRecord(long id, ExpenseRecord r) {
		try {
			data.expenses().updateRecord(id, r);
			targetManager.markDataUpdated();
			summaryGenerator.markDataUpdated();
		} catch (RecordUpdateException e) {
			return false;
		}
		return true;
	}
	@Override
	public boolean modifyRecord(long id, IncomeRecord r) {
		try {
			data.incomes().updateRecord(id, r);
			summaryGenerator.markDataUpdated();
		} catch (RecordUpdateException e) {
			return false;
		}
		return true;
	}
	@Override
	public ExpenseRecord lastExpenseRecord(String name) {
		List<ExpenseRecord> rs = data.expenses().getRecordsBy(name, 1);
		if(rs.size() > 0){
			return rs.get(0);
		}else{
			return null;
		}
	}
	@Override
	public IncomeRecord lastIncomeRecord(String name) {
		List<IncomeRecord> rs = data.incomes().getRecordsBy(name, 1);
		if(rs.size() > 0){
			return rs.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<Category> getAllCategories() {
		return data.expenses().getAllCategories();
	}

	@Override
	public Category addNewCategory(Category newCat) {
		return data.expenses().addNewCategory(newCat);
	}

	@Override
	public Category addNewCategory(String catName) {
		return data.expenses().addNewCategory(catName);
	}

	@Override
	public boolean removeCategory(long identifier) {
		if(data.expenses().removeCategory(identifier)){
			data.targetManager().removeCategoryTarget(identifier);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Category updateCategory(long identifier, Category selectedCat) {
		return data.expenses().updateCategory(identifier, selectedCat);
	}
}
