package ezxpns.data;

import java.util.Date;
import java.util.Vector;


/**
 * A class to hold report information
 * @author tingzhe
 */

public class Report {
	
	// Heading
	private Date start;
	private Date end;
	private double totalIncome;
	private double totalExpense;
	private double balance;
	
	// 1. Expense
	private Vector<ReportCategory> ExpenseCategory;
	private double aveExpense; // Average Expense per day
	//highest expenditure?
	
	/**
	 * Constructor, set date range
	 * @param start
	 * @param end
	 */
	public Report(Date start, Date end){
		this.start = start;
		this.end = end;
	}

	/**
	 * Set 'Heading' of the report, namely: total income, total expense,
	 * and balance.
	 * @param income
	 * @param expense
	 * @param balance
	 */
	public void setHeading(double income, double expense,
			double balance) {
		totalIncome = income;
		totalExpense = expense;
		this.balance = balance;
	}

	/**
	 * Set 'Expense' of the report, namely: Expense table, and
	 * average expense per day
	 * @param expenseCategory
	 */
	public void setSectionExpense(Vector<ReportCategory> expenseCategory) {
		ExpenseCategory = expenseCategory;
		int numDays = (int) (end.getTime() - start.getTime()/(1000 * 60 * 60 * 24));
		aveExpense = totalExpense/numDays;
	}
	
	// Getters
	public Date getStart(){
		return start;
	}
	
	public Date getEnd(){
		return end;
	}
	
	public double getTotalIncome(){
		return totalIncome;
	}
	
	public double getTotalExpense() {
		return totalExpense;
	}
	
	public double getBalance(){
		return balance;
	}
	
	public Vector<ReportCategory> getExpenseCategory(){
		return ExpenseCategory;
	}
	
	public double getAveExpense(){
		return aveExpense;
	}
}
