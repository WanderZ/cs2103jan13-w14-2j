package ezxpns.data;

import java.util.*;

/**
 * @author yyjhao
 * A wrapper containing all data
 * Everything here is handeld by StorageManager to save to file
 * Remember to add transient if the data is not meant to be persistent
 * with some helper functions to query both types
 * TODO: functions to query both types and combine the results
 * 
 */
public class DataManager {
	private RecordManager<ExpenseRecord> _expenses = new RecordManager<ExpenseRecord>();
	private RecordManager<IncomeRecord> _incomes = new RecordManager<IncomeRecord>();
	
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
	
	public boolean isUpdated(){
		return _expenses.isUpdated() || _incomes.isUpdated();
	}
	
	/**
	 * A function to be called after objects are saved
	 */
	public void saved(){
		_expenses.saved();
		_incomes.saved();
	}
}
