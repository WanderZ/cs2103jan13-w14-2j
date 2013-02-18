package ezxpns.data;

import java.util.*;

/**
 * @author yyjhao
 * A wrapper containing both types of records
 * with some helper functions to query both types
 * TODO: functions to query both types and combine the results
 * 
 */
public class DataManager {
	private RecordManager<ExpenseRecord> _expenses = new RecordManager<ExpenseRecord>();
	private RecordManager<IncomeRecord> _incomes = new RecordManager<IncomeRecord>();
	
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
