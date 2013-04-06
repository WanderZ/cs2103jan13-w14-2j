package ezxpns.util;

import java.util.*;

import ezxpns.*;
import ezxpns.data.*;
import ezxpns.data.records.*;
import ezxpns.data.records.RecordManager.RecordUpdateException;

import java.math.*;

/**
 * Generate some random data for testing
 * strictly for testing purpose, may not be maintained
 * @author yyjhao
 */
public class GenData {

	private final static int recordToGen = 10;
	private final static int catToGen = 5;
	private final static int maxAmount = 100000;
	/**
	 * @param args
	 * Change mein to main to run this
	 */
	public static void mein(String[] args) {
		Ezxpns eh = new Ezxpns();
		
		ExpenseRecord coffee = eh.getDataMng().expenses().getRecordsBy("coffee", 1).get(0);
		ExpenseRecord bigmac = eh.getDataMng().expenses().getRecordsBy("Big Mac", 1).get(0);
		ExpenseRecord pineapple = eh.getDataMng().expenses().getRecordsBy("pineapple", 1).get(0);
		
		Calendar cal = Calendar.getInstance();
		for(int d = 0; d < 6; d++){
			cal.set(Calendar.DAY_OF_MONTH, d);
			try {
				if(Math.random() < 0.5)
					eh.getDataMng().expenses().addNewRecord(new ExpenseRecord(
							coffee.getAmount(),
							coffee.getName(), 
							coffee.getRemark(),
							cal.getTime(), 
							coffee.getCategory(), 
							coffee.getExpenseType(),
							coffee.getPaymentMethod()
							));
				if(Math.random() < 0.3)
					eh.getDataMng().expenses().addNewRecord(new ExpenseRecord(
	                        bigmac.getAmount(),
	                        bigmac.getName(), 
	                        bigmac.getRemark(),
	                        cal.getTime(), 
	                        bigmac.getCategory(), 
	                        bigmac.getExpenseType(),
	                        bigmac.getPaymentMethod()
	                        ));
				if(Math.random() < 0.4)
					eh.getDataMng().expenses().addNewRecord(new ExpenseRecord(
	                        pineapple.getAmount(),
	                        pineapple.getName(), 
	                        pineapple.getRemark(),
	                        cal.getTime(), 
	                        pineapple.getCategory(), 
	                        pineapple.getExpenseType(),
	                        pineapple.getPaymentMethod()
	                        ));
			} catch (RecordUpdateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		System.exit(0);

	}

}
