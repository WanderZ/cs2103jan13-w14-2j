package ezxpns.data;

import java.util.Calendar;

public class NWSdata {
	Calendar date;
	private double targetNeeds;
	private double targetWants;
	private double targetSavings;
	private double currentNeeds;
	private double currentWants;
	private double currentSavings;
	private double income;
	private boolean isSet;

	public NWSdata(Calendar date, double tN, 
			double tW, double tS, double cN,
			double cW, double cS, double ic){
		this.date = date;
		targetNeeds = tN;
		targetWants = tW;
		targetSavings = tS;
		currentNeeds = cN;
		currentWants = cW;
		currentSavings = cS;
		income = ic;
		income = 0;
		isSet = true;
	}

	public NWSdata(){
		date = null;
		targetNeeds = 0;
		targetWants = 0;
		targetSavings = 0;
		currentNeeds = 0;
		currentWants = 0;
		currentSavings = 0;
		income = 0;
		isSet = false;
	}

	public void setAll(Calendar date, double tN, 
			double tW, double tS, double cN, 
			double cW, double cS, double ic){
		this.date = date;
		this.date = date;
		targetNeeds = tN;
		targetWants = tW;
		targetSavings = tS;
		currentNeeds = cN;
		currentWants = cW;
		currentSavings = cS;
		income = ic;
		isSet = true;
	}

	public void setDate(Calendar date){
		this.date = date;
	}
	public void setTargetNeeds(double targetNeeds){
		this.targetNeeds = targetNeeds;
	}
	public void setTargetWants(double targetWants){
		this.targetWants = targetWants;
	}
	public void setTargetSavings(double targetSavings){
		this.targetSavings = targetSavings;
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
	
	public double getTargetNeeds(){
		return targetNeeds;
	}

	public double getTargetWants(){
		return targetWants;
	}

	public double getTargetSavings(){
		return targetSavings;
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
	public double getNeedsProgress(){
		if(income==0) 
			return 0;
		else
		return currentNeeds/income;
	}
	
	public double getWantsProgress(){
		if(income==0) 
			return 0;
		else 
			return currentWants/income;
	}
	
	public double getSavingsProgress(){
		if(income==0) 
			return 0;
		else 
			return currentSavings/income;
	}
	

	public boolean isSet(){
		return isSet;
	}
	
	
	public NWSdata copy(){
		return new NWSdata(date, targetNeeds, targetWants, 
				targetSavings, currentNeeds, currentWants, 
				currentSavings, income);
	}
}
