package ezxpns.data;

import java.util.Date;
import java.util.Vector;

import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.util.Pair;

/**
 * Generate a Summary Object
 * 
 * @author tingzhe
 * 
 */

public class SummaryGenerator {

	public static interface DataProvider {
		double getTotalExpense();
		double getTotalIncome();
		double getDailyExpense();
		double getDailyIncome();
		double getMonthlyExpense();
		double getMonthlyIncome();
		double getYearlyExpense();
		double getYearlyIncome();
	}

	private DataProvider data;
	private static SummaryDetails allTime;
	private static SummaryDetails yearly;
	private static SummaryDetails monthly;
	private static SummaryDetails today;
	
	public SummaryDetails getSummaryDetails(SummaryType myType){
		return myType.getSummaryDetails();
	}

	public SummaryDetails getAllTime() {
		return allTime;
	}

	public SummaryDetails getYearly() {
		return yearly;
	}

	public SummaryDetails getMonthly() {
		return monthly;
	}

	public SummaryDetails getToday() {
		return today;
	}

	/**
	 * Hook up the data provider
	 * 
	 * @param data
	 */
	public SummaryGenerator(DataProvider data) {
		this.data = data;
		this.markDataUpdated();
	}

	// Generate a SummaryDetails object
	public void markDataUpdated() {
		allTime = new SummaryDetails(data.getTotalIncome(), data.getTotalExpense(), SummaryType.ALLTIME);
		yearly = new SummaryDetails(data.getYearlyIncome(), data.getYearlyExpense(), SummaryType.YEAR);
		monthly = new SummaryDetails(data.getMonthlyIncome(), data.getMonthlyExpense(), SummaryType.MONTH);
		today = new SummaryDetails(data.getDailyIncome(), data.getDailyExpense(), SummaryType.TODAY);
	}
	
	public enum SummaryType{
		TODAY{
			public SummaryDetails getSummaryDetails(){
				return today;}

			public String getName() {
				return "Today";
			}
		},
		MONTH{
			public SummaryDetails getSummaryDetails(){
				return monthly;}

			public String getName() {
				return "This Month";
			}
		},
		YEAR{
			public SummaryDetails getSummaryDetails(){
				return yearly;}

			public String getName() {
				return "This Year";
			}
		},
		ALLTIME{
			public SummaryDetails getSummaryDetails(){
				return allTime;}

			public String getName() {
				return "All Time";
			}
		};
		public abstract SummaryDetails getSummaryDetails();
		public abstract String getName();
	}
}
