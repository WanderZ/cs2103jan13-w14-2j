package ezxpns.data;

import ezxpns.data.SummaryGenerator.SummaryType;

/**
 * Data structure containing information on 
 * Summary during a specific time range
 * 
 * @author A0087091B
 *
 */

public class SummaryDetails {
	
	private SummaryType summaryType;
	private double income = 0;
	private double expense = 0;
	private double balance = 0;
	private final static double EPSILON = 0.00001; // correction for zero (double)

	
	public SummaryDetails(double income, double expense, SummaryType myType){
		this.income = income;
		this.expense = expense;
		balance = income - expense;
		summaryType = myType;
	}
	
	/**
	 * Get income value for this SummaryDetails
	 * @return
	 */
	public double getIncome(){
		if (Math.abs(balance) < EPSILON)
			return 0;
		return income;
	}
	
	/**
	 * Get expense value for this SummaryDetails
	 * @return
	 */
	public double getExpense(){
		if (Math.abs(balance) < EPSILON)
			return 0;
		return expense;
	}
	
	/**
	 * Get balance value for this SummaryDetails
	 * @return
	 */
	public double getBalance(){
		if (Math.abs(balance) < EPSILON)
			return 0;
		return balance;
	}
	
	/**
	 * Get the SummaryType for this SummaryDetails
	 * @return
	 */
	public SummaryType getSummaryType(){
		return summaryType;
	}
}
