package ezxpns.data;

import java.util.*;

import ezxpns.data.records.Category;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.data.records.RecordManager;

/**
 * @author yyjhao
 * A wrapper containing all data
 * with some helper functions to query both types
 * Everything here is handled by StorageManager to save to file
 * Remember to add transient if the data is not meant to be persistent
 * 
 */
public class DataManager {
	private static class CombinedRecordsQueryHandler implements RecordQueryHandler<Record>{
		private RecordManager<IncomeRecord> incomes;
		private RecordManager<ExpenseRecord> expenses;
		public CombinedRecordsQueryHandler(
				RecordManager<IncomeRecord> _incomes,
				RecordManager<ExpenseRecord> _expenses) {
			incomes = _incomes;
			expenses = _expenses;
		}

		public Vector<Record> getRecordsBy(String name, int max) {
			Vector<Record> rs = new Vector<Record>();
			rs.addAll(incomes.getRecordsBy(name, max));
			rs.addAll(expenses.getRecordsBy(name, max));
			Collections.sort(rs);
			if(max != -1){
				for(int i = rs.size() - 1; i >= max; i--){
					rs.remove(i);
				}
			}
			return rs;
		}

		public Vector<Record> getRecordsBy(Category category, int max) {
			Vector<Record> rs = new Vector<Record>();
			rs.addAll(incomes.getRecordsBy(category, max));
			rs.addAll(expenses.getRecordsBy(category, max));
			Collections.sort(rs);
			if(max != -1){
				for(int i = rs.size() - 1; i >= max; i--){
					rs.remove(i);
				}
			}
			return rs;
		}

		public Vector<Record> getRecordsBy(Date start, Date end, int max,
				boolean reverse) {
			Vector<Record> rs = new Vector<Record>();
			rs.addAll(incomes.getRecordsBy(start, end, max, reverse));
			rs.addAll(expenses.getRecordsBy(start, end, max, reverse));
			Collections.sort(rs);
			if(max != -1){
				for(int i = rs.size() - 1; i >= max; i--){
					rs.remove(i);
				}
			}
			return rs;
		}
		
	}
	
	private RecordManager<ExpenseRecord> _expenses = new RecordManager<ExpenseRecord>();
	private RecordManager<IncomeRecord> _incomes = new RecordManager<IncomeRecord>();
	private transient CombinedRecordsQueryHandler _combined;
	
	private TargetManager _targetManager = new TargetManager();
	
	public TargetManager targetManager(){
		return _targetManager;
	}
	
	public RecordManager<ExpenseRecord> expenses(){
		return _expenses;
	}
	
	public RecordManager<IncomeRecord> incomes(){
		return _incomes;
	}
	
	/*
	 * A query handler that returns both expense and income records
	 */
	public CombinedRecordsQueryHandler combined(){
		return _combined;
	}
	
	public boolean isUpdated(){
		return _expenses.isUpdated() || _incomes.isUpdated()
				|| _targetManager.isUpdated();
	}
	
	public void afterDeserialize(){
		_incomes.afterDeserialize();
		_expenses.afterDeserialize();
		_combined = new CombinedRecordsQueryHandler(_incomes, _expenses);
	}
	/**
	 * A function to be called after objects are saved
	 */
	public void saved(){
		_expenses.saved();
		_incomes.saved();
		_targetManager.saved();
	}
}
