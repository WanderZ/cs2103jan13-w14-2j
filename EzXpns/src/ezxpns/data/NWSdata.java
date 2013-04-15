package ezxpns.data;

import java.util.Calendar;

/**
 * 
 * A data class that holds information of Needs, Wants and Savings
 * 
 * @author A0085413J
 */

public class NWSdata {
	private Calendar date;
	private double targetNeedsRatio; //target needs wrt income
	private double targetWantsRatio; //target wants wrt income
	private double targetSavingsRatio; //target savings wrt income
	private double currentNeeds; //current amount of expense with ExpenseType NEED
	private double currentWants; //current amount of expense with ExpenseType WANT
	private double currentSavings;//current amount of money leftover from income after deducting currentNeeds and currentWants
	private double income; //amount of income
	private boolean state; //state of NWSdata. NWSdata is valid if state is true

	public NWSdata(Calendar date, double tN, 
			double tW, double tS, double cN,
			double cW, double cS, double ic){
		setAll(date, tN, tW, tS, cN, cW, cS, ic);
	}

	public NWSdata(){
		setAll(null, 0, 0, 0, 0, 0, 0, 0);
		state = false;
	}

	/**
	 * Sets all the attributes inside NWSdata
	 * @param date
	 * @param tN
	 * @param tW
	 * @param tS
	 * @param cN
	 * @param cW
	 * @param cS
	 * @param ic
	 */
	public void setAll(Calendar date, double tN, 
			double tW, double tS, double cN, 
			double cW, double cS, double ic){
		this.date = date;
		targetNeedsRatio = tN;
		targetWantsRatio = tW;
		targetSavingsRatio = tS;
		currentNeeds = cN;
		currentWants = cW;
		currentSavings = cS;
		income = ic;
		state = true;
	}

	/**
	 * Returns the income of NWSdata
	 * @return
	 */
	public double getIncome(){
		return income;
	}

	/**
	 * Returns the date of NWSdata
	 * @return
	 */
	public Calendar getDate(){
		return date;
	}

	/**
	 * Returns currentNeeds of NWSdata
	 * @return
	 */
	public double getCurrentNeeds(){
		return currentNeeds;
	}
	
	/**
	 * Returns currentWants of NWSdata
	 * @return
	 */
	public double getCurrentWants(){
		return currentWants;
	}
	
	/**
	 * Returns currentSavings of NWSdata
	 * @return
	 */
	public double getCurrentSavings(){
		return currentSavings;
	}



	/**
	 * Returns the targetNeedsRatio of NWSdata
	 * @return
	 */
	public double getTargetNeedsRatio(){
		return targetNeedsRatio;
	}

	/**
	 * Returns the targetWantsRatio of NWSdata
	 * @return
	 */
	public double getTargetWantsRatio(){
		return targetWantsRatio;
	}

	/**
	 * Returns the targetSavingsRatio of NWSdata
	 * @return
	 */
	public double getTargetSavingsRatio(){
		return targetSavingsRatio;
	}

	/**
	 * Returns the ratio of currentNeeds with respect to income of NWSdata. Returns 0 if income is 0
	 * @return
	 */
	public double getCurrNeedsRatio(){
		if(income==0) 
			return 0;
		else
			return currentNeeds/income;
	}

	/**
	 * Returns the ratio of currentWants with respect to income of NWSdata. Returns 0 if income is 0
	 * @return
	 */
	public double getCurrWantsRatio(){
		if(income==0) 
			return 0;
		else 
			return currentWants/income;
	}

	/**
	 * Returns the ratio of currentSavings with respect to income of NWSdata. Returns 0 if income is 0
	 * @return
	 */
	public double getCurrSavingsRatio(){
		if(income==0) 
			return 0;
		else 
			return currentSavings/income;
	}

	/**
	 * Returns true if NWSdata is valid
	 * @return
	 */
	public boolean isValid(){
		return state;
	}

	/**
	 * Returns a copy of NWSdata
	 * @return
	 */
	public NWSdata copy(){
		return new NWSdata(date, targetNeedsRatio, targetWantsRatio, 
				targetSavingsRatio, currentNeeds, currentWants, 
				currentSavings, income);
	}
}
