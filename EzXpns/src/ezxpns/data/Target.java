package ezxpns.data;

import java.util.Date;

import ezxpns.data.records.Category;

/*
 * This class holds information of a Target
 */

public class Target{
	
	private Category cat;
	private double targetAmt;


	public Target(Category cat, double targetAmt){
		this.cat = cat;
		this.targetAmt = targetAmt;		
	}
	

	public Target copy(){
		return new Target(cat, targetAmt);
	} 
	
	public Category getCategory(){
		return cat;
	}
	
	public double getTarget(){
		return targetAmt;
	}
		
}
