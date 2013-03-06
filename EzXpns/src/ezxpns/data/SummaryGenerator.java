package ezxpns.data;

import java.util.Date;
import java.util.Vector;

import ezxpns.data.ReportGenerator.DataProvider;
import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.util.Pair;

/**
 * Generate a Summary Object 
 * @author tingzhe
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
	
	public Summary generateSummary(){
		SummaryDetails myAllTime = computeSummaryDetails(??); // argument is Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>>
		SummaryDetails myYearly = computeSummaryDetails(??);
		SummaryDetails myMonthly = computeSummaryDetails(??);
		SummaryDetails myToday = computeSummaryDetails(??);
		
		return new Summary(myAllTime, myYearly, myMonthly, myToday);
	}

	private SummaryDetails computeSummaryDetails(Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> pair) {
		double expense = Record.sumAmount(pair.getLeft());
		double income = Record.sumAmount(pair.getRight());
		
		return new SummaryDetails(income, expense);
	}

	

}
