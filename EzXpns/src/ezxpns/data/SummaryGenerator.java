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
	private SummaryDetails allTime, yearly, monthly, today;

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
	}

	// Generate a Summary object
	public void update() {
		allTime = new SummaryDetails(data.getTotalIncome(), data.getTotalExpense());
		yearly = new SummaryDetails(data.getYearlyIncome(), data.getYearlyExpense());
		monthly = new SummaryDetails(data.getMonthlyIncome(), data.getMonthlyExpense());
		today = new SummaryDetails(data.getDailyIncome(), data.getDailyExpense());
	}
}
