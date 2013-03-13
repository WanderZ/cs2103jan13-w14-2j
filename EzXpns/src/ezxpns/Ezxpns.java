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
		RecordHandlerInterface,
		SearchHandlerInterface{
	private StorageManager store;
	private DataManager data;
	private ReportGenerator reportGenerator;
	private TargetManager targetManager;
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
												data.incomes(), 	// ExpenseCategoryHandler
												data.expenses(),	// PaymentMethodHandler
												targetManager, 		// Target Manager
												reportGenerator, 	// Report Generator
												summaryGenerator); 	// Summary Generator
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
			targetManager.markDataUpdated();
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
	public boolean modifyRecord(ExpenseRecord r) {
		try {
			data.expenses().updateRecord(r);
			targetManager.markDataUpdated();
			summaryGenerator.markDataUpdated();
		} catch (RecordUpdateException e) {
			return false;
		}
		return true;
	}
	@Override
	public boolean modifyRecord(IncomeRecord r) {
		try {
			data.incomes().updateRecord(r);
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
}
