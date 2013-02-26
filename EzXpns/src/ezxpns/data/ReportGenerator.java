package ezxpns.data;

import ezxpns.data.records.ExpenseRecord;
import ezxpns.data.records.IncomeRecord;
import ezxpns.util.*;
import java.util.*;

/**
 * Generate a Report Class given a date range
 * @author tingzhe
 */

public class ReportGenerator {
	public static interface DataProvider{
		Pair<Vector<ExpenseRecord>, Vector<IncomeRecord> > getDataInDateRange(Date start, Date end);
	}
	
	private DataProvider data;
	private Pair<Vector<ExpenseRecord>, Vector<IncomeRecord> > records;
	private Vector<ExpenseRecord> expenseRecord;
	private Vector<IncomeRecord> incomeRecord;
	private Report myReport;
	private Vector<ReportCategory> expenseCategory;
	
	/**
	 * Hook up the data provider
	 * @param data
	 */
	public ReportGenerator(DataProvider data){
		this.data = data;
	}
	
	/**
	 * Get the required records within date range
	 * @param start
	 * @param end
	 * @throws Exception 
	 */
	public Report generateReport(Date start, Date end) throws Exception{
		// Exception to handle cases where end > start
		if (dateError(start, end))
			throw (new Exception("start date > end date"));
		records = data.getDataInDateRange(start, end);
		expenseRecord = records.getLeft();
		incomeRecord = records.getRight();
		myReport = new Report(start, end);
		processHeading(); // process heading
		processSectionExpense(); // process Section 1. Expense
		return myReport;
	}

	/**
	 * Calculate totalIncome, totalExpense, balance and deliver
	 * information to Report Object
	 */
	private void processHeading() {
		double totalIncome = 0;
		double totalExpense = 0;
		double balance = 0;
		for (int i = 0; i < incomeRecord.size(); i++){
			totalIncome += incomeRecord.get(i).getAmount();
		}
		for (int i = 0; i < expenseRecord.size(); i++){
			totalExpense += expenseRecord.get(i).getAmount();
		}
		balance = totalIncome - totalExpense;
		myReport.setHeading(totalIncome, totalExpense, balance);
	}
	
	/**
	 * Arrange expense into categories and store them in ReportCategory
	 * object. Also calculates Amount per Frequency. 
	 * Send Vector of ReportCategory to Report Object.
	 */
	private void processSectionExpense() {
		ReportCategory newCategory;
		
		// Store Expenses into Category
		for (int i = 0; i < expenseRecord.size(); i++){	
			// check category
			String categoryName = expenseRecord.get(i).getCategory().getName();
			int id = getCategoryIndex(categoryName);
			if (id == -1){
				newCategory = new ReportCategory(categoryName);
				expenseCategory.add(newCategory);
			}
			else
				newCategory = expenseCategory.get(id);
			newCategory.incrementFreq();
			newCategory.incrementAmount(expenseRecord.get(i).getAmount());		
		} //for
		
		// Calculates Amount per Frequency
		double totalExpense = myReport.getTotalExpense();
		for (int i = 0; i < expenseCategory.size(); i++){
			expenseCategory.get(i).setPercentage(expenseCategory.get(i).getAmount()/
					totalExpense * 100);
			expenseCategory.get(i).calAmtPerFreq();
		}
		// Sort expenseCategory in decending order (percentage)
		Collections.sort(expenseCategory);
		myReport.setSectionExpense(expenseCategory);
	}
	
	/**
	 * Check if expenseRecord contains a specific category, and return 
	 * expenseRecord's array index
	 * @param categoryName
	 * @return array index of expenseRecord if exist, else return -1
	 */
	private int getCategoryIndex(String categoryName){
		if (expenseRecord.size() == 0)
			return -1;
		for (int i = 0; i < expenseRecord.size(); i++){
			if (expenseRecord.get(i).getCategory().getName().equals(categoryName))
				return i;
		}
		return -1;
	}
	
	/**
	 * Check if start date and end date are valid
	 * (start date <= end date)
	 * @param start
	 * @param end
	 * @return true if error, false if OK
	 */
	private boolean dateError(Date start, Date end){
		if (start.getTime() > end.getTime())
			return true;
		return false;
	}
	
	
}
