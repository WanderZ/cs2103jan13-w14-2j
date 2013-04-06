package ezxpns.data;

import java.util.Calendar;

public class NWSdata {
	Calendar date;
	private double targetNeedsRatio;
	private double targetWantsRatio;
	private double targetSavingsRatio;
	private double currentNeeds;
	private double currentWants;
	private double currentSavings;
	private double income;
	private boolean isSet;

	/*public NWSdata(Calendar date, double tN, 
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
		income = 0; //???????????????????????
		isSet = true;
	}*/
	
	public NWSdata(Calendar date, double tN, 
			double tW, double tS, double cN,
			double cW, double cS, double ic){
		setAll(date, tN, tW, tS, cN, cW, cS, ic);
	}

	public NWSdata(){
		setAll(null, 0, 0, 0, 0, 0, 0, 0);
		isSet = false;
	}

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
		isSet = true;
	}

	public void setDate(Calendar date){
		this.date = date;
	}
	public void settargetNeedsRatio(double targetNeedsRatio){
		this.targetNeedsRatio = targetNeedsRatio;
	}
	public void settargetWantsRatio(double targetWantsRatio){
		this.targetWantsRatio = targetWantsRatio;
	}
	public void settargetSavingsRatio(double targetSavingsRatio){
		this.targetSavingsRatio = targetSavingsRatio;
	}
	public void setCurrentNeeds(double currentNeeds){
		this.currentNeeds = currentNeeds;
	}
	public void setCurrentWants(double currentWants){
		this.currentWants = currentWants;
	}
	public void setCurrentSavings(double currentSavings){
		this.currentSavings = currentSavings;
	}
	public void setIncome(double income){
		this.income = income;
	}
	
	public double getIncome(){
		return income;
	}
	
	public Calendar getDate(){
		return date;
	}
	
	public double getTargetNeedsRatio(){
		return targetNeedsRatio;
	}

	public double getTargetWantsRatio(){
		return targetWantsRatio;
	}

	public double getTargetSavingsRatio(){
		return targetSavingsRatio;
	}
	public double getCurrentNeeds(){
		return currentNeeds;
	}
	public double getCurrentWants(){
		return currentWants;
	}
	public double getCurrentSavings(){
		return currentSavings;
	}
	public double getCurrNeedsRatio(){
		if(income==0) 
			return 0;
		else
		return currentNeeds/income;
	}
	
	public double getCurrWantsRatio(){
		if(income==0) 
			return 0;
		else 
			return currentWants/income;
	}
	
	public double getCurrSavingsRatio(){
		if(income==0) 
			return 0;
		else 
			return currentSavings/income;
	}
	

	public boolean isSet(){
		return isSet;
	}
	
	
	public NWSdata copy(){
		return new NWSdata(date, targetNeedsRatio, targetWantsRatio, 
				targetSavingsRatio, currentNeeds, currentWants, 
				currentSavings, income);
	}
}
