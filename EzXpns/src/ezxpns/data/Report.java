package ezxpns.data;

import java.util.Date;
import java.util.Vector;

/**
 * A class to hold report information
 * 
 * @author A0087091B
 */

public class Report {

	// Heading
	private int numRecords = 0;
	private Date start;
	private Date end;
	private double totalIncome = 0;
	private double totalExpense = 0;
	private double balance = 0;
	private double incomePercentage = 0;
	private double expensePercentage = 0;

	// 1. Expense
	private Vector<ReportCategory> ExpenseCategory;
	private double aveExpense; // Average Expense per day

	/**
	 * Constructor, set date range
	 * 
	 * @param start
	 * @param end
	 */
	public Report(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Set 'Heading' of the report, namely: total income, total expense, and
	 * balance.
	 * 
	 * @param income
	 * @param expense
	 * @param balance
	 * @param numRecords 
	 */
	public void setHeading(double income, double expense, double balance, int numRecords) {
		totalIncome = income;
		totalExpense = expense;
		this.balance = balance;
		this.numRecords = numRecords;
		double total = income + expense;
		if (total == 0){
			incomePercentage = 0;
			expensePercentage = 0;
		}
		else{
		incomePercentage = income/total*100;
		expensePercentage = expense/total*100;
		}
	}

	/**
	 * Set 'Expense' of the report, namely: Expense table, and average expense
	 * per day
	 * 
	 * @param expenseCategory
	 */
	public void setSectionExpense(Vector<ReportCategory> expenseCategory) {
		ExpenseCategory = expenseCategory;
		int numDays = (int) (end.getTime() - start.getTime()
				/ (1000 * 60 * 60 * 24));
		aveExpense = totalExpense / numDays;
	}

	/**
	 * Get start date
	 * 
	 * @return
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * Get end date
	 * 
	 * @return
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * Get total income for the period between start date and end date
	 * 
	 * @return
	 */
	public double getTotalIncome() {
		return totalIncome;
	}

	/**
	 * Get total expense for the period between start date and end date
	 * 
	 * @return
	 */
	public double getTotalExpense() {
		return totalExpense;
	}

	/**
	 * Get balance for the period between start date and end date
	 * 
	 * @return
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Get a vector of ReportCategory
	 * 
	 * @return
	 */
	public Vector<ReportCategory> getExpenseCategory() {
		return ExpenseCategory;
	}

	/**
	 * Get the average expense for the period between start date and end date
	 * 
	 * @return
	 */
	public double getAveExpense() {
		return aveExpense;
	}
	
	/**
	 * Get the percentage of income
	 * @return
	 */
	public double getIncomePercentage(){
		return incomePercentage;
	}
	
	/**
	 * Get the percentage of expense
	 * @return
	 */
	public double getExpensePercentage(){
		return expensePercentage;
	}
	
	/**
	 * Get the number of records found
	 * @return
	 */
	public int getNumRecords(){
		return numRecords;
	}
}

