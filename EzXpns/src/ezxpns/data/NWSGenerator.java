package ezxpns.data;

import ezxpns.data.records.ExpenseType;

/**
 * This class generates target ratios for needs,wants and savings 
 * @author Suzzie
 *
 */

public class NWSGenerator extends Storable{
	
	public static interface DataProvider{
		double getMonthlyExpense(ExpenseType type);
		double getMonthlyIncome();
	}
	
	private transient DataProvider data;
	private transient boolean dataUpdated = true;
	
	private final double NEEDS = 0.5;
	private final double WANTS = 0.3;
	private final double SAVINGS = 0.2;
	
	
public NWSGenerator(DataProvider data){
	this.data = data;
	dataUpdated = true;
}

	
public double getNeeds(){
	return NEEDS;
}

public double getWants(){
	return WANTS;
}

public double getSavings(){
	return SAVINGS;
}

public double getMonthlyWants(){
	return data.getMonthlyExpense(ExpenseType.WANT);
}

public double getMonthlyNeeds(){
	return data.getMonthlyExpense(ExpenseType.NEED);
}

public double getMonthlySavings(){
	return data.getMonthlyIncome()-getMonthlyNeeds()-getMonthlyWants();
}
	

}
