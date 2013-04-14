package ezxpns.data.records;

import java.util.*;


/**
 * A special record manager for expense records, since we need to store
 * sums of amount for needs and wants
 * @author A0099621X
 */
public class ExpenseRecordManager extends RecordManager<ExpenseRecord> {
	
	// note that since needsum + wantsum = sum, we do not need to record want sum,
	// simply get it by subtraction
	private transient double needSum = 0, lastNeedSum = 0;
	
	/**
	 * Get sum of amounts of records in this month that are under needs 
	 */
	public double getNeedSum(){
		return needSum;
	}
	
	/**
	 * Get sum of amounts of records in the last month that are under needs 
	 */
	public double getLastNeedSum(){
		return lastNeedSum;
	}
	
	
	@Override
	protected void addSums(ExpenseRecord r){
		super.addSums(r);
		if(r.expenseType == ExpenseType.NEED){
			if(!r.date.before(startOfMonth)){
				needSum += r.amount;
			}else if (!r.date.before(startOfLastMonth)){
				lastNeedSum += r.amount;
			}
		}
	}
	
	@Override
	protected void removeSums(ExpenseRecord r){
		super.removeSums(r);
		if(r.expenseType == ExpenseType.NEED){
			if(!r.date.before(startOfMonth)){
				needSum -= r.amount;
			}else if (!r.date.before(startOfLastMonth)){
				lastNeedSum -= r.amount;
			}
		}
	}
	
}
