package ezxpns.data;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ezxpns.data.records.ExpenseType;

/**
 * This class generates target ratios for needs,wants and savings 
 * @author Suzzie
 *
 */

/**
 * First Month : Uses default NWS ratios
 * Second Month onwards: Uses data from previous month to construct this month's ratios
 * This month's ratio will not change unless user modified last month's data
 * This month ratio will also be saved in an array, thisNWSTarget
 * 
 * if user did not modify last month's record, you can just retrieve the ratios normally
 * 1.getNeeds() getWants() getSavings()
 * 
 * if user modified last month's record, we have to recalculate last month's ratio, then calculate this month's ratio
 * 1. getNewRatios();
 * 2. get all the ratios again! getNeeds() getWants() getSavings()
 * 
 */

public class NWSGenerator extends Storable{
	
	public static interface DataProvider{
		double getMonthlyExpense(ExpenseType type);
		double getMonthlyIncome();
		
		double getPrevMonthlyExpense(ExpenseType type); //get the previous month's data
		double getPrevMonthIncome(); // get previous month's income
		
		/*I will need the NWSdata object to be stored in the database. I will be constantly retrieving them and modifying them
		*the first data can be created using new NWSdata(). nothing inside the constructor. Thereafter, I will be modifying the NWSdata 
		*using the setAll(Calendar date, double n, double w, double s). I wont remove the object or create a new one
		*so i need 2 empty NWSdata inside the database: one for the past month, and one for the current month
		*/
		NWSdata getPastMonthNWS(); 
		NWSdata getThisMonthNWS();
	}
	
	private transient DataProvider data;
	private transient boolean dataUpdated = true;
	
	private final double NEEDS = 0.5;
	private final double WANTS = 0.3;
	private final double SAVINGS = 0.2;
	private final double BUFFER = 0.03; //+- 3% buffer
	
	// 0.1<Needs<0.8
	private final double MAXNEEDS = 0.7;
	private final double MINNEEDS = 0.1;
	//0.1<Wants<0.5
	private final double MAXWANTS = 0.5;
	private final double MINWANTS = 0.1;
	//0.2<Savings<0.7
	private final double MAXSAVINGS = 0.7;
	private final double MINSAVINGS = 0.2;
	
	private transient double targetSavings;
	private transient double targetNeeds;
	private transient double targetWants;
	
