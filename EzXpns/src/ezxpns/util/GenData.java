package ezxpns.util;

import java.util.*;

import ezxpns.*;
import ezxpns.data.*;
import ezxpns.data.records.*;
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
		Random r = new Random();
		long now = (new Date()).getTime();
		long earliest = now - 3600l * 24 * 1000 * 700;
		
		Category cat;
		PaymentMethod pay;
		for(int i = 0; i < catToGen; i++){
			cat = new Category(Long.toString((long)(r.nextDouble()*(1l<<60)), 36));
			eh.getDataMng().expenses().addNewCategory(cat);
			
			cat = new Category(Long.toString((long)(r.nextDouble()*(1l<<60)), 36));
			eh.getDataMng().incomes().addNewCategory(cat);
			
			pay = new PaymentMethod(Long.toString((long)(r.nextDouble()*(1l<<60)), 36));
			eh.getDataMng().expenses().addNewPaymentMethod(pay);
		}
		
		List<Category> exCat = eh.getDataMng().expenses().getAllCategories();
		List<Category> inCat = eh.getDataMng().incomes().getAllCategories();
		
		List<PaymentMethod> pays = eh.getDataMng().expenses().getAllPaymentMethod();
		
		int exCatl = exCat.size();
		int inCatl = inCat.size();
		int paysl = pays.size();
		
		for(int i = 0; i < recordToGen; i++){
			eh.createRecord(new ExpenseRecord((double)r.nextInt(maxAmount) / 100,
					Long.toString((long)(r.nextDouble()*(1l<<60)), 36),
					"nil", new Date((long)(r.nextDouble()*(now - earliest) + earliest)),
					exCat.get(r.nextInt(exCatl)), ExpenseType.NEED,
					pays.get(r.nextInt(paysl))), false, false);
			
			eh.createRecord(new IncomeRecord((double)r.nextInt(maxAmount) / 100,
					Long.toString((long)(r.nextDouble()*(1l<<60)), 36),
					"nil", new Date((long)(r.nextDouble()*(now - earliest) + earliest)),
					inCat.get(r.nextInt(inCatl))), false);
		}
		
		eh.getStore().save();
		System.exit(0);

	}

}
