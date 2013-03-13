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
import ezxpns.GUI.SearchRequest.RecordType;


public class Ezxpns implements
		CategoryHandlerInterface,
		RecordHandlerInterface,
		PaymentMethodHandlerInterface,
		SearchHandlerInterface{
	private StorageManager store;
	private DataManager data;
	private ReportGenerator reportGenerator;
	private TargetManager targetManager;
	private SummaryGenerator summaryGenerator;
	
	public Ezxpns(){
		final UIControl main  = new UIControl(this, this, this, this, this, targetManager, reportGenerator, summaryGenerator);
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
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		reportGenerator = new ReportGenerator(data);
		summaryGenerator = new SummaryGenerator(data);
		targetManager = data.targetManager();
	}
	
	public void applicationQuitting(){
		store.save();
	}

	@Override
	public Vector<Record> search(SearchRequest req) {
		RecordQueryHandler tofind = data.combined();
		switch(req.getType()){
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
			return tofind.getRecordsBy(req.getCategory(), -1);
		}else{
			return null;
		}
	}


	@Override
	public boolean createRecord(IncomeRecord newRecord) {
		try {
			data.incomes().addNewRecord(newRecord);
		} catch (RecordUpdateException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean createRecord(ExpenseRecord newRecord) {
		try {
			data.expenses().addNewRecord(newRecord);
		} catch (RecordUpdateException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean removeRecord(Record r) {
		return removeRecord(r.getId());
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return null;
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
			} catch (RecordUpdateException e) {
				return false;
			}
			return true;
		}else{
			try {
				data.incomes().removeRecord(identifier);
			} catch (RecordUpdateException e) {
				return false;
			}
			return true;
		}
	}
	@Override
	public boolean modifyRecord(ExpenseRecord r) {
		try {
			data.expenses().updateRecord(r);
		} catch (RecordUpdateException e) {
			return false;
		}
		return true;
	}
	@Override
	public boolean modifyRecord(IncomeRecord r) {
		try {
			data.incomes().updateRecord(r);
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
	public boolean removeCategory(long identifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Category addNewCategory(String catName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PaymentMethod> getAllPaymentMethod() {
		// TODO Auto-generated method stub
		return null;
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
