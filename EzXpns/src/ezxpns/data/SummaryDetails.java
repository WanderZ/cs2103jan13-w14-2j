package ezxpns.data;

import ezxpns.data.SummaryGenerator.SummaryType;

/**
 * Data structure containing information on 
 * Summary during a specific timeline
 * 
 * @author tingzhe
 *
 */

public class SummaryDetails {
	
	private SummaryType summaryType;
	private double income = 0;
	private double expense = 0;
	private double balance = 0;
	
	public SummaryDetails(double income, double expense, SummaryType myType){
		this.income = income;
		this.expense = expense;
		balance = income - expense;
		summaryType = myType;
	}
	
	// Getters
	public double getIncome(){
		return income;
	}
	
	public double getExpense(){
		return expense;
	}
	
	public double getBalance(){
		return balance;
	}
	
	public SummaryType getSummaryType(){
		return summaryType;
	}
}
