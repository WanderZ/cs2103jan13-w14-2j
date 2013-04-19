package ezxpns.data;

import java.util.Date;
import java.util.Vector;

import ezxpns.data.ReportGenerator.DataProvider;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.util.Pair;

/**
 * Generate a Summary Object 
 * @author yan
 * 
 */

public class SummaryGenerator {
	
	public static interface DataProvider {
		Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> getDataInDateRange(
				Date start, Date end);
	}
	
	private DataProvider data;

	
	/**
	 * Hook up the data provider
	 * 
	 * @param data
	 */
	public SummaryGenerator(DataProvider data) {
		this.data = data;
	}

}
