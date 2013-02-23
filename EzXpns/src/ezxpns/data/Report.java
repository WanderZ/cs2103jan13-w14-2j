package ezxpns.data;

import java.util.Date;
import java.util.Vector;


/**
 * A class to hold report information
 * @author tingzhe
 *
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
	private double aveExpense; //per day
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
}