	private NWSdata pastMonthNWS;
	private NWSdata thisMonthNWS;
	
	
public NWSGenerator(DataProvider data){
	this.data = data;
	dataUpdated = true;
	pastMonthNWS = data.getPastMonthNWS();
	thisMonthNWS = data.getThisMonthNWS();
	
	//if there is no past month data or this month data, set the ratio as default.
	if((!thisMonthNWS.isSet())&&(!pastMonthNWS.isSet())){
	thisMonthNWS.setAll(new GregorianCalendar(), NEEDS, WANTS, SAVINGS);	
	}
	
	//Need to change the ratio for this month
	else if(thisMonthNWS.isSet()){
		if(isNewMonth(thisMonthNWS.getDate())){
		getNewRatios();
		pastMonthNWS.setAll(thisMonthNWS.getDate(),thisMonthNWS.getNeeds(),thisMonthNWS.getWants(),thisMonthNWS.getSavings());
		thisMonthNWS.setAll(new GregorianCalendar(), targetNeeds, targetWants, targetSavings);
	}
		}
	
	if(!pastMonthNWS.isSet()){
		pastMonthNWS.setAll(new GregorianCalendar(), NEEDS, WANTS, SAVINGS);
	}
	
	
}
	
public double getNeeds(){
	return thisMonthNWS.getNeeds();
}

public double getWants(){
	return thisMonthNWS.getWants();
}

public double getSavings(){
	return thisMonthNWS.getSavings();
}

/**
 * This retrieves the actual amount of money spend on WANTS
 * @return the actual amount of money spend on WANTS
 */
public double getMonthlyWants(){
	return data.getMonthlyExpense(ExpenseType.WANT);
}

/**
 * This retrieves the actual amount of money spend on NEEDS
 * @return the actual amount of money spend on NEEDS
 */
public double getMonthlyNeeds(){
	return data.getMonthlyExpense(ExpenseType.NEED);
}

/**
 * The retrieves actual amount of not spent this month i.e. the savings
 * @return the actual amount of money not spent
 */
public double getMonthlySavings(){
	return data.getMonthlyIncome()-getMonthlyNeeds()-getMonthlyWants();
}


public void getNewRatios(){
	/*i represents the state of the finance.
	 *eg. Needs and Wants have exceeded limit and Savings have not met target
	 *exceed limit -> 1, did not meet target --> 0,
	 *meet a target by a difference of +- 3% -> there will be no change in the ratios.
	 *Needs|Wants|Savings
	 *  1  |  1  |  0   implies i = 110 = 6
	 */
	int i=0;
	
	double past_income = data.getPrevMonthIncome();
	double past_n_exp = data.getPrevMonthlyExpense(ExpenseType.NEED)/past_income;
	double past_w_exp = data.getPrevMonthlyExpense(ExpenseType.WANT)/past_income;
	double past_s_exp = 1 - past_n_exp - past_w_exp;
	
	if(!pastMonthNWS.isSet()){
		targetNeeds = NEEDS;
		targetWants = WANTS;
		targetSavings = SAVINGS;
		return;
		}
	
	double excess_n = pastMonthNWS.getNeeds() - past_n_exp;
	double excess_w = pastMonthNWS.getWants() - past_w_exp;
	double excess_s = pastMonthNWS.getSavings() - past_s_exp;	
	
	
	if(Math.abs(excess_n)>BUFFER && Math.abs(excess_w)>BUFFER &&Math.abs(excess_s)>BUFFER){
		if (excess_n>0){ //exceeded Needs
			i+=4; 
		}
		if(excess_w>0){ //exceeded Wants
			i+=2;
		}

		if(excess_s<0){
			i+=1;	
		}
	
	
	
	switch(i){
	
	case 1://001//reduce wants -> reduce needs -> increase savings 
		targetWants = reduceTarget(MINWANTS, Math.abs(excess_w), targetWants);
		targetNeeds = reduceTarget(MINNEEDS,Math.abs(excess_n), targetNeeds);
		targetSavings = 1 - targetNeeds - targetWants;		
		break;
	case 2://010//reduce needs -> increase wants
		{
		targetNeeds = reduceTarget(MINNEEDS, Math.abs(excess_n),targetNeeds);
		targetWants = 1- targetNeeds - targetSavings;
		}
		break;
	case 3://011 //reduce needs -> increase savings -> increase wants
	{
		targetNeeds = reduceTarget(MINNEEDS, Math.abs(excess_n), targetNeeds);
		targetWants = increaseTarget(MAXWANTS, Math.abs(excess_w), targetWants);
		targetSavings = 1- targetNeeds- targetWants;
		
	}
		break;
	case 4://100 //reduce wants -> reduce savings -> increase needs
	{	
		targetWants = reduceTarget(MINWANTS, Math.abs(excess_w), targetWants);
		targetSavings = reduceTarget(MINSAVINGS, Math.abs(excess_s), targetSavings);
		targetNeeds = 1-targetWants - targetNeeds;
		
	}
		break;
	case 5://101//reduce wants -> increase needs
	{
		targetWants = reduceTarget(MINWANTS, Math.abs(excess_w),targetWants);
		targetNeeds = 1-targetWants - targetSavings;
	}
		
		break;
	case 6://110//excess spendings
		{
		targetSavings = reduceTarget(MINSAVINGS, Math.abs(excess_s), targetSavings);
		targetWants = reduceTarget(MINWANTS, Math.abs(excess_w), targetWants);
		targetNeeds = 1-targetSavings - targetWants;
	}
		break;

	default:
		break;	
	}	
	}
	
}

private double increaseTarget(double MAX, double increment, double oldTarget){
	if(oldTarget+0.75*increment<=MAX)
	return oldTarget+0.75*increment;
	else return MAX;
}

private double reduceTarget(double MIN, double decrement, double oldTarget){
	if(oldTarget-0.75*decrement>=MIN)
		return oldTarget-0.5*decrement;
	else return MIN;
}

/**
 * This method checks if there is a need to recalculate the NWS ratios for a new month
 * @param date
 * @return true if date's Month and Year is different from the date in thisMonthNWS
 */
private boolean isNewMonth(Calendar date){
	Calendar today = new GregorianCalendar();
	if(date.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
		if(date.get(Calendar.YEAR)==today.get(Calendar.YEAR)){
			return false;
		}
	}
	return true;
}

}