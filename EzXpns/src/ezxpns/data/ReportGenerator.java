package ezxpns.data;

import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.data.records.Record;
import ezxpns.util.*;
import java.util.*;

/**
 * Generate a Report Object given a date range
 * @author tingzhe
 * 
 */

public class ReportGenerator {
	public static interface DataProvider {
		/**
		 * Returns a Pair of ExpenseRecord and IncomeRecord vectors within
		 * the start date and end date
		 * @param start
		 * @param end
		 * @return
		 */
		Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> getDataInDateRange(
				Date start, Date end);
	}

	private DataProvider data;
	private Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> records;
	private Vector<ExpenseRecord> expenseRecord;
	private Vector<IncomeRecord> incomeRecord;
	private Report myReport;
	private Vector<ReportCategory> expenseCategory = new Vector<ReportCategory>();
	static int INVALID = -1;

	/**
	 * Hook up the data provider
	 * 
	 * @param data
	 */
	public ReportGenerator(DataProvider data) {
		this.data = data;
	}

	/**
	 * Get the required records within date range Returns Report object.
	 * 
	 * @param start
	 * @param end
	 * @throws Exception
	 */
	public Report generateReport(Date start, Date end) throws Exception {
		// Exception to handle cases where end > start
		if (dateError(start, end))
			throw (new DateOrderException());

		records = getRecords(start, end);
		seperatePair(records);
		return writeReport(start, end);
	}

	private Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> getRecords(
			Date start, Date end) {
		return data.getDataInDateRange(start, end);
	}

	/**
	 * Separate Pair object into Vector<ExpenseRecord> and Vector<IncomeRecord>
	 * @param pair
	 */
	private void seperatePair(
			Pair<Vector<ExpenseRecord>, Vector<IncomeRecord>> pair) {
		expenseRecord = pair.getLeft();
		incomeRecord = pair.getRight();
	}

	/**
	 * Create a new Report object and fill it with new information
	 * @param start
	 * @param end
	 * @return
	 */
	private Report writeReport(Date start, Date end) {
		myReport = new Report(start, end);
		processHeading(); // process heading
		processSectionExpense(); // process Section 1. Expense
		return myReport;
	}

	/**
	 * Calculate totalIncome, totalExpense, balance and deliver information to
	 * Report Object
	 */
	private void processHeading() {
		double totalIncome = calTotalIncome();
		double totalExpense = calTotalExpense();
		double balance = totalIncome - totalExpense;
		int numRecords = expenseRecord.size() + incomeRecord.size();
		myReport.setHeading(totalIncome, totalExpense, balance, numRecords);
	}

	/** 
	 * Calculate sum of income from list of IncomeRecord
	 * @return
	 */
	private double calTotalIncome() {
		return Record.sumAmount(incomeRecord);
	}

	/**
	 * Calculate sum of expense from list of ExpenseRecord
	 * @return
	 */
	private double calTotalExpense() {
		return Record.sumAmount(expenseRecord);
	}

	/**
	 * Arrange expense into categories and store them in ReportCategory object.
	 * Also calculates Amount per Frequency. Send Vector of ReportCategory to
	 * Report Object.
	 */
	private void processSectionExpense() {
		populateExpenseCategory();
		calAmountPerFreq();
		
		// Sort expenseCategory in decending order (percentage)
		Collections.sort(expenseCategory);
		myReport.setSectionExpense(expenseCategory);
	}

	/**
	 * Populate a vector of ReportCategory
	 */
	private void populateExpenseCategory() {
		ReportCategory newCategory;
		// Store Expenses into Category
		for (int i = 0; i < expenseRecord.size(); i++) {
			// check category
			String categoryName = expenseRecord.get(i).getCategory().getName();
			int id = getCategoryIndex(categoryName);
			if (id == INVALID) {
				newCategory = new ReportCategory(categoryName);
				expenseCategory.add(newCategory);
			} else
				newCategory = expenseCategory.get(id);
			newCategory.incrementFreq();
			newCategory.incrementAmount(expenseRecord.get(i).getAmount());
		} // for
	}

	/**
	 * Calculate amount per frequency ratio for an expense category
	 */
	private void calAmountPerFreq() {
		// Calculates Amount per Frequency
		double totalExpense = myReport.getTotalExpense();
		for (int i = 0; i < expenseCategory.size(); i++) {
			expenseCategory.get(i).setPercentage(
					expenseCategory.get(i).getAmount() / totalExpense * 100);
			expenseCategory.get(i).calAmtPerFreq();
		}
	}

	/**
	 * Check if expenseRecord contains a specific category, and return
	 * expenseRecord's array index
	 * 
	 * @param categoryName
	 * @return array index of expenseRecord if exist, else return -1
	 */
	private int getCategoryIndex(String categoryName) {
		if (expenseCategory.size() == 0)
			return INVALID;
		for (int i = 0; i < expenseCategory.size(); i++) {
			if (expenseCategory.get(i).getCategory()
					.equals(categoryName))
				return i;
		}
		return INVALID;
	}

	/**
	 * Check if start date and end date are valid (start date <= end date)
	 * 
	 * @param start
	 * @param end
	 * @return true if error, false if OK
	 */
	private boolean dateError(Date start, Date end) {
		if (start.getTime() > end.getTime())
			return true;
		return false;
	}
	
	/**
	 * Exception Class when "start date" > "end date"
	 * @author yan
	 *
	 */
	public class DateOrderException extends Exception{
		public DateOrderException() { super(); }
	}

}
