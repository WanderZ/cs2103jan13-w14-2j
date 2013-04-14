package ezxpns.data;

import java.util.Date;
import java.util.Vector;

import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.util.Pair;

/**
 * Generate SummaryDetails objects for 4 time frames:
 * today, this month, this year, all time
 * 
 * @author A0087091B
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
	
	/**
	 * Returns a SummaryDetails object based on which SummaryType
	 * is in the parameter
	 * @param myType
	 * @return
	 */
	public SummaryDetails getSummaryDetails(SummaryType myType){
		return myType.getSummaryDetails();
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

	/**
	 * Generate 4 SummaryDetails objects for the different time ranges
	 */
	public void markDataUpdated() {
		allTime = new SummaryDetails(data.getTotalIncome(), data.getTotalExpense(), SummaryType.ALLTIME);
		yearly = new SummaryDetails(data.getYearlyIncome(), data.getYearlyExpense(), SummaryType.YEAR);
		monthly = new SummaryDetails(data.getMonthlyIncome(), data.getMonthlyExpense(), SummaryType.MONTH);
		today = new SummaryDetails(data.getDailyIncome(), data.getDailyExpense(), SummaryType.TODAY);
	}
	
	/**
	 * enum class for the 4 different time ranges 
	 * @author A0087091B
	 *
	 */
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
		
		/**
		 * Returns the enum type's SummaryDetails
		 * @return
		 */
		public abstract SummaryDetails getSummaryDetails();
		
		/**
		 * Returns the string to be displayed on Main Window based
		 * on which time range is selected 
		 * @return
		 */
		public abstract String getName();
	}
}
