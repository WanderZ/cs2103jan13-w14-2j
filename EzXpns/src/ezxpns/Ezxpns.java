package ezxpns;

import ezxpns.data.*;
import ezxpns.data.records.Category;
import ezxpns.data.records.CategoryHandler;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.ExpenseType;
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
import ezxpns.GUI.Calculator.EvaluationException;

import java.awt.EventQueue;
import java.util.*;

import java.util.concurrent.*;

/**
 * Main class that links up various components
 * @author yyjhao
 */
public class Ezxpns implements
		RecordHandler,
		CategoryHandler<ExpenseRecord>,
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
												summaryGenerator,
												data.nwsGen());	// Summary Generator  	
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
		RecordQueryHandler<Record> tofind = data.combined();
		
		Vector<Record> recs;
		if(req.getName() != null){
			recs = tofind.getRecordsBy(req.getName(), -1);
		}else if(req.getDateRange() != null){
			Date start = req.getDateRange().getLeft(),
				 end = req.getDateRange().getRight();
			recs = tofind.getRecordsBy(start, end, -1, true);
		}else if(req.getCategory() != null){
			recs = tofind.getRecordsBy(req.getCategory(), -1);
		}else{
			return null;
		}
		if(req.isMultiple()){
			Vector<Record> re = new Vector<Record>();
			for(Record r : recs){
				if(req.match(r)){
					re.add(r);
				}
			}
			Collections.sort(re);
			return re;
		}else{
			return recs;
		}
	}


	@Override
	public IncomeRecord createRecord(IncomeRecord r, boolean newCat) {
		if(newCat){
			Category cat = data.incomes().addNewCategory(r.getCategory());
			if(cat == null){
				return null;
			}
			r = new IncomeRecord(r.getAmount(), r.getName(), r.getRemark(), r.getDate(), cat);
		}
		IncomeRecord nr = null;
		try {
			nr = data.incomes().addNewRecord(r);
			summaryGenerator.markDataUpdated();
		} catch (RecordUpdateException e) {
			return null;
		}
		return nr;
	}
	
	@Override
	public ExpenseRecord createRecord(ExpenseRecord r, boolean newCat, boolean newPay) {
		if(newCat){
			Category cat = data.expenses().addNewCategory(r.getCategory());
			if(cat == null){
				return null;
			}
			r = new ExpenseRecord(r.getAmount(), r.getName(), r.getRemark(), r.getDate(), cat,
					r.getExpenseType(), r.getPaymentMethod());
		}
		if(newPay){
			PaymentMethod pay = data.expenses().addNewPaymentMethod(r.getPaymentMethod());
			if(pay == null){
				return null;
			}
			r = new ExpenseRecord(r.getAmount(), r.getName(), r.getRemark(), r.getDate(), r.getCategory(),
					r.getExpenseType(), pay);
		}
		ExpenseRecord nr = null;
		try {
			nr = data.expenses().addNewRecord(r);
			targetManager.markDataUpdated();
			summaryGenerator.markDataUpdated();
		} catch (RecordUpdateException e) {
			return null;
		}
		return nr;
	}
	
	@Override
	public List<Record> getRecords(int n) {
		return data.combined().getRecordsBy(new Date(0), new Date(), n, true);
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
	public boolean modifyRecord(long id, ExpenseRecord r, boolean newCat, boolean newPay) {
		if(newCat){
			Category cat = data.expenses().addNewCategory(r.getCategory());
			if(cat == null){
				return false;
			}
			r = new ExpenseRecord(r.getAmount(), r.getName(), r.getRemark(), r.getDate(), cat,
					r.getExpenseType(), r.getPaymentMethod());
		}
		if(newPay){
			PaymentMethod pay = data.expenses().addNewPaymentMethod(r.getPaymentMethod());
			if(pay == null){
				return false;
			}
			r = new ExpenseRecord(r.getAmount(), r.getName(), r.getRemark(), r.getDate(), r.getCategory(),
					r.getExpenseType(), pay);
		}
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
	public boolean modifyRecord(long id, IncomeRecord r, boolean newCat) {
		if(newCat){
			Category cat = data.incomes().addNewCategory(r.getCategory());
			if(cat == null){
				return false;
			}
			r = new IncomeRecord(r.getAmount(), r.getName(), r.getRemark(), r.getDate(), cat);
		}
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

	@Override
	public boolean containsCategoryName(String name) {
		return data.expenses().containsCategoryName(name);
	}

	@Override
	public String validateCategoryName(String name) {
		return data.expenses().validateCategoryName(name);
	}

	@Override
	public Category getCategory(long id) {
		return data.expenses().getCategory(id);
	}

	@Override
	public Vector<ExpenseRecord> getRecordsBy(Category category, int max) {
		return data.expenses().getRecordsBy(category, -1);
	}

	@Override
	public boolean addToCategory(List<ExpenseRecord> records, Category cat) {
		return data.expenses().addToCategory(records, cat);
	}

	@Override
	public Vector<Record> search(String partialMatch) {
		Vector<Record> rs = data.combined().getRecordsWithNamePrefix(partialMatch);
		Vector<Category> cats = data.incomes().getCategoryWithNamePrefix(partialMatch);
		for(Category cat : cats){
			rs.addAll(data.incomes().getRecordsBy(cat, -1));
		}
		cats = data.expenses().getCategoryWithNamePrefix(partialMatch);
		for(Category cat : cats){
			rs.addAll(data.expenses().getRecordsBy(cat, -1));
		}
		Collections.sort(rs);
		return rs;
	}

	@Override
	public Vector<Category> getCategoryWithNamePrefix(String prefix) {
		return data.expenses().getCategoryWithNamePrefix(prefix);
	}
}
