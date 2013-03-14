package ezxpns.data;

/**
 * Data structure containing information on 
 * Summary during a specific timeline
 * 
 * @author tingzhe
 *
 */

public class SummaryDetails {
	
	private double income;
	private double expense;
	private double balance;
	
	public SummaryDetails(double income, double expense){
		this.income = income;
		this.expense = expense;
		balance = income - expense;
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

}
