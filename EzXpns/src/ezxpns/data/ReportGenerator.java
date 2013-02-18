package ezxpns.data;

import ezxpns.util.*;
import java.util.*;

public class ReportGenerator {
	public static interface DataProvider{
		Pair<Vector<ExpenseRecord>, Vector<IncomeRecord> > getDataInDateRange(Date start, Date end);
	}
	private DataProvider data;
	
	/**
	 * Hook up the data provider
	 * @param data
	 */
	public ReportGenerator(DataProvider data){
		this.data = data;
	}
	
	// maybe other data structures
	// if we need graphs/charts
	public String getReport(Date start, Date end){
		// process
		return data.getDataInDateRange(start, end).toString();
	}
}
