package ezxpns.data;

import java.util.TreeMap;
import java.util.Vector;

import ezxpns.data.TargetManager.DataProvider;
import ezxpns.data.records.ExpenseType;

/**
 * This class generates target ratios for needs,wants and savings 
 * @author Suzzie
 *
 */

public class TargetGenerator extends Storable{
	
	public static interface DataProvider{
		double getMonthlyExpense(ExpenseType type);
	}
	
	private transient DataProvider data;
	private transient boolean dataUpdated = true;
	
	/**
	 *This returns a vector of percentages (needs, wants, savings) 
	 * @return
	 */
	public Vector<Integer> getPercentages(){
		Vector<Integer> triple = new Vector<Integer>();
		triple.add(50);
		triple.add(30);
		triple.add(20);
		return triple;		
	}
	
	
	
	
	
	

}
