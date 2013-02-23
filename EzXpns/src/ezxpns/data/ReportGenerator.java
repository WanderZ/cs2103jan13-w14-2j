package ezxpns.data;

import ezxpns.util.*;
import java.util.*;

/**
 * @author tingzhe
 * 
 */

public class ReportGenerator {
	public static interface DataProvider{
		Pair<Vector<ExpenseRecord>, Vector<IncomeRecord> > getDataInDateRange(Date start, Date end);
	}
	
	private DataProvider data;
	private Pair<Vector<ExpenseRecord>, Vector<IncomeRecord> > records;
	private Vector<ExpenseRecord> expenseRecord;
	private Vector<IncomeRecord> incomeRecord;
	private Date start;
	private Date end;
	private Report myReport;
	private Vector<ReportCategory> expenseCategory;
	// number of days?
	
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
		return data.getDataInDateRange(start, end).toString() + "test";
	}
	
	/**
	 * Get the required records within date range
	 * @param start
	 * @param end
	 */
	public Report generateReport(Date start, Date end){
		// Exception to handle cases where end > start?
		this.start = start;
		this.end = end;
		records = data.getDataInDateRange(start, end);
		expenseRecord = records.getLeft();
		incomeRecord = records.getRight();
		myReport = new Report(start, end);
		processHeading(); // process heading
		processSecExpense(); // process Section 1. Expense
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
			totalIncome += incomeRecord.get(i).amount;
		}
		for (int i = 0; i < expenseRecord.size(); i++){
			totalExpense += expenseRecord.get(i).amount;
		}
		balance = totalIncome - totalExpense;
		myReport.setHeading(totalIncome, totalExpense, balance);
	}
	
	/**
	 * Arrange expense into categories and store them in ReportCategory
	 * object. Send Vector of ReportCategory to Report Object.
	 */
	private void processSecExpense() {
		for (int i = 0; i < expenseRecord.size(); i++){
			// check category
			String categoryName = expenseRecord.get(i).category.getName();
			int id = checkCategoryExist(categoryName);
			if (id == 0)
				//create new RecordCategory
		}
		
	}
	
	/**
	 * Check if expenseRecord contains a specific category, and return 
	 * expenseRecord's array index
	 * @param categoryName
	 * @return array index of expenseRecord if exist, else return -1
	 */
	private int checkCategoryExist(String categoryName){
		if (expenseRecord.size() == 0)
			return -1;
		for (int i = 0; i < expenseRecord.size(); i++){
			if (expenseRecord.get(i).category.getName().equals(categoryName))
				return i;
		}
		return -1;
	}
	
	
}
